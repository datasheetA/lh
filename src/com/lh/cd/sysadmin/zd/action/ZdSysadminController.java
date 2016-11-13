package com.lh.cd.sysadmin.zd.action;

import java.util.ArrayList;
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
import com.lh.cd.pub.exception.AppException;
import com.lh.cd.sysadmin.zd.service.ZdSysadminService;

@Controller
@RequestMapping(value = "/sysadmin/zd")
public class ZdSysadminController extends AppController {

	private static Logger logger = Logger.getLogger(ZdSysadminController.class);

	@Autowired
	private ZdSysadminService service;

	@ResponseBody
	@RequestMapping(value = "/getZdSexCtrl")
	public Map<String, Object> getZdSexList(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			rsMap.put("zdList", service.getSexList());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询性别出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getZdCertTypeCtrl")
	public Map<String, Object> getZdCertTypeList(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			rsMap.put("zdList", service.getCertTypeList());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询证件类型出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getZdEducationCtrl")
	public Map<String, Object> getZdEducationList(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			rsMap.put("zdList", service.getEducationList());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询教育情况出错！");
		}
		return rsMap;
	}

	@ResponseBody
	@RequestMapping(value = "/getZdDistrictCtrl")
	public Map<String, Object> getZdDistrictList(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			Parameter para = new Parameter(request);
			int parentId = para.getInt("parentId", -1);
			logger.debug("getZdDistrictCtrl parentId=" + parentId);
			if (parentId == -1) {
				rsMap.put("zdList", new ArrayList<Object>());
			}
			else {
				rsMap.put("zdList", service.getDistrictByParentIdList(parentId));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("查询地区出错！");
		}
		return rsMap;
	}

}
