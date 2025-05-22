
export const searchMethods = {
  async searchMessages() {
    const filters = { ...this.filters };
    if (this.searchMode === 'keyword') filters.keyword = this.searchInput;
    if (this.searchMode === 'sender') filters.senderNickname = this.searchInput;

    const res = await axios.get(`${this.contextPath}/api/chats/groups/${this.group_no}/messages/search`, {
      params: filters
    });

    this.searchResults = res.data.data;

    if (!this.searchResults || this.searchResults.length === 0) {
      alert('검색 결과가 없습니다.');
      return;
    }

    await this.$nextTick();

    const dropdown = document.querySelector('.dropdown-menu.show');
    if (dropdown) dropdown.classList.remove('show');
  },

  async jumpToMessage(messageNo) {
    const res = await axios.get(`${this.contextPath}/api/chats/groups/${this.group_no}/messages/around`, {
      params: { messageNo }
    });

    const newMessages = res.data.data.map(msg => {
      const member = this.members.find(m => m.user_no === msg.sender_no);
      return {
        ...msg,
        profile_img: member?.profile_img || `${this.contextPath}/assets/images/profile/default_pf.png`
      };
    });

    const messageMap = new Map();
    [...this.messages, ...newMessages].forEach(m => messageMap.set(m.message_no, m));
    this.messages = Array.from(messageMap.values()).sort((a, b) => a.message_no - b.message_no);

    await this.$nextTick();
    const el = document.getElementById(`msg-${messageNo}`);
    if (el) {
      el.classList.add('highlight');
      el.scrollIntoView({ behavior: 'smooth', block: 'center' });
      setTimeout(() => el.classList.remove('highlight'), 2000);
    }
  },

  setSearchMode(mode) {
    this.searchMode = mode;
    this.searchInput = '';
    this.$nextTick(() => this.$refs.searchInput?.focus());
  },

  handleBackspace(e) {
    if (this.searchInput === '' && this.searchMode) {
      this.searchMode = '';
    }
  },

  handleInputFocus() {
    if (this.searchResults.length > 0) {
      this.searchResults = [];
    }
  }
};
    