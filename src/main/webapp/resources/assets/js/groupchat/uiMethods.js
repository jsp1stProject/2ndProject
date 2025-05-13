export const uiMethods = {
  scrollToBottom() {
    const c = this.scrollTarget;
    if (c) c.scrollTop = c.scrollHeight;
  },

  getGroupFontSize(name) {
    const len = name.length;
    const max = 19;
    const min = 13;
    const cal = max - (len - 2) * 2;
    const size = Math.max(min, Math.min(max, cal));
    return `font-size: ${size}px`;
  },

  groupMessagesByDate(messages) {
    const grouped = {};
    messages.forEach(msg => {
      const date = dayjs(msg.sent_at).format('YYYY년 M월 D일');
      if (!grouped[date]) grouped[date] = [];
      grouped[date].push(msg);
    });
    return grouped;
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
  }
};
