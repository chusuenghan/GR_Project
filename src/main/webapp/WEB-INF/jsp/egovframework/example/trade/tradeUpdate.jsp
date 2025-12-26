<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TradeUpdate</title>
	<style>
		body {
            font-family: Arial, sans-serif;
            
            align-items: center;
            height: 100vh;
            margin: 0;
        }
		header {
	        background-color: black;
	        color: white;
	        padding: 10px 0;
	        text-align: center;
	        width:100%;
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
            border-collapse: collapse;
            width: 500px;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }
		
		#buttonlay{
			display:flex;
		}
        button {
            margin-top: 10px;
            display: block;
            width: 100px;
            height: 30px;
            margin-left: auto;
            margin-right: auto;
        }
        input[type="text"], input[type="file"] {
	        width: 80%;
	        padding: 5px;
	    }
	
	    textarea {
	        width: 80%;
	        height: 100px;
	        padding: 5px;
	        resize: vertical; /* 사용자가 세로로만 크기를 조절할 수 있게 설정 */
	    }
	    @keyframes blink {
		  0% {
		    background-color: blue;
		  }
		  50% {
		    background-color: yellow;
		  }
		  100% {
		    background-color: blue;
		  }
		}
		
		.blinking {
		  animation: blink 1s infinite;
		}
	</style>
<script src="https://code.jquery.com/jquery-3.6.4.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script>
    window.onload = function() {
        document.querySelector('form').addEventListener('submit', function(event) {
            var title = document.querySelector('input[name="title"]');
            var contents = document.querySelector('textarea[name="contents"]');
            var price = document.querySelector('input[name="price"]');
            var uploadImage = document.querySelector('input[name="uploadImage"]');

            if (title.value.trim() === '') {
                alert('제목을 입력해주세요.');
                title.focus();
                event.preventDefault();
                return false;
            }

            if (contents.value.trim() === '') {
                alert('내용을 입력해주세요.');
                contents.focus();
                event.preventDefault();
                return false;
            }

            if (price.value.trim() === '') {
                alert('가격을 입력해주세요.');
                price.focus();
                event.preventDefault();
                return false;
            }

            if (!uploadImage.value) {
                alert('사진을 업로드해주세요.');
                uploadImage.focus();
                event.preventDefault();
                return false;
            }

            return true;
        });
    }
</script>
<script>
$(document).ready(function() {
	var mainUser = "${sessionScope.USER.userId}";
	var serverUrl = $("#serverUrl").val();
	var socket = new SockJS(serverUrl);
	var stompClient = Stomp.over(socket);
	if(mainUser && mainUser !== null)
	{
		stompClient.connect({}, function(frame) {
	        console.log('Connected: ' + frame);

	        stompClient.subscribe('/topic/notifications/' + mainUser, function(message) {
	        	//var receivedMessage = JSON.parse(message.body);
	            
	            console.log(message.body);
	            var notiDiv = $('#noti');
	            notiDiv.addClass('blinking');
	        });
	    });
		
		$.ajax({
            url: '${pageContext.request.contextPath}/findnotiRead.do',
            method: 'GET',
            data: { 
                userId : mainUser
            },
            dataType:'json',
            success: function(response) {
            	var notiDiv = $('#noti');
            	if (response.message === true) {
            		notiDiv.addClass('blinking');
                }
            	else {
                    notiDiv.removeClass('blinking');
                }
            
        },
        error: function (request,status,error) {
        	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
	    });
		
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
		
	}
});

</script>
</head>
<body>
	<input type="hidden" id="serverUrl" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/chat-st.do">
	<header>
        <div onclick="window.location.href='${pageContext.request.contextPath}/tradeListPage.do'">메인화면</div>
        <c:if test="${USER.userId == NULL }">
            <div onclick="window.location.href='loginPage.do'">로그인</div>
        </c:if>
        <c:if test="${USER.userId != NULL }">
            <div onclick="window.location.href='myPage.do'">[${USER.userId}]님</div>
            <div onclick="window.location.href='logout.do'">로그아웃</div>
            <div onclick="window.location.href='stomp_chat.do'">채팅</div>
            <div id ="noti" onclick="window.location.href='notificationPage.do'">알림</div>
        </c:if>
    </header>
    <section>
	<form action="tradeUpdate.do" method="post"  enctype="multipart/form-data">
		<input type="hidden" name="tradeId" value="${Trade.tradeId}"/>
		<table>
			<tr>
				<th>제목</th>
				<td><input type="text" name="title" value="${trade.title }"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea name="contents"><c:out value="${trade.contents }"></c:out></textarea></td>
			</tr>
			<tr>
				<th>가격</th>
				<td><input type="text" name="price" value="${trade.price }"></td>
			</tr>
			<tr>
				<th>사진</th>
				<td><input type="file" name="uploadImage">
					<%-- <input type="hidden" value="${USER.userId }" name="writerId"> --%>
				</td>
			</tr>
			
		</table>
		<div id="buttonlay">
		<button type="submit">등록</button>
		
		</div>
	</form>
	</section>
</body>
</html>