<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<style>
    body {
        font-family: Arial, sans-serif;
        align-items: center;
        height: 100vh;
    }
    header {
        background-color: black;
        color: white;
        padding: 10px 0;
        text-align: center;
    }

    header div {
        display: inline-block;
        margin: 5px;
        background-color: white;
        color: black;
        padding: 10px;
        cursor: pointer;
    }

    header div:hover {
        background-color: #eee;
    }
	section{
    	display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 80vh;
    }
    table {
        border: 1px solid black;
        border-collapse: collapse;
        width: 350px;
    }

    th, td {
        border: 1px solid black;
        padding: 8px;
        text-align: left;
    }

    input[type="text"], input[type="password"] {
        width: 100%;
        box-sizing: border-box;
    }

    button {
        width: 70px;
        height:30px;
    }
    #buttonWrapper {
        margin-top: 10px;
        text-align: center;
    }
    
    #buttonWrapper button {
        margin: 0 5px; /* 버튼 사이의 간격 */
    }
</style>
</head>
<body>
	<header>
        <div onclick="window.location.href='${pageContext.request.contextPath}/tradeListPage.do'">메인화면</div>
        
        
    </header>
    <section>
    <form action="signUp.do" method="post" id="signUpForm">
        <table>
            <caption style="border: 1px solid black; background-color: #f5f5f5;">회원가입</caption>
            <tbody>
                <tr>
                    <th>아이디*</th>
                    <td><input type="text" name="userId" required="required"/></td>
                </tr>
                <tr>
                    <th>비밀번호*</th>
                    <td><input type="password" id="pwdInput1" name="pwd" required="required"/></td>
                </tr>
                <tr>
                    <th>비밀번호 확인*</th>
                    <td><input type="password" id="pwdInput2" name="pwdCheck" required="required"/></td>
                </tr>
            </tbody>
        </table>
        <br>
        <div id="buttonWrapper">
        <button type="button" id="okBtn">확인</button>
        <button type="reset">초기화</button>
        
        </div>
    </form>
	</section>
<script>
    window.onload = function() {
        var okBtn = document.getElementById("okBtn");
        
        okBtn.onclick = function() {
            var pwd1 = document.getElementById("pwdInput1").value;
            var pwd2 = document.getElementById("pwdInput2").value;

            // Check if any of the input fields are empty
            var inputs = document.getElementsByTagName('input');
            for (var i = 0; i < inputs.length; i++) {
                if (!inputs[i].value) {
                    alert(inputs[i].name + "을(를) 입력해주세요!");
                    inputs[i].focus();
                    return;
                }
            }

            if(pwd1 == pwd2) {
                document.getElementById("signUpForm").submit();
            } else {
                alert("비밀번호가 일치하지 않습니다.");
                document.getElementById("pwdInput1").focus();
            }
        }
    }
</script>

</body>
</html>