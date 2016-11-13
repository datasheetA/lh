package com.lh.cd.sysadmin.frame.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l_h.cd.pub.http.AppController;
import com.l_h.cd.pub.http.JsonMap;
import com.l_h.cd.pub.upload.UploadConfiger;
import com.l_h.cd.pub.upload.UploadFile;
import com.lh.cd.pub.exception.AppException;

@Controller
@RequestMapping(value = "/sysadmin/upload")
public class UploadController extends AppController {

	private static Logger logger = Logger.getLogger(UploadController.class);

	@ResponseBody
	@RequestMapping(value = "/uploadImageCtrl")
	public Map<String, Object> uploadImage(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			logger.debug("####################uploadImage:"+request.getContextPath());
			UploadFile upload = new UploadFile();
			upload.setSaveDir(UploadConfiger.getStringValue("ImageSaveDir"));
			upload.setAllowExtName(UploadConfiger.getStringValue("ImageAllowExtName"));
			upload.setNotAllowExtName(UploadConfiger.getStringValue("ImageNotAllowExtName"));
			upload.setMaxFileNameLength(UploadConfiger.getIntValue("ImageMaxFileNameLength"));
			upload.setMaxSize(UploadConfiger.getIntValue("ImageMaxSize"));
			upload.setAccessUrl(UploadConfiger.getStringValue("ImageAccessUrl"));
			upload.setGenThumbs(false);
			upload.doUpload(request);
			if (upload.hasUploadFile("uploadSelectedImage")) {
				logger.debug("size=" + upload.getSize("uploadSelectedImage"));
				logger.debug("url=" + upload.getAccessUrl("uploadSelectedImage"));
				logger.debug("thumbsurl=" + upload.getThumbsUrl("uploadSelectedImage"));
				logger.debug("originalname=" + upload.getOriginalFileName("uploadSelectedImage"));
				logger.debug("extname=" + upload.getExtName("uploadSelectedImage"));
				rsMap.put("url", request.getContextPath()+upload.getAccessUrl("uploadSelectedImage"));
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("上传图片出错！");
		}
		
		return rsMap;
	}


	@ResponseBody
	@RequestMapping(value = "/uploadAttachCtrl")
	public Map<String, Object> uploadAttach(HttpServletRequest request, HttpServletResponse response) throws AppException {
		JsonMap rsMap = new JsonMap();
		try {
			logger.debug("####################uploadAttach:"+request.getContextPath());
			UploadFile upload = new UploadFile();
			upload.setSaveDir(UploadConfiger.getStringValue("AttachSaveDir"));
			upload.setAllowExtName(UploadConfiger.getStringValue("AttachAllowExtName"));
			upload.setNotAllowExtName(UploadConfiger.getStringValue("AttachNotAllowExtName"));
			upload.setMaxFileNameLength(UploadConfiger.getIntValue("AttachMaxFileNameLength"));
			upload.setMaxSize(UploadConfiger.getIntValue("AttachMaxSize"));
			upload.setAccessUrl(UploadConfiger.getStringValue("AttachAccessUrl"));
			upload.setGenThumbs(false);
			upload.doUpload(request);
			if (upload.hasUploadFile("uploadSelectedAttach")) {
				logger.debug("size=" + upload.getSize("uploadSelectedAttach"));
				logger.debug("url=" + upload.getAccessUrl("uploadSelectedAttach"));
				logger.debug("thumbsurl=" + upload.getThumbsUrl("uploadSelectedAttach"));
				logger.debug("originalname=" + upload.getOriginalFileName("uploadSelectedAttach"));
				logger.debug("extname=" + upload.getExtName("uploadSelectedAttach"));
				rsMap.put("url", request.getContextPath()+upload.getAccessUrl("uploadSelectedAttach"));
				rsMap.put("orgFileName", upload.getOriginalFileName("uploadSelectedAttach"));
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("上传附件出错！");
		}
		
		return rsMap;
	}
}
