<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 목록</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
  <div class="container-fluid" id="app">
    <h2 class="mb-4">펫시터 목록</h2>
	<div class="d-flex justify-content-end mb-3">
      <button class="btn btn-success" @click="goInsert">새 글 쓰기</button>
    </div>
    <!-- 필터 영역 -->
    <div class="mb-4">
      <label for="fd">필터 기준:</label>
      <select v-model="fd" id="fd" class="form-select w-auto d-inline-block">
        <option value="care_loc">지역</option>
        <option value="tag">태그</option>
      </select>

      <div class="form-check form-check-inline" v-for="option in filterOptions" :key="option">
        <input class="form-check-input" type="checkbox" :value="option" v-model="ss">
        <label class="form-check-label">{{ option }}</label>
      </div>

      <button class="btn btn-secondary btn-sm ms-2" @click="dataRecv(1)">검색</button>
      <button class="btn btn-outline-secondary btn-sm ms-2" @click="resetFilter">초기화</button>
    </div>

    <!-- 펫시터 카드 목록 -->
    <div v-if="list.length > 0">
      <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
        <div class="col" v-for="sitter in list" :key="sitter.sitter_no">
          <div class="card h-100">
            <img :src="sitter.sitter_pic" class="card-img-top" alt="sitter_pic">
            <div class="card-body">
              <h5 class="card-title">{{ sitter.user.nickname }} ({{ sitter.user.user_name }})</h5>
              <p class="card-text">{{ sitter.content }}</p>
              <ul class="list-unstyled">
                <li><strong>태그:</strong> {{ sitter.tag }}</li>
                <li><strong>돌봄 횟수:</strong> {{ sitter.carecount }}</li>
                <li><strong>평점:</strong> {{ sitter.score }}</li>
                <li><strong>지역:</strong> {{ sitter.care_loc }}</li>
                <li><strong>시작가:</strong> {{ sitter.pet_first_price }}</li>
              </ul>
              <button class="btn btn-primary mt-2" @click="goDetail(sitter.sitter_no)">
                상세보기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="text-muted">목록이 없습니다.</div>

    <!-- 페이지네이션 -->
    <nav class="mt-5">
      <ul class="pagination justify-content-center">
        <li class="page-item" :class="{ disabled: curpage === 1 }">
          <a class="page-link" href="#" @click.prevent="changePage(curpage - 1)">이전</a>
        </li>

        <li class="page-item" v-for="i in pageRange" :key="i" :class="{ active: curpage === i }">
          <a class="page-link" href="#" @click.prevent="changePage(i)">{{ i }}</a>
        </li>

        <li class="page-item" :class="{ disabled: curpage === totalpage }">
          <a class="page-link" href="#" @click.prevent="changePage(curpage + 1)">다음</a>
        </li>
      </ul>
    </nav>
  </div>

  <script type="module">
    import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'
    import axios from 'https://cdn.skypack.dev/axios'

    createApp({
      data() {
        return {
          list: [],
          curpage: 1,
          totalpage: 1,
          startPage: 1,
          endPage: 1,
          fd: 'care_loc',
          ss: [],
          filterOptions: ['서울', '경기', '인천', '고양이전문', '강아지전문']
        }
      },
      computed: {
        pageRange() {
          const range = []
          for (let i = this.startPage; i <= this.endPage; i++) {
            range.push(i)
          }
          return range
        }
      },
      mounted() {
        this.dataRecv(1)
      },
      methods: {
        async dataRecv(page) {
          try {
            const params = {
              page: page,
              fd: this.fd,
              ss: this.ss.join(',')
            }
            const res = await axios.get('/web/sitter/list_vue', { params })
            this.list = res.data.list
            this.curpage = res.data.curpage
            this.totalpage = res.data.totalpage
            this.startPage = res.data.startPage
            this.endPage = res.data.endPage
          } catch (error) {
            console.error('데이터 로드 오류:', error)
          }
        },
        changePage(page) {
          if (page >= 1 && page <= this.totalpage) {
            this.dataRecv(page)
          }
        },
        resetFilter() {
          this.ss = []
          this.dataRecv(1)
        },
        goDetail(sitter_no) {
          if (!sitter_no) {
            alert("sitter_no가 없습니다!")
            return
          }
          location.href = `/web/sitter/detail?sitter_no=${sitter_no}`
        },
		goInsert() {
          location.href = `/web/sitter/insert`
        }
      }
    }).mount('#app')
  </script>
</body>
</html>
