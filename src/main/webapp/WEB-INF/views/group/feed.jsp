<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
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
    <div class="d-flex gap-3 mt-2 text-muted fs-6">
      <span>❤️ {{ feed_data.like_count }}</span>
      <span>💬 {{ feed_data.comment_count }}</span>
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
      <div v-for="comment in comment_list" :key="comment.no" class="comment-box">
        <div class="d-flex justify-content-between">
          <div>
            <strong>{{ comment.nickname }}</strong>
            <small class="text-muted ms-2">{{ comment.dbday }}</small>
          </div>
          <div>
            <button class="btn btn-sm btn-outline-secondary" @click="toggleEdit(comment.no, comment.msg)">수정</button>
            <button class="btn btn-sm btn-outline-danger" @click="deleteComment(comment.no, comment.group_id,comment.group_step)">삭제</button>
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
        <!-- 대댓글 부분 -->
        <!-- <div v-if="comment.replies && comment.replies.length" class="reply-box mt-2">
		    <div v-for="reply in comment.replies" :key="reply.no" class="mt-2">
		      <strong>{{ reply.nickname }}</strong>
		      <small class="text-muted ms-2">{{ reply.dbday }}</small>
		      <div>{{ reply.msg }}</div>
		    </div>
		  </div>
		
		  ✅ [2] 대댓글 입력창
		  <div class="reply-box mt-2" v-if="replyInputVisible[comment.no]">
		    <textarea v-model="replyInput[comment.no]" class="form-control" rows="2" placeholder="대댓글을 입력하세요"></textarea>
		    <div class="mt-2 text-end">
		      <button class="btn btn-sm btn-secondary me-2" @click="replyInputVisible[comment.no] = false">취소</button>
		      <button class="btn btn-sm btn-primary" @click="submitReply(comment.no)">등록</button>
		    </div>
		  </div>
		
		  ✅ [3] 대댓글 버튼
		  <div class="mt-2">
		    <button class="btn btn-sm btn-outline-success" @click="replyInputVisible[comment.no] = true">답글</button>
		  </div>
		</div> -->
		<div class="reply-box mt-2">
		    <div class="mt-2">
		      <strong>홍길동</strong>
		      <small class="text-muted ms-2">25.05.21</small>
		      <div>대대대대대대대대대대대대대대대대대대대댓글</div>
		    </div>
		  </div>
		
		  <!-- ✅ [2] 대댓글 입력창 -->
		  <div class="reply-box mt-2">
		    <textarea class="form-control" rows="2" placeholder="대댓글을 입력하세요"></textarea>
		    <div class="mt-2 text-end">
		      <button class="btn btn-sm btn-secondary me-2">취소</button>
		      <button class="btn btn-sm btn-primary">등록</button>
		    </div>
		  </div>
		
		  <!-- ✅ [3] 대댓글 버튼 -->
		  <div class="mt-2">
		    <button class="btn btn-sm btn-outline-success">답글</button>
		  </div>
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
        isExpanded: false
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
	},
    methods: {
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
			 console.log(error.res)
		  })
          
        }
		
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
	  updateComment(comment_no, editMsg){
		 if(editMsg.trim() !== '')
		 {
		 	axios.put('../api/feed/comments/'+comment_no,null,{
				params:{
					msg:editMsg,
					no:comment_no
				}
			}).then(res=> {
				const comment = this.list.find(c => c.no === no)
 		 		if (comment) {
 		   			comment.msg = editMsg // 입력값 초기화
					comment.editMsg='';
		  		}
			    this.editingComment = null;
				this.commentDataRecv();
			 	this.dataRecv();
			}).catch(error => {
				console.log(error.res)
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
				console.log(error.res)
			})	
      }
    }
  }).mount('#feedDetailApp');
</script>

