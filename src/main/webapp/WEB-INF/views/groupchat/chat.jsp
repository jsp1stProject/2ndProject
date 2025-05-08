
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<style>
.create-room {
	position: fixed;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.4);
}
.create-room-container {
	position: relative;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 550px;
	background: #fff;
	border-radius: 10px;
	padding: 20px;
	box-sizing: border-box;
}
.content {
	margin-top: 10px;
}
</style>
</head>
<body data-context-path="<%= request.getContextPath() %>">
	<div id="app">
		<h2>테스트</h2>
		<div class="create-room" v-show="createCheck">
			<div class="create-room-container">
				그룹이름
				<input type="text" class="form-control" name="room_name" v-model="group_name">
				<input type="text" class="form-control" name="room_description" v-model="group_description">
				<div class="create-btn gap-3">
					<button @click="createGroup()" class="btn btn-primary btn-sm">생성</button>
					<button @click="groupOpen()" class="btn btn-secondary btn-sm">취소</button>
				</div>
			</div>
		</div>
		<div class="rooms">
			<input type="button" value="그룹 생성" @click="groupOpen()">
		</div>
		<label>방 선택&nbsp;&nbsp;</label>
		<select v-model="group_no" @change="changeGroup()">
			<option v-for="group in availableGroups" :value="group.group_no">{{group.group_name}}</option>
		</select>
		<div class="chat-container" ref="scrollContainer" style="height: 400px; overflow-y: auto;">
			<div v-for="msg in messages" :key="msg.message_no">{{msg.sender_nickname}}: {{msg.content}}</div>
		</div>
		<input v-model="message" @keyup.enter="sendMessage()">
		<div v-for="member in members" :key="member.user_no">
			<span>{{ member.nickname }}</span>
			<span v-if="member.isOnline === true">🟢</span>
			<span v-else>⚪</span>
		</div>
	</div>
</body>
<script type="module">
	import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
	import { initGroupChat } from '${pageContext.request.contextPath}/resources/js/groupchat.js';
	const contextPath = document.body.getAttribute('data-context-path');
	initGroupChat(contextPath, createApp);
</script>
</html>
