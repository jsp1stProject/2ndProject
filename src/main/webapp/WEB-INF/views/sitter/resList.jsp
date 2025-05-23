<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="app">
  <div v-if="unauthorized">로그인이 필요합니다.</div>
  <div v-else>
    <h3>예약 목록</h3>
    <div v-for="res in list" :key="res.res_no" class="card p-3 my-2">
      <p>예약번호: {{ res.res_date }}</p>
      <p>시간: {{ res.start_time }} ~ {{ res.end_time }}</p>
      <p>총액: {{ res.total_price.toLocaleString() }}원</p>
      <p>상태: {{ res.res_status }} / 결제: {{ res.pay_status }}</p>
      <button @click="goToDetail(res.res_no)">상세보기</button>
    </div>
  </div>
</div>

<script type="module">
import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.7/+esm';

createApp({
  data() {
    return {
      list: [],
      unauthorized: false
    }
  },
  mounted() {
    axios.get('${pageContext.request.contextPath}/sitter/resList_vue', { params: { page: 1 }, withCredentials: true })
      .then(res => {
        if (res.data.status === 'unauthorized') {
          this.unauthorized = true;
        } else {
          this.list = res.data.list;
        }
      }).catch(() => {
        this.unauthorized = true;
      });
  },
  methods: {
    goToDetail(res_no) {
      location.href = '${pageContext.request.contextPath}/sitter/resDetail?res_no='+res_no;
    }

  }
}).mount("#app");
</script>
</body>
</html>