<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>1:1 실시간 채팅</title>
  <script src="https://unpkg.com/vue@3.3.4/dist/vue.global.js"></script>
  <script src="https://unpkg.com/vue@3"></script>
	<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	
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
    <div v-for="room in rooms" :key="room.room_no" class="chat-room" @click="enterRoom(room)">
      {{ room.opponent_nick }}<br>
      <small>{{ formatTime(room.reserve_start_time) }}</small>
    </div>
  </div>

  <div class="chat-main" v-if="currentRoom">
    <div class="chat-header">
      {{ currentRoom.opponent_nick }} 와의 채팅
    </div>
    <div class="chat-messages">
      <div v-for="msg in messages" :key="msg.chat_no" :class="['message', msg.sender_no === userNo ? 'mine' : '']">
        <div>{{ msg.content }}</div>
        <small>{{ msg.send_time }}</small>
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

<script type="module">
import { createApp } from 'https://unpkg.com/vue@3.3.4/dist/vue.esm-browser.js';
import axios from 'https://esm.sh/axios';
import SockJS from 'https://esm.sh/sockjs-client';
import { Client } from 'https://esm.sh/@stomp/stompjs';

createApp({
  data() {
    return {
      rooms: [],
      currentRoom: null,
      messages: [],
      chatContent: '',
      stompClient: null,
      userNo: null
    };
  },
  mounted() {
  axios.get('/web/auth/me')
    .then(res => {
      if (!res.data.valid) {
        alert("로그인이 필요한 서비스입니다.");
        return;
      }

      this.userNo = res.data.userNo;
      console.log("userNo:", this.userNo);

      // WebSocket 연결
      const socket = new SockJS('/ws-s');
      this.stompClient = new Client({
        webSocketFactory: () => socket,
        reconnectDelay: 5000,
        onConnect: () => {
          console.log("✅ WebSocket connected");
        }
      });
      this.stompClient.activate();

      // ✅ 반드시 return 해줘야 아래 then으로 전달됨
      return axios.get('/web/sitterchat/list_vue');
    })
    .then(res => {
      if (!res) return;
      this.rooms = res.data.list;
      if (this.rooms.length > 0) {
        this.enterRoom(this.rooms[0]);
      }
      console.log("1");
    })
    .catch(err => {
      console.error("❌ 인증 또는 채팅 로딩 실패", err);
    });
},
  methods: {
    getUserNoFromToken() {
      const token = document.cookie.split('; ').find(row => row.startsWith('accessToken='))?.split('=')[1];
	console.log("🔍 token:", token);      
if (!token) return null;
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return parseInt(payload.sub);
      } catch (e) {
        return null;
      }
    },
    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
    },
    getOpponentNo(room) {
      return (room.user1_no === this.userNo) ? room.user2_no : room.user1_no;
    },
    enterRoom(room) {
      this.currentRoom = room;
      this.messages = [];

      axios.get('/web/sitterchat/msglist', { params: { room_no: room.room_no } }).then(res => {
        this.messages = res.data;
      });

      this.stompClient.subscribe('/ssub/chat/' + room.room_no, msg => {
        this.messages.push(JSON.parse(msg.body));
      });
    },
    sendMessage() {
      if (!this.chatContent.trim()) return;
      const msg = {
        roomId: this.currentRoom.room_no,
        receiverNo: this.getOpponentNo(this.currentRoom),
        chatContent: this.chatContent,
        chatType: 'text'
      };
      this.stompClient.send("/spub/chatSend", {}, JSON.stringify(msg));
      this.chatContent = '';
    }
  }
}).mount('#app');
</script>
</body>
</html>
