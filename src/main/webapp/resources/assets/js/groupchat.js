const axios = window.axios;
const Stomp = window.Stomp;
dayjs.locale('ko');
dayjs.extend(dayjs_plugin_advancedFormat);
export function initGroupChat(contextPath, createApp) {
  createApp({
    data() {
      return {
        stompClient: null,
        token: '',
        sender_no: '',
        sender_nickname: '',

        group_no: '',
        availableGroups: [],
        members: [],
        onlineUserNos: [],

        messages: [],
        message: '',
        lastMessageNo: null,
        isLoading: false,
        noMoreMessages: false,

        subscription: null,
        createCheck: false,
        group_name: '',
        currentGroupName: '',
        group_description: '',

        scrollTarget: null
      };
    },
    mounted() {
      this.initialize();
      this.$nextTick(() => {
        this.scrollTarget = this.$refs.scrollContainer;
        if (this.scrollTarget) {
          this.addEventListeners();
        } else {
          console.warn('scrollContainer ref를 찾지 못했습니다.');
        }
      });
    },
    beforeUnmount() {
      this.removeEventListeners();
      this.subscription?.unsubscribe();
      this.stompClient?.disconnect(() => console.log('STOMP 연결 종료'));
    },
    methods: {
      async initialize() {
        try {
          const res = await axios.get(`${contextPath}/api/token`);
          this.token = res.data.token;
          this.sender_no = res.data.userNo;
          this.sender_nickname = res.data.nickname;

          const protocol = location.protocol === 'https:' ? 'wss' : 'ws';
          const host = location.host;
          const socketUrl = `${protocol}://${host}${contextPath}/ws`;

          const socket = new WebSocket(socketUrl);
          this.stompClient = Stomp.over(socket);
          this.stompClient.heartbeat.outgoing = 10000;
          this.stompClient.heartbeat.incoming = 10000;

          this.stompClient.connect(
            { Authorization: 'Bearer ' + this.token },
            this.loadGroups,
            err => console.error('STOMP 연결 실패', err)
          );
        } catch (e) {
          console.error('토큰 가져오기 실패:', e);
        }
      },
      async loadGroups() {
        const res = await axios.get(`${contextPath}/api/groups/${this.sender_no}`);
        this.availableGroups = res.data.data;
        if (this.availableGroups.length > 0) {
          await this.joinGroup(this.availableGroups[0].group_no);
        }
      },
      async joinGroup(groupNo) {
        const seleted = this.availableGroups.find(g => g.group_no === groupNo);
        this.currentGroupName = seleted?.group_name || '그룹';

        this.group_no = groupNo;
        this.messages = [];
        this.lastMessageNo = null;
        this.noMoreMessages = false;

        this.sendJoinMessage();
        await this.loadMessages();
        this.scrollToBottom();
        await this.loadGroupMembers();
        await this.loadInitialOnlineUsers();
        this.subscribeGroupMessages();
        this.subscribeGroupOnline();
      },
      sendJoinMessage() {
        const payload = {
          groupNo: this.group_no,
          nickname: this.sender_nickname
        };
        this.stompClient.send("/pub/user/join", {}, JSON.stringify(payload));
      },
      async loadMessages() {
        const container = this.scrollTarget;
        let url = `${contextPath}/api/chats/groups/${this.group_no}/messages`;
        if (this.lastMessageNo) url += `?lastMessageNo=${this.lastMessageNo}`;

        const previousHeight = container?.scrollHeight || 0;
        const previousScrollTop = container?.scrollTop || 0;

        this.isLoading = true;
        const res = await axios.get(url);
        console.log(res);
        const newMessages = res.data.data;

        if (newMessages.length === 0) {
          this.noMoreMessages = true;
          this.isLoading = false;
          return;
        }

        if (!this.lastMessageNo) {
          this.messages = newMessages;
        } else {
          this.messages.unshift(...newMessages);
        }

        this.lastMessageNo = newMessages[0].message_no;

        await Vue.nextTick();

        if (!this.lastMessageNo) {
          this.scrollToBottom();
        } else {
          const newHeight = container?.scrollHeight || 0;
          container.scrollTop = newHeight - previousHeight + previousScrollTop;
        }

        this.isLoading = false;
      },
      async loadGroupMembers() {
        const res = await axios.get(`${contextPath}/api/groups/members`, {
          params: { groupNo: this.group_no }
        });
        this.members = res.data.data.map(m => ({
          user_no: m.user_no,
          nickname: m.nickname,
          isOnline: false
        }));
      },
      async loadInitialOnlineUsers() {
        const res = await axios.get(`${contextPath}/api/groups/${this.group_no}/online`);
        this.onlineUserNos = res.data.data.map(u => Number(u.userNo));
        this.updateMemberOnlineStatus();
      },
      subscribeGroupMessages() {
        this.subscription?.unsubscribe();
        this.subscription = this.stompClient.subscribe(`/sub/chats/groups/${this.group_no}`, async msg => {
          const body = JSON.parse(msg.body);
          const container = this.scrollTarget;
          const atBottom = container.scrollHeight - (container.scrollTop + container.clientHeight) < 300;
          this.messages.push(body);
          await Vue.nextTick();
          if (body.sender_no === this.sender_no || atBottom) this.scrollToBottom();
        });
      },
      subscribeGroupOnline() {
        this.stompClient.subscribe(`/topic/groups/${this.group_no}/online`, msg => {
          const onlineList = JSON.parse(msg.body);
          this.onlineUserNos = onlineList.map(u => Number(u.userNo));
          this.updateMemberOnlineStatus();
        });
      },
      updateMemberOnlineStatus() {
        const set = new Set(this.onlineUserNos);
        this.members = this.members.map(m => ({
          ...m, 
          isOnline: set.has(Number(m.user_no))
        }));
      },
      scrollToBottom() {
        const c = this.scrollTarget;
        if (c) c.scrollTop = c.scrollHeight;
      },
      changeGroup() {
        if (this.group_no) this.joinGroup(this.group_no);
      },
      sendMessage() {
        if (!this.message.trim()) return;
        const msg = {
          sender_no: this.sender_no,
          sender_nickname: this.sender_nickname,
          content: this.message,
          group_no: this.group_no
        };
        this.stompClient.send(`/pub/chats/groups/${this.group_no}`, {}, JSON.stringify(msg));
        this.message = '';
      },
      groupOpen() {
        this.createCheck = !this.createCheck;
      },
      async createGroup() {
        const res = await axios.post(`${contextPath}/api/groups`, {
          owner: this.sender_no,
          group_name: this.group_name,
          description: this.group_description
        });
        alert(`그룹 이름: ${res.data.group_name} 으로 생성되었습니다.`);
        this.createCheck = false;
        await this.loadGroups();
      },
      addEventListeners() {
        const c = this.scrollTarget;
        if (c) c.addEventListener('scroll', this.onScroll);
        document.addEventListener('visibilitychange', this.handleVisibilityChange);
      },
      removeEventListeners() {
        const c = this.scrollTarget;
        if (c) c.removeEventListener('scroll', this.onScroll);
        document.removeEventListener('visibilitychange', this.handleVisibilityChange);
      },
      handleVisibilityChange() {
        if (document.visibilityState === 'visible' && !this.stompClient?.connected) {
          this.initialize();
        }
      },
      async onScroll() {
        const c = this.scrollTarget;
        if (c.scrollTop === 0 && !this.isLoading && !this.noMoreMessages) {
          this.lastMessageNo = this.messages[0]?.message_no;
          await this.loadMessages();
        }
      },
      getGroupFontSize(name) {
        const len = name.length;
        const max = 19;
        const min = 13;
        const cal = max - (len - 2) * 2;
        const size = Math.max(min, Math.min(max, cal));
        return `font-size: ${size}px`
      },
      formatMessageTime(datetime) {
        const now = dayjs();
        const msgTime = dayjs(datetime);

        if (msgTime.isSame(now, 'day')) {
          return msgTime.format('A h:mm');
        } else {
          return msgTime.format('YYYY-MM-DD A h:mm');
        }
      },
      groupMessagesByDate(messages) {
        const grouped = {};

        messages.forEach(msg => {
          const date = dayjs(msg.sent_at).format('YYYY년 M월 D일');
          if (!grouped[date]) {
            grouped[date] = [];
          }
          grouped[date].push(msg);
        });
        return grouped;
      }
    }
  }).mount('#app');
}
