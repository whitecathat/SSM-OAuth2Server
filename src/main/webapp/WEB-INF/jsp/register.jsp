<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>${message}</h1>
<form action="/center/user/check" method="POST">
	昵称：<input type="text" name="name"/><br>
	邮箱：<input type="text" name="email"><br>
	密码：<input type="password" name="password"/><br>
	密码：<input type="password" name="repassword" placeholder="与上述密码一致"/><br>
	<input type="submit" value="注册">
</form>
</body>
</html>