package com.l_h.cd.pub.other;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import com.lh.cd.pub.exception.AppException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 生产缩略图
 * @author Administrator
 *
 */
public class ThumbsGenerate {
	private static Logger logger = Logger.getLogger(ThumbsGenerate.class);
	
	private final static String thumbsExtName = "_s.jpg";
	private String allowThumbsExtName = "jpg,png,gif";
	private int thumbsWidth = 60;
	private int thumbsHeight = 60;
	
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

	/**
	 * 产生缩略图
	 * @param savePath 保存的物理路径
	 * @param accessUrl 保存的url
	 * @return
	 * @throws AppException
	 */
	public String genThumbs(String savePath, String accessUrl) throws AppException {
		String ret = "";
		FileOutputStream out = null;
		String baseName = "";
		logger.debug("savePath=" + savePath);
		try {
			String extName = "";
			if (savePath.indexOf(".") > 0) {
				// 获取文件名和扩展名
				baseName = savePath.substring(0, savePath.lastIndexOf("."));
				extName = savePath.substring(savePath.lastIndexOf(".") + 1);
			}
			else {
				throw new AppException("文件名不正确！");
			}

			// 判断扩展名是否正确
			if (!isAllowExtName(extName, getAllowThumbsExtName())){
				logger.info("extName=" + extName);
				logger.info("AllowThumbsExtName=" + getAllowThumbsExtName());
				throw new AppException("文件扩展名" + extName + "不允许生成缩略图！");
			}

			// 缩略图名称
			String  destName = baseName + thumbsExtName;
			int width = getThumbsWidth(); // 缩略图宽
			int height = getThumbsHeight(); // 缩略图高

			File srcFile = new File(savePath); // 读入文件
			if (!srcFile.exists()) {
				throw new AppException("生成缩略图出错（原始图片文件不存在）！");
			}
			
			Image srcImage = javax.imageio.ImageIO.read(srcFile); // 构造Image对象

			BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			bufImage.getGraphics().fillRect(0, 0, width, height);
			bufImage.getGraphics().drawImage(srcImage, 0, 0, width, height, null); // 绘制缩略图
			out = new FileOutputStream(destName); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(bufImage); // 近JPEG编码

			//返回可访问的url
			if (accessUrl.indexOf(".") > 0) {
				ret = accessUrl.substring(0, accessUrl.lastIndexOf(".")) + thumbsExtName;
			}
			else {
				ret = accessUrl;
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("生成缩略图出错！");
		}
		finally {
			if (out != null) {
				try {
					out.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	
	/**
	 * 判断扩展名是否允许
	 * @param extName 扩展名
	 * @param allowExtNames 允许的扩展名（例：txt,pdf,doc,xls）
	 * @return
	 */
	private boolean isAllowExtName(String extName, String allowExtNames){
		try {
			if (allowExtNames == null || allowExtNames.equals("")){
				//没有任何限制
				return true;
			}
			
			if (extName != null  && !extName.equals("")){
				String[] arr = allowExtNames.split(",");
				for(int i=0; i<arr.length; i++){
					if (extName.equals(arr[i])){
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
