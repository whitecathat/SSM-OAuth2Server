<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="/center/OAuth/authorize" method="POST">
	<input type="hidden" name="client_id" value="${clientId}">
	<input type="hidden" name="redirect_uri" value="${redirectURI}">
	<input type="hidden" name="response_type" value="code">
	邮箱：<input type="text" name="email"/>
	密码：<input type="password" name="password"/>
	<input type="submit" value="登录">
</form>
</body>
</html>