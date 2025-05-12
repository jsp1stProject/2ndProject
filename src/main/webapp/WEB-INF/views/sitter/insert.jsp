<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 새 글 작성</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
  <div class="container mt-4" id="app">
    <h2 class="mb-4">펫시터 새 글 작성</h2>

    <form @submit.prevent="handleInsert">
      <div class="mb-3">
        <label class="form-label">태그</label>
        <input type="text" v-model="form.tag" ref="tagRef" class="form-control" required>
      </div>
      <div class="mb-3">
        <label class="form-label">내용</label>
        <textarea v-model="form.content" ref="contentRef" class="form-control" rows="4" required></textarea>
      </div>
      <div class="mb-3">
        <label class="form-label">돌봄 가능 횟수</label>
        <input type="number" v-model="form.carecount" ref="countRef" class="form-control" min="1" required>
      </div>
      <div class="mb-3">
        <label class="form-label">지역</label>
        <input type="text" v-model="form.care_loc" ref="locRef" class="form-control" required>
      </div>
      <div class="mb-3">
        <label class="form-label">시작가</label>
        <input type="text" v-model="form.pet_first_price" ref="priceRef" class="form-control" required>
      </div>
      <div class="mb-3">
        <label class="form-label">사진 URL</label>
        <input type="text" v-model="form.sitter_pic" ref="picRef" class="form-control" required>
      </div>

      <button type="submit" class="btn btn-success">작성 완료</button>
    </form>
  </div>

  <script type="module">
    import { createApp, reactive, ref } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'
    import axios from 'https://cdn.skypack.dev/axios'

    createApp({
      setup() {
        const form = reactive({
          tag: '',
          content: '',
          carecount: '',
          care_loc: '',
          pet_first_price: '',
          sitter_pic: ''
        })

        const tagRef = ref(null)
        const contentRef = ref(null)
        const countRef = ref(null)
        const locRef = ref(null)
        const priceRef = ref(null)
        const picRef = ref(null)

        const handleInsert = async () => {
          try {
            const res = await axios.post('/web/sitter/insert', form)
            if (res.data === 'success') {
              alert("등록 완료!")
              location.href = "/web/sitter/list"
            } else {
              alert("등록 실패")
            }
          } catch (e) {
            alert("서버 오류 발생")
            console.error(e)
          }
        }

        return {
          form, tagRef, contentRef, countRef, locRef, priceRef, picRef, handleInsert
        }
      }
    }).mount('#app')
  </script>
</body>
</html>
