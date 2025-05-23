const Stomp = window.Stomp;

export const wsMethods = {
  async initialize() {
    try {
      const res = await axios.get(`${this.contextPath}/api/token`);
      this.token = res.data.token;
      this.sender_no = res.data.userNo;
      this.sender_nickname = res.data.nickname;

      const protocol = location.protocol === 'https:' ? 'wss' : 'ws';
      const socketUrl = `${protocol}://${location.host}${this.contextPath}/ws-user`;
      const socket = new WebSocket(socketUrl);

      this.stompClient = Stomp.over(socket);
      this.stompClient.heartbeat.outgoing = 10000;
      this.stompClient.heartbeat.incoming = 10000;

      this.stompClient.connect(
        { Authorization: 'Bearer ' + this.token },
        async () => {
          await this.loadGroups();
          this.subscribeNotify();
        },
        err => console.error('STOMP 연결 실패', err)
      );
    } catch (e) {
      console.error('토큰 가져오기 실패:', e);
    }
  },

  sendJoinMessage() {
    const payload = {
      groupNo: this.group_no,
      nickname: this.sender_nickname
    };
    this.stompClient.send("/pub/user/join", {}, JSON.stringify(payload));
  },

  subscribeGroupMessages() {
    this.subscription?.unsubscribe();
    this.subscription = this.stompClient.subscribe(`/sub/chats/groups/${this.group_no}`, async msg => {
      const body = JSON.parse(msg.body);

      const sender = this.members.find(m => m.user_no === body.sender_no);
      body.profile_img = sender?.profile_img || `${this.contextPath}/assets/images/profile/default_pf.png`;

      const container = this.scrollTarget;
      const atBottom = container.scrollHeight - (container.scrollTop + container.clientHeight) < 300;

      this.messages.push(body);

      await Vue.nextTick();
      if (body.sender_no === this.sender_no || atBottom) this.scrollToBottom();
    });
  },

  subscribeGroupOnline() {
    this.stompClient.subscribe(`/topic/groups/${this.group_no}/online`, msg => {
      console.log('온라인 상태 수신:', msg.body);
      const onlineList = JSON.parse(msg.body);
      this.onlineUserNos = onlineList.map(u => Number(u.userNo));
      this.updateMemberOnlineStatus();
    })
  },

  updateMemberOnlineStatus() {
    const set = new Set(this.onlineUserNos);
    this.members = this.members.map(m => ({
      ...m,
      isOnline: set.has(Number(m.user_no))
    }));
  },

  subscribeNotify() {
    console.log('subscribNotify', this);
    this.stompClient.subscribe('/user/queue/notify', msg => {
      console.log('알림 수신 성공', msg.body);

      const payload = JSON.parse(msg.body);
      const { groupNo } = payload;
      console.log('알림 수신된 groupNo', groupNo);

      const group = this.availableGroups.find(g => g.group_no === groupNo);
      if (group) {
        console.log('해당 그룹 찾음', group.group_name);
        group.hasUnread = true;
      } else {
        console.log('그룹 못찾음');
      }
    }, { id: 'notify-sub'});
  }
};
