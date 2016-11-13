package com.lh.cd.sysadmin.user.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l_h.cd.pub.http.AppController;
import com.l_h.cd.pub.http.JsonMap;
import com.l_h.cd.pub.http.Parameter;
import com.l_h.cd.pub.util.DateUtil;
import com.l_h.cd.pub.util.ExtjsUtil;
import com.l_h.cd.pub.util.MD5;
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.entity.model.TabSysRole;
import com.lh.cd.entity.model.TabUserAccount;
import com.lh.cd.entity.model.TabUserInfo;
import com.lh.cd.entity.model.TabfDistrict;
import com.lh.cd.pub.data.ResultMap;
import com.lh.cd.pub.db.Paginate;
import com.lh.cd.pub.exception.AppException;
import com.lh.cd.sysadmin.user.service.UserService;

@Controller
@RequestMapping(value = "/sysadmin/user")
public class UserController extends AppController {

	private static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService service;

	
	
	@ResponseBody
	@RequestMapping(value = "getUserListCtrl")
	public JsonMap getUserList(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			
			
			String operFlag = para.getString("operFlag", "");
			String userAccount = para.getString("userAccount", "");
			String userName = para.getString("userName", "");
			String status = para.getString("status", "");
			String tel = para.getString("tel", "");
			String startBirthday = para.getString("startBirthday", "");
			String endBirthday = para.getString("endBirthday", "");
			String sort = para.getString("sort", "");
			String dir = para.getString("dir", "");
			String searchField = para.getString("query", "");
			int start = para.getInt("start", 0);
			int limit = para.getInt("limit", 0);

			logger.debug("operFlag=" + operFlag);
			logger.debug("userAccount=" + userAccount);
			logger.debug("userName=" + userName);
			logger.debug("status=" + status);
			logger.debug("tel=" + tel);
			logger.debug("startBirthday=" + startBirthday);
			logger.debug("endBirthday=" + endBirthday);
			logger.debug("start=" + start);
			logger.debug("limit=" + limit);
			logger.debug("sort=" + sort);
			logger.debug("dir=" + dir);
			logger.debug("searchField=" + searchField);

			Map<String, Object> paraMap = new HashMap<String, Object>();
			if (!searchField.equals("")) {
				paraMap.put("userAccount", searchField);
			}
			else{
				paraMap.put("userAccount", userAccount);
				paraMap.put("userName", userName);
				paraMap.put("status", status);
				paraMap.put("tel", tel);
				paraMap.put("startBirthday", startBirthday);
				paraMap.put("endBirthday", endBirthday);
			}
			paraMap.put("sort", sort);
			paraMap.put("dir", dir);

			Paginate paginate = ExtjsUtil.getExtPaginate(start, limit);
			List<?> userList = service.getUserPageList(paraMap, paginate);

			rsMap.put("userList", userList);
			rsMap.put("totalProperty", paginate.getTotalCount());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询用户出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getUserInfoCtrl")
	public JsonMap getUserInfo(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int userId = para.getInt("userId", 0);
			logger.debug("userId=" + userId);

			TabUserAccount userAccount = service.getUserAccount(userId);
			TabUserInfo userInfo = service.getUserInfo(userId);

			if (userAccount == null || userInfo == null) {
				throw new AppException("用户不存在！");
			}

			if (userInfo.getBirthplace() != 0) {
				rsMap.put("birthplaceString", service.getDistrictNameById(userInfo.getBirthplace()));
			}

			if (userInfo.getWorkArea() != null && !userInfo.getWorkArea().equals("")) {
				rsMap.put("workAreaString", service.getWorkArea(userInfo.getWorkArea()));
			}

			HashMap<String, Object> map = service.getUserRole(userId);
			rsMap.put("roleIds", map.get("id"));
			rsMap.put("roleNames", map.get("name"));

			rsMap.put("userAccount", userAccount);
			rsMap.put("userInfo", userInfo);
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询用户出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteUserCtrl")
	public JsonMap deleteUser(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			String operFlag = para.getString("operFlag", "");
			logger.debug("operFlag=" + operFlag);
			if (operFlag.equals("delete")) {
				String userIds = para.getString("userIds", "");
				logger.debug("userIds=" + userIds);
				service.deleteUser(userIds);
			}
			rsMap.put("message", "删除用户成功！");
			// if (true)throw new AppException("删除用户出错！");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("删除用户出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/saveUserCtrl")
	public JsonMap saveUser(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		String operFlag = "";
		
		
		try {
			Parameter para = new Parameter(request);
			
			
			TabUserAccount userAccount = null;
			TabUserInfo userInfo = null;
			
			operFlag = para.getString("operFlag", "");
			int userId = para.getInt("userId", 0);
			logger.debug("##################saveUserInfo operFlag=" + operFlag + ",userId=" + userId);
			logger.debug("##########birthplace=" + para.getString("birthplace", ""));
			logger.debug("##########workArea=" + para.getString("workArea", ""));

			if (operFlag.equals("update")) {// 修改
				userAccount = service.getUserAccount(userId);
				userInfo = service.getUserInfo(userId);

				if (userAccount == null || userInfo == null) {
					throw new AppException("用户不存在！");
				}

				if (!para.getString("passwd", "").equals("")) {
					userAccount.setPasswd(MD5.md5crypt(MD5.md5crypt(para.getString("passwd", "")) + userAccount.getSalt()));
				}
			}
			else if (operFlag.equals("add")) {// 新增
				userAccount = new TabUserAccount();
				userInfo = new TabUserInfo();

				userAccount.setUserAccount(para.getString("userAccount", ""));
				userAccount.setUserType(1);
				userAccount.setSalt(MD5.md5crypt("" + Math.random()).substring(0, 6));
				userAccount.setPasswd(MD5.md5crypt(MD5.md5crypt(para.getString("passwd", "")) + userAccount.getSalt()));
				userAccount.setCurSessionId("");
				userAccount.setSessionInterval(-1);
				userAccount.setRegisterTime(new Date());
			}
			else {
				throw new AppException("操作不合法！");
			}

			// 用户账号表
			userAccount.setUserName(para.getString("userName", ""));
			userAccount.setStatus(para.getInt("status", 1));
			userAccount.setUpdTime(new Date());

			// 用户信息表
			userInfo.setBirthday(DateUtil.parse(para.getString("birthday", ""), "yyyy-MM-dd"));
			userInfo.setSex(para.getInt("sex", 0));
			userInfo.setCertType(para.getInt("certType", 0));
			userInfo.setCertNumber(para.getString("certNumber", ""));
			userInfo.setEmail(para.getString("email", ""));
			userInfo.setIntro(para.getString("intro", ""));
			userInfo.setTel(para.getString("tel", ""));
			userInfo.setMobile(para.getString("mobile", ""));
			userInfo.setMsn(para.getString("msn", ""));
			userInfo.setQq(para.getString("qq", ""));
			userInfo.setPortrait(para.getString("portrait", ""));
			userInfo.setAddress(para.getString("address", ""));
			userInfo.setPostalcode(para.getString("postalcode", ""));
			userInfo.setCountry(0);
			userInfo.setProvince(para.getInt("province", 0));
			userInfo.setCity(para.getInt("city", 0));
			userInfo.setCounty(para.getInt("county", 0));
			userInfo.setDistrict(para.getInt("district", 0));
			userInfo.setBirthplace(para.getInt("birthplace", 0));
			userInfo.setWorkArea(para.getString("workArea", ""));
			userInfo.setUpdTime(new Date());

			String roleIds = para.getString("roleIds", "");
			logger.debug("roleIds=" + roleIds);

			// 上传图片
			service.uploadImages(request, userInfo);

			if (operFlag.equals("update")) {
				service.updateUser(userAccount, userInfo, roleIds);
				rsMap.put("message", "修改用户成功！");
			}
			else {
				if (service.existSameUserAccount(userAccount.getUserAccount(), 0)) {
					throw new AppException("存在相同的用户账号！");
				}
				service.insertUser(userAccount, userInfo, roleIds);
				rsMap.put("message", "添加用户成功！");
			}
		}
		catch (AppException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException((operFlag.equals("update") ? "修改用户出错！" : "添加用户出错！"));
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getUserAccountListCtrl")
	public JsonMap getUserAccountList(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			String userAccount = para.getString("userAccount", "");
			int showMax = para.getInt("showMax", 0);
			logger.debug("getUserAccountList userAccount=" + userAccount + ",showMax=" + showMax);

			rsMap.put("userList", service.getUserAccountList(userAccount, showMax));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询用户账号出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getRoleTreeCtrl")
	public JsonMap getRoleTree(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int userId = para.getInt("userId", 0);
			logger.debug("getRoleTree userId=" + userId);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("userId", userId);
			paraMap.put("status", 1);
			List<?> list = service.getRoleTree(paraMap);
			List<HashMap<String, Object>> roleList = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				ResultMap map2 = (ResultMap) list.get(i);
				map1.put("id", map2.getInt("role_id", 0));
				map1.put("text", map2.getString("role_name", ""));
				map1.put("id", map2.getInt("role_id", 0));
				map1.put("leaf", true);
				map1.put("checked", map2.getInt("user_id", 0) != 0);
				roleList.add(map1);
			}
			rsMap.put("roleList", roleList);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询用户账号出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getBirthplaceCtrl")
	public Map<String, Object> getBirthplace(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int parentId = para.getInt("parentId", -1);
			logger.debug("getBirthplace parentId=" + parentId);
			if (parentId == -1) {
				rsMap.put("zdList", new ArrayList<Object>());
			}
			else {
				List<HashMap<String, Object>> zdList = new ArrayList<HashMap<String, Object>>();
				List<TabfDistrict> dataList = service.getDistrictByParentIdList(parentId);
				for (int i = 0; i < dataList.size(); i++) {
					TabfDistrict bean = dataList.get(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", bean.getId());
					map.put("text", bean.getName());
					map.put("leaf", bean.getIsParent() == 0);
					zdList.add(map);
				}
				rsMap.put("zdList", zdList);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询出生地出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getWorkAreaCtrl")
	public Map<String, Object> getWorkArea(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int parentId = para.getInt("parentId", -1);
			int userId = para.getInt("userId", 0);
			String checked = para.getString("checked", "false");
			logger.debug("##########################getWorkArea parentId=" + parentId + ",userId=" + userId + ",checked=" + checked);
			if (parentId == -1) {
				rsMap.put("zdList", new ArrayList<Object>());
			}
			else {
				String workArea = service.getWorkAreaByUserId(userId);
				List<TabfDistrict> dataList = service.getDistrictByParentIdList(parentId);
				List<HashMap<String, Object>> zdList = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < dataList.size(); i++) {
					TabfDistrict bean = dataList.get(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", bean.getId());
					map.put("text", bean.getName());
					map.put("leaf", bean.getIsParent() == 0);
					map.put("checked", (";" + workArea + ";").indexOf(";" + bean.getId() + ";") >= 0);
					zdList.add(map);
				}
				rsMap.put("zdList", zdList);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询工作地区出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getUserRoleCtrl")
	public Map<String, Object> getUserRole(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int userId = para.getInt("userId", 0);
			String checked = para.getString("checked", "false");
			logger.debug("##########################getUserRole userId=" + userId + ",checked=" + checked);

			String roleIds = service.getRoleByUserId(userId);
			List<TabSysRole> dataList = service.getSysRoleList();
			List<HashMap<String, Object>> zdList = new ArrayList<HashMap<String, Object>>();
			logger.debug("roleIds=" + roleIds);
			for (int i = 0; i < dataList.size(); i++) {
				TabSysRole bean = dataList.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", bean.getRoleId());
				map.put("text", bean.getRoleName());
				map.put("leaf", true);
				map.put("checked", (";" + roleIds + ";").indexOf(";" + bean.getRoleId() + ";") >= 0);
				zdList.add(map);
			}
			rsMap.put("zdList", zdList);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询用户角色出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getGrantUserAuthCtrl")
	public Map<String, Object> getGrantUserAuth(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int userId = para.getInt("userId", 0);
			int menuId = para.getInt("menuId", 0);
			logger.debug("userId=" + userId + ",menuId=" + menuId);

			List<TabSysMenu> allMenuList = service.getAllMenu();
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("userId", userId);
			paraMap.put("parentId", menuId);
			paraMap.put("status", 1);
			List<?> list = service.getGrantUserAuthByUserId(paraMap);
			List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				ResultMap resultMap = (ResultMap) list.get(i);
				int curMenuId = resultMap.getInt("menu_id", 0);
				map.put("id", curMenuId);
				map.put("text", resultMap.getString("menu_name", ""));
				map.put("leaf", service.isLeaf(curMenuId, allMenuList));
				map.put("checked", resultMap.getInt("user_id", 0) != 0);
				menuList.add(map);
			}
			rsMap.put("menuList", menuList);
			// Thread.sleep(3000);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询角色权限出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getRealUserAuthCtrl")
	public Map<String, Object> getRealUserAuth(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int userId = para.getInt("userId", 0);
			int menuId = para.getInt("menuId", 0);
			logger.debug("userId=" + userId + ",menuId=" + menuId);

			List<TabSysMenu> allMenuList = service.getAllMenu();
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("userId", userId);
			paraMap.put("parentId", menuId);
			paraMap.put("status", 1);
			List<?> list = service.getRealUserAuthByUserId(paraMap);
			List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				ResultMap resultMap = (ResultMap) list.get(i);
				int curMenuId = resultMap.getInt("menu_id", 0);
				map.put("id", curMenuId);
				map.put("text", resultMap.getString("menu_name", ""));
				map.put("leaf", service.isLeaf(curMenuId, allMenuList));
				// map.put("checked", resultMap.getInt("user_id",0)!=0);
				if (resultMap.getInt("user_id", 0) != 0) {
					menuList.add(map);
				}
			}
			rsMap.put("menuList", menuList);
			// Thread.sleep(3000);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询角色权限出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/saveUserAuthCtrl")
	public Map<String, Object> saveUserAuth(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int userId = para.getInt("userId", 0);
			String menuIds = para.getString("menuIds", "");
			logger.debug("userId=" + userId + ",menuIds=" + menuIds);
			service.saveUserAuth(userId, menuIds);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("保存角色权限出错！");
		}
		return rsMap;
	}
}
