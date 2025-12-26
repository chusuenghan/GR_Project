<%@ page contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Chatting</title>
<script src="https://code.jquery.com/jquery-3.6.4.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f5f5f5;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        padding: 0 20px 20px 20px;
        flex-direction: column;
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
    
    #chatContainer {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
	}
	
	#usersList, #chatBox, #messageInput, #sendButton {
	    margin: 10px;
	}
	
	#usersList {
	    flex: 1;
	    width: 200px;
	    height: 500px;
	    background-color: white;
	    overflow-y: auto;
	    padding: 10px;
	    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
	    
	}
	
	
	#usersList > div {
    padding: 10px;
    border-bottom: 1px solid #ccc;
    cursor: pointer;
    background-color: gray;
    color:white;
	}
	
	#chatContent {
    display: flex;
    flex-direction: column; /* 수직 방향으로 배열 */
    width: 800px; /* usersList의 너비와 chatContent의 너비 합계 */
    height:650px;
	}
	
	#chatBox {
	    flex: 1;
	    width:600px;
	    height:440px;
	    overflow-y: auto;
	    padding: 0;
	    background-color: #fff;
	    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
	    margin-bottom: 20px;
	    margin-top: 0;
	}
	
	#inputContainer {
	    display: flex;
	    justify-content: flex-start;
	    width: 100%;
	    height: 50px;
	    align-items: center;
	    padding: 0;
	}
	
	#messageInput {
	   
	    width:480px;
	    height:50px;
	    padding: 10px;
	    overflow-y: auto;
	    border: none;
	    box-sizing: border-box;
	    padding-top: 15px;
	    float: left;
	    resize: none; 
	    white-space: pre-wrap;  /* 줄바꿈 허용 */
   		overflow-wrap: break-word;  /* 긴 단어가 있을 경우 줄바꿈 */
	}
	
	#sendButton {
		width: 100px;
    	height: 50px;
	    padding: 10px;
	    /* padding: 10px 20px; */
	    border: none;
	    box-sizing: border-box;
	    background-color : red;
	} 
	
    #sendButton:hover {
        background-color: #0056b3;
    }

    #messageList {
        list-style-type: none;
        padding: 0;
        margin-top: 0;
    }
	
	.clearfix::after {
	    content: "";
	    display: table;
	    clear: both;
	}
	
    #messageList > li {
        margin: 0 0 20px 0;
        clear: both;
    }

    #messageList li span {
        /* margin: 0; */
        font-size: 12px;
        color: #666;
    }
    
    #messageList li.others div.messageHeader {
    	display: flex;
    	justify-content: flex-start;
	}
    
    #messageList li.mine div.messageHeader {
    	display: flex;
    	justify-content: flex-end;
	}

    #messageList li div.messageContent {
        padding: 10px;
        border-radius: 15px;
        display: inline-block;
        max-width: 70%;
        box-sizing: border-box;
    }

    /* 외부 사용자의 메시지 */
    #messageList li.others div.messageContent {
        background-color: skyblue;
	    border-radius: 10px;
	    /* display: inline-block; */
	    padding: 5px 10px;
	    float: left; /* 왼쪽 정렬 */
	    clear: both; /* 이전 요소와 겹치지 않게 설정 */
    }

    #messageList li.others span:first-child {
        float: left;
        margin-right: 5px;
    }

    #messageList li.others span:nth-child(2) {
        float: left;
        clear: left;
    }

    /* 내 메시지 */
    #messageList > li.mine div.messageContent {
        background-color: yellow;
	    border-radius: 10px;
	    /* display: inline-block; */
	    padding: 5px 10px;
	    float: right; /* 오른쪽 정렬 */
	    clear: both; /* 이전 요소와 겹치지 않게 설정 */
    }

    #messageList > li.mine span:first-child {
        display: none;
    }

    #messageList li.mine span:nth-child(2) {
        float: right;
        clear: right;
    }
	#actionButtons {
	    display: flex;
	    justify-content: space-between;
	    margin-top: 50px; /* input과의 거리 */
	}
	
	#actionButtons > button {
	    width: calc(50% - 10px); /* chatBox의 가로길이의 50%에서 버튼 간 거리의 절반을 뺌 */
	    height: 50px;
	    border: none;
	    font-size: 16px;
	    cursor: pointer;
	    outline: none;
	}
	
	#tradeRoom{
		height:130px;
		width:100%;
	}
	
	#tradeButton {
	    background-color: blue;
	    color: white;
	}
	
	#cancelButton {
	    background-color: red;
	    color: white;
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
<script type="text/javascript">
$(document).ready(function() {
	var targetroomId;
	<%-- var mainUser = "<%=session.getAttribute("USER")%>"; --%>
	var mainUser = "${sessionScope.USER.userId}";
	var currentSubscription;
	
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
	
	    $.ajax({
            url: '${pageContext.request.contextPath}/chatList.do',
            method: 'GET',
            dataType:'json',
            success: function(response) {
            	var users = response.chatList;
            	users.forEach(function(user) {
            	var userElem = document.createElement("div");
	                userElem.innerText = user.otherUserId;
	                userElem.addEventListener("click", function() {
	                    targetroomId = user.roomId;
	                    loadChatHistory(targetroomId);
	                    subscribeToTopic(targetroomId);
                });
	                document.getElementById("usersList").appendChild(userElem);
            });
            
        },
        error: function (request,status,error) {
        	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
	    });
	

	    function loadChatHistory(targetroomId) {
	        $.ajax({
	            url: "${pageContext.request.contextPath}/chatLog.do/" + targetroomId,
	            method: "GET",
	            dataType:'json',
	            success: function(response) {
	            	var messages = response.chatLog;
	                var item = response.trade;
	                //$('#messageList').val('');
	                var chatContent = '';
	                $('#messageList').empty();
	                $('#tradeRoom').empty();
	                var tradeElem = '';
	                var messageElem = '';
	                var formattedDate = formatDate(item.now_date);
	                
	                tradeElem += '<div style="display: flex; align-items: start; margin-left: 10px; background-color:white;width:600px;">';
	                tradeElem += '<img alt="image" width="100px" height="100px" src="/img/'+item.image+' "/>';
	                tradeElem += '<div style="margin-left: 10px;">';
	                tradeElem += '<div class="trade-card-content">';
	                tradeElem += '<strong>'+item.title+'</strong><br>';
	                tradeElem += '<div>' + formattedDate + '</div>';
	                tradeElem += '</div>';
	                tradeElem += '<div>'+item.price+' 원</div>';
	                tradeElem += '</div>';  // 오른쪽 내용들의 div 닫기
	                tradeElem += '</div>';  // 상위 container 닫기
	                $('#tradeRoom').append(tradeElem);
	                messages.forEach(function(message) {
	                	if(message.sender == mainUser){
		                    messageElem += '<li class="mine clearfix">';
		                    messageElem += '<div class="messageHeader">';
		                    messageElem += '<span class="userName">' + message.sender + '</span>';
		                    messageElem += '<span class="timeStamp">' + message.chatTime + '</span>';
		                    messageElem += '</div>';
		                    messageElem += '<div class="messageContent">' + message.chatContent + '</div>';
		                    messageElem += '</li>';
	                	}
	                	else{
		                    messageElem += '<li class="others clearfix">';
		                    messageElem += '<div class="messageHeader">';
		                    messageElem += '<span class="userName">' + message.sender + '</span>';
		                    messageElem += '<span class="timeStamp">' + message.chatTime + '</span>';
		                    messageElem += '</div>';
		                    messageElem += '<div class="messageContent">' + message.chatContent + '</div>';
		                    messageElem += '</li>';
	                	}
	                });
	                $('#messageList').append(messageElem);
	                var chatBox = $('#chatBox');
	                chatBox.scrollTop(chatBox.prop("scrollHeight"));
	            },
	            error: function (request,status,error) {
	            	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	            }
	        });
	    }

	 // Websocket connection
	 var serverUrl = $("#serverUrl").val();
	    var socket = new SockJS(serverUrl);
	    var stompClient = Stomp.over(socket);

	    stompClient.connect({}, function(frame) {
	        console.log('Connected: ' + frame);

	        stompClient.subscribe('/topic/chat/' + targetroomId, function(message) {
	        	var receivedMessage = JSON.parse(message.body);
	            showMessage(receivedMessage);
	        });
	        
	        stompClient.subscribe('/topic/notifications/' + mainUser, function(message) {	            
	            console.log(message.body);
	            var notiDiv = $('#noti');
	            notiDiv.addClass('blinking');
	        });
	    });

	    document.getElementById("sendButton").addEventListener("click", sendMessage);

	    function showMessage(message) {
	    	var messageElem = '';
	    	if(message.sender == mainUser){
                messageElem += '<li class="mine clearfix">';
                messageElem += '<div class="messageHeader">';
                messageElem += '<span class="userName">' + message.sender + '</span>';
                messageElem += '<span class="timeStamp">' + message.chatTime + '</span>';
                messageElem += '</div>';
                messageElem += '<div class="messageContent">' + message.chatContent + '</div>';
                messageElem += '</li>';
        	}
        	else{
                messageElem += '<li class="others clearfix">';
                messageElem += '<div class="messageHeader">';
                messageElem += '<span class="userName">' + message.sender + '</span>';
                messageElem += '<span class="timeStamp">' + message.chatTime + '</span>';
                messageElem += '</div>';
                messageElem += '<div class="messageContent">' + message.chatContent + '</div>';
                messageElem += '</li>';
        	}
            $('#messageList').append(messageElem);
            var chatBox = $('#chatBox');
            chatBox.scrollTop(chatBox.prop("scrollHeight"));
	    }

	    function sendMessage() {
	        var messageInput = document.getElementById("messageInput");
	        
	        if (!messageInput.value.trim()) {
	            alert("메세지를 입력하세요.");
	            return;
	        }
	        
	        stompClient.send("/app/chat/" + targetroomId, {}, JSON.stringify({'roomId': targetroomId, 'sender': mainUser,'chatContent': messageInput.value }));
	        messageInput.value = '';
	    }
	    
	    function subscribeToTopic(roomId) {
	        // 기존 구독이 있다면 해제
	        if (currentSubscription) {
	            currentSubscription.unsubscribe();
	        }
	        
	        // 새로운 채팅방의 토픽에 구독
	        currentSubscription = stompClient.subscribe('/topic/chat/' + roomId, function(message) {
	            var receivedMessage = JSON.parse(message.body);
	            showMessage(receivedMessage);
	        });
	    }
	    function formatDate(input) {
	        var date = new Date(input);
	        var year = date.getFullYear();
	        var month = ("0" + (date.getMonth() + 1)).slice(-2);
	        var day = ("0" + date.getDate()).slice(-2);
	        var hour = ("0" + date.getHours()).slice(-2);
	        var minute = ("0" + date.getMinutes()).slice(-2);
	        
	        return year + "년" + month + "월" + day + "일" + hour + ":" + minute;
	    }
	    
});
</script>
<style>
#chatArea {
	width: 200px; height: 100px; overflow-y: auto; border: 1px solid black;
}
</style>
</head>
<body>
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
	<input type="hidden" id="serverUrl" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/chat-st.do" style="width:400px">
	<div id="chatContainer">
	    <div id="usersList">
	        <!-- Filled by AJAX -->
	    </div>
		
	    <div id="chatContent">
	    	<div id="tradeRoom"></div>
	        <div id="chatBox">
	            <ul id="messageList"></ul>
	        </div>
	
	        <div id="inputContainer">
	            <textarea id="messageInput" placeholder="Type a message"></textarea>
	            <button id="sendButton">Send</button>
	        </div>
	        
	    </div>
	</div>
</body>
</html>