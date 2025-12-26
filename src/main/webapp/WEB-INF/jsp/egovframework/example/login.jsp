<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	
<meta charset="UTF-8">
<title>로그인</title>
<script src="https://code.jquery.com/jquery-3.6.4.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<style>
		body {
        font-family: Arial, sans-serif;
        align-items: center;
        height: 100vh; /* 추가된 코드: 화면 높이를 100%로 설정 */
        margin: 0; /* 추가된 코드: 마진 제거 */
    }
    header {
    	width: 100%;
        background-color: black;
        color: white;
        padding: 10px 0;
        text-align: center;
    }
    section{
    	display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 80vh;
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
    fieldset {
        width: 300px;
        border: 1px solid gray; /* 추가된 코드: 테두리 설정 */
        padding: 15px; /* 추가된 코드: 안쪽 여백 설정 */
        box-sizing: border-box; /* 추가된 코드: 패딩이 너비에 포함되도록 설정 */
    }
    legend {
        font-weight: bold;
    }
    table {
        width: 100%; /* 추가된 코드: 테이블을 fieldset 너비에 맞게 확장 */
        margin: 0 auto; /* 추가된 코드: 중앙 정렬 */
    }
    input[type="text"], input[type="password"] {
        height:30px;
    }
    button {
        width: 70px;
        height: 30px;
        margin-right: 5px; /* 추가된 코드: 버튼 사이의 간격 설정 */
    }
    /* 버튼들을 중앙에 정렬하기 위한 스타일 */
    form {
        text-align: center;
    }
    
	</style>
<script>
document.addEventListener("DOMContentLoaded", function() {
    // 폼 제출 이벤트를 가로챕니다.
    document.querySelector('form').addEventListener('submit', function(event) {
        var userId = document.getElementById('userId');
        var pwd = document.getElementById('pwd');

        // 사용자 ID가 비어 있는지 확인합니다.
        if (userId.value.trim() === '') {
            alert('ID를 입력해주세요.');
            userId.focus();
            event.preventDefault();  // 폼 제출을 중단합니다.
            return false;           // 추가적인 폼 제출 처리를 중단합니다.
        }

        // 비밀번호가 비어 있는지 확인합니다.
        if (pwd.value.trim() === '') {
            alert('비밀번호를 입력해주세요.');
            pwd.focus();
            event.preventDefault();  // 폼 제출을 중단합니다.
            return false;           // 추가적인 폼 제출 처리를 중단합니다.
        }

        // 모든 검사를 통과하면 폼 제출을 계속합니다.
        return true;
    });
});
</script>
<script>
$(document).ready(function() {
	if ('serviceWorker' in navigator) {
	    navigator.serviceWorker.register('${pageContext.request.contextPath}/service-worker.js')
	    .then(function(registration) {
	        console.log('Service Worker 등록 성공:', registration);
	        // 원하시면 여기서 추가적인 작업을 할 수 있습니다. 예: Push 구독 설정
	    })
	    .catch(function(error) {
	        console.log('Service Worker 등록 실패:', error);
	    });
	}
	if ("Notification" in window) {
	    if (Notification.permission === "granted") {
	        console.log('이미 알림 권한이 허용되어 있습니다.');
	    } else if (Notification.permission === "denied") {
	        console.log('사용자가 알림 권한을 거부하였습니다.');
	    } else {
	        console.log('알림 권한이 아직 설정되지 않았습니다.');
	    }
	} else {
	    console.log('이 브라우저는 웹 알림을 지원하지 않습니다.');
	}
});
</script>
</head>
<body>
	<header>
        <div onclick="window.location.href='${pageContext.request.contextPath}/tradeListPage.do'">메인화면</div>
        
    </header>
    <section>
	<form action="login.do" method="post">
		<fieldset>
			<legend>Login</legend>
			<table>
				<tr>
					<th>ID</th>
					<td><input type="text" id="userId" name="userId" placeholder="ID를 입력해주세요"/></td>
				</tr>
				<tr>
					<th>PWD</th>
					<td><input type="password" id="pwd" name="pwd" placeholder="비밀번호를 입력해주세요"/></td>
				</tr>
			</table>
			<br>
			<button type="button" onclick="window.location.href='signUpPage.do'">회원가입</button>
			<button type="submit">로그인</button>
			<button type="reset">초기화</button>
		</fieldset>
	</form>
	</section>
</body>
</html>