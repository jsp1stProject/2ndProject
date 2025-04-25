export function initGroupChat(contextPath, createApp) {
    Vue.createApp({
        data() {
            return {
                stompClient: null,
                messages: [],
                message: '',
                sender_no: '',
                sender_nickname: '',
                availableGroups: [],
                group_no: '',
                subscription: null,
                createCheck: false,
                group_name: '',
                group_description: '',
                lastMessageNo: null,
            };
        },
        mounted() {
            this.initialize();

            this._handleVisibilityChange = () => {
                if(document.visibilityState === 'visible') {
                    if(!this.stompClient?.connected) {
                        console.warn('STOMP 연결 없음');
                        this.initialize();
                    }
                }
            };
            document.addEventListener('visibilitychange', this._handleVisibilityChange);
        },
        beforeUnmount() {
            if (this._handleVisibilityChange) {
                document.removeEventListener('visibilitychange', this._handleVisibilityChange);
            }
            if(this.stompClient?.connected) {
                this.stompClient.disconnect(() => {
                    console.log('STOMP 연결 종료');
                })
            }
        },
        methods: {
            async errTest() {
                try {
                    const res = await axios.get(`${contextPath}/api/err/test`);
                    const data = res.data;
                } catch (error) {
                    const { code, message } = error.response.data;
                    alert(`[${code}] ${message}`);
                    console.error('err: ', error);
                }
            },
            async initialize() {
                try {
                    const res = await axios.get(`${contextPath}/api/token`);
                    const accessToken = res.data.token;
                    this.sender_no = res.data.userNo;
                    this.sender_nickname = res.data.nickname;
                    const socket = new SockJS(`${contextPath}/ws`);
                    this.stompClient = Stomp.over(socket);

                    this.stompClient.heartbeat.outgoing = 10000; // client -> server
                    this.stompClient.heartbeat.incoming = 10000; // server -> client

                    this.stompClient.connect(
                        { Authorization: 'Bearer ' + accessToken,},
                        () => {
                            console.log('STOMP 연결 성공');
                            try {
                                this.loadGroups();
                            } catch (error) {
                                console.log('에러발생');
                                setTimeout(connect, 3000);
                            }
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
                    console.log('groupNo: ' + this.group_no);
                    const res = await axios.get(`${contextPath}/api/groups`);
                    this.availableGroups = res.data.data;
                    if (this.availableGroups.length > 0) {
                        this.group_no = this.availableGroups[0].group_no;
                        this.subscribeGroup();
                    }
                } catch (err) {
                    console.error('group_id 불러오기 실패: ', err);
                }						 
            },
            async loadMessages() {
                if (!this.group_no) {
                    alert('그룹이 존재하지 않습니다.');
                    console.log('그룹 없음: ' + this.group_no);
                    return;
                }  
                let url = `${contextPath}/api/chats/groups/${this.group_no}/messages`;

                if (this.lastMessageNo) {
                    url += `?lastMessageNo=${this.lastMessageNo}`;
                }
                try {
                    const res = await axios.get(url);

                    const newMessages = res.data.data;
                    console.log('res: ', res.data);
                    if (!this.lastMessageNo) {
                        this.messages = newMessages;
                    } else {
                        this.messages.unshift(...newMessages);
                    }

                    if (newMessages.length > 0) {
                        this.lastMessageNo = newMessages[newMessages.length - 1].message_no;
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
                this.lastMessageNo = null;
                this.loadMessages();
                this.subscription = this.stompClient.subscribe(`/sub/chats/groups/${this.group_no}`, (msg) => {
                    const body = JSON.parse(msg.body);
                    this.messages.push(body);
                },{
                    nickname: this.sender_nickname
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
                    sender_no: this.sender_no,
                    sender_nickname: this.sender_nickname,
                    content: this.message,
                    group_no: this.group_no,
                };
                
                if (this.stompClient && this.stompClient.connected) {
                    this.stompClient.send(`/pub/chats/groups/${this.group_no}`, {}, JSON.stringify(chatMessage));
                    this.message = ''; // /app/chat => 클라이언트 -> 서버
                }
            },
            groupOpen() {
                this.createCheck = !this.createCheck;
            },
            async createGroup() {
                try {
                    const res = await axios.post(`${contextPath}/api/groups`, {
                        owner: this.sender_no,
                        group_name: this.group_name,
                        description: this.group_description
                    });
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