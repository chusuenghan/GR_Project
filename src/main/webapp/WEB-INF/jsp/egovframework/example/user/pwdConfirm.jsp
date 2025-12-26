<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 확인</title>
</head>
<body>
	<form action="userUpdatePage.do" method="post">
		<input type="hidden" name="userId" value="${USER.userId}">
		비밀번호 확인 : <input type="password" name="pwd">
		<div>
			<button type="submit">확인</button>
			<button type="button" onclick="window.history.go(-1)">이전</button>
		</div>
	</form>
</body>
</html>