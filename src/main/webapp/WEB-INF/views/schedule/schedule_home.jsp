<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script> -->
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<style>
.fc-custom-event {
  font-size: 14px;
  white-space: normal;
}
</style>
<!-- 전체 페이지 템플릿 구조 -->
<div class="container pt-header mt-4" id="app">
  <div class="row">
    <!-- 왼쪽 패널 -->
    <div class="col-md-3">
      <div class="card shadow-sm mb-3">
        <div class="card-header fw-bold">📌 D-Day</div>
        <div class="card-body">
          <p v-if="schedule_ddayList.length === 0" class="text-muted">등록된 D-Day 일정이 없습니다.</p>
            <p v-for="d in schedule_ddayList" :key="d.sche_no" class="mb=1">
            <i class="bi bi-calendar-check"></i>
             {{d.sche_title}}
             <span :class="getDdayText(d.sche_end_str).includes('-') ? 'text-danger' : 'text-muted'">
    {{getDdayText(d.sche_end_str)}}
  </span>
        </div>
      </div>

      <div class="card shadow-sm">
	  <div class="card-header fw-bold">📌 중요일정</div>
	  <div class="card-body p-2">
	    <ul class="list-group list-group-flush">
	      <li v-if="schedule_importList.length === 0" class="text-muted list-group-item">
	        등록된 중요 일정이 없습니다.
	      </li>
	      <li
		  v-for="schedule in schedule_importList"
		  :key="schedule.sche_no"
		  class="list-group-item d-flex justify-content-between align-items-center"
		>
		  
		  <span class="me-2">
		    🕒 {{ schedule.sche_start_str }}
		    <template v-if="schedule.sche_start_str !== schedule.sche_end_str">
		      ~ {{ schedule.sche_end_str }}
		    </template>
		    - {{ schedule.sche_title }}
		  </span>
		  <span class="badge bg-secondary">
		    {{ schedule.type === 0 ? '개인' : '그룹' }}
		  </span>
		</li>
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
            <li class="list-group-item d-flex justify-content-between align-items-center" v-for="event in selected_date_schedules" :key="event.sche_no">
              {{ event.sche_start_str }} ~ {{ event.sche_end_str}} - {{ event.sche_title }}
              <span class="badge bg-info">({{ event.type === 0 ? '개인' : '그룹' }})</span>
            </li>
            <li v-if="selected_date_schedules.length === 0" class="list-group-item text-muted">
		          📭 선택된 일정이 없습니다.{{selected_date_schedules.length}}
		    </li>
          </ul>
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
        <form @submit.prevent="addSchedule">
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
          <div class="form-check mb-3">
			  <input class="form-check-input" type="checkbox" v-model="newSchedule.alarm" id="alarmCheck">
			  <label class="form-check-label" for="alarmCheck">🔔 일정 전에 알림 받기</label>
			</div>
          <div class="text-end">
            <button type="submit" class="btn btn-success">등록하기</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<div class="modal fade" id="scheduleDetailModal" tabindex="-1" aria-labelledby="scheduleDetailLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
    
      <div class="modal-header">
        <h5 class="modal-title" id="scheduleDetailLabel">
          📋 일정 상세보기
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      
      <div class="modal-body">
       <div v-if="!isEditMode">
        <div class="mb-3">
          <h5>
            <span class="badge bg-primary me-2" v-if="detail_schedule.type === 0">개인</span>
            <span class="badge bg-success me-2" v-else>그룹</span>
            {{ detail_schedule.sche_title }}
          </h5>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-bold">📅 일정 시간</label>
          <div>
            {{ detail_schedule.sche_start_str }} ~ {{ detail_schedule.sche_end_str }}
          </div>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-bold">📄 내용</label>
          <div class="border rounded p-2 bg-light">
            {{ detail_schedule.sche_content }}
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label fw-bold">👥 참여자</label>
          <ul class="list-group list-group-flush">
            <li v-for="user in detail_schedule.participants" :key="user.user_no" class="list-group-item">
              {{ user.nickname }}
            </li>
            <li v-if="!detail_schedule.participants || detail_schedule.participants.length === 0" class="list-group-item text-muted">
              참여자가 없습니다
            </li>
          </ul>
        </div>

        <div class="mb-3">
          <label class="form-label fw-bold">⭐️ 중요</label>
          <span :class="detail_schedule.is_important === 1 ? 'text-warning' : 'text-muted'">
            {{ detail_schedule.is_important === 1 ? '중요 일정입니다.' : '일반 일정입니다.' }}
          </span>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-bold">🔔 알림 설정</label>
          <span :class="detail_schedule.alarm === 1 ? 'text-primary' : 'text-muted'">
            {{ detail_schedule.alarm === 1 ? '알림이 설정되어 있습니다.' : '알림 없음' }}
          </span>
        </div>
       </div>
       
       <div v-else>
	    <div class="mb-3">
	      <label class="form-label">제목</label>
	      <input type="text" class="form-control" v-model="editForm.sche_title">
	    </div>
	    <div class="mb-3">
	      <label class="form-label">내용</label>
	      <textarea class="form-control" rows="3" v-model="editForm.sche_content"></textarea>
	    </div>
	    <div class="mb-3">
	      <label class="form-label">시작 시간</label>
	      <input type="datetime-local" class="form-control" v-model="editForm.sche_start_str">
	    </div>
	    <div class="mb-3">
	      <label class="form-label">종료 시간</label>
	      <input type="datetime-local" class="form-control" v-model="editForm.sche_end_str">
	    </div>
	    <div class="form-check mb-2">
	      <input type="checkbox" class="form-check-input" v-model="editForm.is_important" id="editImportant">
	      <label for="editImportant" class="form-check-label">⭐️ 중요 일정</label>
	    </div>
	    <div class="form-check mb-2">
	      <input type="checkbox" class="form-check-input" v-model="editForm.alarm" id="editAlarm">
	      <label for="editAlarm" class="form-check-label">🔔 알림</label>
	    </div>
	  </div>
      </div>

      <div class="modal-footer">
  <div v-if="!isEditMode">
    <button class="btn btn-primary" @click="startEdit()">수정</button>
    <button class="btn btn-danger" @click="deleteSchedule(detail_schedule.sche_no, detail_schedule.type)">삭제</button>
  </div>
  <div v-else>
    <button class="btn btn-success" @click="submitEdit(detail_schedule.sche_no)">저장</button>
    <button class="btn btn-secondary" @click="isEditMode = false">취소</button>
  </div>
  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
</div>

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
		newSchedule: {
			title:'',
			content:'',
			start:'',
			end:'',
			is_important: false,
			alarm:false
		},
		clickTimer: null,
		lastClickedDate: null,
		schedule_totalList: [],
		detail_schedule:{},
        selected_date_schedules:[],
		schedule_ddayList: [],
		schedule_importList:[],
		isEditMode: false,
		editForm: {},
      }
    },
    computed: {
      filteredSchedule() {
        return this.scheduleList.filter(e => e.date === this.selectedDate);
      }
    },
    mounted() {
		console.log(Math.abs(-3));
      this.dataRecv()
    },
    methods: {
	  deleteSchedule(sche_no,type) {
    	if (!confirm("정말 삭제하시겠습니까?")) return;

    	axios.post('../api/schedules/'+sche_no+'/delete', {
    		sche_no: sche_no,
    		type: type
  		})
      	.then(res => {
        alert("삭제가 완료되었습니다.");

        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(
          document.getElementById('scheduleDetailModal')
        );
        modal.hide();

        // 캘린더 이벤트 새로고침
        this.dataRecv();
      })
      .catch(err => {
        console.error("삭제 중 오류 발생", err);
        alert("삭제에 실패했습니다.");
      });
  	  },
	 startEdit() {
  		console.log("수정")
		this.isEditMode = true;
  		this.editForm = { ...this.detail_schedule };
	  },
	  submitEdit(sche_no) {
        const updateData = {
    		...this.editForm,
    		is_important: this.editForm.is_important ? 1 : 0,
    		alarm: this.editForm.alarm ? 1 : 0
  		};

  		axios.post('../api/schedules/'+sche_no+'/update', updateData)
    	.then(res => {
      	alert('수정이 완료되었습니다');
      	this.isEditMode = false;
      	const modal = bootstrap.Modal.getInstance(document.getElementById('scheduleDetailModal'));
      	modal.hide();
      	this.dataRecv();
       })
    	.catch(err => {
      	console.error('수정 실패', err);
      	alert('수정이 실패하였습니다');
       });
	  },
	  getDdayText(endDateStr){
		if (!endDateStr) return "";		

		const today = new Date();
		today.setHours(0, 0, 0, 0);

		const endDate = new Date(endDateStr);
		endDate.setHours(0, 0, 0, 0);

		const diffTime = endDate - today;
		const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
		if (diffDays > 0) return "D-" + diffDays;
		
    	if (diffDays === 0) return "D-DAY";
    	return "D+" + Math.abs(diffDays);
	  },
	  addSchedule(){
		console.log("스케쥴 입력 실행 ")
		const formData = new FormData();
		formData.append('sche_title',this.newSchedule.title);
		formData.append('sche_content',this.newSchedule.content);
		formData.append('sche_start_str',this.newSchedule.start);
		formData.append('sche_end_str',this.newSchedule.end);
		formData.append('is_important',this.newSchedule.is_important ? 1 : 0);
		formData.append('alarm',this.newSchedule.alarm ? 1 : 0);
		axios.post('../api/schedules',formData)
		.then(res => {
			const schedulemodal = bootstrap.Modal.getInstance(document.getElementById('addScheduleModal'));
  			schedulemodal.hide();
    		this.dataRecv()
		})
		.catch(err => {
     		 console.error("일정 등록 실패", err);
    	});
	  },
	  
	  handleDateClick(info){
		const clickedDate = info.dateStr;

	    if (this.clickTimer && this.lastClickedDate === clickedDate) {
      		clearTimeout(this.clickTimer);
     		 this.clickTimer = null;
     		 this.handleDateDoubleClick(clickedDate);
    	} 
		else {
			this.lastClickedDate = clickedDate;
      		this.clickTimer = setTimeout(() => {
      			this.handleDateSingleClick(clickedDate);
        		this.clickTimer = null;
				this.lastClickedDate = null;
      		}, 300);
    	}
  	  },
  	  handleDateSingleClick(clickedDate) {
   		 console.log("해당날짜 일정 출력")
		 console.log(clickedDate)
		 axios.get('../api/schedules/date/'+clickedDate)
		 .then(res => {
			console.log(res.data)
			this.selected_date_schedules = res.data
		 })
  	  },
	  handleDateDoubleClick(clickedDate) {
		console.log("해당날짜 일정 추가")
		const modal = new bootstrap.Modal(document.getElementById('addScheduleModal'));
   		modal.show();
	  },
  	  handleEventClick(info) {
		const sche_no = info.event.id;
		console.log("일정클릭시 디테일모달창으로")
		axios.get('../api/schedules/'+sche_no+'/detail')
		.then(res => {
			console.log(res.data)
			this.detail_schedule=res.data
			const modal = new bootstrap.Modal(document.getElementById('scheduleDetailModal'));
   		 	modal.show();
		})
		.catch(err => {
     		 console.error("일정 조회 실패", err);
    	});
	  },
	  dataRecv(){
		axios.get('../api/schedules')
		.then(res => {
			console.log(res.data);
			this.schedule_totalList = res.data.list;
			this.schedule_ddayList = res.data.ddayList;
			this.schedule_importList = res.data.importList;
			const calendarEl = document.getElementById('calendar');
      		this.calendar = new FullCalendar.Calendar(calendarEl, {
        		initialView: 'dayGridMonth',
        		locale: 'ko',
				dayMaxEventRows: true,
  				views: {
  				dayGridMonth: {
      				dayMaxEventRows: 3 // 최대 3개까지만 보이고, 초과 시 "+n개 더보기"로 표시
    				}
  				},
       	 		dateClick: this.handleDateClick,
				eventClick: this.handleEventClick,
				eventContent: function(arg) {
					const props = arg.event.extendedProps;
					//console.log("🧩 title:", arg.event.title);
  					//console.log("🧩 props:", props);

					let icons='';
					if(props.is_important ===1 ) icons +='⭐';
					if (props.alarm === 1) icons += '🔔';
    				icons += props.type === 0 ? '👤' : '👥';
					const container = document.createElement('div');
					container.innerHTML = '<b>' + icons + ' ' + arg.event.title + '</b>';
					return {
						domNodes: [container]
    				};
				},
        		events: this.schedule_totalList.map(s => ({
					id: s.sche_no,
          			title: s.sche_title,
          			start: s.sche_start_str.replace(' ', 'T'),
					// start는 필수 속성이라서 값이 없으면 출력 불가, 표시 안하고 싶으면 아래처럼 allDay를 적어두면 됨
					end: (() => {
    					const end = new Date(s.sche_end_str.replace(' ', 'T'));
    					end.setDate(end.getDate() + 1);
    					return end.toISOString();
  					})(),
					allDay: true,
					backgroundColor: s.type === 0 ? '#17EBCF' : '#FF9AB5',
					borderColor: s.type === 0 ? '#12D1B8' : '#FF7A9F',
					is_important: s.is_important,
 					alarm: s.alarm,
  					type: s.type
				}))
      	 	});
      		this.calendar.render();
		})
       }
    }
  }).mount('.container')
</script>