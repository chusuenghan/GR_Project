<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Notification</title>
<script src="https://code.jquery.com/jquery-3.6.4.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<style>
	body{
		width:80%;
		margin:0 auto;
        font-family: 'Arial', sans-serif;
        background-color: #f5f5f5;
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
	.trade-card[data-ch-trade-id] {
	    border: 2px solid yellow;
	    display: inline-block;
	    padding-right:30px;
	    margin: 5px;
	    position: relative; /* For the delete button positioning */
	    height:30px;
	    font-size:13px;
	    background-color:white;
	}
	
	/* Push notification input and button */
	.push-input-container {
	    display: flex;
	    margin-bottom: 20px;
	}
	
	.push-input {
	    width:200px;
	    margin-right: 10px;
	}
	#keyCon{
		display:flex;
		width:100%;
	}
	#allContent{
		display: flex;
		width:100%;
		flex-wrap:wrap;
	}
	/* For the tradeList */
	.trade-list-card[data-tr-trade-id] {
	    border: 2px solid yellow;
	    display: flex;
	    align-items: center;
	    width: 30%; /* To ensure 2 cards fit in one row */
	    height:150px;
	    margin: 5px;
	    position: relative;
	    background-color:white;
	}
	
	.trade-list-card img {
	    float: left;
	    margin: 10px 10px 10px 10px;
	    
	    height:80%;
	    width:20%;
    	box-sizing: border-box;
	}
	
	.trade-card-content {
	    overflow: hidden; /* To handle the floated img */
	}
	
	/* Delete Button Common Style */
	.delete-button {
	    position: absolute;
	    right: 5px;
	    background-color: red;
	    color: white;
	    border: none;
	    cursor: pointer;
	}
	.tr-delete-button{
		position: absolute;
		top:5px;
	    right: 5px;
	    background-color: red;
	    color: white;
	    border: none;
	    cursor: pointer;
	}
</style>
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
	document.getElementById("allowPush").addEventListener("click", function() {
	    Notification.requestPermission().then(function(permission) {
	      if (permission === "granted") {
	        console.log('사용자가 알림 권한을 허용하였습니다.');
	        // 권한이 허용되면 이후 웹 푸시 구독 로직을 여기서 수행할 수 있습니다.
	        navigator.serviceWorker.ready.then(function(registration) {
				  var vapidPublicKey = "BDoPnG6xdPyxwyzX-EQLZh8DRKfkT5jfC95knX47jyLecsZ0JLTbN0iKBrzesfPq6F_nWl7XnHb_hK-ohOEM01M";
	
				  var convertedVapidKey = urlBase64ToUint8Array(vapidPublicKey);
	
				  registration.pushManager.subscribe({
				    userVisibleOnly: true,
				    applicationServerKey: convertedVapidKey
				  }).then(function(subscription) {
					  var dataToSend = {
							    userId: mainUser,
							    subscription: subscription
							};

							var jsonData = JSON.stringify(dataToSend);
					  $.ajax({
					      url: '${pageContext.request.contextPath}/saveNotiSubscription',
					      type: 'POST',
					      data: JSON.stringify(dataToSend),
					      dataType: 'json',
					      contentType: "application/json", 
					      success: function(response) {
					        if (response.success) {
					          console.log('Server responded with an success.');
					        }
					      },
					      error: function(err) {
					        console.error('Failed to send subscription to server:', err);
					      }
					    });
				  });
				});
	
				// Base64 문자열을 Uint8Array로 변환하는 유틸리티 함수
				function urlBase64ToUint8Array(base64String) {
				  var padding = '='.repeat((4 - base64String.length % 4) % 4);
				  var base64 = (base64String + padding)
				    .replace(/\-/g, '+')
				    .replace(/_/g, '/');
	
				  var rawData = window.atob(base64);
				  var outputArray = new Uint8Array(rawData.length);
	
				  for (let i = 0; i < rawData.length; ++i) {
				    outputArray[i] = rawData.charCodeAt(i);
				  }
				  return outputArray;
				} 
	      } else {
	        console.log('사용자가 알림 권한을 거부하였습니다.');
	      }
	    });
	});

	/* document.getElementById("denyPush").addEventListener("click", function() {
	    console.log('사용자가 알림 권한 요청을 거부하였습니다.');
	    // 필요한 경우 여기서 추가적인 로직을 수행할 수 있습니다.
	    navigator.serviceWorker.ready.then(function(registration) {
	    	  registration.pushManager.getSubscription().then(function(subscription) {
	    	    if (subscription) {
	    	      return subscription.unsubscribe();
	    	    }
	    	  });
	    	});
	}); */
	}
	
	$(".delete-button").click(function() {
        var keywordId = $(this).data("delete-keyword-id");
        deleteKeyword(keywordId);
    });
	
	$(".tr-delete-button").click(function(event) {
        var notiId = $(this).data("delete-noti-id");
        deletenoti(event, notiId);
    });
	
	$('#submitKeyword').click(function() {
        var keyword = $('#keywordInput').val();

        if (keyword === "") {
            alert("키워드를 입력해주세요.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/saveKeyword.do", // 여기에 서버 URL을 입력하세요.
            data: {
            	userId : mainUser,
                keyword: keyword
            },
            success: function(response) {
                // 서버 응답 처리. 성공적인 경우 페이지 새로 고침.
                if (response.success) {
                    location.reload(); // 페이지 새로고침
                } else {
                    alert("키워드 설정에 실패했습니다.");
                }
            },
            error: function(error) {
                console.error("Error:", error);
                alert("서버와의 통신 중 문제가 발생했습니다.");
            }
        });
    });
	
});
function trClickEvent(tradeId){
	location.href="${pageContext.request.contextPath}/tradeInfoPage/"+ tradeId + ".do";
}
function deleteKeyword(keywordId) {
    $.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/deleteKeyword.do",
        data: { keywordId: keywordId },
        success: function(response) {
            $(`.trade-card[data-ch-trade-id='${keywordId}']`).hide();
            window.location.reload(); 
        }
    });
}
function deletenoti(event, notiId) {
	event.stopPropagation();
	
    $.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/deletenoti.do",
        data: { notiId: notiId },
        success: function(response) {
            $(`.trade-list-card[data-tr-trade-id='${notiId}']`).remove();
            window.location.reload(); 
        }
    });
}
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
	<button id="allowPush">Push 알림 허용</button>
	<!-- <button id="denyPush">Push 알림 거부</button> -->
	<hr>
	<div class="push-input-container">
	    <input type="text" id="keywordInput" class="push-input" placeholder="키워드 입력...">
	    <button id="submitKeyword">키워드 설정</button>
	</div>
	<hr>
	<div id="keyCon">
	<c:forEach items="${userkeyword }" var="item">
            <div class="trade-card" data-ch-trade-id="${item.keywordId}">
            <button class="delete-button" data-delete-keyword-id="${item.keywordId}">X</button>
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.keyword }"/></strong><br>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
	<hr>
	<div id="allContent">
	<c:forEach items="${tradeList }" var="item">
	<c:choose>
		<c:when test="${item.notiStatus == 'IN' }">
            <div class="trade-list-card" data-tr-trade-id="${item.notiId}" onclick="trClickEvent('${item.tradeId}')">
            	<button class="tr-delete-button" data-delete-noti-id="${item.notiId}">X</button>
                <img alt="image" width="50px" height="50px" src="/img/<c:out value='${item.image }'/>">
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.title }"/></strong><br>
                        <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div>게시물이 등록되었습니다.</div>
                    <div>알림을 받은 인원 : <c:out value="${item.userCount }"/>명</div>
                </div>
            </div>
        </c:when>
        <c:when test="${item.notiStatus == 'RE' }">
            <div class="trade-list-card" data-tr-trade-id="${item.tradeId}" onclick="trClickEvent('${item.tradeId}')">
            <button class="tr-delete-button" data-delete-noti-id="${item.notiId}">X</button>
                <img alt="image" width="50px" height="50px" src="/img/<c:out value='${item.image }'/>">
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.title }"/></strong><br>
                        <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div>게시물에 타인이 대화요청을 했습니다.</div>
                    <div>알림을 받은 인원 : <c:out value="${item.userCount }"/>명</div>
                </div>
            </div>
        </c:when>
        <c:when test="${item.notiStatus == 'MY' }">
            <div class="trade-list-card" data-tr-trade-id="${item.tradeId}">
            <button class="tr-delete-button" data-delete-noti-id="${item.notiId}">X</button>
                <img alt="image" width="100px" height="100px" src="/img/<c:out value='${item.image }'/>">
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.title }"/></strong><br>
                        <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div>게시물에 대화요청이 왔습니다.</div>
                </div>
            </div>
        </c:when>
        <c:when test="${item.notiStatus == 'MA' }">
            <div class="trade-list-card" data-tr-trade-id="${item.tradeId}">
            <button class="tr-delete-button" data-delete-noti-id="${item.notiId}">X</button>
                <img alt="image" width="50px" height="50px" src="/img/<c:out value='${item.image }'/>">
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.title }"/></strong><br>
                        <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div>게시물이 타인과 매칭되었습니다.</div>
                    <div>알림을 받은 인원 : <c:out value="${item.userCount }"/>명</div>
                </div>
            </div>
        </c:when>
        <c:when test="${item.notiStatus == 'MM' }">
            <div class="trade-list-card" data-tr-trade-id="${item.tradeId}">
            <button class="tr-delete-button" data-delete-noti-id="${item.notiId}">X</button>
                <img alt="image" width="50px" height="50px" src="/img/<c:out value='${item.image }'/>">
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.title }"/></strong><br>
                        <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div>게시물과 매칭되었습니다.</div>
                </div>
            </div>
        </c:when>
        <c:when test="${item.notiStatus == 'EN' }">
            <div class="trade-list-card" data-tr-trade-id="${item.tradeId}">
            <button class="tr-delete-button" data-delete-noti-id="${item.notiId}">X</button>
                <img alt="image" width="100px" height="100px" src="/img/<c:out value='${item.image }'/>">
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.title }"/></strong><br>
                        <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div>게시물이 거래 완료 되었습니다.</div>
                </div>
            </div>
        </c:when>
        
    </c:choose>
    </c:forEach>
    </div>
</body>
</html>