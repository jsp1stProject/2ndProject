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
                isLoading: false,
                noMoreMessages: false,
            };
        },
        mounted() {
            this.initialize();

            this.$refs.scrollContainer.addEventListener('scroll', this.onScroll);

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
            scrollToBottom() {
                Vue.nextTick(() => {
                    const container = this.$refs.scrollContainer;
                    if (container) {
                        container.scrollTop = container.scrollHeight;
                    }
                });
            },
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
                        { Authorization: 'Bearer ' + accessToken },
                        () => this.loadGroups(),
                        (error) => console.error('STOMP 연결 실패', error)
                    );
                } catch (err) {
                    console.error('토큰 가져오기 실패:', err);
                }
            },
            async loadGroups() {
                try {
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
                if (!this.group_no || this.noMoreMessages) {
                    return;
                }  
                let url = `${contextPath}/api/chats/groups/${this.group_no}/messages`;
                if (this.lastMessageNo) {
                    url += `?lastMessageNo=${this.lastMessageNo}`;
                }

                const container = this.$refs.scrollContainer;
                const oldScrollHeight = container.scrollHeight;

                try {
                    this.isLoading = true;
                    const res = await axios.get(url);

                    const newMessages = res.data.data;
                    console.log('res: ', res.data);

                    if (newMessages.length === 0) {
                        this.noMoreMessages = true;
                        return newMessages;
                    }
                    if (!this.lastMessageNo) {
                        this.messages = newMessages;
                    } else {
                        this.messages.unshift(...newMessages);
                    }
                    if (newMessages.length > 0) {
                        this.lastMessageNo = newMessages[0].message_no; // 이 부분 수정
                    }
                    Vue.nextTick(() => {
                        const newScrollHeight = container.scrollHeight;
                        if (this.lastMessageNo) {
                            container.scrollTop = newScrollHeight - oldScrollHeight;
                        } else {
                            container.scrollTop = container.scrollHeight;
                        }
                    });

                    return newMessages;
                } catch (err) {
                    console.error('이전 메시지 불러오기 실패: ', err);
                } finally {
                    this.isLoading = false;
                }
            },
            async onScroll() {
                const container = this.$refs.scrollContainer;
                if (container.scrollTop === 0 && !this.isLoading && !this.noMoreMessages) {
                    if (this.message.length > 0) {
                        this.lastMessageNo = this.messages[0].message_no;
                    }
                    const newMessages = await this.loadMessages();
                    if (newMessages && newMessages.length === 0) {
                        this.noMoreMessages = true;
                    }
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

                    if (body.sender_no === this.sender_no) {
                        Vue.nextTick(() => {
                            this.scrollToBottom();
                        });
                    }
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
        },
        watch: {
            messages() {
                Vue.nextTick(() => {
                    const container = this.$refs.scrollContainer;
                    if (container) {
                        container.scrollTop = container.scrollHeight;
                    }
                });
            }
        }
    }).mount('#app');
}
