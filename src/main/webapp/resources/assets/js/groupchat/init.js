import { groupChatData } from './data.js';
import { apiMethods } from './apiMethods.js';
import { wsMethods } from './wsMethods.js';
import { uiMethods } from './uiMethods.js';
import { utilMethods } from './utilMethods.js';

export function initGroupChat(contextPath, createApp) {
  createApp({
    data() {
      return {
        ...groupChatData,
        contextPath
      };
    },
    mounted() {
      this.initialize();
      this.$nextTick(() => {
        this.scrollTarget = this.$refs.scrollContainer;
        if (this.scrollTarget) this.addEventListeners();
        else console.warn('scrollContainer ref를 찾지 못했습니다.');
      });
    },
    beforeUnmount() {
      this.removeEventListeners();
      this.subscription?.unsubscribe();
      this.stompClient?.disconnect(() => console.log('STOMP 연결 종료'));
    },
    methods: {
      ...apiMethods,
      ...wsMethods,
      ...uiMethods,
      ...utilMethods,

      async joinGroup(groupNo) {
        const selected = this.availableGroups.find(g => g.group_no === groupNo);
        this.currentGroupName = selected?.group_name || '그룹';
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

      async loadMessages() {
        const container = this.scrollTarget;
        let url = `${this.contextPath}/api/chats/groups/${this.group_no}/messages`;
        if (this.lastMessageNo) url += `?lastMessageNo=${this.lastMessageNo}`;

        const previousHeight = container?.scrollHeight || 0;
        const previousScrollTop = container?.scrollTop || 0;

        this.isLoading = true;
        const res = await axios.get(url);
        const newMessages = res.data.data.map(msg => ({
          ...msg,
          profile_img: msg.profile || `${this.contextPath}/assets/images/profile/default_pf.png`
        }));

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
      handleProfileImgChange(e) {
        const file = e.target.files[0];
        if (file) {
          const reader = new FileReader();
          reader.onload = e => {
            this.groupDetail.profile_img = e.target.result; 
          };
          reader.readAsDataURL(file);
        }
      },
      getNickname(userNo) {
        const m = this.members.find(m => m.user_no === userNo);
        return m?.nickname || '알 수 없음';
      }

    }
  }).mount('#app');
}
