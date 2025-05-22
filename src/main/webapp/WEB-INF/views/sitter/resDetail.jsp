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
  <div v-if="unauthorized">접근 권한이 없습니다.</div>
  <div v-else-if="res">
    <h3>예약 상세</h3>
    <p>펫시터 닉네임: {{ res.sitterVO.user.nickname }}</p>
    <p>예약일: {{ formatDate(res.res_date) }}</p>
    <p>시간: {{ res.start_time }} ~ {{ res.end_time }}</p>
    <p>총액: {{ res.total_price.toLocaleString() }}원</p>
    <p>결제 상태: {{ res.pay_status }} / 예약 상태: {{ res.res_status }}</p>
    <p>신청자: {{ res.userVO.nickname }}</p>
    <p>펫시터 요금: {{ res.sitterVO.pet_first_price }}</p>
    <p>돌봄 아이들:</p>
    <ul>
      <li v-for="pet in res.petsList" :key="pet.pet_no">{{ pet.pet_name }}</li>
    </ul>
<button class="btn btn-primary" @click="goToChat(resNo)">채팅하기</button>

  </div>
</div>

<script type="module">
import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.7/+esm';

createApp({
  data() {
    return {
      resNo: new URL(location.href).searchParams.get("res_no"),
      res: null,
      unauthorized: false
    }
  },
methods: {
	goToChat(resNo) {
    window.location.href = '/web/sitterchat/chat?res_no=' + resNo;
  },
  formatDate(ts) {
    const date = new Date(ts);
    return date.toISOString().slice(0, 10); // yyyy-MM-dd
  }
},
  mounted() {
    axios.get('/web/sitter/resDetail_vue', {
      params: { res_no: this.resNo },
      withCredentials: true
    }).then(res => {
      if (res.data === 'unauthorized' || res.data === 'forbidden') {
        this.unauthorized = true;
      } else {
        this.res = res.data;
      }
    }).catch(() => {
      this.unauthorized = true;
    });
  }
}).mount("#app");
</script>

</body>
</html>