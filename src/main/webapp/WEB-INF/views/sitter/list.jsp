<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 목록</title>
  <style>
    .card-title {
      font-weight: 600;
      font-size: 1.1rem;
    }
    .card-text {
      font-size: 0.9rem;
      color: #555;
    }
    .card-img-top {
      height: 200px;
      object-fit: cover;
    }
    .sidebar-box {
      background: #fff;
      padding: 1rem;
      border-radius: 0.75rem;
      box-shadow: 0 0 10px rgba(0,0,0,0.05);
    }
    .filter-label {
      font-weight: 600;
      margin-top: 0.5rem;
    }
  </style>
</head>
<body>
<div class="container-fluid py-4" id="app">
  <h2 class="mb-4 text-center">펫시터 찾기</h2>
  <div class="row">
    <div class="col-md-3">
      <div class="sidebar-box">
        <label class="filter-label" for="fd">필터 기준</label>
        <select v-model="fd" id="fd" class="form-select mb-3">
          <option value="care_loc">지역</option>
          <option value="tag">태그</option>
        </select>

        <div class="form-check" v-for="option in filterOptions" :key="option">
          <input class="form-check-input" type="checkbox" :value="option" v-model="st">
          <label class="form-check-label">{{ option }}</label>
        </div>

        <button class="btn btn-primary btn-sm mt-3 w-100" @click="dataRecv(1)">검색</button>
        <button class="btn btn-outline-secondary btn-sm mt-2 w-100" @click="resetFilter">초기화</button>
        <button class="btn btn-success btn-sm mt-3 w-100" @click="goToReservationList">📋 내 예약 목록 보기</button>
      </div>
    </div>

    <div class="col-md-9">
      <div class="mb-5">
        <h4 class="mb-3">♥ 찜한 펫시터</h4>
        <div v-if="jjimList.length > 0" class="row g-4">
          <div class="col-md-6 col-lg-4" v-for="sitter in jjimList" :key="'jjim-' + sitter.sitter_no">
            <div class="card h-100 shadow-sm">
              <img :src="sitter.profile" class="card-img-top" alt="profile">
              <div class="card-body">
                <h5 class="card-title">{{ sitter.nickname }}</h5>
                <button class="btn btn-sm btn-outline-danger me-2" @click="toggleJjim(sitter.sitter_no)">♥ 찜 해제</button>
                <button class="btn btn-sm btn-primary" @click="goDetail(sitter.sitter_no)">상세보기</button>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="text-muted">찜한 펫시터가 없습니다.</div>
      </div>

      <div>
        <h4 class="mb-3">전체 펫시터 목록</h4>
        <div v-if="list.length > 0" class="row g-4">
          <div class="col-md-6 col-lg-4" v-for="sitter in list" :key="sitter.sitter_no">
            <div class="card h-100 shadow-sm">
              <img :src="sitter.sitter_pic" class="card-img-top" alt="sitter_pic">
              <div class="card-body">
                <h5 class="card-title">{{ sitter.user.nickname }} ({{ sitter.user.user_name }})</h5>
                <p class="card-text">{{ sitter.content }}</p>
                <ul class="list-unstyled mb-3">
                  <li><strong>태그:</strong> {{ sitter.tag }}</li>
                  <li><strong>돌봄:</strong> {{ sitter.carecount }}회</li>
                  <li><strong>지역:</strong> {{ sitter.care_loc }}</li>
                  <li><strong>시작가:</strong> {{ sitter.pet_first_price }}</li>
                  <li v-if="sitter.sitterApp"><strong>자격증:</strong> {{ sitter.sitterApp.license }}</li>
                  <li v-if="sitter.sitterApp"><strong>경력:</strong> {{ sitter.sitterApp.history }}</li>
                </ul>
                <button class="btn btn-sm btn-outline-danger me-2" @click="toggleJjim(sitter.sitter_no)">{{ sitter.jjimCheck ? '♥' : '♡' }}</button>
                <button class="btn btn-sm btn-primary" @click="goDetail(sitter.sitter_no)">상세보기</button>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="text-muted">펫시터가 없습니다.</div>
      </div>

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
  </div>
</div>

<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
  import axios from 'https://cdn.skypack.dev/axios';

  createApp({
    data() {
      return {
        list: [], jjimList: [], curpage: 1, totalpage: 1,
        startPage: 1, endPage: 1, fd: 'care_loc', st: [],
        filterOptions: ['서울', '경기', '인천', '고양이전문', '강아지전문']
      }
    },
    computed: {
      pageRange() {
        const range = []
        for (let i = this.startPage; i <= this.endPage; i++) range.push(i)
        return range
      }
    },
    mounted() {
      this.dataRecv(1)
      this.loadJjimList()
    },
    methods: {
      async dataRecv(page) {
        try {
          const params = { page: page, fd: this.fd, st: this.st.join(',') }
          const res = await axios.get('${pageContext.request.contextPath}/sitter/list_vue', { params })
          if (res.data.code === '200') {
            const d = res.data.data
            this.list = d.list
            this.curpage = d.curpage
            this.totalpage = d.totalpage
            this.startPage = d.startPage
            this.endPage = d.endPage
          } else {
            alert(res.data.message)
          }
        } catch (error) {
          console.error('❌ 데이터 로드 오류:', error)
        }
      },
      async loadJjimList() {
        try {
          const res = await axios.get('${pageContext.request.contextPath}/sitter/jjim/list', { withCredentials: true })
          if (res.data.code === '200') {
            this.jjimList = res.data.data
          } else {
            alert(res.data.message)
          }
        } catch (e) {
          console.error('찜 목록 로딩 실패:', e)
        }
      },
      async toggleJjim(sitter_no) {
        try {
          const res = await axios.post('${pageContext.request.contextPath}/sitter/jjim/toggle', { sitter_no }, { withCredentials: true })
          if (res.data.code === '200') {
            this.loadJjimList()
            this.dataRecv(this.curpage)
          } else {
            alert(res.data.message)
          }
        } catch (e) {
          alert("찜 처리 실패")
        }
      },
      goToReservationList() { location.href = '${pageContext.request.contextPath}/sitter/resList' },
      changePage(page) {
        if (page >= 1 && page <= this.totalpage) this.dataRecv(page)
      },
      resetFilter() { this.st = []; this.dataRecv(1) },
      goInsert() { location.href = `${pageContext.request.contextPath}/sitter/insert` },
      goDetail(sitter_no) {
        if (!sitter_no) return
        location.href = '${pageContext.request.contextPath}/sitter/detail?sitter_no=' + sitter_no
      }
    }
  }).mount('#app')
</script>
</body>
</html>
