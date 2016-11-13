package com.l_h.cd.pub.mail;

/**
 * 此处插入类型说明。 创建日期：(2006-4-21 14:57:16)
 * @author：Administrator
 */
import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.apache.log4j.Logger;

import com.lh.cd.pub.exception.AppException;

public class SendMail {
	private static Logger logger = Logger.getLogger(SendMail.class);
	
	/** 读取配置文件位置 */
	private static final String ConfigureFileName = "/config/mail.properties";
	private static Properties prop = null;
	private static boolean isLoaded = false;
	private static String mailAccount = null;
	private static String mailPass = null;
	private static String smtpHost = null;// SMTP主机地址
	
	private String mailTo = null;// 要发送Mail地址
	private String mailFrom = null;// Mail发送的起始地址

	private boolean debug = false;// 是否采用调试方式
	private String subject;// Mail主题
	private String msgContent;// Mail内容
	private Vector<String> attachedFileList;
	private String messageContentMimeType = "text/html; charset=gbk";
	private String mailbccTo = null;// 暗送
	private String mailccTo = null;// 抄送

	private static String activeAccountUrl = null;//激活账号Url
	private static String retrievePasswordUrl = null;// 找回密码url

	@SuppressWarnings("static-access")
	public void setMailAccount(String strAccount) {
		this.mailAccount = strAccount;
	}

	@SuppressWarnings("static-access")
	public void setMailPass(String strMailPass) {
		this.mailPass = strMailPass;
	}	

	@SuppressWarnings("static-access")
	public void setSMTPHost(String host) {
		this.smtpHost = host;
	}
	
	public void setDebug(boolean debugFlag) {
		this.debug = debugFlag;
	}
	
	public void setAttachedFileList(Vector<String> filelist) {
		this.attachedFileList = filelist;
	}

	public void setMailbccTo(String bccto) {
		this.mailbccTo = bccto;
	}

	public void setMailccTo(String ccto) {
		this.mailccTo = ccto;
	}

	public void setMailFrom(String from) {
		this.mailFrom = from;
	}

	public void setMailTo(String to) {
		this.mailTo = to;
	}

	public void setMessageContentMimeType(String mimeType) {
		this.messageContentMimeType = mimeType;
	}

	public void setMsgContent(String content) {
		this.msgContent = content;
	}

	public void setSubject(String sub) {
		this.subject = sub;
	}

	public String getRetrievePasswordUrl() {
		return retrievePasswordUrl;
	}

	public String getActiveAccountUrl() {
		return activeAccountUrl;
	}

	/**
	 * SendMailService 构造子注解。
	 */
	public SendMail() {
		super();
	}
	

	static {
		if (!isLoaded) {
			prop = new Properties();

			InputStream is = null;

			try {
				logger.debug("loading "+ ConfigureFileName + " ......");
				is = SendMail.class.getResourceAsStream(ConfigureFileName);
				prop.load(is);
				if (is != null) {
					is.close();
					is = null;
				}

				if (prop != null) {
					smtpHost = prop.getProperty("smtpHost");
					mailAccount = prop.getProperty("mailAccount");
					mailPass = prop.getProperty("mailPass");
					activeAccountUrl = prop.getProperty("activeAccountUrl");
					retrievePasswordUrl = prop.getProperty("retrievePasswordUrl");
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
	 * 此处插入方法说明。
	 */
	public void init() {
	}

	private void fillMail(Session session, MimeMessage msg) throws IOException, MessagingException, AppException {
		String fileName = null;
		Multipart mPart = new MimeMultipart();

		if (mailFrom == null) {
			setMailFrom(mailAccount);
		}
		
		if (mailFrom != null) {
			msg.setFrom(new InternetAddress(mailFrom));
			logger.debug("发送人Mail地址：" + mailFrom);
		}
		else {
			throw new AppException("没有指定发送人邮件地址！");
		}

		if (mailTo != null) {
			InternetAddress[] address = InternetAddress.parse(mailTo);
			msg.setRecipients(Message.RecipientType.TO, address);
			logger.debug("收件人Mail地址：" + mailTo);
		}
		else {
			throw new AppException("没有指定收件人邮件地址！");
		}

		if (mailccTo != null) {
			InternetAddress[] ccaddress = InternetAddress.parse(mailccTo);
			logger.debug("CCMail地址：" + mailccTo);
			msg.setRecipients(Message.RecipientType.CC, ccaddress);
		}

		if (mailbccTo != null) {
			InternetAddress[] bccaddress = InternetAddress.parse(mailbccTo);
			logger.debug("BCCMail地址：" + mailbccTo);
			msg.setRecipients(Message.RecipientType.BCC, bccaddress);
		}

		msg.setSubject(subject);
		InternetAddress[] replyAddress = { new InternetAddress(mailFrom) };
		msg.setReplyTo(replyAddress);

		// create and fill the first message part
		MimeBodyPart mBodyContent = new MimeBodyPart();
		if (msgContent != null) {
			mBodyContent.setContent(msgContent, messageContentMimeType);
		}
		else {
			mBodyContent.setContent("", messageContentMimeType);
		}
		mPart.addBodyPart(mBodyContent);

		// attach the file to the message
		if (attachedFileList != null) {
			for (Enumeration<?> fileList = attachedFileList.elements(); fileList.hasMoreElements();) {
				fileName = (String) fileList.nextElement();
				MimeBodyPart mBodyPart = new MimeBodyPart();
				// attach the file to the message
				FileDataSource fds = new FileDataSource(fileName);
				logger.debug("Mail发送的附件为：" +  fileName);
				mBodyPart.setDataHandler(new DataHandler(fds));
				mBodyPart.setFileName(fileName);
				mPart.addBodyPart(mBodyPart);
			}
		}
		msg.setContent(mPart);
		msg.setSentDate(new Date());
	}

	public void addAttach(String filename) {
		if (attachedFileList == null){
			attachedFileList = new Vector<String>();			
		}
		attachedFileList.add(filename);
	}
	
	/**
	 * 发送e_mail，返回类型为boolean 当返回值为true时，抛出异常时发送失败
	 */
	@SuppressWarnings("static-access")
	public boolean sendMail() throws AppException {
		logger.debug("smtpHost="+smtpHost+",mailAccount="+mailAccount+",mailPass="+mailPass);
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.auth", "true");
		MailAuthenticator auth = new MailAuthenticator(mailAccount, mailPass);
		Session session = Session.getInstance(props, auth);
		session.setDebug(debug);
		MimeMessage msg = new MimeMessage(session);
		Transport trans = null;
		try {
			fillMail(session, msg);

			// send the message
			trans = session.getTransport("smtp");
			try {
				trans.connect(smtpHost, mailAccount, mailPass);
			}
			catch (AuthenticationFailedException e) {
				e.printStackTrace();
				throw new AppException("邮件服务器认证错误.");
			}
			catch (MessagingException e) {
				throw new AppException("连接邮件服务器错误.");
			}
			trans.send(msg);
			trans.close();
			trans = null;
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				logger.error(ex.toString());
				ex.printStackTrace();
			}
			throw new AppException("发送邮件失败.");
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("发送邮件失败.");
		}
		finally {
			try {
				if (trans != null && trans.isConnected()) {
					trans.close();
				}
			}
			catch (Exception e) {
				logger.error(e.toString());
			}
		}

		logger.error("发送邮件成功！");
		return true;
	}
	
	public static void main(String[] argv) throws Exception {
		SendMail sm = new SendMail();
		sm.setSMTPHost("smtp.sohu.com");
		sm.setMailAccount("cdygw@sohu.com");
		sm.setMailPass("82903020");
		sm.setMailFrom("cdygw@sohu.com");
		sm.setMailTo("zhangyu@forlink.com");
		sm.setMsgContent("内容");
		sm.setSubject("标题");
		sm.sendMail();
	}
}
