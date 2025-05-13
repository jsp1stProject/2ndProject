export const groupChatData = {
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
