<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

<style>
    body {
      background-color: #f6f7f8;
      font-family: 'Segoe UI', sans-serif;
      padding: 20px;
    }
    .custom-container {
      width: 70%;
      margin: 0 auto;
    }
    .feed-card {
      background: #fff;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
      max-width: 600px;  /* ✅ 추가 */
  	  margin: 0 auto;     /* ✅ 가운데 정렬 */
    }
    .feed-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .feed-author {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .feed-author img {
      width: 42px;
      height: 42px;
      border-radius: 50%;
      object-fit: cover;
    }
    .feed-title {
      font-size: 1.25rem;
      font-weight: bold;
    }
    .feed-content {
      margin-top: 12px;
      font-size: 1rem;
    }
    .carousel img {
      max-height: 400px;
      object-fit: cover;
    }
    .comment-box {
      background: #fff;
      padding: 16px;
      border: 1px solid #ddd;
      border-radius: 8px;
      margin-bottom: 12px;
    }
    .comment-input {
      min-height: 40px;
      resize: none;
      transition: all 0.3s ease;
    }
    .comment-input.expand {
      min-height: 100px;
    }
    .reply-box {
      margin-left: 20px;
      border-left: 2px solid #eee;
      padding-left: 10px;
    }
    @media (max-width: 768px) {
      .custom-container { width: 95%; }
    }
</style>
<div class="container pt-header mt-4" id="feedDetailApp">

   <div class="feed-card mb-4">
    <!-- 작성자 및 날짜 -->
    <div class="feed-header">
      <div class="feed-author">
        <img :src="feed_data.profile ? 
          (feed_data.profile.startsWith('http') ? feed_data.profile : 'https://pet4u.s3.ap-northeast-2.amazonaws.com/' + feed_data.profile) 
          : '/assets/images/profile/default.png'" alt="프로필">
        <div>
          <div>{{ feed_data.nickname }}</div>
          <small class="text-muted">{{ feed_data.dbday }}</small>
        </div>
      </div>
      <div class="dropdown">
        <button class="btn btn-sm btn-light" data-bs-toggle="dropdown">
          <i class="bi bi-three-dots-vertical"></i>
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="#">수정</a></li>
          <li><a class="dropdown-item text-danger" href="#">삭제</a></li>
        </ul>
      </div>
    </div>

    <!-- 제목 / 내용 -->
    <div class="feed-title mt-3">{{ feed_data.title }}</div>
    <div class="feed-content">{{ feed_data.content }}</div>
    
    <!-- 이미지 슬라이드 -->
    <div v-if="feed_data.images && feed_data.images.length" :id="'carousel-' + feed_data.feed_no" class="carousel slide my-3" data-bs-ride="carousel">
      <div class="carousel-inner">
        <div class="carousel-item" 
             v-for="(img, index) in feed_data.images" 
             :class="{ active: index === 0 }" :key="index">
          <img :src="img.startsWith('http') ? img : 'https://pet4u.s3.ap-northeast-2.amazonaws.com/' + img" class="d-block w-100 rounded">
        </div>
      </div>
      <button class="carousel-control-prev" type="button" :data-bs-target="'#carousel-' + feed_data.feed_no" data-bs-slide="prev">
        <span class="carousel-control-prev-icon"></span>
      </button>
      <button class="carousel-control-next" type="button" :data-bs-target="'#carousel-' + feed_data.feed_no" data-bs-slide="next">
        <span class="carousel-control-next-icon"></span>
      </button>
    </div>

    <!-- 좋아요 / 댓글 수 -->
    <div class="d-flex align-items-center gap-3 mt-3">
	  <!-- 좋아요 버튼: 클릭 여부에 따라 아이콘 변경 -->
	  <button @click="selectLike" class="btn btn-sm p-0 border-0 bg-transparent">
	    <i :class="liked ? 'bi bi-heart-fill text-danger fs-4' : 'bi bi-heart fs-4 text-muted'"></i>
	  </button>
	
	  <!-- 댓글 수: 동일한 스타일로 맞춤 -->
	  <div class="d-flex align-items-center text-muted fs-5">
	    <i class="bi bi-chat-dots me-1"></i>
	    <span>{{ feed_data.comment_count }}</span>
	  </div>
	</div>
	<!-- 댓글 입력 -->
    <div class="mt-4">
      <textarea v-model="newComment" 
                @focus="isExpanded = true"
                :class="['form-control comment-input', { expand: isExpanded }]"
                placeholder="댓글을 입력하세요..."></textarea>
      <div class="text-end mt-2" v-if="isExpanded">
        <button class="btn btn-sm btn-secondary me-2" @click="cancelComment">취소</button>
        <button class="btn btn-sm btn-primary" @click="submitComment">댓글쓰기</button>
      </div>
    </div>

    <!-- 댓글 목록 -->
    <div class="mt-4">
      <div v-for="comment in comment_list.filter(c => c.group_step === 0)" :key="comment.no" class="comment-box">
        <div class="d-flex justify-content-between">
          <div>
            <strong>{{ comment.nickname }}</strong>
            <small class="text-muted ms-2">{{ comment.dbday }}</small>
          </div>
          <div>
            <button class="btn btn-sm btn-outline-secondary" v-if="comment.user_no === user_no" @click="toggleEdit(comment.no, comment.msg)">수정</button>
            <button class="btn btn-sm btn-outline-danger" v-if="comment.user_no === user_no" @click="deleteComment(comment.no, comment.group_id,comment.group_step)">삭제</button>
          </div>
        </div>

        <!-- 수정 모드 -->
        <div v-if="editingComment === comment.no">
          <textarea v-model="comment.editMsg" class="form-control mt-2"></textarea>
          <div class="mt-2">
            <button class="btn btn-sm btn-outline-success me-2" @click="updateComment(comment.no, comment.editMsg)">수정 완료</button>
            <button class="btn btn-sm btn-outline-secondary" @click="CommentCancelUpdate(comment.no)">취소</button>
          </div>
        </div>
        <div v-else class="mt-2">{{ comment.msg }}</div>
		<div class="mt-2">
		  <button class="btn btn-sm btn-outline-success" @click="toggleReplyInput(comment.no)">
		    답글
		  </button>
		</div>
        <!-- 대댓글모드 -->
	    <div class="reply-box mt-2" v-if="replyInputVisible[comment.no]">
	      <textarea v-model="replyInput[comment.no]" class="form-control" rows="2" placeholder="대댓글을 입력하세요"></textarea>
	      <div class="mt-2 text-end">
	        <button class="btn btn-sm btn-secondary me-2" @click="cancelReply(comment.no)">취소</button>
	        <button class="btn btn-sm btn-primary" @click="submitReply(comment.group_id,comment.no)">등록</button>
	      </div>
	    </div>
	
	    <!-- 해당 댓글의 대댓글 리스트 -->
	    <div class="reply-box mt-2" v-for="reply in comment_list.filter(r => r.group_id === comment.group_id && r.group_step === 1 && r.no !== comment.no)" :key="reply.no">
	      <div class="d-flex justify-content-between">
	        <div>
	          <strong>{{ reply.nickname }}</strong>
	          <small class="text-muted ms-2">{{ reply.dbday }}</small>
	        </div>
	        <div>
	          <button class="btn btn-sm btn-outline-secondary me-1" v-if="reply.user_no === user_no" @click="toggleEdit(reply.no, reply.msg)">수정</button>
	          <button class="btn btn-sm btn-outline-danger me-1" v-if="reply.user_no === user_no" @click="deleteComment(reply.no, reply.group_id, reply.group_step)">삭제</button>
	        </div>														
	      </div>
	
	      <div v-if="editingComment === reply.no">
	        <textarea v-model="reply.editMsg" class="form-control mt-2"></textarea>
	        <div class="mt-2 text-end">
	          <button class="btn btn-sm btn-outline-primary me-1" @click="updateComment(reply.no, reply.editMsg)">수정 완료</button>
	          <button class="btn btn-sm btn-outline-secondary" @click="CommentCancelUpdate(reply.no)">취소</button>
	        </div>
	      </div>
	      <div v-else class="mt-2">{{ reply.msg }}</div>

	     </div>
 
      </div>
      <!-- 더보기 -->
      <div v-if="list.length > visibleComments.length" class="text-center mt-3">
        <button class="btn btn-outline-dark btn-sm" @click="loadMore">댓글 더보기</button>
      </div>
    </div>
    
  </div>
</div>
	
<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';

  createApp({
    data() {
      return {
		feed_data:{},
		list:[],
		visibleCount:5,
        comment_list:[],
		feed_no:0,
		user_no:0,
		msg:'',
		page:1,
		newComment: '',
		editingComment:null,
		liked: false,
        isExpanded: false,
		replyInput: {}, // 대댓글 입력 내용 저장
    	replyInputVisible: {} // 대댓글 입력창 표시 여부
      }
    },
	computed: {
      visibleComments() {
        return this.list.slice(0, this.visibleCount);
      }
    },
	mounted(){
		const params = new URLSearchParams(window.location.search);
    	const feedNoParam = params.get('feed_no');
    	if (feedNoParam) {
      	this.feed_no = parseInt(feedNoParam);
    	}
		this.dataRecv();
		this.commentDataRecv();
		this.fetchLoginUser();
	},
    methods: {
		async fetchLoginUser() {
			
			const contextPath = "${pageContext.request.contextPath}";
			console.log('contextPath:', contextPath);
			try{
				const res=await axios.get(contextPath+'/api/token');
				this.user_no = res.data.userNo;
				console.log('로그인 유저 번호:', this.user_no);
			} catch(err) {
				console.error("로그인 사용자 정보 가져오기 실패",err)
			}
		},
		selectLike() {
    		axios.post('../api/feed/'+this.feed_no+'/like')
      		.then(() => {
        		this.liked = !this.liked;
      		})
      		.catch(err => {
        		console.log(err);
      		});
  		},
	    async dataRecv(){
			console.log("dataRecv 실행")
			console.log(this.feed_no)
			const res = await axios.get('../api/feeds/'+this.feed_no)
			.then(res=>{
				console.log(res.data)
				this.feed_data=res.data;
			}).catch(err=>{
				console.log(err)
			})
            
	  },
	   async commentDataRecv(){
			console.log("dataRecv 실행")
			console.log(this.feed_no)
			const res = await axios.get('../api/feeds/'+this.feed_no+'/comments',{
				params:{
					page:this.page
				}
			})
			.then(res=>{
				console.log(res.data)
				this.comment_list=res.data.list
			}).catch(err=>{
				console.log(err)
			})
            
		},
	  loadMore() {
        this.visibleCount += 5;
      },
      expandInput() {
        this.isExpanded = true;
      },
      cancelComment() {
        this.isExpanded = false;
        this.newComment = '';
      },
 	  cancelReply(commentNo) {
  		this.replyInputVisible[commentNo] = false;
  		this.replyInput = {
    		...this.replyInput,
    		[commentNo]: ''
  		};
	  },
      submitComment() {
        if (this.newComment.trim() !== '') {
          axios.post('../api/feed/'+this.feed_no+'/comments',null,{
				params:{
					feed_no:this.feed_no,
					msg:this.newComment
				}
		  }).then(res=>{
			 console.log(res.data)
			 this.list=res.data.list
			 this.curpage=res.data.curpage
   		 	 this.totalpage=res.data.totalpage
   			 this.startPage=res.data.startPage
   			 this.endPage=res.data.endPage
   			 this.cancelComment(); //입력창 비우기, 확장 해제 등 후처리
			 this.dataRecv();
			 this.commentDataRecv();
		  }).catch(error=> {
			 console.log(error)
		  })
          
        }
		
      },
	  submitReply(groupId,comment_no) {
    	console.log("등록하기버튼")
		const msg = this.replyInput[comment_no];
		console.log("msg"+msg)
    	if (!msg || msg.trim() === '') 
		{	
			alert("메시지를 입력하세요")	
			return;
		}	

    	axios.post('../api/feed/reply/'+comment_no, {
			no:comment_no,
      		feed_no: this.feed_no,
      		group_id: groupId,
      		msg: msg,
    		}, {
  				headers: {
    				'Content-Type': 'application/json'
  				}
			}).then(() => {
      		this.replyInput[comment_no] = '';
      		this.replyInputVisible[comment_no] = false;
			this.commentDataRecv();
    	}).catch(error=> {
			 console.log("대댓글 등록 오류:", error)
		});
  	  },
      replyUpdateForm(no){
		 console.log("수정버튼 클릭")
         console.log("no값은 "+no)
		 if(this.editingComment === no) {
			this.editingComment = null;
		 }
		 else {
            console.log("this.editingComment값" + this.editingComment)
			this.editingComment = no;
			console.log("변경 후this.editingComment값" + this.editingComment)
		 }
	  },
	  CommentCancelUpdate(no){
		 const comment = this.list.find(c => c.no === no)
 		 if (comment) {
 		   comment.editMsg = '' // 입력값 초기화
		  }
         this.editingComment = null;
		 
	  },
	  toggleEdit(no, msg) {
        this.editingComment = no;
        const comment = this.list.find(c => c.no === no);
        if (comment) comment.editMsg = msg;
      },
	  toggleReplyInput(commentNo) {
    	this.replyInputVisible[commentNo] = true;
  	  },
	  updateComment(comment_no, editMsg){
		 if(editMsg.trim() !== '')
		 {
		 	axios.put('../api/feed/comments/'+comment_no,null,{
				params:{
					msg:editMsg,
					no:comment_no
				}
			}).then(res=> {
				const comment = this.list.find(c => c.no === comment_no)
 		 		if (comment) {
 		   			comment.msg = editMsg // 입력값 초기화
					comment.editMsg='';
		  		}
			    this.editingComment = null;
				this.commentDataRecv();
			 	this.dataRecv();
			}).catch(error => {
				console.log(error)
			})	
		 }
	  },
	  deleteComment(comment_no, group_id,group_step) {
        axios.delete('../api/feed/comments/'+comment_no,{
				params:{
					no:comment_no,
					group_id:group_id,
					group_step:group_step
				}
			}).then(res=> {
				this.commentDataRecv();
			 	this.dataRecv();
			}).catch(error => {
				console.log(error)
			})	
      }
    }
  }).mount('#feedDetailApp');
</script>

