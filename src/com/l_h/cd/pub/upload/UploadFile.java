package com.l_h.cd.pub.upload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l_h.cd.pub.other.ThumbsGenerate;
import com.l_h.cd.pub.util.StringUtil;
import com.lh.cd.pub.exception.AppException;

/**
 * 文件上传类
 * @author Administrator
 *
 */
public class UploadFile {
	private static Logger logger = Logger.getLogger(UploadFile.class);
	
	private List<Map<String, String>> uploadList = null;
	private String accessUrl = "";
	private String allowExtName = "";
	private String notAllowExtName = "";
	private String saveDir = "";
	private String uptosftpdir="";
	private int maxSize = 1024;
	private int maxFileNameLength = 100;
	private String serverFileName = "";
	private boolean genThumbs = false;
	private String allowThumbsExtName = "";
	private int thumbsWidth = 60;
	private int thumbsHeight = 60;
	


	public List<Map<String, String>> getUploadList() {
		return uploadList;
	}

	public void setUploadList(List<Map<String, String>> uploadList) {
		this.uploadList = uploadList;
	}

	public int getMaxFileNameLength() {
		return maxFileNameLength;
	}

	public void setMaxFileNameLength(int maxFileNameLength) {
		this.maxFileNameLength = maxFileNameLength;
	}

	public boolean isGenThumbs() {
		return genThumbs;
	}

	public void setGenThumbs(boolean genThumbs) {
		this.genThumbs = genThumbs;
	}


	public String getAllowThumbsExtName() {
		return allowThumbsExtName;
	}

	public void setAllowThumbsExtName(String allowThumbsExtName) {
		this.allowThumbsExtName = allowThumbsExtName;
	}

	public int getThumbsWidth() {
		return thumbsWidth;
	}

	public void setThumbsWidth(int thumbsWidth) {
		this.thumbsWidth = thumbsWidth;
	}

	public int getThumbsHeight() {
		return thumbsHeight;
	}

	public void setThumbsHeight(int thumbsHeight) {
		this.thumbsHeight = thumbsHeight;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}
	public String getUptosftpdir() {
		return uptosftpdir;
	}

	public void setUptosftpdir(String uptosftpdir) {
		this.uptosftpdir = uptosftpdir;
	}

	public String getAllowExtName() {
		return allowExtName;
	}

	public void setAllowExtName(String allowExtName) {
		this.allowExtName = allowExtName;
	}

	public String getNotAllowExtName() {
		return notAllowExtName;
	}

	public void setNotAllowExtName(String notAllowExtName) {
		this.notAllowExtName = notAllowExtName;
	}

	public String getSaveDir() {
		return saveDir;
	}

	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public String getServerFileName() {
		return serverFileName;
	}

	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}

	public UploadFile(){
		uploadList = new ArrayList<Map<String, String>>();
	}	

	public void doUpload(HttpServletRequest request) throws AppException {
		MultipartHttpServletRequest multipartRequest = null;
		try {			
			try {
				multipartRequest = (MultipartHttpServletRequest) request;
			}
			catch (Exception e) {
				throw new AppException("请选择要上传的文件！");
			}

			//读取系统变量
			String separator = System.getProperty("file.separator");
			
			//保存路径
			StringBuffer sbfBaseSaveDir = new StringBuffer();
			sbfBaseSaveDir.append(getSaveDir());
			if (!new File(sbfBaseSaveDir.toString()).exists()) {
				new File(sbfBaseSaveDir.toString()).mkdirs();
			}
			
			//访问路径
			StringBuffer sbfBaseAccessUrl = new StringBuffer();
			sbfBaseAccessUrl.append(getAccessUrl());

			//建立月份目录
			SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMM");
			String dateDirName = dateFormater.format(new Date());

			sbfBaseSaveDir.append(separator);
			sbfBaseSaveDir.append(dateDirName);
			if (!new File(sbfBaseSaveDir.toString()).exists()) {
				new File(sbfBaseSaveDir.toString()).mkdirs();
			}
			
			//绝对路径
			sbfBaseAccessUrl.append("/");
			sbfBaseAccessUrl.append(dateDirName);

			logger.debug("init file.separator=" + separator);
			logger.debug("init MaxFileNameLength=" + getMaxFileNameLength());
			logger.debug("init MaxSize=" + getMaxSize());
			logger.debug("init sbfBaseSaveDir=" + sbfBaseSaveDir.toString());
			logger.debug("init sbfBaseAccessUrl=" + sbfBaseAccessUrl.toString());

			int filecount = 1;
			for (Iterator<?> it = multipartRequest.getFileNames(); it.hasNext();) {
				StringBuffer sbfSaveDir = new StringBuffer();				
				sbfSaveDir.append(sbfBaseSaveDir.toString());
				
				StringBuffer sbfAccessUrl = new StringBuffer();
				sbfAccessUrl.append(sbfBaseAccessUrl.toString());
				
				String key = (String) it.next();
				MultipartFile file = multipartRequest.getFile(key);

				if (file == null || file.isEmpty()) {
					continue;
				}

				// 文件大小判断
				long FileSize = file.getSize();
				if (FileSize > getMaxSize() * 1024 * 1024) {
					logger.debug("FileSize="+FileSize+",getMaxSize="+getMaxSize() * 1024 * 1024);
					throw new AppException("上传文件不能大于" + getMaxSize() + "M！");
				}

				//原始文件名
				String OriginalFileName = file.getOriginalFilename();

				// 文件名长度判断
				if (OriginalFileName.length() > getMaxFileNameLength()) {
					throw new AppException("上传文件名长度不能大于" + getMaxFileNameLength() + "！");
				}

				//扩展名
				String ExtFileName = "";
				if (OriginalFileName != null && OriginalFileName.indexOf(".") >= 0) {
					ExtFileName = OriginalFileName.substring(OriginalFileName.lastIndexOf(".")+1);
				}

				// 判断扩展名是否允许
				if (!isAllowExtName(ExtFileName, getAllowExtName())){
					throw new AppException("文件扩展名"+ExtFileName+"不允许！");
				}

				// 判断扩展名是否允许
				if (isNotAllowExtName(ExtFileName, getNotAllowExtName())){
					throw new AppException("文件扩展名"+ExtFileName+"不允许！");
				}

				// 以时间作为文件名
				dateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				
				// 时间文件名加上序号避免重复
				StringBuffer sbfTimeFileName = new StringBuffer();
				if (getServerFileName() == null || getServerFileName().equals("")){
					sbfTimeFileName.append(dateFormater.format(new Date()));
					sbfTimeFileName.append(String.valueOf(filecount));
				}
				else{
					sbfTimeFileName.append(getServerFileName());
				}
				
				//加上扩展名
				if(!ExtFileName.equals("")){
					sbfTimeFileName.append(".");
					sbfTimeFileName.append(ExtFileName);
				}

				//保存路径
				sbfSaveDir.append(separator);
				sbfSaveDir.append(sbfTimeFileName.toString());

				//访问路径
				sbfAccessUrl.append("/");
				sbfAccessUrl.append(sbfTimeFileName.toString());

				if (!save(sbfSaveDir.toString(), file)) {
					logger.debug("file.getName=" + file.getName());
					logger.debug("file.getSize=" + FileSize);
					logger.debug("file.getOriginalFilename=" + OriginalFileName);
					logger.debug("ExtFileName=" + ExtFileName);
					logger.debug("timeFileName=" + sbfTimeFileName.toString());
					logger.debug("sbfSaveDir=" + sbfSaveDir.toString());
					logger.debug("sbfAccessUrl=" + sbfAccessUrl.toString());
					throw new AppException("文件上传出错！");
				}

				StringBuffer sbfThumbsUrl = new StringBuffer();
				if (isGenThumbs()){
					//生缩略图
					if (isAllowExtName(ExtFileName, getAllowThumbsExtName())){
						ThumbsGenerate thumbs = new ThumbsGenerate();
						thumbs.setAllowThumbsExtName(getAllowThumbsExtName());
						thumbs.setThumbsWidth(getThumbsWidth());
						thumbs.setThumbsHeight(getThumbsHeight());
						
						String thumbsName = thumbs.genThumbs(sbfSaveDir.toString(), sbfAccessUrl.toString());
						sbfThumbsUrl.append(thumbsName);
						thumbs = null;
					}
				}
								
				Map<String, String> map = new HashMap<String, String>();
				map.put("submitname", file.getName());
				map.put("originalname", OriginalFileName);
				map.put("size", ""+FileSize);
				map.put("extname", ExtFileName);
				map.put("savedir", sbfSaveDir.toString());
				logger.debug("submitname="+file.getName()+",getName=" + sbfSaveDir);
				map.put("accessurl", StringUtil.Replace(sbfAccessUrl.toString(), "\\", "/"));
				map.put("thumbsurl", StringUtil.Replace(sbfThumbsUrl.toString(), "\\", "/"));
				uploadList.add(map);
				
				filecount++;

				sbfTimeFileName = null;
				sbfThumbsUrl = null;
				
				sbfSaveDir = null;
				sbfAccessUrl = null;
			}
			sbfBaseSaveDir = null;
			sbfBaseAccessUrl = null;
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("文件上传出错！");
		}
	}

	
	private boolean save(String path, MultipartFile file) throws Exception {
		boolean result = false;
		if (file != null && !file.isEmpty()) {
			DataOutputStream out = null;
			InputStream is = null;
			try {
				out = new DataOutputStream(new FileOutputStream(path));
				is = file.getInputStream();
				byte[] buffer = new byte[1024];
				int len =0;
				while ((len = is.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				is.close();
				is = null;
				
				out.close();				
				out = null;
				
				result = true;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			}			
		}
		return result;
	}
	
	private boolean isAllowExtName(String extName, String allowExtNames){
		try {
			if (allowExtNames == null || allowExtNames.equals("")){
				//没有任何限制
				return true;
			}
			
			if (extName != null  && !extName.equals("")){
				String[] arr = allowExtNames.split(",");
				for(int i=0; i<arr.length; i++){
					if (arr[i] != null && (extName.toLowerCase().equals(arr[i].toLowerCase()) || arr[i].equals("*"))){
						return true;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	private boolean isNotAllowExtName(String extName, String notAllowExtNames){
		try {
			if (notAllowExtNames == null || notAllowExtNames.equals("")){
				//没有任何限制
				return false;
			}
			
			if (extName != null  && !extName.equals("")){
				String[] arr = notAllowExtNames.split(",");
				for(int i=0; i<arr.length; i++){
					if (arr[i] != null && (extName.toLowerCase().equals(arr[i].toLowerCase()) || arr[i].equals("*"))){
						return true;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean hasUploadFile(String SubmitName){
		if (uploadList != null){
			for (int i=0; i<uploadList.size(); i++){
				Map<?, ?> map = (HashMap<?, ?>) uploadList.get(i);
				if (map.get("submitname") != null && map.get("submitname").equals(SubmitName)){
					return true;
				}
			}
		}
		return false;
	}
	
	private String getUploadItem(String SubmitName, String target) throws AppException{
		logger.debug("SubmitName "+SubmitName+" target "+target);
		if (uploadList != null){
			for (int i=0; i<uploadList.size(); i++){
				Map<?, ?> map = (HashMap<?, ?>) uploadList.get(i);
				if (map.get("submitname") != null && map.get("submitname").equals(SubmitName)){
					if (map.get(target) != null){
						return (String)map.get(target);
					}
				}
			}
		}
		throw new AppException("获取上传文件信息出错！");
	}
	
	public String getOriginalFileName(String SubmitName) throws AppException{
		return getUploadItem(SubmitName, "originalname");
	}
	
	public String getAccessUrl(String SubmitName) throws AppException{
		return getUploadItem(SubmitName, "accessurl");
	}
	
	public String getSaveDir(String SubmitName) throws AppException{
		return getUploadItem(SubmitName, "savedir");
	}
	public String getExtName(String SubmitName) throws AppException{
		return getUploadItem(SubmitName, "extname");
	}
	
	public String getThumbsUrl(String SubmitName) throws AppException{
		return getUploadItem(SubmitName, "thumbsurl");
	}
	
	public long getSize(String SubmitName) throws AppException{
		try {
			return Long.parseLong(getUploadItem(SubmitName, "size"));
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw new AppException("获取上传文件信息出错！");
		}
	}
}
