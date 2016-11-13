package com.lh.cd.sysadmin.role.action;

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
import com.l_h.cd.pub.util.ExtjsUtil;
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.entity.model.TabSysRole;
import com.lh.cd.pub.data.ResultMap;
import com.lh.cd.pub.db.Paginate;
import com.lh.cd.pub.exception.AppException;
import com.lh.cd.sysadmin.role.service.RoleService;

@Controller
@RequestMapping(value = "/sysadmin/role")
public class RoleController extends AppController {

	private static Logger logger = Logger.getLogger(RoleController.class);

	@Autowired
	private RoleService service;

	@ResponseBody
	@RequestMapping(value = "getRoleListCtrl")
	public JsonMap getRoleList(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			String operFlag = para.getString("operFlag", "");
			String roleName = para.getString("roleName", "");
			String status = para.getString("status", "");
			String remark = para.getString("remark", "");
			String sort = para.getString("sort", "");
			String dir = para.getString("dir", "");
			String searchField = para.getString("query", "");
			int start = para.getInt("start", 0);
			int limit = para.getInt("limit", 0);

			logger.debug("operFlag=" + operFlag);
			logger.debug("roleName=" + roleName);
			logger.debug("status=" + status);
			logger.debug("remark=" + remark);
			logger.debug("start=" + start);
			logger.debug("limit=" + limit);
			logger.debug("sort=" + sort);
			logger.debug("dir=" + dir);
			logger.debug("searchField=" + searchField);

			Map<String, Object> paraMap = new HashMap<String, Object>();
			 if (!searchField.equals("")) {
				paraMap.put("roleName", searchField);
			}
			else{
				paraMap.put("roleName", roleName);
				paraMap.put("status", status);
				paraMap.put("remark", remark);
			}
			paraMap.put("sort", sort);
			paraMap.put("dir", dir);

			Paginate paginate = ExtjsUtil.getExtPaginate(start, limit);
			List<TabSysRole> roleList = service.getRolePageList(paraMap, paginate);

			rsMap.put("roleList", roleList);
			rsMap.put("totalProperty", paginate.getTotalCount());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询角色出错！");
		}
		return rsMap;
	}


	@ResponseBody
	@RequestMapping(value = "/getRoleInfoCtrl")
	public JsonMap getRoleInfo(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int roleId = para.getInt("roleId", 0);
			logger.debug("roleId=" + roleId);

			TabSysRole roleInfo = service.getRole(roleId);
			if (roleInfo == null) {
				throw new AppException("角色不存在！");
			}

			rsMap.put("roleInfo", roleInfo);
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询角色出错！");
		}
		return rsMap;
	}

	
	@ResponseBody
	@RequestMapping(value = "/deleteRoleCtrl")
	public JsonMap deleteRole(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			String operFlag = para.getString("operFlag", "");
			logger.debug("operFlag=" + operFlag);
			if (operFlag.equals("delete")) {
				String roleIds = para.getString("roleIds", "");
				logger.debug("roleIds=" + roleIds);
				service.deleteRole(roleIds);
			}
			rsMap.put("message", "删除角色成功！");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("删除角色出错！");
		}
		return rsMap;
	}

	
	@ResponseBody
	@RequestMapping(value = "/saveRoleCtrl")
	public JsonMap saveRole(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		String operFlag = "";
		try {
			Parameter para = new Parameter(request);
			TabSysRole roleInfo = null;
			operFlag = para.getString("operFlag", "");
			int roleId = para.getInt("roleId", 0);
			logger.debug("##################saveRoleInfo operFlag=" + operFlag + ",roleId=" + roleId);

			if (operFlag.equals("update")) {// 修改
				roleInfo = service.getRole(roleId);

				if (roleInfo == null) {
					throw new AppException("角色不存在！");
				}
			}
			else if (operFlag.equals("add")) {// 新增
				roleInfo = new TabSysRole();
			}
			else {
				throw new AppException("操作不合法！");
			}

			roleInfo.setRoleName(para.getString("roleName", ""));
			roleInfo.setStatus(para.getInt("status", 1));
			roleInfo.setRemark(para.getString("remark", ""));
			roleInfo.setUpdTime(new Date());
			String userIds = para.getString("userIds", "");

			if (service.existSameRoleName(roleInfo.getRoleName(), (roleInfo.getRoleId() == null) ? 0 : roleInfo.getRoleId())) {
				throw new AppException("存在相同的角色名称！");
			}

			if (operFlag.equals("update")) {
				service.updateRole(roleInfo, userIds);
				rsMap.put("message", "修改角色成功！");
			}
			else {
				service.insertRole(roleInfo, userIds);
				rsMap.put("message", "添加角色成功！");
			}

		}
		catch (AppException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException((operFlag.equals("update") ? "修改角色出错！" : "添加角色出错！"));
		}
		return rsMap;
	}

	
	@ResponseBody
	@RequestMapping(value = "/getSelectedUserByRoleIdCtrl")
	public JsonMap getSelectedUserByRoleId(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int roleId = para.getInt("roleId", 0);
			String sort = para.getString("sort", "");
			String dir = para.getString("dir", "");
			
			logger.debug("roleId=" + roleId);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("roleId", roleId);
			paraMap.put("sort", sort);
			paraMap.put("dir", dir);
			rsMap.put("userList", service.getSelectedUserByRoleId(paraMap));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询角色用户出错！");
		}
		return rsMap;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getUsableUserByRoleIdCtrl")
	public JsonMap getUsableUserByRoleId(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int roleId = para.getInt("roleId", 0);
			String sort = para.getString("sort", "");
			String dir = para.getString("dir", "");
			logger.debug("roleId=" + roleId);
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("roleId", roleId);
			paraMap.put("sort", sort);
			paraMap.put("dir", dir);
			rsMap.put("userList", service.getUsableUserByRoleId(paraMap));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询角色用户出错！");
		}
		return rsMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRoleAuthTreeCtrl")
	public Map<String, Object> getRoleAuthTree(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int roleId = para.getInt("roleId", 0);
			int menuId = para.getInt("menuId", 0);
			logger.debug("roleId=" + roleId + ",menuId=" + menuId);

			List<TabSysMenu> allMenuList = service.getAllMenu();
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("roleId", roleId);
			paraMap.put("parentId", menuId);
			paraMap.put("status", 1);
			List<?> list = service.getRoleAuthTreeByRoleId(paraMap);
			List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
			for(int i=0; i<list.size(); i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				ResultMap resultMap = (ResultMap) list.get(i);
				int curMenuId = resultMap.getInt("menu_id",0);
				map.put("id", curMenuId);
				map.put("text", resultMap.getString("menu_name",""));
				map.put("leaf", service.isLeaf(curMenuId, allMenuList));
				map.put("checked", resultMap.getInt("role_id",0)!=0);
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
	@RequestMapping(value = "/saveRoleAuthCtrl")
	public Map<String, Object> saveRoleAuth(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int roleId = para.getInt("roleId", 0);
			String menuIds = para.getString("menuIds", "");
			logger.debug("roleId=" + roleId + ",menuIds=" + menuIds);
			service.saveRoleAuth(roleId, menuIds);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("保存角色权限出错！");
		}
		return rsMap;
	}
}
