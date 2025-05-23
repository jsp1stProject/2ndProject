<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 정보 수정</title>
</head>
<body>
  <div class="container mt-4" id="app">
    <h2 class="mb-4">펫시터 정보 수정</h2>

    <form @submit.prevent="handleUpdate">
      <div class="mb-3">
        <label class="form-label">태그</label>
        <input type="text" v-model="form.tag" class="form-control">
      </div>
      <div class="mb-3">
        <label class="form-label">내용</label>
        <textarea v-model="form.content" class="form-control" rows="4"></textarea>
      </div>
      <div class="mb-3">
        <label class="form-label">돌봄 횟수</label>
        <input type="number" v-model="form.carecount" class="form-control" min="1">
      </div>
      <div class="mb-3">
        <label class="form-label">지역</label>
        <input type="text" v-model="form.care_loc" class="form-control">
      </div>
      <div class="mb-3">
        <label class="form-label">시작가</label>
        <input type="text" v-model="form.pet_first_price" class="form-control">
      </div>
      <!-- <div class="mb-3">
        <label class="form-label">사진 URL</label>
        <input type="text" v-model="form.sitter_pic" class="form-control">
      </div> -->

      <button type="submit" class="btn btn-primary">수정 완료</button>
    </form>
  </div>
  <script type="module">
    import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'
    import axios from 'https://cdn.skypack.dev/axios'

    createApp({
      data() {
        return {
          sitter_no: null,
          form: {
            tag: '',
            content: '',
            carecount: '',
            care_loc: '',
            pet_first_price: '',
            sitter_pic: ''
          }
        }
      },
      mounted() {
  const sitter_no = new URLSearchParams(window.location.search).get("sitter_no")
  if (!sitter_no) {
    alert("잘못된 접근입니다.")
    return
  }

  this.sitter_no = sitter_no

  axios.get('${pageContext.request.contextPath}/sitter/detail_vue', {
  params: { sitter_no },
  withCredentials: true
}).then(res => {
  if (res.data.code === '200' && res.data.data) {
    const sitter = res.data.data.Sitter
    Object.assign(this.form, res.data.data.sitter)
  } else {
    alert("불러오기 실패: " + res.data.message)
    console.warn("응답 구조:", res.data)
  }
}).catch(e => {
  alert("기존 데이터를 불러올 수 없습니다.")
  console.error("예외 발생:", e)
})
},
methods: {
  async handleUpdate() {
    try {
      const res = await axios.post('${pageContext.request.contextPath}/sitter/update', {
        sitter_no: this.sitter_no,
        ...this.form
      }, {
        withCredentials: true
      })

      if (res.data.code === '200' && res.data.data === 'success') {
        alert("수정 완료!")
        location.href = '${pageContext.request.contextPath}/sitter/detail?sitter_no=' + this.sitter_no
      } else {
        alert("수정 실패: " + res.data.message)
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
