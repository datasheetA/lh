<%@ page language="java" pageEncoding="utf-8" import="com.lh.cd.entity.model.*"%>
<%
    String extjsPath = "/extjs-4.1.0";
	String appPath = request.getContextPath();
	String appBasePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + appPath;

%>
<script type="text/javascript">
  //域路径
  var appPath = '<%=appPath%>';
  //域名访问路径
  var appBasePath = '<%=appBasePath%>';
  
  var extjsPath = '<%=extjsPath%>';
	

  
</script>

<!-- 样式区 （开始） -->
<!-- Extjs 核心样式库 -->
<link rel="stylesheet" type="text/css" href="<%=appPath%><%=extjsPath%>/resources/css/ext-all.css" />
<!-- Extjs 中文字体显示效果补丁 -->
<link rel="stylesheet" type="text/css" href="<%=appPath%>/css/ext-ch-patch.css" />
<!-- 小图标样式 -->
<link rel="stylesheet" type="text/css" href="<%=appPath%>/css/style.css" />
<!-- 样式区 （结束） -->

<!-- JavaScript （开始） -->
<!-- Extjs 核心脚本 -->
<script type="text/javascript" src="<%=appPath%><%=extjsPath%>/bootstrap.js"></script>
<!-- Extjs 中文脚本 -->
<script type="text/javascript" src="<%=appPath%><%=extjsPath%>/locale/ext-lang-zh_CN.js"></script>
<!-- Extjs 补丁脚本 -->
<script type="text/javascript" src="<%=appPath%>/js/check.js"></script>
<script type="text/javascript" src="<%=appPath%>/js/common.js"></script>
<script type="text/javascript" src="<%=appPath%>/js/expection.js"></script>
<script type="text/javascript" src="<%=appPath%>/js/patch.js"></script>

<!-- 定义Extjs常量 -->
<script type="text/javascript">
  // 默认后台分页数据量
  var defaultPageSize = 10;
  var varCenterTabPanel=0;
  Ext.BLANK_IMAGE_URL = appPath + '/images/s.gif';
  Ext.form.Field.prototype.msgTarget = 'side'; 
</script>
<!-- JavaScript （结束） -->
