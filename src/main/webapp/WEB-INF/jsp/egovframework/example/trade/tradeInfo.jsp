<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%-- <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %> --%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trade Board</title>
<style>
	body {
        width: 80%;
        margin: 0 auto;
        font-family: Arial, sans-serif;
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
    section {
        display: flex;
        flex-direction: column;
        align-items: center;
        
    }

    fieldset {
        margin: 20px 0;
        display: block;
        max-width: 700px;
        width: 100%;
        background-color: white;
    }

    button {
        margin: 5px;
        padding: 10px 15px;
        border: none;
        cursor: pointer;
        background-color: #007BFF;
        color: #ffffff;
        border-radius: 5px;
        transition: background-color 0.3s ease;
    }

    button:hover {
        background-color: #0056b3;
    }
    .commentButton{
    	vertical-align: middle;
    }
    #comments{
    	width:100%;
    }
	#comments > div {
        border: 1px solid #ccc;
        padding: 10px;
        margin-top: 10px;
        background-color: white;
    }
    .reply-form, div[id^="commentForm-"] {
        margin-top: 10px;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        background-color: white;
    }
    .comment-box {
        border: 1px solid #ccc;
        padding: 10px;
        margin-top: 10px;
        background-color: white;
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
	window.onload=function(){
		
		var updateBtn = document.getElementById("updateBtn");
		
		updateBtn.onclick = function(){
			var path = "${pageContext.request.contextPath}/updateTradePage.do";
			var params = {
					"tradeId" : "${trade.tradeId}"
			}
			post(path,params);
		}
		
		var deleteBtn = document.getElementById("deleteBtn");
		
		deleteBtn.onclick = function(){
			if(confirm("게시글을 삭제하시겠습니까?") == true){
				var path = "${pageContext.request.contextPath}/tradeDelete.do";
				var params = {
						"tradeId" : "${trade.tradeId}",
						"image" : "${trade.image}"
				}
				post(path,params);
			}
			else{
				return;
			}
		}
				
		var createChat = document.getElementById("createChat");
		
		createChat.onclick = function(){
			
		}
		
	};
	
	function post(path, params){
		var form = document.createElement("form");
		form.action = path;
		form.method = "post";
		
		for(var key in params){
			if(params.hasOwnProperty(key)){
				var hiddenField = document.createElement("input");
				hiddenField.type = "hidden";
				hiddenField.name = key;
				hiddenField.value = params[key];
				form.appendChild(hiddenField);
			}
		}
		document.body.appendChild(form);
		form.submit();
	}
	
	
</script>
<script type="text/javascript">
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
	}
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
	var data = {
            tradeId: "${trade.tradeId}",
            requesterId: mainUser,
            writerId: "${trade.writerId }"
        };
	$.ajax({
        url: '${pageContext.request.contextPath}/showComment.do',
        type: 'GET',
        data: {tradeId : '${trade.tradeId}'},
        dataType:'json',
        success: function (response) {
            if (response) {
            	var out;
            	var comments = response.comments;
            	
            	out = recursiveComments(comments, 0);
            	
            	$('#comments').append(out);
            }
            else{
            	
            }
        },
        error: function (request,status,error) {
        	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });
	
	$.get("${pageContext.request.contextPath}/checkChatRequestStatus.do", data, function(response) {
		var createChatBtn = document.getElementById('createChat');
		if(response.status == 'PENDING') {
        	createChatBtn.disabled = true;
            createChatBtn.style.backgroundColor = 'gray';
            createChatBtn.innerText = '요청 중...';
        }
		else if(response.status == 'ACCEPTED'){
			createChatBtn.disabled = true;
            createChatBtn.style.backgroundColor = 'gray';
            createChatBtn.innerText = '거래 중...';
		}
    }, "json");
	
	$("#createChat").click(function() {
        
        var createChatBtn = document.getElementById('createChat');
        
        $.ajax({
        	url: "${pageContext.request.contextPath}/sendChatRequest.do", 
        	type: 'POST',
        	data:{tradeId: "${trade.tradeId}",
                requesterId: mainUser,
                writerId: "${trade.writerId }"}, 
            success: function (response) {
            if(response.status == 'success') {
                alert(response.message);
                createChatBtn.disabled = true;
                createChatBtn.style.backgroundColor = 'gray';
                createChatBtn.innerText = '요청 중...';
            	} 
            },
            error: function (request,status,error) {
            	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            }
		});
    });
	
	$("body").on("click", ".writecom", function () {
	    const commentId = $(this).val();
	    if (!validateCommentForm('commentForm-' + commentId)) {
	        // 유효성 검사에 실패한 경우 Ajax 요청을 중지
	        return;
	    }
	    
	    $.ajax({
	        url: '${pageContext.request.contextPath}/writeComment.do',
	        type: 'POST',
	        data: $('#commentForm-' + commentId).serialize(),
	        dataType:'json',
	        success: function (response) {
	            if (response) {
	            	var out;
	            	var comments = response.comments;
	            	$('.textcontent').val('');
	            	out = recursiveComments(comments, 0);
	            	
	            	$('#comments').empty();
	            	$('#comments').append(out);
	            }
	            else{
	            	
	            }
	        },
	        error: function (request,status,error) {
	        	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	        }
	    });
	});
});
function showReplyForm(parentId) {
    $('.reply-form').hide();
    $('#reply-form-' + parentId).show();
}

function replyhide(){
	$('.reply-form').val('');
	$('.reply-form').hide();
}

function validateCommentForm(formId) {
    const form = document.getElementById(formId);
    const textarea = form.querySelector(".textcontent");

    if (!textarea.value.trim()) { // trim()을 사용하여 공백만 있는 경우도 처리합니다.
        alert("댓글을 작성하셔야합니다.");
        textarea.focus(); // textarea로 포커스를 이동시킵니다.
        return false;
    }
    return true;
}

function recursiveComments(comments, indent) {
	  let output = "";
	  let indentlength = indent * 20;

		
	  comments.forEach(function(comment) {
		  output += '<div class="comment-box" style="margin-left: ' + indentlength + 'px;">';
		if (comment.parentId != 0) {
		   output += '<p style="font-size: 20px; display:inline;">ㄴ</p>';
		}
		
		output += '<p style="display:inline;">' + comment.writerId + ':' + comment.content + '</p>';
		output += ' <c:if test="${USER.userId != NULL }"><button class="commentButton" onclick="showReplyForm(' + comment.commentId + ')">작성</button></c:if></div>';
		output += '<c:if test="${USER.userId != NULL }">';
		output += '<div id="reply-form-' + comment.commentId + '" class="reply-form" style="display: none; margin-left: 20px;">';
    	output += '<form id="commentForm-' + comment.commentId + '">';
    	output += '<input type="hidden" id="trade-id" name="tradeId" value="${trade.tradeId }">';
    	output += '<input type="hidden" id="parent-id-' + comment.commentId + '" name="parentId" value="' + comment.commentId + '">';
    	output += '내용: <textarea id="content-' + comment.commentId + '" name="content" class="textcontent" rows="5" cols="30" required></textarea><br>';
    	output += '<input type="hidden" id="writer-' + comment.commentId + '" name="writerId" value="${USER.userId }" required><br>';
    	output += '<button type="button" class="writecom" value="' + comment.commentId + '" onclick="if(!validateCommentForm(\'commentForm-' + comment.commentId + '\')) return false;">작성</button>';
    	output += '<button type="button" onclick="replyhide()">취소</button>';
    	output += '</form></div></c:if>';

	    if (comment.children && comment.children.length > 0) {
	      output += recursiveComments(comment.children, indent + 1);
	    }

	    output += "</div>";
	  });

	  return output;
	}

</script>
</head>
<body>	
	<input type="hidden" id="serverUrl" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/chat-st.do">
	<header>
        <div onclick="window.location.href='${pageContext.request.contextPath}/tradeListPage.do'">메인화면</div>
        <c:if test="${USER.userId == NULL }">
            <div onclick="window.location.href='${pageContext.request.contextPath}/loginPage.do'">로그인</div>
        </c:if>
        <c:if test="${USER.userId != NULL }">
            <div onclick="window.location.href='${pageContext.request.contextPath}/myPage.do'">[${USER.userId}]님</div>
            <div onclick="window.location.href='${pageContext.request.contextPath}/logout.do'">로그아웃</div>
            <div onclick="window.location.href='${pageContext.request.contextPath}/stomp_chat.do'">채팅</div>
            <div id ="noti" onclick="window.location.href='${pageContext.request.contextPath}/notificationPage.do'">알림</div>
        </c:if>
    </header>
	<section>
	<fieldset style="width:700px;">
		<legend><h3>게시글 상세</h3></legend>
		<table style="width:600px;">
			<tr>
				<th>제목</th>
				<td><c:out value="${trade.title }"/></td>
			</tr>
			<tr>
				<th>작성자</th>
				<td><c:out value="${trade.writerId }"/></td>
			</tr>
			
			<tr>
				<th>내용</th>
				<td><c:out value="${trade.contents }"/></td>
			</tr>
			<tr>
				<th>가격</th>
				<td><c:out value="${trade.price }"/></td>
			</tr>
			<tr>
				<th>등록날짜</th>
				<td>
					<fmt:parseDate value="${trade.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
					<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
				</td>
			</tr>
			<tr>
				<th>이미지</th>
				<td><img alt="image" width="300px" height="300px" src="/img/<c:out value='${trade.image }'/>"></td>
			</tr>
		</table>
		<br>
		
		</fieldset>
		<div style=" margin-top:10px;">
		<button type="button" onclick="window.history.go(-1)">이전</button>
		<c:if test="${trade.writerId == USER.userId }">
			<button type="button" id="updateBtn">수정</button>
			<button type="button" id="deleteBtn">삭제</button>
		</c:if>
		<c:if test="${USER.userId != NULL && trade.writerId != USER.userId }">
			<button type="button" id="createChat">1:1 대화하기</button>
		</c:if>
		</div>
		<c:if test="${USER.userId != NULL }">
		<h2>댓글 작성</h2>
		<form id="commentForm-0" onsubmit="return validateCommentForm('commentForm-0');">
			<input type="hidden" id="trade-id" name="tradeId" value="${trade.tradeId }">
			<input type="hidden" id="parent-id-0" name="parentId" value="0">
			내용: <textarea id="content-0" name="content" class="textcontent" rows="5" cols="30" required></textarea><br>
			<input type="hidden" id="writerId-0" name="writerId" value="${USER.userId }" required><br>
		    <button type="button" class="writecom" value="0" onclick="return validateCommentForm('commentForm-0')">작성</button>
		</form>
		</c:if>
		<div id="comments">
			
		
		</div>
		
	</section>
</body>

</html>