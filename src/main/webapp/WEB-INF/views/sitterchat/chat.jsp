<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sist.web.security.JwtTokenProvider" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%
    String accessToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("accessToken".equals(c.getName())) {
                accessToken = c.getValue();
                break;
            }
        }
    }

    if (accessToken == null || accessToken.isEmpty()) {
        response.sendRedirect("/user/login");
        return;
    }

    JwtTokenProvider jwt = new JwtTokenProvider();
    String userNo = null;
    try {
        userNo = jwt.getUserNoFromToken(accessToken);
        if (userNo == null || userNo.isEmpty()) {
            response.sendRedirect("/user/login");
            return;
        }
    } catch (Exception e) {
        response.sendRedirect("/user/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>1:1 실시간 채팅</title>
  <script src="https://unpkg.com/vue@3"></script>
  <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
  <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script> -->

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
    .message { margin-bottom: 0.75rem; padding: 0.5rem; border-radius: 8px; max-width: 70%; background-color: #40444b; }
    .message.mine { margin-left: auto; background-color: #7289da; text-align: right; }
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
        <div>
          <strong v-if="msg.sender_no !== userNo">{{ msg.sender_nick }}: </strong>{{ msg.content }}
        </div>
        <small>{{ formatTime(msg.send_time) }}</small>
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
Vue.createApp({
  data() {
    return {
      rooms: [],
      currentRoom: null,
      messages: [],
      chatContent: '',
      stompClient: null,
      userNo: null,
      token: ''
    };
  },
  mounted() {
	  this.initWebSocket();      
  },
  methods: {
	  async initWebSocket() {
		    try {
		      const res = await axios.get('/web/auth/me');
		      if (!res.data.valid) {
		        alert('로그인이 필요한 서비스입니다.');
		        return;
		      }

		      this.token = res.data;
		      this.userNo = res.data.userNo;

		      const socket = new WebSocket("ws://localhost:8080/web/ws-sitter");
		      this.stompClient = Stomp.over(socket);
		      this.stompClient.reconnect_delay = 5000;

		      this.stompClient.connect(
		        { Authorization: 'Bearer ' + this.token },
		        () => {
		          console.log("WebSocket connected");
		          this.loadRooms();
		        }
		      );
		    } catch (err) {
		      console.error("인증 또는 WebSocket 오류:", err);
		    }
		  },
    loadRooms() {
      axios.get('/web/sitterchat/list_vue').then(res => {
        this.rooms = res.data.list;
        if (this.rooms.length > 0) {
          this.enterRoom(this.rooms[0]);
        }
      });
    },
    formatTime(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
    },
    getOpponentNo(room) {
      return (room.user1_no === this.userNo) ? room.user2_no : room.user1_no;
    },
    enterRoom(room) {
      if (!this.stompClient || !this.stompClient.connected) {
        console.warn("❗ stompClient 연결 안 됨");
        return;
      }

      this.currentRoom = room;
      this.messages = [];

      axios.get('/web/sitterchat/msglist', {
        params: { room_no: room.room_no }
      }).then(res => {
        this.messages = res.data;

        this.$nextTick(() => {
          const msgBox = document.querySelector('.chat-messages');
          if (msgBox) msgBox.scrollTop = msgBox.scrollHeight;
        });
      });

      this.stompClient.subscribe('/ssub/chat/' + room.room_no, msg => {
        const newMsg = JSON.parse(msg.body);
        this.messages.push(newMsg);

        this.$nextTick(() => {
          const msgBox = document.querySelector('.chat-messages');
          if (msgBox) msgBox.scrollTop = msgBox.scrollHeight;
        });
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

      this.stompClient.send("/spub/Send", {}, JSON.stringify(msg));
      this.chatContent = '';
    }
  }
}).mount('#app');
</script>

</body>
</html>
