<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script> -->
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>

<!-- 전체 페이지 템플릿 구조 -->
<div class="container pt-header mt-4" id="app">
  <div class="row">
    <!-- 왼쪽 패널 -->
    <div class="col-md-3">
      <div class="card shadow-sm mb-3">
        <div class="card-header fw-bold">📌 D-Day</div>
        <div class="card-body">
          <!-- <p v-if="ddayList.length === 0" class="text-muted">등록된 D-Day 일정이 없습니다.</p>
            <p v-for="d in ddayList" :key="d.title"> -->
            <p class="mb-1"> 디데이출력부분 </p>
            <i class="bi bi-calendar-check"></i>
             병원 - 
             <span class="text-danger">D-5</span>
<%--             <i class="bi bi-calendar-check"></i> {{ d.title }} - <span :class="d.dday <= 3 ? 'text-danger' : (d.dday <= 7 ? 'text-warning' : '')">D-{{ d.dday }}</span> --%>
        </div>
      </div>

      <div class="card shadow-sm">
        <div class="card-header fw-bold">📌 오늘의 일정</div>
        <div class="card-body p-2">
          <ul class="list-group list-group-flush">
            <p>
              🕒 2025-05-18 - 산책가는날 <span class="badge bg-secondary">산책</span>
            </p>
            <p>
              🕒 2025-05-18 - 산책가는날 <span class="badge bg-secondary">산책</span>
            </p>
            <p>
              🕒 2025-05-18 - 산책가는날 <span class="badge bg-secondary">산책</span>
            </p>
            
          </ul>
        </div>
      </div>
    </div>

    <!-- 중앙 FullCalendar -->
    <div class="col-md-9">
      <!-- 모바일 전용 toggle 버튼 -->
      <button class="btn btn-secondary d-md-none mb-2" data-bs-toggle="collapse" data-bs-target="#calendarWrap">
          📅 큰 달력 보기
        </button>
      <div id="calendarWrap" class="collapse show d-md-block">
          <div class="card shadow-sm mb-3">
            <div class="card-header fw-bold">📆 큰 달력 보기</div>
            <div class="card-body">
              <div id="calendar"></div>
            </div>
          </div>
        </div>

      <!-- 선택된 날짜 일정 요약 -->
      <div class="card shadow-sm">
        <div class="card-header fw-bold"><i class="bi bi-list-task"></i>📋 선택한 날짜 일정</div>
        <div class="card-body">
         <div class="text-end mt-2">
		  <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addScheduleModal">
		    ➕ 일정 추가
		  </button>
		 </div>
          <ul class="list-group">
            <!-- 
            <li class="list-group-item" v-for="event in filteredSchedule" :key="event.title">
              {{ event.time }} - {{ event.title }} ({{ event.type }})
            </li>
             -->
             <li class="list-group-item d-flex justify-content-between align-items-center" >
             🗓️ 2025-05-28 - 병원 <span class="badge bg-info">병원 예약 3시</span>
             </li>
             <!-- <li
		          v-if="filteredSchedule.length === 0"
		          class="list-group-item text-muted"
		        >
		          📭 선택된 일정이 없습니다.
		        </li> -->
		     
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" id="addScheduleModal" tabindex="-1" aria-labelledby="addScheduleLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addScheduleLabel">📅 새 일정 등록</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="submitSchedule">
          <div class="mb-3">
            <label class="form-label">제목</label>
            <input type="text" class="form-control" v-model="newSchedule.title" required>
          </div>
          <div class="mb-3">
            <label class="form-label">내용</label>
            <textarea class="form-control" rows="3" v-model="newSchedule.content"></textarea>
          </div>
          <div class="mb-3">
            <label class="form-label">시작 시간</label>
            <input type="datetime-local" class="form-control" v-model="newSchedule.start" required>
          </div>
          <div class="mb-3">
            <label class="form-label">종료 시간</label>
            <input type="datetime-local" class="form-control" v-model="newSchedule.end" required>
          </div>
          <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" v-model="newSchedule.is_important" id="importantCheck">
            <label class="form-check-label" for="importantCheck">⭐️ 중요 일정</label>
          </div>
          <div class="text-end">
            <button type="submit" class="btn btn-success">등록하기</button>
          </div>
        </form>
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
  }).mount('.container')
</script>