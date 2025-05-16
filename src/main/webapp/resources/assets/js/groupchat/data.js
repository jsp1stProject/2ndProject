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

  scrollTarget: null,
  
  groupDetail: {
    group_no: '',
    group_name: '',
    description: '',
    profile_img: null,
    capacity: 0,
    is_public: '',
    owner: ''
  },
  allTags: [
    '산책', '사료공유', '훈련정보', '미용', 
    '병원정보', '입양', '사진공유', '소형견', 
    '대형견', '고양이', '중성화', '혼종사랑'
  ],
  selectedTags: [],
  groupEditMode: false
};
