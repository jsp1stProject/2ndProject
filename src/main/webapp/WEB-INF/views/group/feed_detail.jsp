<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    .card-style {
      background-color: #fff;
      border-radius: 12px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.05);
      padding: 20px;
      margin-bottom: 20px;
    }
    .comment-input {
      transition: all 0.3s;
      min-height: 40px;
      resize: none;
    }
    .comment-input.expand {
      min-height: 100px;
    }
    .reply-box {
      margin-top: 10px;
      padding-left: 20px;
      border-left: 2px solid #e0e0e0;
    }
</style>
<div class="container pt-header" id="app">
 <!-- 피드 정보 카드 -->
  <div class="card-style">
    <div class="d-flex justify-content-between">
      <div>
        <h5 class="fw-bold mb-1">{{ vo.title }}</h5>
        <div class="text-muted small">작성자 {{ vo.nickname }} · {{ vo.dbday }}</div>
      </div>
      <div class="dropdown">
        <button class="btn btn-sm btn-light rounded-circle" data-bs-toggle="dropdown">
          <i class="bi bi-three-dots"></i>
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" href="#">수정</a></li>
          <li><a class="dropdown-item text-danger" href="#">삭제</a></li>
        </ul>
      </div>
    </div>
    <p class="mt-3">{{ vo.content }}</p>

    <!-- 이미지 출력 -->
    <div v-if="vo.images && vo.images.length" class="carousel slide mb-3" id="feedImages" data-bs-ride="carousel">
      <div class="carousel-inner">
        <div class="carousel-item" v-for="(img, index) in vo.images" :class="{ active: index === 0 }">
          <img :src="'/web/images/' + img" class="d-block w-100 rounded" style="object-fit: cover; max-height: 400px;">
        </div>
      </div>
      <button class="carousel-control-prev" type="button" data-bs-target="#feedImages" data-bs-slide="prev">
        <span class="carousel-control-prev-icon"></span>
      </button>
      <button class="carousel-control-next" type="button" data-bs-target="#feedImages" data-bs-slide="next">
        <span class="carousel-control-next-icon"></span>
      </button>
    </div>

    <div class="text-end small text-muted">
      ❤️ 좋아요수 · 💬 댓글수
    </div>
  </div>

  <!-- 댓글 작성창 -->
  <div class="card-style">
    <textarea v-model="newComment" @focus="isExpanded = true"
              :class="['form-control comment-input', { expand: isExpanded }]"
              placeholder="댓글을 입력하세요..."></textarea>
    <div class="text-end mt-2">
      <button class="btn btn-outline-secondary btn-sm me-2" v-if="isExpanded" @click="cancelComment">취소</button>
      <button class="btn btn-primary btn-sm" @click="submitComment">댓글쓰기</button>
    </div>
  </div>

  <!-- 댓글 리스트 -->
  <div v-for="vo in visibleComments" :key="vo.no" class="card-style">
    <div class="d-flex justify-content-between">
      <div><strong>{{ vo.user_no }}</strong> <small class="text-muted">{{ vo.dbday }}</small></div>
      <div class="dropdown">
        <button class="btn btn-sm btn-light rounded-circle" data-bs-toggle="dropdown">
          <i class="bi bi-three-dots"></i>
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
          <li><a class="dropdown-item" @click="replyUpdateForm(vo.no, vo.msg)">수정</a></li>
          <li><a class="dropdown-item text-danger" @click="replyDeleteForm(vo.no)">삭제</a></li>
          <li><a class="dropdown-item" @click="replyReplyInsertForm(vo.no)">답글</a></li>
        </ul>
      </div>
    </div>
    <div class="mt-2" v-if="editingComment !== vo.no">{{ vo.msg }}</div>
    <div class="mt-2" v-else>
      <textarea v-model="vo.editMsg" class="form-control mb-2"></textarea>
      <div class="text-end">
        <button class="btn btn-sm btn-outline-success me-2" @click="replyUpdate(vo.no, vo.editMsg)">수정완료</button>
        <button class="btn btn-sm btn-outline-secondary" @click="replyCancelUpdate(vo.no)">취소</button>
      </div>
    </div>

    <!-- 대댓글 출력 placeholder -->
    <!-- <div class="reply-box" v-if="vo.replies && vo.replies.length">
      <div v-for="reply in vo.replies" :key="reply.no"> -->
    <div class="reply-box">
      <div>  
        <strong>대댓글작성자</strong> <small class="text-muted">대댓글날짜</small>
        <div>대댓글내용</div>
      </div>
    </div>
  </div>

  <!-- 더보기 버튼 -->
  <%-- <button class="btn btn-light w-100 mb-3" v-if="visibleCount < list.length" @click="visibleCount += 5">
    댓글 더보기
  </button> --%>
  <button class="btn btn-light w-100 mb-3" @click="visibleCount += 5">
    댓글 더보기
  </button>

</div>

<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';

  createApp({
    data() {
      return {
        list: [],
        vo: {},
        feed_no: ${vo.feed_no},
        user_no: ${user_no},
        page: 1,
        newComment: '',
        editingComment: null,
        isExpanded: false,
        visibleCount: 5
      }
    },
    computed: {
      visibleComments() {
        return this.list.slice(0, this.visibleCount);
      }
    },
    mounted() {
      this.dataRecv();
    },
    methods: {
      dataRecv() {
        axios.get('../feed/comments', {
          params: {
            feed_no: this.feed_no,
            page: this.page
          }
        }).then(res => {
          this.list = res.data.list;
          this.vo = res.data.vo;
        });
      },
      cancelComment() {
        this.isExpanded = false;
        this.newComment = '';
      },
      submitComment() {
        if (this.newComment.trim()) {
          axios.post('../feed/comments', null, {
            params: {
              feed_no: this.feed_no,
              msg: this.newComment
            }
          }).then(res => {
            this.list = res.data.list;
            this.cancelComment();
          });
        }
      },
      replyUpdateForm(no, msg) {
        this.editingComment = no;
        const comment = this.list.find(c => c.no === no);
        if (comment) comment.editMsg = msg;
      },
      replyCancelUpdate(no) {
        const comment = this.list.find(c => c.no === no);
        if (comment) comment.editMsg = '';
        this.editingComment = null;
      },
      replyUpdate(no, editMsg) {
        if (editMsg.trim()) {
          axios.post('../feed/comments_update', null, {
            params: { no, msg: editMsg }
          }).then(() => {
            const comment = this.list.find(c => c.no === no);
            if (comment) {
              comment.msg = editMsg;
              comment.editMsg = '';
            }
            this.editingComment = null;
          });
        }
      },
      replyDeleteForm(no) {
        if (confirm('정말 삭제하시겠습니까?')) {
          axios.post('../feed/comments_delete', null, {
            params: { no }
          }).then(() => {
            this.dataRecv();
          });
        }
      },
      replyReplyInsertForm(no) {
        alert("대댓글 작성 기능은 아직 미구현 상태입니다.");
      }
    }
  }).mount('#app');
</script>