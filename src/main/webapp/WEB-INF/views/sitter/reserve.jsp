<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 예약하기</title>
  <script type="module" src="https://unpkg.com/vue@3/dist/vue.esm-browser.js"></script>
  <script type="module" src="https://unpkg.com/axios/dist/axios.min.js"></script>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .time-btn.active {
      background-color: #0d6efd;
      color: white;
    }
  </style>
</head>
<body>
<div id="app" class="container mt-4">
  <h2 class="mb-4">펫시터 예약하기</h2>

  <form @submit.prevent="submitReservation">
    <!-- 반려동물 선택 -->
    <div class="mb-3">
      <label class="form-label">돌봄 아이</label>
      <div class="d-flex flex-wrap gap-2">
        <div v-for="pet in petsList" :key="pet.pet_no" class="form-check">
          <input class="form-check-input" type="checkbox" :id="'pet_' + pet.pet_no"
                 :value="pet.pet_no" v-model="form.pet_nos">
          <label class="form-check-label" :for="'pet_' + pet.pet_no">
            <img :src="pet.pet_profilepic" width="50" height="50" class="me-1 rounded-circle">
            {{ pet.pet_name }}
          </label>
        </div>
      </div>
    </div>

    <!-- 방문지 -->
    <div class="mb-3">
      <label class="form-label">방문지 주소</label>
      <input type="text" v-model="form.location_detail" class="form-control" required>
    </div>

    <!-- 출입 방법 -->
    <div class="mb-3">
      <label class="form-label">출입 방법</label>
      <select v-model="form.location_type" class="form-select" required>
        <option disabled value="">선택하세요</option>
        <option>공동 현관 비밀번호 있음</option>
        <option>경비실 호출</option>
        <option>문 열어둠</option>
        <option>기타</option>
      </select>
    </div>

    <!-- 돌봄 일자 -->
    <div class="mb-3">
      <label class="form-label">돌봄 일자</label>
      <input type="date" v-model="form.res_date" class="form-control" @change="fetchDisabledHours" required>
    </div>

    <!-- 시간대 선택 -->
    <div class="mb-3">
      <label class="form-label">시간대 선택</label>
      <div class="d-flex flex-wrap gap-1">
        <button v-for="hour in 24" type="button" class="btn btn-outline-primary time-btn"
                :class="{ active: form.selectedTimes.includes(hour) }"
                :disabled="disabledHours.includes(hour)"
                @click="toggleTime(hour)">
          {{ hour.toString().padStart(2, '0') }}:00
        </button>
      </div>
    </div>

    <!-- 남길 말 -->
    <div class="mb-3">
      <label class="form-label">펫시터에게 남길 말</label>
      <textarea class="form-control" rows="3" v-model="form.memo"></textarea>
    </div>

    <!-- 실시간 요약 -->
    <div class="border p-3 rounded bg-light">
      <h5>신청 요약</h5>
      <p><strong>선택한 아이:</strong> {{ selectedPetNames.join(', ') }}</p>
      <p><strong>돌봄 일자:</strong> {{ form.res_date }}</p>
      <p><strong>시간:</strong> {{ startTime }} ~ {{ endTime }}</p>
      <p><strong>총 시간:</strong> {{ timeCount }}시간</p>
      <p><strong>총 금액:</strong> {{ totalPrice.toLocaleString() }}원</p>
    </div>

    <div class="text-end mt-3">
      <button type="submit" class="btn btn-success">예약 신청</button>
    </div>
  </form>
</div>

<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
  import axios from 'https://unpkg.com/axios/dist/axios.min.js';

  createApp({
    data() {
      return {
        sitterNo: parseInt('<%= request.getAttribute("sitterNo") %>'),
        petFirstPrice: parseInt('<%= request.getAttribute("petFirstPrice") %>'),
        petsList: JSON.parse('<%= ((String)request.getAttribute("petsJson")).replaceAll("\"", "\\\\\"") %>'),
        disabledHours: [],
        form: {
          pet_nos: [],
          location_detail: '',
          location_type: '',
          res_date: '',
          selectedTimes: [],
          memo: ''
        }
      };
    },
    computed: {
      timeCount() {
        return this.form.selectedTimes.length;
      },
      startTime() {
        if (this.form.selectedTimes.length === 0) return '';
        return this.form.selectedTimes[0].toString().padStart(2, '0') + ":00";
      },
      endTime() {
        if (this.form.selectedTimes.length === 0) return '';
        const last = this.form.selectedTimes[this.form.selectedTimes.length - 1] + 1;
        return (last % 24).toString().padStart(2, '0') + ":00";
      },
      selectedPetNames() {
        return this.petsList
          .filter(p => this.form.pet_nos.includes(p.pet_no))
          .map(p => p.pet_name);
      },
      totalPrice() {
        return this.form.pet_nos.length * this.timeCount * this.petFirstPrice;
      }
    },
    methods: {
      toggleTime(hour) {
        const idx = this.form.selectedTimes.indexOf(hour);
        if (idx === -1) this.form.selectedTimes.push(hour);
        else this.form.selectedTimes.splice(idx, 1);
        this.form.selectedTimes.sort((a, b) => a - b);
      },
      async fetchDisabledHours() {
        if (!this.form.res_date) return;
        try {
          const res = await axios.get("/sitter/res/disabled_hours", {
            params: {
              sitter_no: this.sitterNo,
              res_date: this.form.res_date
            }
          });
          this.disabledHours = res.data.disabledHours;
        } catch (err) {
          console.error("시간대 로딩 실패");
        }
      },
      async submitReservation() {
        const payload = {
          sitter_no: this.sitterNo,
          pet_nos: this.form.pet_nos,
          res_date: this.form.res_date,
          start_time: this.startTime,
          end_time: this.endTime,
          location_type: this.form.location_type,
          location_detail: this.form.location_detail,
          total_price: this.totalPrice,
          pay_status: "미결제",
          res_status: "요청",
        };
        try {
          const res = await axios.post("/sitter/reserve_vue", payload);
          if (res.data === "success") {
            alert("예약 신청이 완료되었습니다!");
            location.href = "/sitter/resList";
          } else {
            alert("예약 신청에 실패했습니다.");
          }
        } catch (err) {
          alert("오류가 발생했습니다.");
        }
      }
    }
  }).mount("#app");
</script>
</body>
</html>
