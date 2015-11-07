<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<!DOCTYPE html>
<html>
<head>
<title><sitemesh:write property="title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath }/resource/css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
hr {
	margin: 8px 2px 8px 2px;
	border: 0;
	height: 1px;
	background: #000;
}

a.title {
	text-align: center;
	margin: 15px 50px 15px 50px;
	float: left;
}
</style>
<script type="text/javascript" data-main="${pageContext.request.contextPath }/resource/js/main" src="${pageContext.request.contextPath }/resource/js/require/require-2.1.20.min.js"></script>
<script type="text/javascript">
	document.ctx = "${pageContext.request.contextPath }";
	function login(){
		var name = document.getElementById("username").value;
		window.location.href = document.ctx + "/login/" + name;
		return false;
	};
</script>
<sitemesh:write property="head"/>
</head>
<body>
	<hr>
	<a class="title" href="${pageContext.request.contextPath }/restful/employees">employees</a>
	<a class="title" href="${pageContext.request.contextPath }/websocket/index">file</a>
	<a class="title">
		<span style="color:red;">${requestScope.error.message }</span>
	</a>
	<div style="margin:15px 50px 15px 0;float:right;">
		<input id="username" placeholder="用户姓名" style="width:80px;" value="张三1">
		<c:if test="${sessionScope.name == null || sessionScope.name == ''}">
			<a href="#" onclick="login()">登录</a>
		</c:if>${sessionScope.name}
		<c:if test="${sessionScope.name != null && sessionScope.name != ''}">
			<a href="${pageContext.request.contextPath }/logout">注销</a>
		</c:if>
	</div>
	<div style="margin:15px 0 15px 0;width:100%;float:left;"><hr></div>
	<sitemesh:write property="body"/>
</body>
</html>
