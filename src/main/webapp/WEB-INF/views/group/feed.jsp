<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>피드 상세보기</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
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
@media (max-width: 768px) {
  .custom-container { width: 95%; }
}
</style>
</head>
<body>
<div id="app" class="container-fluid custom-container">
  <div class="card mb-4">
    <div class="card-header d-flex justify-content-between align-items-center">
      <div>
        <strong>아이디</strong>
        <small class="text-muted ms-2">{{vo.dbday}}</small>
      </div>
      <button class="btn btn-sm btn-light">...</button>
    </div>

    <div class="card-body">
      <strong>{{vo.title}}</strong>
      <p class="card-text">{{vo.content}}</p>

      <!-- 이미지 슬라이드 -->
      <div v-if="feedData.images && feedData.images.length" id="feedCarousel" class="carousel slide my-3" data-bs-ride="carousel">
        <div class="carousel-inner">
          <div v-for="(img, index) in vo.images" :class="['carousel-item', { active: index === 0 }]">
            <img :src="'/web/images/' + img" class="d-block w-100 rounded" style="height: 400px; object-fit: cover;">
          </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#feedCarousel" data-bs-slide="prev">
          <span class="carousel-control-prev-icon"></span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#feedCarousel" data-bs-slide="next">
          <span class="carousel-control-next-icon"></span>
        </button>
      </div>

      <!-- 댓글쓰기 버튼 -->
      <div class="my-3">
        <textarea v-model="newComment" @focus="expandInput" :class="['form-control', { expand: isExpanded }]" placeholder="댓글을 입력하세요..."></textarea>
        <div v-if="isExpanded" class="mt-2 d-flex justify-content-end gap-2">
          <button class="btn btn-secondary btn-sm" @click="cancelComment">취소</button>
          <button class="btn btn-primary btn-sm" @click="submitComment">댓글쓰기</button>
        </div>
      </div>

      <!-- 댓글 목록 -->
      <div class="mt-4">
        <h6>댓글 {{ comments.length }}개</h6>
        <div v-for="comment in comments" class="border-bottom py-2">
          <strong>{{ comment.userName }}</strong>
          <p class="mb-0">{{ comment.content }}</p>
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
        feedData: {
          vo:[],
		  userName: '사용자 이름',
          regdate: '2025-04-28',
          content: '여기에 피드 내용이 들어갑니다.',
          images: ['woman.png', 'man.png']
        },
        newComment: '',
        isExpanded: false,
        comments: []
      }
    },
	mounted(){
		const params = new URLSearchParams(window.location.search);
  		const feedNoParam = params.get('feed_no');

  		if (feedNoParam) {
    		this.feed_no = parseInt(feedNoParam);
  		}
		this.dataRecv()
	},
    methods: {
	  async dataRecv(){
			console.log("dataRecv 실행")
			console.log(this.feed_no)
			const res = await axios.get('../feed/detail',{
					params:{
							feed_no:this.feed_no
					}
			})
            this.vo=res.data.vo
			console.log(res.data)
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
          this.comments.push({
            userName: '나',
            content: this.newComment
          });
          this.cancelComment();
        }
      }
    }
  }).mount('#app');
</script>
</body>
</html>
