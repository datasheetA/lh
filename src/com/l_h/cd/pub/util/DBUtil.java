package com.l_h.cd.pub.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * DB相关操作 Apr 10, 2008
 */
public class DBUtil {

	private static Logger logger = Logger.getLogger(DBUtil.class);

	public void praseDBConfigFile(String xmlpath, Properties configProp) throws ParserConfigurationException, SAXException, IOException {
		File is = new File(xmlpath);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringElementContentWhitespace(true);

		DocumentBuilder db;
		db = dbf.newDocumentBuilder();

		db.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//				if (publicId.equals("-//iBATIS.com//DTD SQL Map Config 2.0//EN")) {
//					return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='utf-8'?>".getBytes()));
//				}
				return null;
			}
		});
System.out.println("xmlpath="+xmlpath);
		Document doc = db.parse(is);
		Element root = doc.getDocumentElement();

		NodeList list1 = root.getElementsByTagName("beans");
		System.out.println("list1="+list1.getLength());
		for (int i = 0; i < list1.getLength(); i++) {
			Node node1 = list1.item(i);
			if (node1.getNodeName().equals("beans")) {
				System.out.println("node1.getNodeName()="+node1.getNodeName());
				NodeList list2 = node1.getChildNodes();
				for (int j = 0; j < list2.getLength(); j++) {
					Node node2 = list2.item(j);
					if (node2.getNodeName().equals("dataSource")) {
						NodeList list3 = node2.getChildNodes();
						for (int k = 0; k < list3.getLength(); k++) {
							Node node3 = list3.item(k);
							if (node3.getNodeName().equals("property")) {
								if (node3.getAttributes().getNamedItem("name").getNodeValue().equals("JDBC.Username")) {
									configProp.put("dbUserName", node3.getAttributes().getNamedItem("value").getNodeValue());
								}
								else if (node3.getAttributes().getNamedItem("name").getNodeValue().equals("JDBC.Password")) {
									configProp.put("dbUserPasswd", node3.getAttributes().getNamedItem("value").getNodeValue());
								}
								else if (node3.getAttributes().getNamedItem("name").getNodeValue().equals("JDBC.Driver")) {
									configProp.put("dbDriver", node3.getAttributes().getNamedItem("value").getNodeValue());
								}
								else if (node3.getAttributes().getNamedItem("name").getNodeValue().equals("JDBC.ConnectionURL")) {
									configProp.put("dbUrl", node3.getAttributes().getNamedItem("value").getNodeValue());
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void getBean(String xmlpath) throws SQLException
    {
//		ApplicationContext cxt =
//			 new FileSystemXmlApplicationContext(xmlpath);
//		Object obj=cxt.getBean("dataSource");
//

		ApplicationContext cxt = new ClassPathXmlApplicationContext(xmlpath);
		DriverManagerDataSource obj=(DriverManagerDataSource)cxt.getBean("dataSource"); 
		System.out.println(obj.getUrl());
		System.out.println(obj.getUsername());
		System.out.println(obj.getPassword());
		System.out.println(obj.getConnection());


		Connection conn = obj.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("select *  from tab_user_account");
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.toString());
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int totalColumn = rsmd.getColumnCount();
				for (int i = 1; i <= totalColumn; i++) {
					System.out.println("==>"+rsmd.getColumnName(i).toLowerCase()+":"+((rs.getString(i) == null) ? "" : rs.getString(i)));
				}
				rsmd = null;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		finally {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}

			if (rs != null) {
				rs.close();
				rs = null;
			}
		}
    }


	public static void main(String[] arg) throws Exception {
		logger.debug("main run.");
		DBUtil db = new DBUtil();
		//db.praseDBConfigFile("E:\\eclipse\\workspace\\sm3-admin\\WebRoot\\WEB-INF\\dispatcher-servlet.xml", new Properties());
		db.getBean("E:\\eclipse\\workspace\\sm3-admin\\WebRoot\\WEB-INF\\dispatcher-servlet.xml");
	}
}
