<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TradeList</title>
<script src="https://code.jquery.com/jquery-3.6.4.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<style type="text/css">

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

    section {
        margin-top: 20px;
        align-items: center;
        
    }
	
	#seclay{
		justify-content: center;
	    display: flex;
	    align-items: center;
	    flex-direction: column;
	}
    .search-box {
        display: flex;
        justify-content: flex-end;
        margin-bottom: 10px;
    }

    .search-box input[type="text"] {
        padding: 5px;
    }

    .search-box button {
        margin-left: 10px;
    }

    hr {
        margin: 10px 0;
    }

    .trade-card {
        border: 1px solid yellow;
        margin-bottom: 20px;
        padding: 10px;
        text-align: center;
        width:500px;
        background-color: white;
    }

    .trade-card img {
        width: 300px;
        height: 300px;
        margin: 0 auto;
        display: block;
    }

    .trade-card-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 10px;
    }

    .pagination {
        text-align: center;
        margin-top: 20px;
    }
	
	.pagination a, .pagination b{
		height: 20px;
		color: black;  /* 텍스트 색상 */
        text-decoration: none;  /* 밑줄 제거 */
        padding: 5px 10px;  /* 위/아래, 좌/우 패딩 */
        border: 1px solid black;  /* 테두리 */
        margin: 0 2px;  /* 마진 */
        display: inline-block;  /* inline 형태의 block 요소로 만들기 */
        line-height: 20px;
        text-align: center;
	}
	
	/* 클릭했을 때 스타일 */
    .pagination a:active, .pagination a:visited {
        color: black;  /* 텍스트 색상 유지 */
    }

    /* 현재 페이지 스타일 */
    .pagination b {
        font-weight: bold;  /* 볼드체 */
    }
	
    .pagination div {
        display: inline-block;
        margin: 5px;
        cursor: pointer;
        padding: 5px 10px;
        border: 1px solid #ddd;
    }

    .pagination div:hover {
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
});
	function trClickEvent(tradeId){
		location.href="tradeInfoPage/"+ tradeId + ".do";
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

    <section>
    	<c:if test="${USER.userId != NULL }">
		<button type="button" onclick="window.location.href='tradeInsertPage.do'">게시글 등록</button>
		</c:if>
        <div class="search-box">
		    <form action="tradeListPage.do" method="GET">
		        <input type="text" name="searchTerm" placeholder="제목 검색...">
		        <button type="submit">검색</button>
		    </form>
		</div>
        <hr>
        <h3>게시판</h3>
		<div id="seclay">
        <c:forEach items="${tradeList }" var="item">
            <div class="trade-card" onclick="trClickEvent('${item.tradeId}')">
                <img alt="image" width="100px" height="100px" src="/img/<c:out value='${item.image }'/>">
                <div class="trade-card-content">
                    <div>
                        <strong><c:out value="${item.title }"/></strong><br>
                        <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div><c:out value="${item.price }"/>원</div>
                    <div><c:out value="${item.writerId }"/></div>
                </div>
            </div>
        </c:forEach>
		</div>
        <div class="pagination">
            <c:if test="${pagination.currentPage > pagination.pageBlockSize}">
	            <a href="${pageContext.request.contextPath}/tradeListPage.do?currentPage=1&pageSize=${pagination.pageSize}">처음</a>
	            <a href="${pageContext.request.contextPath}/tradeListPage.do?currentPage=${pagination.startPage - 1}&pageSize=${pagination.pageSize}">이전</a>
	        </c:if>
	
	        <c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="page">
	            <c:choose>
	                <c:when test="${page == pagination.currentPage}">
	                    <b>${page}</b>
	                </c:when>
	                <c:otherwise>
	                    <a href="${pageContext.request.contextPath}/tradeListPage.do?currentPage=${page}&pageSize=${pagination.pageSize}">${page}</a>
	                </c:otherwise>
	            </c:choose>
	        </c:forEach>
	
	        <c:if test="${pagination.endPage < pagination.totalPages}">
	            <a href="${pageContext.request.contextPath}/tradeListPage.do?currentPage=${pagination.endPage + 1}&pageSize=${pagination.pageSize}">다음</a>
	            <a href="${pageContext.request.contextPath}/tradeListPage.do?currentPage=${pagination.totalPages}&pageSize=${pagination.pageSize}">마지막</a>
	        </c:if>
        </div>
    </section>
</body>
</html>
