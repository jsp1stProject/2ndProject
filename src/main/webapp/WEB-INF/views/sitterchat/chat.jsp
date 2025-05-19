<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>1:1 채팅</title>
  <script src="https://unpkg.com/vue@3.3.4/dist/vue.global.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <style>
    body { margin: 0; }
    #app { display: flex; height: 100vh; }
    .sidebar { width: 250px; background: #2f3136; color: white; padding: 1rem; overflow-y: auto; }
    .chat-main { flex: 1; display: flex; flex-direction: column; background: #36393f; color: white; }
    .chat-header { padding: 1rem; border-bottom: 1px solid #202225; font-weight: bold; background: #2f3136; }
    .chat-messages { flex: 1; padding: 1rem; overflow-y: auto; }
    .chat-input { padding: 1rem; border-top: 1px solid #202225; background: #2f3136; }
    .chat-input textarea { width: 100%; resize: none; height: 60px; padding: 0.5rem; border-radius: 4px; border: none; }
    .message { margin-bottom: 0.75rem; }
    .message.mine { text-align: right; }
    .chat-room { cursor: pointer; padding: 0.5rem; border-radius: 4px; }
    .chat-room:hover { background: #40444b; }
  </style>
</head>
<body>
<div id="app">
  <div class="sidebar">
    <h5>채팅 목록</h5>
    <div v-for="room in rooms" :key="room.roomId" class="chat-room" @click="enterRoom(room)">
      {{ room.opponentNickname }}<br>
      <small>{{ room.resDate }} {{ room.startTimeStr }}</small>
    </div>
  </div>

  <div class="chat-main" v-if="currentRoom">
    <div class="chat-header">
      {{ currentRoom.opponentNickname }} 와의 채팅
    </div>
    <div class="chat-messages">
      <div v-for="msg in messages" :key="msg.chatNo" :class="['message', msg.senderNo === userNo ? 'mine' : '']">
        <div>{{ msg.chatContent }}</div>
        <small>{{ msg.sendAt }}</small>
      </div>
    </div>
    <div class="chat-input">
      <textarea v-model="chatContent" @keydown.enter.prevent="sendMessage"></textarea>
    </div>
  </div>

  <div class="chat-main d-flex align-items-center justify-content-center" v-else>
    <h4>채팅방을 선택해주세요</h4>
  </div>
</div>

<script>
const { createApp } = Vue;
createApp({
  data() {
    return {
      rooms: [],
      currentRoom: null,
      messages: [],
      chatContent: '',
      stompClient: null,
      userNo: null  // 실제는 쿠키/JWT에서 파싱 필요
    }
  },
  mounted() {
	this.userNo = this.getUserNoFromToken();
	if (!this.userNo) {
		  alert("로그인이 필요한 서비스입니다.");
		  return;
		}
    axios.get('/sitterchat/list_vue').then(res => {
      this.rooms = res.data.list;
    });

    const socket = new SockJS('/ws-s');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, () => {
      console.log("WebSocket connected");
    });
  },
  methods: {
	  getUserNoFromToken() {
		  const token = document.cookie
		    .split('; ')
		    .find(row => row.startsWith('accesstoken='))
		    ?.split('=')[1];
		  if (!token) return null;
		  try {
		    const payload = JSON.parse(atob(token.split('.')[1]));
		    return parseInt(payload.sub); // 또는 payload.userNo 등, 서버 발급 구조에 맞게
		  } catch (e) {
		    return null;
		  }
		},
    enterRoom(room) {
      this.currentRoom = room;
      this.messages = [];
      axios.get('/sitterchat/msglist', { params: { room_id: room.roomId } }).then(res => {
        this.messages = res.data;
      });

      this.stompClient.subscribe('/ssub/chat/' + room.roomId, msg => {
        this.messages.push(JSON.parse(msg.body));
      });
    },
    sendMessage() {
      if (!this.chatContent.trim()) return;
      const msg = {
        roomId: this.currentRoom.roomId,
        senderNo: this.userNo,
        receiverNo: this.currentRoom.opponentNo, // 이 필드는 서버에서 구분용으로 필요
        chatContent: this.chatContent,
        chatType: 'text'
      };
      this.stompClient.send("/spub/chatSend", {}, JSON.stringify(msg));
      this.chatContent = '';
    }
  }
}).mount('#app')
</script>
</body>
</html>
