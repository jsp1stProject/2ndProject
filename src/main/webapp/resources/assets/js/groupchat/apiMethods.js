export const apiMethods = {
  async loadGroups() {
    const res = await axios.get(`${this.contextPath}/api/groups/${this.sender_no}`);
    this.availableGroups = res.data.data;
    if (this.availableGroups.length > 0) {
      await this.joinGroup(this.availableGroups[0].group_no);
    }
  },

  async createGroup() {
    const res = await axios.post(`${this.contextPath}/api/groups`, {
      owner: this.sender_no,
      group_name: this.group_name,
      description: this.group_description
    });
    alert(`그룹 이름: ${res.data.group_name} 으로 생성되었습니다.`);
    this.createCheck = false;
    await this.loadGroups();
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
    console.log('res: ', res);
    return res.data.data;
  },

  async updateGroupDetail() {
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
      `${this.contextPath}/api/groups/${this.groupDetail.group_no}`, formData
    );

    return res.data;
  }
};
