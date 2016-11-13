package com.lh.cd.sysadmin.frame.action;

import java.util.ArrayList;
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

import com.l_h.cd.pub.http.AdminLoginUtil;
import com.l_h.cd.pub.http.AppController;
import com.l_h.cd.pub.http.JsonMap;
import com.l_h.cd.pub.http.Parameter;
import com.lh.cd.entity.model.TabSysMenu;
import com.lh.cd.pub.Constants;
import com.lh.cd.pub.data.ResultMap;
import com.lh.cd.pub.exception.AppException;
import com.lh.cd.sysadmin.frame.service.FrameService;



@Controller
@RequestMapping(value = "/sysadmin/frame")
public class FrameController extends AppController {

	private static Logger logger = Logger.getLogger(FrameController.class);

	@Autowired
	private FrameService service;
	
	@ResponseBody
	@RequestMapping(value = "/getFrameMenuCtrl")
	public Map<String, Object> getFrameMenu(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
	
			Parameter para = new Parameter(request);
			int menuId = para.getInt("menuId", 0);
			int userId = AdminLoginUtil.getLoginUserId(request);
			logger.debug("menuId=" + menuId + ",userId=" + userId);

			List<TabSysMenu> allMenuList = service.getAllMenuList();
			Map<String, Object> paraMap = new HashMap<String, Object>();
			
			if(userId != Constants.SYSTEM_ADMIN_ID){
					//普通用户从这里进入,左右的树有哪些菜单都是由这里加了这句决定的
				paraMap.put("userId", userId);
			}
			
			paraMap.put("parentId", menuId);
			paraMap.put("status", 1);
			List<?> list = service.getRealUserAuthByUserId(paraMap);
			List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				ResultMap resultMap = (ResultMap) list.get(i);
				if (resultMap.getInt("user_id", 0) != 0) {
					int curMenuId = resultMap.getInt("menu_id", 0);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", curMenuId);
					map.put("text", resultMap.getString("menu_name", ""));
					map.put("indexUrl", resultMap.getString("index_url", "#"));
					map.put("leaf", service.isLeaf(curMenuId, allMenuList));
					menuList.add(map);
				}
			}
			rsMap.put("menuList", menuList);
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
	@RequestMapping(value = "/getLoginUserAccountCtrl")
	public Map<String, Object> getLoginUserAccount(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			int userId = AdminLoginUtil.getLoginUserId(request);
			String userAccount = AdminLoginUtil.getLoginUserAccount(request);
			logger.debug("userId="+userId+",userAccount="+userAccount);

			rsMap.put("userId", ""+userId);
			rsMap.put("userAccount", userAccount);
		}
		catch (AppException e) {
			throw new AppException(e.getErrorCode(), e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("获取登录用户信息出错！");
		}
		return rsMap;
	}
}
