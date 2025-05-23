import { groupChatData } from './data.js';
import { apiMethods } from './apiMethods.js';
import { wsMethods } from './wsMethods.js';
import { uiMethods } from './uiMethods.js';
import { utilMethods } from './utilMethods.js';
import { searchMethods } from './searchMethods.js';

export function initGroupChat(contextPath, createApp) {
  createApp({
    data() {
      return {
        ...groupChatData,
        contextPath
      };
    },
    computed: {
      searchModelLabel() {
        switch (this.searchMode) {
          case 'keyword': return '메세지 내용:';
          case 'sender': return '보낸 사람:';
          default: return '';
        }
      }
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
      ...searchMethods,

      async joinGroup(groupNo) {
        const prevGroupNo = this.group_no;

        if (prevGroupNo && prevGroupNo !== groupNo) {
          await this.markExit(prevGroupNo);
        }

        const group = this.availableGroups.find(g => g.group_no === groupNo);
        if (group) {
          console.log('알림 초기화', group.group_name);
          group.hasUnread = false;
        }

        await this.markViewing(groupNo, true);

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
        await this.loadGlobalOnlineUsers();
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
      },
      toggleEditNickname() {
        if (!this.editNicknameMode) {
          this.editedNickname = this.selectedUser.nickname;
          this.editNicknameMode = true;
        } else {
          if (!this.editedNickname.trim()) {
            alert('닉네임을 입력해주세요.');
            return;
          }
          console.log('selectedUser', this.selectedUser);
          console.log('nickname 변경 요청:', this.selectedUser.userNo, this.editedNickname);
          this.updateNickname(this.selectedUser.userNo, this.editedNickname);
        }
      },

      async updateNickname(userNo, nickname) {
        try {
          await axios({
            method: 'patch',
            url: `${this.contextPath}/api/groups/members/nickname`,
            params: { userNo, nickname }
          });
          alert('닉네임이 변경되었습니다.');
          this.selectedUser.nickname = nickname;
          const member = this.members.find(m => m.user_no === userNo);
          if (member) member.nickname = nickname;
          if (userNo === this.sender_no) this.sender_nickname = nickname;

          this.editNicknameMode = false;
        } catch (e) {
          console.error('닉네임 변경 실패', e);
          alert('닉네임 변경에 실패했습니다.');
        }
      }

    }
  }).mount('#app');
}
