package com.lh.cd.pub.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

import com.l_h.cd.pub.http.AdminLoginUtil;
import com.lh.cd.pub.Constants;
import com.lh.cd.pub.exception.AppException;

/**
 *	
 *   过滤器 
 * @author Administrator
 */
public class CheckValidAdminFilter implements Filter {
	private DriverManagerDataSource dataSource = null;

	private String loginUrl;// 登录url
	private String[] onlyCheckLoginUrls;// 仅仅需要检查是否登录的url
	private String[] noCheckUrls;// 不需要检查的url

	private PathMatcher pathMatcher = new AntPathMatcher();
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
		//springmvc中 ,urlPathHelper：用于解析请求路径的工具类，默认为org.springframework.web.util.UrlPathHelper
	
	/**
	 * 初始化
	 */
	public void init(FilterConfig config) throws ServletException {
		System.out.println("######init " + this.getClass().getName() + ".");
		// 读取配置参数
		String dbConfigFile = config.getInitParameter("dbConfigFile");
		this.loginUrl = config.getInitParameter("loginUrl");
		String onlyCheckLoginConfig = config.getInitParameter("onlyCheckLoginUrls");
		String noCheckConfig = config.getInitParameter("noCheckUrls");

		if (this.loginUrl == null || onlyCheckLoginConfig == null || noCheckConfig == null || dbConfigFile == null) {
			throw new ServletException("web.xml中filterServlet没有配置初始化参数\"loginUrl\"，\"onlyCheckLoginUrls\"，\"noCheckUrls\"或\"dbConfigFile\".");
		}

		// 数据库连接信息
		ApplicationContext cxt = new ClassPathXmlApplicationContext(dbConfigFile);
		dataSource = (DriverManagerDataSource) cxt.getBean("dataSource");

		// 解析仅仅需要检查是否登录的url
		StringBuffer sbf = new StringBuffer();
		StringTokenizer strTokenizer = new StringTokenizer(onlyCheckLoginConfig, ";");
		while (strTokenizer.hasMoreTokens()) {
			String item = strTokenizer.nextToken();
			if (item != null && item.length() > 0) {
				sbf.append((sbf.toString().length() == 0) ? "" : ";");
				
				sbf.append(item);
			}
		}
		this.onlyCheckLoginUrls = sbf.toString().split(";");

		// 解析不需要检查的url
		sbf = new StringBuffer();
		strTokenizer = new StringTokenizer(noCheckConfig, ";");
		while (strTokenizer.hasMoreTokens()) {
			String item = strTokenizer.nextToken();
			if (item != null && item.length() > 0) {
				sbf.append((sbf.toString().length() == 0) ? "" : ";");
				sbf.append(item);
			}
		}
		this.noCheckUrls = sbf.toString().split(";");
	}

	/**
	 * 过滤的方法 
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("Filter protects only HTTP resources");
		}

		request.setCharacterEncoding("utf-8");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		try {
			this.checkAccessUrl(httpRequest, httpResponse);
			fc.doFilter(httpRequest, httpResponse);
		}catch (AppException e) {
//			 e.printStackTrace();
			System.out.println("######deny url:" + this.urlPathHelper.getLookupPathForRequest(httpRequest) + "");
			String requestedWith = httpRequest.getHeader("x-requested-with");
			/*
				使用
				 request.getHeader("x-requested-with");
				  为 null，则为传统同步请求；
				 为 XMLHttpRequest，则为 Ajax 异步请求。 
			 */
			if (requestedWith != null && "XMLHttpRequest".equals(requestedWith)) {//异步请求
				httpResponse.setHeader("session-status", "timeout");
				httpResponse.setCharacterEncoding("utf-8");
				httpResponse.setStatus(500);//HTTP 500 – 内部服务器错误
				httpResponse.getWriter().print(getJsonError(e.getMessage()));
				return;
			}else {//传统同步请求,
				httpResponse.setCharacterEncoding("gbk");
				PrintWriter out = httpResponse.getWriter();
				out.println("<html>");
				out.println("<script>");
				out.println("alert('" + e.getMessage() + "');");
				out.println("window.open ('" + httpRequest.getContextPath() + loginUrl + "','_top');");
							// window.open('lh/login.do','_top');
				out.println("</script>");
				out.println("</html>");
				return;
			}
		}
	}

	/**
	 * 过滤url
	 * 
	 * @param request
	 * @param response
	 * @throws AppException
	 */
	private void checkAccessUrl(HttpServletRequest request, HttpServletResponse response) throws AppException {
		
		//拿到请求路径
		String url = this.urlPathHelper.getLookupPathForRequest(request);
		url = url.replaceAll("/+", "/");

		if (!this.isNeedCheck(url)) {// 不需要检查的url,直接返回
			return;
		} 
		
		else if (loginUrl != null && this.pathMatcher.match(url, loginUrl)) {// 如果是登录url,也不需要检查
			return;
		}
		
		else if (this.isOnlyCheckLogin(url)) {// 只检查是否登录的url
			if (AdminLoginUtil.getLoginUserId(request) == 0) {
				throw new AppException("您没有访问此页面的权限！");
			} else {
				this.checkSessionId(request);
			}
		}
		
		else {//剩下的就是要去检查权限
			if (this.checkAuth(request, response, url)) {
				this.checkSessionId(request);
			} else {
				throw new AppException("您没有访问此页面的权限！");
			}
		}
	}

	/**
	 * 判断url是否需要检查
	 * (返回false表示不需要检查)
	 * (返回true表示需要检查)
	 * @param urlPath
	 * @return
	 */
	private boolean isNeedCheck(String urlPath) {
		for (int i = 0; i < this.noCheckUrls.length; i++) {
			String registeredPath = noCheckUrls[i];
			if (registeredPath == null) {
				throw new IllegalArgumentException("Entry number " + i + " in noCheckAccessUrls array is null.");
			} else {
				if(urlPath.endsWith(".js")){
					return false;
				}
				if (this.pathMatcher.match(registeredPath, urlPath)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 判断url是否只需要检查登录
	 * (返回true表示需要检查是否登陆)
	 * @param urlPath
	 * @return
	 */
	private boolean isOnlyCheckLogin(String urlPath) {
		for (int i = 0; i < this.onlyCheckLoginUrls.length; i++) {
			String registeredPath = onlyCheckLoginUrls[i];
			if (registeredPath == null) {
				throw new IllegalArgumentException("Entry number " + i + " in checkAccessUrls array is null.");
			}
			else {
				if (this.pathMatcher.match(registeredPath, urlPath)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查用户是否有访问此url的权限
	 * (返回true说明这个用户有权访问这个url)
	 * @param request
	 * @param response
	 * @param url
	 * @return
	 * @throws AppException
	 */
	private boolean checkAuth(HttpServletRequest request, HttpServletResponse response, String url) throws AppException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			int userId = AdminLoginUtil.getLoginUserId(request);
			if (userId == Constants.SYSTEM_ADMIN_ID) {
				return true;
			}
			
			Connection conn = dataSource.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select x.menu_id, x.index_url, y.file_url from ");
			sql.append("(select a.menu_id, a.index_url from ");
			sql.append("((select menu_id from tab_user_auth where user_id=" + userId + ") ");
			sql.append("union (select m.menu_id from tab_sys_role_auth m, tab_user_role n ");
			sql.append("where m.role_id=n.role_id  and n.user_id=" + userId);
			sql.append(")) as b, tab_sys_menu a ");
			sql.append("where a.menu_id=b.menu_id and status=1");
			sql.append(") x left join ");
			sql.append("(select menu_id,file_url from tab_sys_menu_file) y ");
			sql.append("on x.menu_id=y.menu_id");

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toString());
			while (rs.next()) {
				
				//-----------
				String indexUrl = rs.getString("index_url");//这个用户能访问的菜单url
				if (indexUrl != null && !indexUrl.equals("") && this.pathMatcher.match(indexUrl, url)) {
					
					System.out.println("ok__menu__url");
					return true;
				}
				
				//-----------
				String fileUrl = rs.getString("file_url");//这个用户能访问的菜单文件url

				if(fileUrl	!=  null	&&	!"".equals(fileUrl)){
					String[]	str = fileUrl.split("\n");
					for (String string : str) {
						if (string != null && !string.equals("") && this.pathMatcher.match(string, url)) {
							System.out.println("ok__menu_file__url");
							return true;
						}
					}
				
				}
				//------------
			}
		}
		catch (AppException e) {
			 e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("检查用户访问权限出错！");
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (rs != null) {
					rs.close();
					rs = null;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 验证session中的sessionid和数据库中存的sessionid
	 * @param request
	 * @throws AppException
	 */
	private void checkSessionId(HttpServletRequest request) throws AppException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			int userId = AdminLoginUtil.getLoginUserId(request);

			String sessionId = null;
			String loginSessionId = request.getSession().getId();//本来session中就存的有sessionid

			Connection conn = dataSource.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("select cur_session_id from tab_user_account where user_id=" + userId);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toString());
			if (rs.next()) {
				sessionId = rs.getString("cur_session_id");
			}

			if (sessionId == null || loginSessionId == null) {
				throw new AppException("检查用户登录信息出错！");
			}
			else if (!sessionId.equals(loginSessionId)) {
				throw new AppException("此账号已在其他地方登录！");
			}
		}
		catch (AppException e) {
			throw new AppException(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AppException("检查用户登录信息出错！");
		}
		finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}

				if (rs != null) {
					rs.close();
					rs = null;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 得到json格式的错误信息
	 * 
	 * @param message
	 * @return
	 */
	private String getJsonError(String message) {
		return "{\"failure\":true,\"exception.message\":\"" + message + "\",\"errorCode\":\"" + Constants.ERRORCODE_NEED_LOGIN + "\"}";
	}

	public void destroy() {
	}
}
