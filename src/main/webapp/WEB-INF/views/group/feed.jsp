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
.comment-input {
  transition: all 0.3s;
  min-height: 40px;
  resize: none;
}
.comment-input.expand {
  min-height: 100px;
}

.comment-menu {
  position: absolute;
  right: 0;
  background: #fff;
  border: 1px solid #ddd;
  padding: 8px;
  border-radius: 6px;
  z-index: 10;
}

.comment-box {
  background-color: #fff;
  border: 1px solid #ddd;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
  position: relative;
}

.comment-author {
  font-weight: 600;
}

.comment-date {
  font-size: 0.85rem;
  color: #999;
  margin-bottom: 5px;
}

.comment-actions button {
  margin-right: 6px;
}

.reply-box {
  margin-top: 10px;
  padding-left: 20px;
  border-left: 2px solid #e0e0e0;
}

textarea.form-control {
  resize: none;
}

.reply-form {
  margin-top: 10px;
}

@media (max-width: 768px) {
  .custom-container { width: 95%; }
}
</style>
</head>
<body>
<div class="container pt-header mt-4" id="app">
  <div class="card mb-4">
    <div class="card-header d-flex justify-content-between align-items-center">
      <div>
        <strong>${user_no }</strong>
        <small class="text-muted ms-2">${vo.dbday}</small>
      </div>
      <button class="btn btn-sm btn-light">...</button>
    </div>

    <div class="card-body">
      <strong>${vo.title}</strong>
      <p class="card-text">${vo.content}</p>

      <!-- 이미지 슬라이드 -->
      <c:if test="${not empty vo.images}"> <!-- vo.images != null 이거보다 더 안전하고 간결한 표현 -->
      <div id="feedCarousel" class="carousel slide my-3" data-bs-ride="carousel">
        <div class="carousel-inner">
          <c:forEach var="img" items="${vo.images}"  varStatus="status">
          <div class="carousel-item ${status.index == 0 ? 'active' : ''}">
            <img src="/web/images/${img}" class="d-block w-100 rounded" style="height: 400px; object-fit: cover;">
          </div>
          </c:forEach>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#feedCarousel" data-bs-slide="prev">
          <span class="carousel-control-prev-icon"></span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#feedCarousel" data-bs-slide="next">
          <span class="carousel-control-next-icon"></span>
        </button>
      </div>
      </c:if>

      <!-- 댓글 작성 영역 -->
	  <div class="mb-4">
		  <textarea v-model="newComment" @focus="isExpanded = true"
		            :class="['form-control', { expand: isExpanded }]"
		            placeholder="댓글을 입력하세요..."></textarea>
		  <div class="mt-2 text-end">
		    <button class="btn btn-sm btn-secondary me-2" v-if="isExpanded" @click="cancelComment">취소</button>
		    <button class="btn btn-sm btn-primary" @click="submitComment">댓글쓰기</button>
		  </div>
		</div>
		
		<!-- 댓글 목록 -->
		<div v-for="vo in list" :key="vo.no" class="mb-3">
		  <!-- 댓글 -->
		  <div class="p-3 bg-white rounded shadow-sm">
		    <div class="d-flex justify-content-between">
		      <div>
		        <strong>{{ vo.user_no }}</strong>
		        <small class="text-muted ms-2">{{ vo.dbday }}</small>
		      </div>
		      <div>
		        <button v-if="editingComment !==vo.no" class="btn btn-sm btn-outline-primary me-1" @click="replyUpdateForm(vo.no, vo.msg)">수정</button>
		        <button class="btn btn-sm btn-outline-danger me-1" @click="replyDeleteForm(vo.no)">삭제</button>
		        <button class="btn btn-sm btn-outline-success" @click="replyReplyInsertForm(vo.no)">답글</button>
		      </div>
		    </div>
		    <div class="mt-2">{{ vo.msg }}</div>
		    <textarea v-if="editingComment == vo.no" v-model="vo.editMsg" class="form-control mt-2"></textarea>
		    <div v-if="editingComment == vo.no">
		     <button class="btn btn-sm btn-outline-success me-1" @click="replyUpdate(vo.no, vo.editMsg)">수정하기</button>
		     <button class="btn btn-sm btn-outline-secondary me-1" @click="replyCancelUpdate(vo.no)">취소</button>
		    </div>
		  </div>
		
		  <!-- 대댓글 -->
		  
		</div>
	
	  
	</div>
    </div>
  </div>
</div>

<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';

  createApp({
    data() {
      return {
		list:[],
        comment_list:[],
		feed_no:${vo.feed_no},
		user_no:${user_no},
		msg:'',
		page:1,
		newComment: '',
		editingComment:null,
        isExpanded: false
      }
    },
	mounted(){
		this.dataRecv()
	},
    methods: {
	    async dataRecv(){
			console.log("dataRecv 실행")
			console.log(this.feed_no)
			const res = await axios.get('../feed/comments',{
					params:{
							feed_no:this.feed_no,
							page:this.page
					}
			}).then(res=>{
				console.log(res.data)
				this.list=res.data.list
			}).catch(res=>{
				console.log(error.response)
			})
            
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
          axios.post('../feed/comments',null,{
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
	  replyCancelUpdate(no){
		 const comment = this.list.find(c => c.no === no)
 		 if (comment) {
 		   comment.editMsg = '' // 입력값 초기화
		  }
         this.editingComment = null;
		 
	  },
	  replyUpdate(no, editMsg){
		 if(editMsg.trim() !== '')
		 {
		 	axios.post('../feed/comments_update',null,{
				params:{
					msg:editMsg,
					no:no
				}
			}).then(res=> {
				const comment = this.list.find(c => c.no === no)
 		 		if (comment) {
 		   			comment.msg = editMsg // 입력값 초기화
					comment.editMsg='';
		  		}
			    this.editingComment = null;
			 	
			}).catch(error => {
				console.log(error.res)
			})	
		 }
	  }
    }
  }).mount('#app');
</script>

