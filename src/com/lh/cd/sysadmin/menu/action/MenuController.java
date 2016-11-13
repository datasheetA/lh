package com.lh.cd.sysadmin.menu.action;

import java.util.Date;
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
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.pub.exception.AppException;
import com.lh.cd.sysadmin.menu.service.MenuService;

@Controller
@RequestMapping(value = "/sysadmin/menu")
public class MenuController extends AppController {

	private static Logger logger = Logger.getLogger(MenuController.class);

	@Autowired
	private MenuService service;

	@ResponseBody
	@RequestMapping(value = "/getMenuTreeCtrl")
	public Map<String, Object> getMenuTree(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int menuId = para.getInt("menuId", 0);
			logger.debug("menuId=" + menuId);

			rsMap.put("menuList", service.getMenuListByParentId(menuId));
			// Thread.sleep(3000);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询菜单出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getMenuInfoCtrl")
	public JsonMap getMenuInfo(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int menuId = para.getInt("menuId", 0);
			logger.debug("menuId=" + menuId);

			TabSysMenu menuInfo = service.getMenuInfo(menuId);

			if (menuInfo == null) {
				throw new AppException("菜单不存在！");
			}

			rsMap.put("menuInfo", menuInfo);
			rsMap.put("menuFileUrl", service.getMenuFileUrl(menuId));
			if (menuInfo.getParentId() != 0) {
				rsMap.put("parentName", service.getFullMenuName(menuInfo.getParentId()));
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询菜单出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteMenuCtrl")
	public JsonMap deleteMenu(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			String operFlag = para.getString("operFlag", "");
			logger.debug("operFlag=" + operFlag);
			if (operFlag.equals("delete")) {
				int menuId = para.getInt("menuId", 0);
				logger.debug("menuId=" + menuId);
				service.deleteMenu(menuId);
			}
			rsMap.put("message", "删除菜单成功！");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("删除菜单出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/saveMenuCtrl")
	public JsonMap saveMenu(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		String operFlag = "";
		try {
			Parameter para = new Parameter(request);
			TabSysMenu menuInfo = null;
			operFlag = para.getString("operFlag", "");
			int menuId = para.getInt("menuId", 0);
			logger.debug("##################saveMenuInfo operFlag=" + operFlag + ",menuId=" + menuId);

			if (operFlag.equals("update")) {// 修改
				menuInfo = service.getMenuInfo(menuId);

				if (menuInfo == null) {
					throw new AppException("菜单不存在！");
				}
			}
			else if (operFlag.equals("add")) {// 新增
				menuInfo = new TabSysMenu();
			}
			else {
				throw new AppException("操作不合法！");
			}

			menuInfo.setMenuName(para.getString("menuName", ""));
			menuInfo.setParentId(para.getInt("parentId", 0));
			menuInfo.setIndexUrl(para.getString("indexUrl", ""));
			menuInfo.setStatus(para.getInt("status", 1));
			menuInfo.setSiteNo(para.getString("siteNo", ""));
			menuInfo.setRemark(para.getString("remark", ""));
			menuInfo.setUpdTime(new Date());
			String otherUrl = para.getString("otherUrl", "");

			if (service.existSameMenuName(menuInfo.getMenuName(), menuInfo.getParentId(), (menuInfo.getMenuId() == null) ? 0 : menuInfo.getMenuId())) {
				throw new AppException("存在相同的菜单名称！");
			}

			if (operFlag.equals("update")) {
				service.updateMenu(menuInfo, otherUrl);
				rsMap.put("message", "修改菜单成功！");
			}
			else {
				service.insertMenu(menuInfo, otherUrl);
				rsMap.put("message", "添加菜单成功！");
			}
			rsMap.put("menuId", menuInfo.getMenuId());
		}
		catch (AppException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException((operFlag.equals("update") ? "修改菜单出错！" : "添加菜单出错！"));
		}
		return rsMap;
	}

}
