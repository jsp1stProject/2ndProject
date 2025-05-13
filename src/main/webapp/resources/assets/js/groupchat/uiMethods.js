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

  async openGroupSettingsModal() {
    this.groupDetail = await this.fetchGroupDetail();
    this.groupEditMode = false;

    const modal = new bootstrap.Modal(document.getElementById('groupSettingsModal'));
    modal.show();
  },

  toggleGroupEditMode() {
    this.groupEditMode = true;
  },

  async saveGroupSettings() {
    await this.updateGroupDetail();
    alert('수정되었습니다.');
    this.groupEditMode = false;
    await this.loadGroups();
  }
};
