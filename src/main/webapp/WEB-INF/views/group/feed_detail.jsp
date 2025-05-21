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