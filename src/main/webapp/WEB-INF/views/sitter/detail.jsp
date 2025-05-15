<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 상세보기</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container mt-4" id="app">
  <h2 class="mb-4">펫시터 상세보기</h2>

  <div v-if="sitter" class="card">
    <img :src="sitter.sitter_pic" class="card-img-top" alt="sitter_pic">
    <div class="card-body">
      <p class="card-text">{{ sitter.content }}</p>
      <ul class="list-group list-group-flush">
        <li class="list-group-item"><strong>태그:</strong> {{ sitter.tag }}</li>
        <li class="list-group-item"><strong>돌봄 횟수:</strong> {{ sitter.carecount }}</li>
        <li class="list-group-item"><strong>평점:</strong> {{ sitter.score }}</li>
        <li class="list-group-item"><strong>지역:</strong> {{ sitter.care_loc }}</li>
        <li class="list-group-item"><strong>시작가:</strong> {{ sitter.pet_first_price }}</li>
        <li class="list-group-item" v-if="sitter.sitterApp && sitter.sitterApp.license">
          <strong>자격증:</strong> {{ sitter.sitterApp.license }}
        </li>
        <li class="list-group-item" v-if="sitter.sitterApp && sitter.sitterApp.history">
          <strong>경력:</strong> {{ sitter.sitterApp.history }}
        </li>
      </ul>
      <button class="btn btn-warning mt-3" @click="goUpdate(sitter.sitter_no)">수정하기</button>
      <button class="btn btn-danger mt-3" @click="deletePost">삭제하기</button>
    </div>
  </div>
  <div v-else class="text-muted">불러오는 중입니다...</div>

  <!-- 리뷰 -->
  <div class="card p-3 my-4">
    <h5>리뷰 작성</h5>
    <textarea v-model="newReview.rev_comment" class="form-control mb-2" placeholder="리뷰를 입력하세요"></textarea>
    <div class="d-flex">
      <input type="number" v-model="newReview.rev_score" min="1" max="5" class="form-control w-25 me-2">
      <button @click="submitReview" class="btn btn-primary">등록</button>
    </div>
  </div>

  <div v-for="review in reviews" :key="review.review_no"
       :style="{ marginLeft: review.group_step > 0 ? '2rem' : '0' }"
       class="border rounded p-3 mb-3">
    <div class="d-flex align-items-center mb-2">
      <strong>{{ review.user.nickname }}</strong>
      <span class="ms-2 text-warning">
        <span v-for="n in Math.round(review.rev_score)" :key="n">⭐</span>
      </span>
    </div>

    <div v-if="editTarget === review.review_no">
      <textarea v-model="editReview.rev_comment" class="form-control mb-2"></textarea>
      <div class="d-flex align-items-center">
        <input type="number" v-model="editReview.rev_score" min="1" max="5" class="form-control w-25 me-2">
        <button @click="submitEdit(review.review_no)" class="btn btn-success btn-sm me-1">수정 완료</button>
        <button @click="cancelEdit" class="btn btn-secondary btn-sm">취소</button>
      </div>
    </div>
    <div v-else>
      <p class="mb-1">{{ review.rev_comment }}</p>
      <small class="text-muted">
        {{ review.rev_date ? new Date(review.rev_date).toISOString().substring(0,10) : '' }}
      </small>
      <div class="mt-2">
        <button @click="startEdit(review)" class="btn btn-outline-primary btn-sm me-1">수정</button>
        <button @click="deleteReview(review.review_no)" class="btn btn-outline-danger btn-sm me-1">삭제</button>
        <button @click="toggleReplyBox(review.review_no)" class="btn btn-outline-secondary btn-sm">답글</button>
      </div>
    </div>

    <div v-if="replyTarget === review.review_no" class="mt-3">
      <textarea v-model="replyComment" class="form-control mb-2" placeholder="답글을 입력하세요"></textarea>
      <button @click="submitReply(review)" class="btn btn-sm btn-dark me-2">답글 등록</button>
      <button @click="cancelReply" class="btn btn-sm btn-outline-secondary">취소</button>
    </div>
  </div>
  <button class="btn btn-dark position-fixed bottom-0 end-0 m-3" @click="scrollTop">🔝</button>
</div>

<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'
  import axios from 'https://cdn.skypack.dev/axios'

  createApp({
    data() {
      return {
        sitter: null,
        sitter_no: null,
        reviews: [],
        newReview: { rev_comment: '', rev_score: 5 },
        editTarget: null,
        editReview: { rev_comment: '', rev_score: 5 },
        replyTarget: null,
        replyComment: ''
      }
    },
    mounted() {
  const sitter_no = new URLSearchParams(location.search).get("sitter_no")
  if (!sitter_no) return
  this.sitter_no = sitter_no
  this.fetchReviews(sitter_no)
  axios.get(`/web/sitter/detail_vue`, { params: { sitter_no } })
       .then(res => {
         this.sitter = res.data
       })
},
    methods: {
      goUpdate(sitter_no) {
        location.href ='/web/sitter/update?sitter_no='+sitter_no
      },
      async deletePost() {
        if (!confirm("정말 삭제하시겠습니까?")) return
        const res = await axios.delete('/web/sitter/delete', { params: { sitter_no: this.sitter_no } })
        if (res.data === 'success') {
          alert("삭제 완료")
          location.href = "/web/sitter/list"
        } else {
          alert("삭제 실패")
        }
      },
      fetchReviews(sitter_no) {
        axios.get(`/web/sitter/review`, { params: { sitter_no } })
             .then(res => this.reviews = res.data)
      },
      submitReview() {
        if (!this.newReview.rev_comment.trim()) {
          alert("내용을 입력하세요")
          return
        }
        axios.post('/web/sitter/review', {
          sitter_no: this.sitter_no,
          rev_comment: this.newReview.rev_comment,
          rev_score: this.newReview.rev_score
        }).then(res => {
          if (res.data === 'success') {
            alert("등록 완료")
            this.newReview.rev_comment = ''
            this.newReview.rev_score = 5
            this.fetchReviews(this.sitter_no)
          }
        })
      },
      startEdit(review) {
        this.editTarget = review.review_no
        this.editReview = {
          rev_comment: review.rev_comment,
          rev_score: review.rev_score
        }
      },
      cancelEdit() {
        this.editTarget = null
        this.editReview = { rev_comment: '', rev_score: 5 }
      },
      submitEdit(review_no) {
        axios.put('/web/sitter/review', {
          review_no,
          rev_comment: this.editReview.rev_comment,
          rev_score: this.editReview.rev_score
        }).then(res => {
          if (res.data === 'success') {
            alert("수정 완료")
            this.editTarget = null
            this.fetchReviews(this.sitter_no)
          }
        })
      },
      deleteReview(review_no) {
        if (!confirm("정말 삭제하시겠습니까?")) return
        axios.delete('/web/sitter/review', { params: { review_no } })
             .then(res => {
               if (res.data === 'success') {
                 alert("삭제 완료")
                 this.fetchReviews(this.sitter_no)
               }
             })
      },
      toggleReplyBox(review_no) {
        this.replyTarget = review_no
        this.replyComment = ''
      },
      cancelReply() {
        this.replyTarget = null
        this.replyComment = ''
      },
      submitReply(review) {
        if (!this.replyComment.trim()) {
          alert("내용을 입력하세요")
          return
        }
        axios.post('/web/sitter/review/reply', {
          sitter_no: this.sitter_no,
          rev_comment: this.replyComment,
          group_id: review.group_id,
          group_step: review.group_step + 1
        }).then(res => {
          if (res.data === 'success') {
            alert("답글 등록 완료")
            this.replyTarget = null
            this.fetchReviews(this.sitter_no)
          }
        })
      },
      scrollTop() {
        window.scrollTo({ top: 0, behavior: 'smooth' })
      },
      onImageError(e) {
        e.target.src = '/sitter/img/default-profile.png'
      }
    }
  }).mount('#app')
</script>
</body>
</html>
