export function initGroupChat(contextPath, createApp) {
    Vue.createApp({
        data() {
            return {
                stompClient: null,
					messages: [],
					message: '',
					sender_id: '',
					availableGroups: [],
					group_id: '',
					subscription: null,
					createCheck: false,
					group_name: '',
					group_description: '',
					lastMessageId: null,
            };
        },
        mounted() {
            this.initialize();
        },
        methods: {
            async initialize() {
                try {
                    const res = await axios.get(`${contextPath}/api/token`);
                    const accessToken = res.data.token;
                    this.sender_id = res.data.userId;
                    console.log(accessToken);
                    
                    const socket = new SockJS(`${contextPath}/ws`);
                    this.stompClient = Stomp.over(socket);
                    
                    this.stompClient.connect(
                        { Authorization: 'Bearer ' + accessToken },
                        () => {
                            console.log('STOMP 연결 성공');
                            this.loadGroups();
                        },
                        (error) => {
                            console.error('STOMP 연결 실패', error);
                        }
                    );
                } catch (err) {
                    console.error('토큰 가져오기 실패:', err);
                }
            },
            async loadGroups() {
                try {
                    const res = await axios.get(`${contextPath}/groups`, {
                        params: {
                            userId: 'user'
                        }
                    });
                    console.log('res: ' + JSON.stringify(res.data));
                    this.availableGroups = res.data;
                    this.group_id = this.availableGroups[0].group_id; // default room
                    this.subscribeGroup();
                } catch (err) {
                    console.error('group_id 불러오기 실패: ', err);
                }						 
            },
            async loadMessages() {
                let url = `${contextPath}/chats/groups/${this.group_id}/messages`;
                if (this.lastMessageId) {
                    url += `?lastMessageId=${this.lastMessageId}`;
                }
                try {
                    const res = await axios.get(url);
                    const newMessages = res.data;

                    if (!this.lastMessageId) {
                        this.messages = newMessages;
                    } else {
                        this.messages.unshift(...newMessages);
                    }

                    if (newMessages.length > 0) {
                        this.lastMessageId = newMessages[newMessages.length - 1].message_id;
                    }
                } catch (err) {
                    console.error('이전 메시지 불러오기 실패: ', err);
                }
            },
            subscribeGroup() {
                if (this.subscription) {
                    this.subscription.unsubscribe();
                }
                this.messages = []; // /topic/chat/ => 서버 -> 클라이언트
                this.lastMessageId = null;
                this.loadMessages();
                this.subscription = this.stompClient.subscribe(`/sub/chats/groups/${this.group_id}`, (msg) => {
                    const body = JSON.parse(msg.body);
                    this.messages.push(body);
                });
            },
            changeGroup() {
                this.subscribeGroup();
            },
            sendMessage() {
                if (!this.message.trim()) {
                    return;
                }
                const chatMessage = {
                    sender_id: this.sender_id,
                    content: this.message,
                    group_id: this.group_id,
                };
                
                if (this.stompClient && this.stompClient.connected) {
                    this.stompClient.send(`/pub/chats/groups/${this.group_id}`, {}, JSON.stringify(chatMessage));
                    this.message = ''; // /app/chat => 클라이언트 -> 서버
                }
            },
            groupOpen() {
                this.createCheck = !this.createCheck;
            },
            async createGroup() {
                try {
                    const res = await axios.post(`${contextPath/groups}`, {
                        created_by: 'user',
                        group_name: this.group_name,
                        description: this.group_description
                    });
                    console.log('room_res: ' + res.data);
                    alert('그룹 이름: ' + res.data.group_name + '으로 생성되었습니다.');
                    this.createCheck = !this.createCheck;
                    this.loadGroups();
                } catch (err) {
                    console.error('요청 실패: ', err);
                }	
            }
        }
    }).mount('#app');
}