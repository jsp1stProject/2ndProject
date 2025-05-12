<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 정보 수정</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
  <div class="container mt-4" id="app">
    <h2 class="mb-4">펫시터 정보 수정</h2>

    <form @submit.prevent="handleUpdate">
      <div class="mb-3">
        <label class="form-label">태그</label>
        <input type="text" v-model="form.tag" ref="tagRef" class="form-control">
      </div>
      <div class="mb-3">
        <label class="form-label">내용</label>
        <textarea v-model="form.content" ref="contentRef" class="form-control" rows="4"></textarea>
      </div>
      <div class="mb-3">
        <label class="form-label">돌봄 횟수</label>
        <input type="number" v-model="form.carecount" ref="countRef" class="form-control" min="1">
      </div>
      <div class="mb-3">
        <label class="form-label">지역</label>
        <input type="text" v-model="form.care_loc" ref="locRef" class="form-control">
      </div>
      <div class="mb-3">
        <label class="form-label">시작가</label>
        <input type="text" v-model="form.pet_first_price" ref="priceRef" class="form-control">
      </div>
      <div class="mb-3">
        <label class="form-label">사진 URL</label>
        <input type="text" v-model="form.sitter_pic" ref="picRef" class="form-control">
      </div>

      <button type="submit" class="btn btn-primary">수정 완료</button>
    </form>
  </div>

  <script type="module">
    import { createApp, reactive, ref, onMounted } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'
    import axios from 'https://cdn.skypack.dev/axios'

    createApp({
      setup() {
        const sitter_no = new URLSearchParams(location.search).get("sitter_no")
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

        onMounted(async () => {
          if (!sitter_no) return
          try {
            const res = await axios.get(`/web/sitter/detail_vue`, {
              params: { sitter_no }
            })
            Object.assign(form, res.data)
          } catch (e) {
            alert("기존 데이터를 불러올 수 없습니다.")
            console.error(e)
          }
        })

        const handleUpdate = async () => {
          if (!form.tag.trim()) { alert("태그 입력!"); tagRef.value.focus(); return }
          if (!form.content.trim()) { alert("내용 입력!"); contentRef.value.focus(); return }
          if (!form.carecount || form.carecount <= 0) { alert("돌봄 횟수 입력!"); countRef.value.focus(); return }
          if (!form.care_loc.trim()) { alert("지역 입력!"); locRef.value.focus(); return }
          if (!form.pet_first_price.trim()) { alert("시작가 입력!"); priceRef.value.focus(); return }
          if (!form.sitter_pic.trim()) { alert("사진 URL 입력!"); picRef.value.focus(); return }

          try {
            const res = await axios.put('/web/sitter/update', {
              sitter_no, ...form
            })
            if (res.data === 'success') {
              alert("수정 완료!")
              location.href = `/web/sitter/detail?sitter_no=${sitter_no}`
            } else {
              alert("수정 실패")
            }
          } catch (e) {
            alert("서버 오류 발생")
            console.error(e)
          }
        }

        return {
          form, tagRef, contentRef, countRef, locRef, priceRef, picRef, handleUpdate
        }
      }
    }).mount('#app')
  </script>
</body>
</html>
