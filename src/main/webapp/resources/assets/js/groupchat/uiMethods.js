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

  onScroll() {
    const c = this.scrollTarget;
    if (!c || this.isLoading || this.noMoreMessages) return;

    if (c.scrollTop <= 10) {
      this.loadMessages();
    }
  },

  async openGroupSettingsModal() {
    const data = await this.fetchGroupDetail();
    this.selectedTags = data.tags || [];
    console.log('tags', this.selectedTags);
    this.groupDetail.group_no = data.group_no;
    this.groupDetail.group_name = data.group_name;
    this.groupDetail.description = data.description;
    this.groupDetail.capacity = data.capacity;
    this.groupDetail.is_public = data.is_public === 'Y';
    this.groupDetail.owner = data.owner;
    this.groupDetail.profile_img = data.profile_img;


    Object.assign(this.groupDetail, {
    ...data,
    is_public: data.is_public === 'Y' ? 'Y' : 'N'  
    });

    this.groupEditMode = String(this.sender_no) === String(this.groupDetail.owner);

    await this.$nextTick(() => {
      const modalEl = document.getElementById('groupSettingsModal');
      modalEl.removeAttribute('aria-hidden');
      if (modalEl) {
        new bootstrap.Modal(modalEl).show();
      }
    });
  },

  toggleGroupEditMode() {
    this.groupEditMode = true;
  },

  async saveGroupSettings() {
    await this.updateGroupDetail();
    alert('수정되었습니다.');

    const currentGroupno = this.group_no;

    await this.loadGroups();

    await this.joinGroup(currentGroupno);

    const updated = await this.fetchGroupDetail();

    Object.assign(this.groupDetail, {
      ...updated,
      is_public: ['Y', 'N'].includes(updated.is_public) ? updated.is_public : 'N'
    });
  },

  toggleTag(tag) {
    const idx = this.selectedTags.indexOf(tag);
    if (idx > -1) {
      this.selectedTags.splice(idx, 1);
    } else {
      this.selectedTags.push(tag);
    }
  },
  openCreateGroupModal() {
    Object.assign(this.groupDetail, {
      group_no: '',
      group_name: '',
      description: '',
      profile_img: null,
      capacity: 5,
      is_public: 'Y',
      owner: this.sender_no
    });
    this.selectedTags = [];

    this.$nextTick(() => {
      const modalEl = document.getElementById('createGroupModal');
      modalEl?.removeAttribute('aria-hidden');
      new bootstrap.Modal(modalEl).show();
    });
  },

  async openUserDetail(userNo) {
    try {
      const data = await this.fetchGroupMemberDetail(this.group_no, userNo);
      this.selectedUser = data;
      this.selectedUser.role = this.selectedUser.role === 'OWNER' ? '그룹장' : '일반 멤버';

      await this.$nextTick(() => {
        const modal = document.getElementById('userDetailModal');
        if (modal) new bootstrap.Modal(modal).show();
      });
    } catch (e) {
      alert('유저 정보를 불러오는 데 실패했습니다.');
      console.error(e);
    }
  }

};
