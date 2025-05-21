export const apiMethods = {
  async loadGroups() {
    const res = await axios.get(`${this.contextPath}/api/groups/${this.sender_no}`);
    this.availableGroups = res.data.data;
    if (this.availableGroups.length > 0) {
      await this.joinGroup(this.availableGroups[0].group_no);
    }
  },

  async loadGroupMembers() {
    const res = await axios.get(`${this.contextPath}/api/groups/${this.group_no}/members`);
    this.members = res.data.data.map(m => ({
      user_no: m.user_no,
      nickname: m.nickname,
      profile_img: m.profile,
      isOnline: false
    }));
  },

  async loadInitialOnlineUsers() {
    const res = await axios.get(`${this.contextPath}/api/groups/${this.group_no}/online`);
    this.onlineUserNos = res.data.data.map(u => Number(u.userNo));
    this.updateMemberOnlineStatus();
  },
  
  async fetchGroupDetail() {
    const res = await axios.get(`${this.contextPath}/api/groups/${this.group_no}/detail`);
    return res.data.data;
  },

  async updateGroupDetail() {
    if (!this.groupDetail.group_name?.trim()) {
      alert('그룹명을 입력해주세요.');
      return;
    }
    if (!this.groupDetail.description?.trim()) {
      alert('그룹 설명을 입력해주세요.');
      return;
    }

    const dto = {
      ...this.groupDetail,
      tags: this.selectedTags,
    };
    
    const formData = new FormData();
    formData.append(
      'groupDetail',
      new Blob([JSON.stringify(dto)], { type: 'application/json' })
    );

    const fileInput = this.$refs.profileImgInput;
    if (fileInput && fileInput.files && fileInput.files[0]) {
      formData.append('profileImg', fileInput.files[0]);
    }

    const res = await axios.put(
      `${this.contextPath}/api/groups/${this.groupDetail.group_no}`, formData);

    return res.data;
  },
  async submitCreateGroup() {
    if (!this.groupDetail.group_name?.trim()) {
      alert('그룹명을 입력해주세요.');
      return;
    }
    if (!this.groupDetail.description?.trim()) {
      alert('그룹 설명을 입력해주세요.');
      return;
    }
    const dto = { ...this.groupDetail, tags: this.selectedTags };
    const formData = new FormData();
    formData.append('groupDetail', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

    const fileInput = this.$refs.profileImgInput;
    if (fileInput?.files?.[0]) {
      formData.append('profileImg', fileInput.files[0]);
    }
    try {
      const res = await axios.post(`${this.contextPath}/api/groups`, formData);
      alert('그룹이 생성되었습니다.');
      bootstrap.Modal.getInstance(document.getElementById('createGroupModal')).hide();
      await this.loadGroups();
    } catch (error) {
      console.error('그룹 생성 실패', error);
      alert('그룹 생성에 실패했습니다.');
    }
  },

  async markViewing(groupNo, isViewing) {
    try {
      await axios.patch(`${this.contextPath}/api/chats/groups/${groupNo}/viewing`, null, {
        params: { viewing: isViewing }
      });
      console.log('markViewing 성공');
    } catch (e) {
      console.error('viewing 상태 업데이트 실패: ', e);
    }
  },

  async markExit(groupNo) {
    try {
      await axios.patch(`${this.contextPath}/api/chats/groups/${groupNo}/exit`);
      console.log('markExit 성공');
    } catch (e) {
      console.error('채팅방 나가기 실패', e);
    }
  }
};
