package com.l_h.cd.pub.upload;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.l_h.cd.pub.util.StringUtil;
import com.lh.cd.pub.exception.AppException;

/**
 * 公用读取配置文件类
 * @author tom
 * @version 1.0 2006-02-01
 */
public class UploadConfiger {

	/** 读取配置文件位置 */
	private static final String ConfigureFileName = "/config/upload.properties";

	private static Logger logger = Logger.getLogger(UploadConfiger.class);
	
	private static Properties prop = null;

	private static boolean isLoaded = false;

	static {
		if (!isLoaded) {
			prop = new Properties();

			InputStream is = null;

			try {
				logger.debug("loading "+ ConfigureFileName + " ......");
				is = UploadConfiger.class.getResourceAsStream(ConfigureFileName);
				prop.load(is);
				if (is != null) {
					is.close();
					is = null;
				}

				if (prop != null) {
					isLoaded = true;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 功能：根据配置名称获取配置值
	 * @param confName 配置名称
	 * @return 从配置文件读取的配置信息
	 */
	public static String getStringValue(String confName) throws AppException {
		String ret = prop.getProperty(confName);
		if (ret == null || ret.equals("")){
			//logger.debug("读取配置文件出错（"+confName+"）!");
			//throw new AppException("读取配置文件出错！");
			return "";
		}
		else{
			return ret.trim();			
		}
	}

	
	/**
	 * 功能：根据配置名称获取配置值
	 * @param confName 配置名称
	 * @return 从配置文件读取的配置信息
	 */
	public static int getIntValue(String confName) throws AppException {
		String strSize = getStringValue(confName);
		if (StringUtil.isNumber(strSize)){			
			return Integer.parseInt(strSize);
		}
		else{
			return 0;
		}
	}

}