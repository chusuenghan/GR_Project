<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
</head>
<body>
	<form action="userUpdate.do" method="post" id="userUpdateForm">
		<table>
			<thead>
				<tr>
					<th colspan="2">회원정보 수정</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>아이디</th>
					<td><input type="text" name="userId" required="required" value="${USER.userId}" readonly/></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" id="pwdInput1" name="pwd" required="required"/></td>
				</tr>
				<tr>
					<th>비밀번호 확인</th>
					<td><input type="password" id="pwdInput2" required="required"/></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" name="name" value="${USER.name}" required="required"/></td>
				</tr>
				<tr>
					<th>부서</th>
					<td>
						<select name="deptCd" required="required">
							<option value="${USER.deptCd}" hidden>${USER.deptNm}</option>
							<c:forEach items="${dept}" var="item">
								<option value="${item.deptCd}">${item.deptNm}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>직급</th>
					<td><input type="text" name="job" value="${USER.job}"/></td>
				</tr>
				<tr>
					<th>연락처</th>
					<td><input type="text" name="phone" value="${USER.phone}"/></td>
				</tr>
				<tr>
					<th>메일</th>
					<td><input type="text" name="email" value="${USER.email}"/></td>
				</tr>
			</tbody>
		</table>
		
		<button type="button" onclick="window.history.back()">이전</button>
		<button type="button" id="okBtn">확인</button>
		<button type="button" id="deleteBtn">탈퇴</button>
	</form>
</body>

<script>
	
	window.onload = function() {
		
		var okBtn = document.getElementById("okBtn");
		
		okBtn.onclick = function() {
			var pwd1 = document.getElementById("pwdInput1").value;
			var pwd2 = document.getElementById("pwdInput2").value;
			
			console.log(pwd1, pwd2);
			
			if(pwd1 == pwd2) {
				document.getElementById("userUpdateForm").submit();
			} else {
				alert("비밀번호가 일치하지 않습니다.");
			}
		}
		
		var deleteBtn = document.getElementById("deleteBtn");
		
		deleteBtn.onclick = function(){
			if(confirm("정말로 탈퇴하시겠습니까?")){
// 				console.log("탈퇴");
				window.location.href="userDelete.do";
			} else{
// 				console.log("취소");
				return false;
			}
		}
	}
	
</script>

</html>