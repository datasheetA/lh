package com.lh.cd.sysadmin.user.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.l_h.cd.pub.upload.UploadConfiger;
import com.l_h.cd.pub.upload.UploadFile;
import com.l_h.cd.pub.util.DateUtil;
import com.lh.cd.entity.mapper.TabSysMenuMapper;
import com.lh.cd.entity.mapper.TabSysRoleMapper;
import com.lh.cd.entity.mapper.TabUserAccountMapper;
import com.lh.cd.entity.mapper.TabUserAuthMapper;
import com.lh.cd.entity.mapper.TabUserInfoMapper;
import com.lh.cd.entity.mapper.TabUserRoleMapper;
import com.lh.cd.entity.mapper.TabfDistrictMapper;
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.entity.model.TabSysMenuExample;
import com.lh.cd.entity.model.TabSysRole;
import com.lh.cd.entity.model.TabSysRoleExample;
import com.lh.cd.entity.model.TabUserAccount;
import com.lh.cd.entity.model.TabUserAccountExample;
import com.lh.cd.entity.model.TabUserAuth;
import com.lh.cd.entity.model.TabUserAuthExample;
import com.lh.cd.entity.model.TabUserInfo;
import com.lh.cd.entity.model.TabUserRole;
import com.lh.cd.entity.model.TabUserRoleExample;
import com.lh.cd.entity.model.TabfDistrict;
import com.lh.cd.entity.model.TabfDistrictExample;
import com.lh.cd.pub.Constants;
import com.lh.cd.pub.db.Paginate;
import com.lh.cd.pub.exception.AppException;
import com.lh.cd.sysadmin.frame.mapper.SysadminMapper;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class UserService {

	private static Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private TabUserAccountMapper tabUserAccountMapper;

	@Autowired
	private TabUserInfoMapper tabUserInfoMapper;

	@Autowired
	private TabUserAuthMapper tabUserAuthMapper;
	
	@Autowired
	private TabfDistrictMapper tabfDistrictMapper;

	@Autowired
	private TabSysRoleMapper tabSysRoleMapper;

	@Autowired
	private TabUserRoleMapper tabUserRoleMapper;

	@Autowired
	private TabSysMenuMapper tabSysMenuMapper;
		
	@Autowired
	private SysadminMapper userMapper;

	public List<?> getUserPageList(Map<String, Object> paraMap, Paginate paginate) {
		paraMap.put("paginate", paginate);
		paginate.setTotalCount(userMapper.selectUserListByPageCount(paraMap));
		
		logger.debug("paginate.getPageSize()=" + paginate.getPageSize());
		logger.debug("paginate.getTotalCount()=" + paginate.getTotalCount());
		logger.debug("paginate.getTotalPage()=" + paginate.getTotalPage());
		
		return userMapper.selectUserListByPage(paraMap);
	}

	public TabUserAccount getUserAccount(int userId) {
		return tabUserAccountMapper.selectByPrimaryKey(userId);
	}

	public TabUserInfo getUserInfo(int userId) {
		return tabUserInfoMapper.selectByPrimaryKey(userId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void insertUser(TabUserAccount userAccount, TabUserInfo userInfo, String roleIds) throws Exception {
		tabUserAccountMapper.insertSelective(userAccount);
		userInfo.setUserId(userAccount.getUserId());
		tabUserInfoMapper.insertSelective(userInfo);
		this.saveUserRole(userAccount.getUserId(), roleIds);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateUser(TabUserAccount userAccount, TabUserInfo userInfo, String roleIds) {
		tabUserAccountMapper.updateByPrimaryKeySelective(userAccount);
		tabUserInfoMapper.updateByPrimaryKeySelective(userInfo);
		this.saveUserRole(userAccount.getUserId(), roleIds);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteUser(String userIds) {
		String[] arrUserIds = userIds.split(",");
		for (int i = 0; i < arrUserIds.length; i++) {
			int userId = Integer.parseInt(arrUserIds[i]);
			tabUserAccountMapper.deleteByPrimaryKey(userId);
			tabUserInfoMapper.deleteByPrimaryKey(userId);
			logger.debug("deleteUser userId=" + userId);
			this.deleteAssocData(userId);
		}
	}

	public void deleteUserRole(int userId) {
		TabUserRoleExample example = new TabUserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId);
		tabUserRoleMapper.deleteByExample(example);
	}
	
	public void deleteUserAuth(int userId) {
		TabUserAuthExample example = new TabUserAuthExample();
		example.createCriteria().andUserIdEqualTo(userId);
		tabUserAuthMapper.deleteByExample(example);
	}

	public void saveUserRole(int userId, String roleIds) {
		this.deleteUserRole(userId);

		if (!roleIds.trim().equals("")) {
			String[] arrRoleIds = roleIds.split(";");
			for (int i = 0; i < arrRoleIds.length; i++) {
				int roleId = Integer.parseInt(arrRoleIds[i]);
				TabUserRole record = new TabUserRole();
				record.setRoleId(roleId);
				record.setUserId(userId);
				record.setUpdTime(new Date());
				tabUserRoleMapper.insertSelective(record);
			}
		}
	}

	private void deleteAssocData(int userId) {
		this.deleteUserRole(userId);
		this.deleteUserAuth(userId);
	}

	public boolean existSameUserAccount(String userAccount, int userId) {
		TabUserAccountExample example = new TabUserAccountExample();
		example.createCriteria().andUserAccountEqualTo(userAccount).andUserIdNotEqualTo(userId);
		return tabUserAccountMapper.selectByExample(example).size() > 0;
	}

	public List<TabUserAccount> getUserAccountList(String userAccount, int showMax) {
		TabUserAccountExample example = new TabUserAccountExample();
		example.createCriteria().andUserAccountLike("%" + userAccount + "%");
		example.setPaginate(new Paginate(0, showMax));
		example.setOrderByClause("user_account asc");
		return tabUserAccountMapper.selectByExample(example);
	}

	public void uploadImages(HttpServletRequest request, TabUserInfo userInfo) throws AppException {
		try {
			UploadFile upload = new UploadFile();
			upload.setSaveDir(UploadConfiger.getStringValue("ImageSaveDir"));
			upload.setAllowExtName(UploadConfiger.getStringValue("ImageAllowExtName"));
			upload.setNotAllowExtName(UploadConfiger.getStringValue("ImageNotAllowExtName"));
			upload.setMaxFileNameLength(UploadConfiger.getIntValue("ImageMaxFileNameLength"));
			upload.setMaxSize(UploadConfiger.getIntValue("ImageMaxSize"));
			upload.setAccessUrl(UploadConfiger.getStringValue("ImageAccessUrl"));
			upload.setGenThumbs(false);
			upload.doUpload(request);
			if (upload.hasUploadFile("portrait")) {
				logger.debug("size=" + upload.getSize("portrait"));
				logger.debug("url=" + upload.getAccessUrl("portrait"));
				logger.debug("thumbsurl=" + upload.getThumbsUrl("portrait"));
				logger.debug("originalname=" + upload.getOriginalFileName("portrait"));
				logger.debug("extname=" + upload.getExtName("portrait"));
				userInfo.setPortrait(upload.getAccessUrl("portrait"));
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("上传头像出错！");
		}
	}

	public List<TabfDistrict> getDistrictList(int districtId) {
		TabfDistrictExample example = new TabfDistrictExample();
		example.createCriteria().andParentIdEqualTo(districtId);
		example.setOrderByClause("id");
		return tabfDistrictMapper.selectByExample(example);
	}

	public String getDistrictNameById(int districtId) {
		TabfDistrictExample example = new TabfDistrictExample();
		example.createCriteria().andIdEqualTo(districtId);
		List<TabfDistrict> list = tabfDistrictMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return "";
		}
		else {
			return ((TabfDistrict) list.get(0)).getName();
		}
	}

	public List<?> getRoleTree(Map<String, Object> paraMap) {
		return userMapper.selectRoleTreeByUserId(paraMap);
	}

	public String getWorkArea(String districtIds) {
		StringBuffer sbf = new StringBuffer();
		if (districtIds != null && !districtIds.equals("")) {
			String[] arrId = districtIds.split(";");
			for (int i = 0; i < arrId.length; i++) {
				int districtId = Integer.parseInt("" + arrId[i]);
				sbf.append((i > 0) ? ";" : "");
				sbf.append(getDistrictNameById(districtId));
			}
		}
		return sbf.toString();
	}

	public void initUserInfoData(Map<String, Object> rsMap) throws Exception {
		rsMap.put("zdSexList", getSexList());
		rsMap.put("zdCertTypeList", getCertTypeList());
		rsMap.put("currentDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
	}

	public HashMap<String, Object> getUserRole(int userId) {
		TabSysRoleExample example1 = new TabSysRoleExample();
		example1.createCriteria().andStatusEqualTo(1);
		List<TabSysRole> roleList = tabSysRoleMapper.selectByExample(example1);

		TabUserRoleExample example2 = new TabUserRoleExample();
		example2.createCriteria().andUserIdEqualTo(userId);
		List<TabUserRole> authList = tabUserRoleMapper.selectByExample(example2);

		StringBuffer sbfIds = new StringBuffer();
		StringBuffer sbfNames = new StringBuffer();
		for (TabUserRole record : authList) {
			sbfNames.append(sbfIds.toString().equals("") ? "" : ";");
			sbfNames.append(this.getRoleNameById(record.getRoleId(), roleList));
			sbfIds.append(sbfIds.toString().equals("") ? "" : ";");
			sbfIds.append(record.getRoleId());
		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", sbfIds.toString());
		map.put("name", sbfNames.toString());
		return map;
	}

	private String getRoleNameById(int roleId, List<TabSysRole> roleList) {
		for (TabSysRole record : roleList) {
			if (roleId == record.getRoleId()) {
				return record.getRoleName();
			}
		}
		return "";
	}

	public String getWorkAreaByUserId(int userId) {
		TabUserInfo bean = tabUserInfoMapper.selectByPrimaryKey(userId);
		return (bean == null || bean.getWorkArea() == null) ? "" : bean.getWorkArea();
	}

	public String getRoleByUserId(int userId) {
		StringBuffer sbf = new StringBuffer();
		TabUserRoleExample example = new TabUserRoleExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<TabUserRole> list = tabUserRoleMapper.selectByExample(example);
		for (TabUserRole record : list) {
			sbf.append(sbf.toString().equals("") ? "" : ";");
			sbf.append(record.getRoleId());
		}
		return sbf.toString();
	}

	public List<TabSysRole> getSysRoleList() throws Exception {
		TabSysRoleExample example = new TabSysRoleExample();
		example.createCriteria().andStatusEqualTo(1);
		example.setOrderByClause("role_name");
		return tabSysRoleMapper.selectByExample(example);
	}
	
	public List<TabSysMenu> getAllMenu() {
		TabSysMenuExample example = new TabSysMenuExample();
		example.createCriteria();
		return tabSysMenuMapper.selectByExample(example);
	}


	public List<?> getGrantUserAuthByUserId(Map<String, Object> paraMap) {
		return userMapper.selectGrantUserAuthByUserId(paraMap);
	}
	
	public List<?> getRealUserAuthByUserId(Map<String, Object> paraMap) {
		return userMapper.selectRealUserAuthByUserId(paraMap);
	}
	
	public boolean isLeaf(int menuId, List<TabSysMenu> allList) {
		for (TabSysMenu bean : allList) {
			if (bean.getParentId() == menuId) {
				return false;
			}
		}
		return true;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveUserAuth(int userId, String menuIds) {
		this.deleteUserAuth(userId);

		if (!menuIds.trim().equals("")) {
			String[] arrMenuIds = menuIds.split(",");
			for (int i = 0; i < arrMenuIds.length; i++) {
				TabUserAuth record = new TabUserAuth();
				int menuId = Integer.parseInt(arrMenuIds[i]);
				record.setMenuId(menuId);
				record.setUserId(userId);
				record.setUpdTime(new Date());
				tabUserAuthMapper.insertSelective(record);
			}
		}
	}

	
	/**
	 * 查询地区
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TabfDistrict> getDistrictByParentIdList(int parentId) throws Exception {
		TabfDistrictExample example = new TabfDistrictExample();
		example.createCriteria().andParentIdEqualTo(parentId);
		example.setOrderByClause("order_no asc");
		return tabfDistrictMapper.selectByExample(example);
	}

	/**
	 * 获取性别
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> getSexList() throws Exception {
		Constants consts = new Constants();
		return consts.getList("ZD_SEX");
	}

	/**
	 * 获取证件类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<?> getCertTypeList() throws Exception {
		Constants consts = new Constants();
		return consts.getList("ZD_CERTTYPE");
	}

}
