package com.lh.cd.sysadmin.role.service;

import java.util.Date;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.l_h.cd.pub.util.ExtjsUtil;
import com.lh.cd.entity.mapper.TabSysMenuMapper;
import com.lh.cd.entity.mapper.TabSysRoleAuthMapper;
import com.lh.cd.entity.mapper.TabSysRoleMapper;
import com.lh.cd.entity.mapper.TabUserRoleMapper;
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.entity.model.TabSysMenuExample;
import com.lh.cd.entity.model.TabSysRole;
import com.lh.cd.entity.model.TabSysRoleAuth;
import com.lh.cd.entity.model.TabSysRoleAuthExample;
import com.lh.cd.entity.model.TabSysRoleExample;
import com.lh.cd.entity.model.TabUserRole;
import com.lh.cd.entity.model.TabUserRoleExample;
import com.lh.cd.entity.model.TabSysRoleExample.Criteria;
import com.lh.cd.pub.db.Paginate;
import com.lh.cd.sysadmin.frame.mapper.SysadminMapper;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class RoleService {

	private static Logger logger = Logger.getLogger(RoleService.class);

	@Autowired
	private TabSysRoleMapper tabSysRoleMapper;

	@Autowired
	private TabUserRoleMapper tabUserRoleMapper;

	@Autowired
	private TabSysRoleAuthMapper tabSysRoleAuthMapper;

	@Autowired
	private TabSysMenuMapper tabSysMenuMapper;

	@Autowired
	private SysadminMapper sysadminMapper;

	public List<TabSysRole> getRolePageList(Map<String, Object> paraMap, Paginate paginate) {
		TabSysRoleExample example = new TabSysRoleExample();
		Criteria criteria = example.createCriteria();
		if (paraMap.get("roleName") != null && !paraMap.get("roleName").equals("")){
			criteria.andRoleNameLike("%" + paraMap.get("roleName") + "%");
		}
		if (paraMap.get("status") != null && !paraMap.get("status").equals("")) {
			criteria.andStatusEqualTo(Integer.parseInt("" + paraMap.get("status")));
		}
		if (paraMap.get("remark") != null && !paraMap.get("remark").equals("")) {
			criteria.andRemarkLike("%" + paraMap.get("remark") + "%");
		}

		example.setOrderByClause(ExtjsUtil.getExtOrderBy(paraMap, "role_name asc"));

		example.setPaginate(paginate);

		paginate.setTotalCount(tabSysRoleMapper.countByExample(example));

		return tabSysRoleMapper.selectByExample(example);
	}

	public TabSysRole getRole(int roleId) {
		return tabSysRoleMapper.selectByPrimaryKey(roleId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void insertRole(TabSysRole record, String userIds) {
		tabSysRoleMapper.insertSelective(record);
		this.saveRoleUser(record.getRoleId(), userIds);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateRole(TabSysRole record, String userIds) {
		tabSysRoleMapper.updateByPrimaryKeySelective(record);
		this.saveRoleUser(record.getRoleId(), userIds);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteRole(String roleIds) {
		if (!roleIds.trim().equals("")) {
			String[] arrRoleIds = roleIds.split(",");
			for (int i = 0; i < arrRoleIds.length; i++) {
				int roleId = Integer.parseInt(arrRoleIds[i]);
				tabSysRoleMapper.deleteByPrimaryKey(roleId);
				this.deleteRoleUser(roleId);
				this.deleteRoleAuth(roleId);
				logger.debug("deleteRole roleId=" + roleId);
			}
		}
	}

	public boolean existSameRoleName(String roleName, int roleId) {
		TabSysRoleExample example = new TabSysRoleExample();
		example.createCriteria().andRoleNameEqualTo(roleName).andRoleIdNotEqualTo(roleId);
		return tabSysRoleMapper.selectByExample(example).size() > 0;
	}

	private void saveRoleUser(int roleId, String userIds) {
		logger.debug("saveRoleUser roleId=" + roleId + ",userIds=" + userIds);
		this.deleteRoleUser(roleId);

		if (!userIds.trim().equals("")) {
			String[] arrUserIds = userIds.split(",");
			for (int i = 0; i < arrUserIds.length; i++) {
				int userId = Integer.parseInt(arrUserIds[i]);
				TabUserRole record = new TabUserRole();
				record.setRoleId(roleId);
				record.setUserId(userId);
				record.setUpdTime(new Date());
				tabUserRoleMapper.insertSelective(record);
			}
		}
	}

	private void deleteRoleUser(int roleId) {
		TabUserRoleExample example = new TabUserRoleExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		tabUserRoleMapper.deleteByExample(example);
	}

	private void deleteRoleAuth(int roleId) {
		TabSysRoleAuthExample example = new TabSysRoleAuthExample();
		example.createCriteria().andRoleIdEqualTo(roleId);
		tabSysRoleAuthMapper.deleteByExample(example);
	}

	public List<?> getSelectedUserByRoleId(Map<String, Object> paraMap) {
		return sysadminMapper.selectSelectedUserByRoleId(paraMap);
	}

	public List<?> getUsableUserByRoleId(Map<String, Object> paraMap) {
		return sysadminMapper.selectUsableUserByRoleId(paraMap);
	}

	public List<?> getRoleAuthTreeByRoleId(Map<String, Object> paraMap) {
		return sysadminMapper.selectMenuTreeByRoleId(paraMap);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveRoleAuth(int roleId, String menuIds) {
		this.deleteRoleAuth(roleId);

		if (!menuIds.trim().equals("")) {
			String[] arrMenuIds = menuIds.split(",");
			for (int i = 0; i < arrMenuIds.length; i++) {
				TabSysRoleAuth record = new TabSysRoleAuth();
				int menuId = Integer.parseInt(arrMenuIds[i]);
				record.setMenuId(menuId);
				record.setRoleId(roleId);
				record.setUpdTime(new Date());
				tabSysRoleAuthMapper.insertSelective(record);
			}
		}
	}

	public List<TabSysMenu> getAllMenu() {
		TabSysMenuExample example = new TabSysMenuExample();
		example.createCriteria();
		return tabSysMenuMapper.selectByExample(example);
	}

	public boolean isLeaf(int menuId, List<TabSysMenu> allList) {
		for (TabSysMenu bean : allList) {
			if (bean.getParentId() == menuId) {
				return false;
			}
		}
		return true;
	}
}
