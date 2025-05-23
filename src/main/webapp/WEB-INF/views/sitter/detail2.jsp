<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 상세보기</title>
</head>
<body>
<div class="container mt-4" id="app">
  <h2 class="mb-4">펫시터 상세보기</h2>

  <div v-if="sitter" class="card">
     <div :style="sitter.sitter_pic ? { backgroundImage: 'url(${pageContext.request.contextPath}/s3/'+sitter.sitter_pic+')' } : {}" style="padding-top:30%;width:200px;height:100px;
    background-repeat: no-repeat;
background-size: cover;"></div>
    <div class="card-body">
      <p class="card-text">{{ sitter.content }}</p>
      <ul class="list-group list-group-flush">
        <li class="list-group-item"><strong>태그:</strong> {{ sitter.tag }}</li>
        <li class="list-group-item"><strong>지난 돌봄 횟수:</strong> {{ sitter.carecount }}</li>
        <li class="list-group-item"><strong>지역:</strong> {{ sitter.care_loc }}</li>
        <li class="list-group-item"><strong>시작가:</strong> {{ sitter.pet_first_price }}</li>
        <li class="list-group-item" v-if="sitter.sitterApp?.license">
          <strong>자격증:</strong> {{ sitter.sitterApp.license }}
        </li>
        <li class="list-group-item" v-if="sitter.sitterApp?.history">
          <strong>경력:</strong> {{ sitter.sitterApp.history }}
        </li>
      </ul>
     <button class="btn btn-info mt-3" @click="goReserve(sitter.sitter_no)">예약하기</button>
     <button v-if="parseInt(sitter.user_no) === myUserNo" class="btn btn-warning mt-3" @click="goUpdate(sitter.sitter_no)">수정하기</button>
      <button v-if="parseInt(sitter.user_no) === parseInt(myUserNo)" class="btn btn-danger mt-3" @click="deletePost">삭제하기</button>
    </div>
  </div>
  <div v-else class="text-muted">불러오는 중입니다...</div>

  <!-- 리뷰 작성 -->
  <div class="card p-3 my-4">
    <h5>리뷰</h5>
    <textarea v-model="newReview.rev_comment" class="form-control mb-2" placeholder="리뷰를 입력하세요"></textarea>
    <div class="d-flex">
      <input type="number" v-model="newReview.rev_score" min="1" max="5" class="form-control w-25 me-2">
      <button @click="submitReview" class="btn btn-primary">등록</button>
    </div>
  </div>

  <!-- 리뷰 출력 -->
  <div v-for="review in reviews" :key="review.review_no" class="border rounded p-3 mb-3">
    <div class="d-flex align-items-center mb-2">
      <strong>{{ review.user?.nickname || '탈퇴회원' }}</strong>
      <span class="ms-2 text-warning" v-if="review.group_step === 0">
        <span v-for="n in Math.round(review.rev_score)" :key="n">⭐</span>
      </span>
    </div>

    <div v-if="editTarget === review.review_no">
      <textarea v-model="editReview.rev_comment" class="form-control mb-2"></textarea>
      <div class="d-flex align-items-center">
        <input type="number" v-if="review.group_step === 0" v-model="editReview.rev_score" min="1" max="5" class="form-control w-25 me-2">
        <button @click="submitEdit(review.review_no)" class="btn btn-success btn-sm me-1">수정 완료</button>
        <button @click="cancelEdit" class="btn btn-secondary btn-sm">취소</button>
      </div>
    </div>
    <div v-else>
      <p class="mb-1">{{ review.rev_comment }}</p>
      <small class="text-muted">{{ review.rev_date ? new Date(review.rev_date).toISOString().substring(0,10) : '' }}</small>
      <div class="mt-2">
        <template v-if="review.user.user_no === myUserNo">
          <button @click="startEdit(review)" class="btn btn-outline-primary btn-sm me-1">수정</button>
          <button @click="deleteReview(review.review_no)" class="btn btn-outline-danger btn-sm me-1">삭제</button>
        </template>
        <template v-if="review.group_step === 0 && sitter.user_no === myUserNo">
          <button @click="toggleReplyBox(review.review_no)" class="btn btn-outline-secondary btn-sm">답글</button>
        </template>
      </div>
    </div>

    <div v-if="replyTarget === review.review_no && sitter.user_no === myUserNo" class="mt-3">
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
      myUserNo: null,
      newReview: { rev_comment: '', rev_score: 5 },
      reviews: [],
      editTarget: null,
      editReview: { rev_comment: '', rev_score: 5 },
      replyTarget: null,
      replyComment: ''
    }
  },
  mounted() {
  const sitter_no = new URLSearchParams(location.search).get("sitter_no")
  if (!sitter_no) return
  this.sitter_no = parseInt(sitter_no)
 this.fetchReviews(this.sitter_no)
  const token = document.cookie.split('; ').find(row => row.startsWith('accessToken='))?.split('=')[1]
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      this.myUserNo = parseInt(payload.user_no) 
    } catch (e) {
      this.myUserNo = null
      console.log("사용자 번호 파싱 실패")
    }
  }

  axios.get(`${pageContext.request.contextPath}/sitter/detail_vue`, { params: { sitter_no },withCredentials: true })
       .then(res => {
  if (res.data.code === '200') {
    this.sitter = res.data.data.sitter
    this.myUserNo = res.data.data.myUserNo
  }
})
 
console.log("📦 sitter_no:", this.sitter_no)
},
  methods: {
    goUpdate(sitter_no) {
      location.href = '${pageContext.request.contextPath}/sitter/update?sitter_no=' + sitter_no
    },
	goReserve(sitter_no) {
      location.href = '${pageContext.request.contextPath}/sitter/reserve?sitter_no=' + sitter_no
    },
    async deletePost() {
      if (!confirm("정말 삭제하시겠습니까?")) return
      const res = await axios.delete('${pageContext.request.contextPath}/sitter/delete', { params: { sitter_no: this.sitter_no },withCredentials: true })
		console.log(res.data.data)
      if (res.data.code === '200' && res.data.data === 'success') {
        alert("삭제 완료")
        location.href = "${pageContext.request.contextPath}/sitter/list"
      } else {
        alert("삭제 실패: " + res.data.message)
      }
    },
    fetchReviews(sitter_no) {
  console.log("🔄 fetchReviews 호출됨, sitter_no =", sitter_no)
  axios.get(`${pageContext.request.contextPath}/sitter/review/list_vue`, {
    params: { sitter_no },
    withCredentials: true
  }).then(res => {
    console.log("📥 fetchReviews 응답 =", res.data)
    if (res.data.code === '200') {
      this.reviews = res.data.data
      console.log("✅ 리뷰 목록 할당됨:", this.reviews.length)
    } else {
      alert(res.data.message)
    }
  })
},
    submitReview() {
  if (!this.newReview.rev_comment.trim()) {
    alert("내용을 입력하세요");
    return;
  }

  axios.post(`${pageContext.request.contextPath}/sitter/review`, {
    sitter_no: this.sitter_no,
    rev_comment: this.newReview.rev_comment,
    rev_score: this.newReview.rev_score
  }, {
    withCredentials: true
  }).then(res => {
    if (res.data.code === '200' && res.data.data === 'success') {
      alert("등록 완료");

      const newReview = {
        review_no: Date.now(), // 임시 키
        rev_comment: this.newReview.rev_comment,
        rev_score: this.newReview.rev_score,
        group_step: 0,
        user: {
          nickname: '나',
          user_no: this.myUserNo,
          profile: ''
        }
      };

      this.reviews = [newReview, ...this.reviews];

      this.newReview.rev_comment = '';
      this.newReview.rev_score = 5;
    } else {
      alert("등록 실패: " + res.data.message);
    }
  });
},
    startEdit(review) {
      this.editTarget = review.review_no
      this.editReview = {
        rev_comment: review.rev_comment,
        rev_score: review.rev_score || 5
      }
    },
    cancelEdit() {
      this.editTarget = null
      this.editReview = { rev_comment: '', rev_score: 5 }
    },
    submitEdit(review_no) {
      axios.put('${pageContext.request.contextPath}/sitter/review', {
        review_no,
        rev_comment: this.editReview.rev_comment,
        rev_score: this.editReview.rev_score
      }).then(res => {
        if (res.data.code === '200' && res.data.data === 'success') {
          alert("수정 완료")
          this.editTarget = null
          this.fetchReviews(this.sitter_no)
        } else {
          alert("수정 실패: " + res.data.message)
        }
      })
    },
    deleteReview(review_no) {
      if (!confirm("정말 삭제하시겠습니까?")) return
      axios.delete(`${pageContext.request.contextPath}/sitter/review/delete`, {
    params: { review_no }
}).then(res => {
  if (res.data.code === '200' && res.data.data === '삭제 성공') {
    alert("삭제 완료")
    this.reviews = this.reviews.filter(r => r.review_no !== review_no)
  } else {
    alert("삭제 실패: " + res.data.message)
  }
})
.catch(err => {
  alert("요청 실패: " + err.response?.data?.message || err.message)
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
    alert("내용을 입력하세요");
    return;
  }
  axios.post(`${pageContext.request.contextPath}/sitter/review/reply`, {
    sitter_no: this.sitter_no,
    rev_comment: this.replyComment,
    group_id: review.group_id,         // ✅ 부모 댓글 group_id
    group_step: 1,                      // ✅ 항상 1
    parent_no: review.review_no         // ✅ 부모 댓글 번호
  }, {
    withCredentials: true
  }).then(res => {
    if (res.data.code === '200') {
      alert("답글 등록 완료");
      this.replyTarget = null;
      this.replyComment = '';
      this.fetchReviews(this.sitter_no);
    } else {
      alert("답글 실패: " + res.data.message);
    }
  });
},
    scrollTop() {
      window.scrollTo({ top: 0, behavior: 'smooth' })
    }
  }
}).mount('#app')
</script>
</body>
</html>
