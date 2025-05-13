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
    const res = await axios.get(`${this.contextPath}/api/groups/members`, {
      params: { groupNo: this.group_no }
    });
    this.members = res.data.data.map(m => ({
      user_no: m.user_no,
      nickname: m.nickname,
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
    const payload = {
      group_name: this.groupDetail.group_name,
      description: this.groupDetail.description
    };
    const res = await axios.put(`${this.contextPath}/api/groups/${this.groupDetail.group_no}`, payload);
    return res.data;
  }
};
