<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyPage</title>
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
    
    .trade-card {
        border: 1px solid yellow;
        margin-bottom: 20px;
        padding: 10px;
        text-align: center;
        background-color: white;
    }
    .finish {
        border: 1px solid yellow;
        margin-bottom: 20px;
        padding: 10px;
        text-align: center;
        background-color: white;
    }
    .trade-container {
        display: flex;
        justify-content: space-between;
    }

    .trade-column {
        flex: 1; /* 각 컬럼이 동일한 넓이를 갖도록 설정 */
        margin: 0 10px; /* 양옆 간격 조절 */
        box-sizing: border-box;
    }

    .trade-title {
        text-align: center;
        font-weight: bold;
        margin-bottom: 10px;
    }
    .rating-div {
	    display: none; /* 처음에는 숨겨둡니다. */
	    border: 1px solid #ddd;
	    padding: 10px;
	    background-color: white;
	}
	
	.rating-option {
	    margin-right: 10px;  /* 간격을 약간 더 늘립니다. */
	    cursor: pointer;
	    background-color: white;  /* 초기 배경색 설정 */
	    padding: 2px 5px;  /* 숫자 주위에 약간의 패딩 추가 */
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
	.requester-id{
		background-color: white;
		border: 1px solid black;
	}
</style>
<script src="https://code.jquery.com/jquery-3.6.4.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script>
var mainUser = "${sessionScope.USER.userId}";
$(document).ready(function() {
    $(".accept-btn").click(function() {
        var tradeId = $(this).data("accept-trade-id");
        updateTradeStatus(tradeId, "FINISH");
    });

    $(".decline-btn").click(function() {
        var tradeId = $(this).data("decline-trade-id");
        updateTradeStatus(tradeId, "FIND");
    });
    
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

function updateTradeStatus(tradeId, status) {
    $.ajax({
        url: '${pageContext.request.contextPath}/updateTradeStatus.do',
        type: 'POST',
        data: { 
            tradeId: tradeId,
            status: status
        },
        dataType: 'json',
        success: function(response) {
            if (response.success) {
                // 성공적으로 상태가 업데이트되었을 때의 로직
                location.reload(); // 예: 페이지를 다시 로드하여 변경된 상태 반영
            } else {
                // 상태 업데이트에 실패한 경우의 로직
                alert(response.message);
            }
        },
        error: function(error) {
            console.error("Error updating trade status:", error);
        }
    });
}

var isChatRequestVisible = false;
var currentedTradeId = null;

function checkStatus(tradeId){
    // 이미 표시된 chat-request div가 있는지 확인하고 제거합니다.
    $('.chat-requests').remove();
    
    if (currentTradeId == tradeId && isChatRequestVisible) {
        // 같은 trade-card를 다시 클릭한 경우
        isChatRequestVisible = false;
        currentedTradeId = null;
    } else {
        // AJAX 요청 부분
        $.ajax({
            url: '${pageContext.request.contextPath}/getChatRequests.do',
            type: 'GET',
            data: { tradeId: tradeId },
            dataType: 'json',
            success: function(response) {
                var chatRequestsHtml = '<div class="chat-requests">';
                var requestIdLists = response.requestIdList;
                requestIdLists.forEach(function(requestIdList) {
                    chatRequestsHtml += '<div class="chat-request">';
                    chatRequestsHtml += '<div class="requester-id">' + requestIdList.requesterId;
                    chatRequestsHtml += '<button class="accept-button" data-requester-id="'+requestIdList.requesterId+'" data-trade-id="'+requestIdList.tradeId+'">매칭 수락</button>';
                    chatRequestsHtml += '<button class="decline-button" data-requester-id="'+requestIdList.requesterId+'" data-trade-id="'+requestIdList.tradeId+'">매칭 거절</button>';
                    chatRequestsHtml += '</div></div>';
                });
                chatRequestsHtml += '</div>';

                var tradeCard = $(`.trade-card[data-ch-trade-id='${tradeId}']`);
                var tradecard = $('#trade-'+tradeId);
                tradeCard.after(chatRequestsHtml);  // chat-requests div 추가
                tradecard.after(chatRequestsHtml);
                isChatRequestVisible = true;
                currentedTradeId = tradeId;
            },
            error: function(error) {
                console.error("Error fetching chat requests:", error);
            }
        });
    }
}

$(document).on('click', '.accept-button', function() {
    var requesterId = $(this).data('requester-id');
    var tradeId = $(this).data('trade-id');
    // 여기에 수락 로직을 구현합니다.
    // 예를 들어, 상태를 'ACCEPTED'로 변경하고 페이지를 리다이렉트합니다.
    acceptRequest(requesterId, tradeId);
});

$(document).on('click', '.decline-button', function() {
    var requesterId = $(this).data('requester-id');
    var tradeId = $(this).data('trade-id');
    // 여기에 거부 로직을 구현합니다.
    // 예를 들어, 상태를 'DECLINED'로 변경하고 페이지를 리다이렉트합니다.
    declineRequest(requesterId, tradeId);
});

function acceptRequest(requesterId, tradeId) {
    // AJAX 요청을 통해 수락 로직을 처리
    $.ajax({
        url: '${pageContext.request.contextPath}/acceptChatRequest.do',
        type: 'POST',
        data: { requesterId: requesterId, tradeId: tradeId, writerId:mainUser },
        dataType: 'json',
        success: function(response) {
            if (response.success) {
                window.location.reload();  // 페이지 리로드
            } else {
                console.error("Error accepting chat request:", response.message);
            }
        },
        error: function(error) {
            console.error("Error accepting chat request:", error);
        }
    });
}

function declineRequest(requesterId, tradeId) {
    // AJAX 요청을 통해 거부 로직을 처리
    $.ajax({
        url: '${pageContext.request.contextPath}/declineChatRequest.do',
        type: 'POST',
        data: { requesterId: requesterId, tradeId: tradeId },
        dataType: 'json',
        success: function(response) {
            if (response.success) {
                window.location.reload();  // 페이지 리로드
            } else {
                console.error("Error declining chat request:", response.message);
            }
        },
        error: function(error) {
            console.error("Error declining chat request:", error);
        }
    });
}

var currentRatingDiv = null;  // 현재 표시된 평점 div
var currentFinishDiv = null;  // 현재 선택된 finish div
var selectedRating = null;    // 사용자가 선택한 평점
var currentTradeId = null;

function showRatingDiv(finishDiv) {
    // 기존에 표시된 평점 div가 있으면 숨깁니다.
    currentTradeId = $(finishDiv).data("rate-trade-id");
    if (currentRatingDiv) {
        $(currentRatingDiv).remove();
        currentRatingDiv = null;
    }

    // 동일한 finish div를 다시 클릭했을 경우
    if (finishDiv == currentFinishDiv) {
        currentFinishDiv = null;
        return;
    }

    // 새로운 평점 div를 생성합니다.
    var ratingDivHtml = '';
    ratingDivHtml += '<div class="rating-div">';
    ratingDivHtml += '<span class="rating-option" onclick="selectRating(1, this)">1</span>';
    ratingDivHtml += '<span class="rating-option" onclick="selectRating(2, this)">2</span>';
    ratingDivHtml += '<span class="rating-option" onclick="selectRating(3, this)">3</span>';
    ratingDivHtml += '<span class="rating-option" onclick="selectRating(4, this)">4</span>';
    ratingDivHtml += '<span class="rating-option" onclick="selectRating(5, this)">5</span>';
    ratingDivHtml += '<button onclick="submitRating()">전송</button>';
    ratingDivHtml += '<button onclick="cancelRating()">취소</button>';
    ratingDivHtml += '</div>';

    $(finishDiv).after(ratingDivHtml);
    currentFinishDiv = finishDiv;
    currentRatingDiv = $(finishDiv).next('.rating-div');
    $(currentRatingDiv).show(); // show 메소드로 div를 표시합니다.
}

function selectRating(rating, spanElement) {
    selectedRating = rating;

    // 모든 숫자의 배경색을 초기화합니다.
    $('.rating-option').css('background-color', '');

    // 선택한 숫자만 노란색으로 표시합니다.
    $(spanElement).css('background-color', 'yellow');
}

function cancelRating() {
    if (currentRatingDiv) {
        $(currentRatingDiv).remove();
        currentRatingDiv = null;
        currentFinishDiv = null;
        selectedRating = null;
    }
}

function submitRating() {
    // AJAX로 서버에 선택한 평점 전송
    // 예:
    $.ajax({
        url: '${pageContext.request.contextPath}/submitRating.do',
        type: 'GET',
        data: { rating: selectedRating,
        		tradeId : currentTradeId
        	},
        success: function(response) {
            // ... (서버 응답 처리)

            // 성공 시 평점 div와 finish div 제거
            if (currentRatingDiv) {
                $(currentRatingDiv).remove();
                $(currentFinishDiv).remove();
                currentRatingDiv = null;
                currentFinishDiv = null;
                selectedRating = null;
            }
        }
    });
}

// finish div를 클릭하면 showRatingDiv 함수를 호출하도록 설정
$('.finish').click(function() {
    showRatingDiv(this);
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
    <h1>평점 : <c:choose><c:when test="${rating.rating != NULL }"><fmt:formatNumber value="${rating.rating}" pattern="#.#" /></c:when><c:otherwise>0</c:otherwise></c:choose></h1>
    <br>
	<div class="trade-container">
    <!-- 진행중 (FIND) -->
    <div class="trade-column">
        <div class="trade-title">진행중</div>
        <c:forEach items="${tradeList}" var="item">
            <c:if test="${item.tradeStatus == 'FIND'}">
            
                <div class="trade-card" id="trade-${item.tradeId}" data-ch-trade-id="${item.tradeId}" onclick="checkStatus('${item.tradeId}')">
					<img alt="image" width="100px" height="100px" src="/img/<c:out value='${item.image }'/>">
                	<div class="trade-card-content">
	                    <strong><c:out value="${item.title }"/></strong><br>
	                    <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div><c:out value="${item.price }"/>원</div>
                    
                </div>
            </c:if>
        </c:forEach>
    </div>

    <!-- 매칭중 (MATCH) -->
    <div class="trade-column">
        <div class="trade-title">매칭중</div>
        <c:forEach items="${tradeList}" var="item">
            <c:if test="${item.tradeStatus == 'MATCH'}">
                <div class="trade-card">
                    <img alt="image" width="100px" height="100px" src="/img/<c:out value='${item.image }'/>">
                	<div class="trade-card-content">
	                    <strong><c:out value="${item.title }"/></strong><br>
	                    <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div><c:out value="${item.price }"/>원</div>
                    <button class="accept-btn" data-accept-trade-id="${item.tradeId}">거래 완료</button>
					<button class="decline-btn" data-decline-trade-id="${item.tradeId}">거래 거절</button>
                </div>
            </c:if>
        </c:forEach>
    </div>

    <!-- 종료 (FINISH) -->
    <div class="trade-column">
        <div class="trade-title">평점</div>
        <c:forEach items="${rateList}" var="item">
            <c:if test="${item.tradeStatus == 'FINISH'}">
            
                <div class="trade-card finish" data-rate-trade-id="${item.tradeId}" onclick="showRatingDiv(this)">
                    <img alt="image" width="100px" height="100px" src="/img/<c:out value='${item.image }'/>">
                	<div class="trade-card-content">
	                    <strong><c:out value="${item.title }"/></strong><br>
	                    <fmt:parseDate value="${item.now_date }" pattern="yyyy-MM-dd HH:mm" var="registrationDate"/>
						<fmt:formatDate value="${registrationDate }" pattern="yyyy년MM월dd일HH:mm"/>
                    </div>
                    <div><c:out value="${item.price }"/>원</div>
                </div>
            
            </c:if>
        </c:forEach>
    </div>
</div>
</body>
</html>