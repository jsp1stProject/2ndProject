<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<style type="text/css">
.fc-event {
  border-radius: 6px !important;
  padding: 2px 5px;
  font-size: 14px;
}
.fc-daygrid-event-dot {
  display: none;
}
</style>
</head>
<body>
<!-- 전체 페이지 템플릿 구조 -->
<div class="container-fluid mt-4" id="app">
  <div class="row">
    <!-- 왼쪽 패널 -->
    <div class="col-md-3">
      <div class="card mb-3">
        <div class="card-header bg-primary text-white">📌 D-Day</div>
        <div class="card-body">
          <!-- <p v-for="d in ddayList" :key="d.title" class="mb-1" >✔ {{ d.title }} - D-{{ d.dday }}</p> -->
          <p class="mb-1"> 디데이출력부분 </p>
        </div>
      </div>

      <div class="card">
        <div class="card-header bg-success text-white">📌 오늘의 일정</div>
        <div class="card-body p-2">
          <ul class="list-group list-group-flush">
            <!-- <li class="list-group-item" v-for="event in filteredSchedule" :key="event.title"> -->
            <li class="list-group-item">
              <!-- 🗓️ {{ event.time }} - {{ event.title }} <span class="badge bg-info">{{ event.type }}</span> -->
              🗓️ 2025-05-18 - 산책가는날 <span class="badge bg-info">산책</span>
            </li>
            <li class="list-group-item">
              <!-- 🗓️ {{ event.time }} - {{ event.title }} <span class="badge bg-info">{{ event.type }}</span> -->
              🗓️ 2025-05-28 - 병원 <span class="badge bg-info">병원 예약 3시</span>
            </li>
            <li class="list-group-item">
              <!-- 🗓️ {{ event.time }} - {{ event.title }} <span class="badge bg-info">{{ event.type }}</span> -->
              🗓️ 2025-06-18 - 예방접종 <span class="badge bg-info">주사맞아야하는 날 </span>
            </li>
            <!-- <li v-if="filteredSchedule.length === 0" class="list-group-item text-muted">일정이 없습니다.</li> -->
            <li class="list-group-item text-muted">일정이 없습니다.</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 중앙 FullCalendar -->
    <div class="col-md-9">
      <div class="card mb-3 shadow-sm">
        <div class="card-header bg-light fw-bold">📆 큰 달력 보기</div>
        <div class="card-body">
          <div id="calendar"></div>
        </div>
      </div>

      <!-- 선택된 날짜 일정 요약 -->
      <div class="card shadow-sm">
        <div class="card-header bg-secondary text-white">📋 선택한 날짜 일정</div>
        <div class="card-body p-0">
          <ul class="list-group list-group-flush">
            <!-- 
            <li class="list-group-item" v-for="event in filteredSchedule" :key="event.title">
              {{ event.time }} - {{ event.title }} ({{ event.type }})
            </li>
             -->
             <li class="list-group-item">
             🗓️ 2025-05-28 - 병원 <span class="badge bg-info">병원 예약 3시</span>
             </li>
             <!-- <li
		          v-if="filteredSchedule.length === 0"
		          class="list-group-item text-muted"
		        >
		          📭 선택된 일정이 없습니다.
		        </li> -->
		      <li
		          class="list-group-item text-muted"
		        >
		          📭 선택된 일정이 없습니다.
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
      this.calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
        dateClick: this.handleDateClick,
		eventClick: this.handleEventClick,
        events: this.scheduleList.map(e => ({
          title: e.title,
          start: e.date
        }))
      });
      this.calendar.render();
      
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