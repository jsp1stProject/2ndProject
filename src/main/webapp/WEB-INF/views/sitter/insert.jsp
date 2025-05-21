<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 새 글 작성</title>
</head>
<body>
  <div class="container mt-4" id="app">
    <h2 class="mb-4">펫시터 새 글 작성</h2>

    <form @submit.prevent="handleInsert" enctype="multipart/form-data">
      <div class="mb-3">
        <label class="form-label">태그</label>
        <input type="text" v-model="form.tag" class="form-control" required>
      </div>

      <div class="mb-3">
        <label class="form-label">내용</label>
        <textarea v-model="form.content" class="form-control" rows="4" required></textarea>
      </div>

      <div class="mb-3">
        <label class="form-label">지난 돌봄 횟수</label>
        <input type="number" v-model="form.carecount" class="form-control" min="1" required>
      </div>

      <div class="mb-3">
        <label class="form-label">지역</label>
        <input type="text" v-model="form.care_loc" class="form-control" required>
      </div>

      <div class="mb-3">
        <label class="form-label">시작가</label>
        <input type="number" v-model="form.pet_first_price" class="form-control" required>
      </div>

      <div class="mb-3">
        <label class="form-label">사진 업로드</label>
        <input type="file" @change="handleFileUpload" class="form-control" accept="image/*" required>
      </div>

      <div v-if="previewUrl" class="mb-3">
        <label class="form-label">미리보기</label><br>
        <img :src="previewUrl" class="img-thumbnail" style="max-width: 300px;">
      </div>

      <button type="submit" class="btn btn-success">작성 완료</button>
    </form>
  </div>

  <script type="module">
    import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'
    import axios from 'https://cdn.skypack.dev/axios'

    createApp({
      data() {
        return {
          form: {
            tag: '',
            content: '',
            carecount: '',
            care_loc: '',
            pet_first_price: ''
          },
          imageFile: null,
          previewUrl: null
        }
      },
      methods: {
        handleFileUpload(e) {
          const file = e.target.files[0]
          if (file) {
            this.imageFile = file
            this.previewUrl = URL.createObjectURL(file)
          }
        },
        async handleInsert() {
  if (!this.imageFile) {
    alert("이미지를 선택해주세요")
    return
  }

  try {
    const formData = new FormData()
    formData.append("tag", this.form.tag)
    formData.append("content", this.form.content)
    formData.append("carecount", this.form.carecount)
    formData.append("care_loc", this.form.care_loc)
    formData.append("pet_first_price", this.form.pet_first_price)
    formData.append("upload", this.imageFile)

    const res = await axios.post('/web/sitter/insert', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      withCredentials: true
    })

    if (res.data.code === '200' && res.data.data === 'success') {
      alert("등록 완료!")
      location.href = "/web/sitter/list"
    } else {
      alert("등록 실패: " + res.data.message)
    }
  } catch (e) {
    alert("서버 오류 발생")
    console.error(e)
  }
}

      }
    }).mount('#app')
  </script>
</body>
</html>
