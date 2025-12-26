<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MY Community</title>
<style>
	#wrapper{
	width:100%;
	height:800px;
 	
 	margin:0 auto;
 	display:flex;
 	flex-direction: column;
    justify-content: center;
    align-items: center;
	}
	
	
	.box{
		
		display:inline-block;
		width:300px;
		height:100px;
		text-align: center;
  		
 		align-items: center;
 		border:3px solid black;
 		margin-bottom:20px;
 		
	h1{
		padding-top : 10px;
		align-items: center;
  		text-align: center;
	}
</style>
</head>
<body>
	<div id="wrapper">
		
		<div onclick="location.href='chattingPage.do'" class="box">
			<h1>밀양 -> 부산대</h1>
		</div>
		
		
		
		<div onclick="location.href='echoPage.do'" class="box">
			<h1>부산대 -> 밀양</h1>
		</div>
		
		<div onclick="location.href='tradeListPage.do'" class="box">
			<h1>중고거래</h1>
		
		</div>
		
		<div onclick="location.href='chatPage.do'" class="box">
			<h1>채팅</h1>
		
		</div>
		
	</div>
</body>
</html>