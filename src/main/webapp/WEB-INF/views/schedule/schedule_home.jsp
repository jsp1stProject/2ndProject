<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>
<body>
<!-- 전체 페이지 템플릿 구조 -->
<div class="container-fluid mt-4" id="app">
  <div class="row">
    <!-- 왼쪽 패널 -->
    <div class="col-md-3">
      <div class="card mb-3">
        <div class="card-header">📌 D-Day</div>
        <div class="card-body">
          <!-- <p v-for="d in ddayList" :key="d.title">✔ {{ d.title }} - D-{{ d.dday }}</p> -->
          <p> 디데이출력부분 </p>
        </div>
      </div>

      <div class="card">
        <div class="card-header">📅 작은 달력</div>
        <div class="card-body">
          <!-- 미니 달력 자리 -->
          <input type="date" class="form-control" v-model="selectedDate">
        </div>
      </div>
    </div>

    <!-- 중앙 FullCalendar -->
    <div class="col-md-9">
      <div class="card mb-3">
        <div class="card-header">📆 전체 일정 보기</div>
        <div class="card-body">
          <div id="calendar"></div>
        </div>
      </div>

      <!-- 선택된 날짜 일정 요약 -->
      <div class="card">
        <div class="card-header">📋 선택한 날짜 일정</div>
        <div class="card-body">
          <ul class="list-group">
            <!-- 
            <li class="list-group-item" v-for="event in filteredSchedule" :key="event.title">
              {{ event.time }} - {{ event.title }} ({{ event.type }})
            </li>
             -->
             <li>
             선택된 날짜 일정들
             </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

  createApp({
    data() {
      return {
        ddayList: [
          { title: "프로젝트 마감", dday: 2 },
          { title: "시험", dday: 5 }
        ],
        selectedDate: new Date().toISOString().slice(0, 10), // YYYY-MM-DD
        scheduleList: [
          { date: '2025-05-13', time: '10:00', title: '회의', type: '개인' },
          { date: '2025-05-13', time: '14:00', title: '팀 회의', type: '그룹' },
          { date: '2025-05-14', time: '09:30', title: '스터디', type: '개인' }
        ]
      }
    },
    computed: {
      filteredSchedule() {
        return this.scheduleList.filter(e => e.date === this.selectedDate);
      }
    },
    mounted() {
      const calendarEl = document.getElementById('calendar');
      const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
        dateClick: this.handleDateClick,
        events: this.scheduleList.map(e => ({
          title: e.title,
          start: e.date
        }))
      });
      calendar.render();
    },
    methods: {
      handleDateClick(info) {
        this.selectedDate = info.dateStr;
      }
    }
  }).mount('#app')
</script>
</body>
</html>