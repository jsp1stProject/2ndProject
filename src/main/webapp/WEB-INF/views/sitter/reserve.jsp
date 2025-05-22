<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.google.gson.Gson" %> 
<%@ page import="com.sist.web.user.vo.UserVO" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 예약하기</title>
</head>
<body>
<div id="app" class="container mt-4">
  <h2 class="mb-4">펫시터 예약하기</h2>

  <form @submit.prevent="submitReservation">
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

    <div class="mb-3">
      <label class="form-label">방문지 주소</label>
      <input type="text" v-model="form.location_detail" class="form-control" required>
    </div>

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

    <div class="mb-3">
      <label class="form-label">돌봄 일자</label>
      <input type="date" v-model="form.res_date" class="form-control" @change="fetchDisabledHours" required>
    </div>

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

    <div class="mb-3">
      <label class="form-label">펫시터에게 남길 말</label>
      <textarea class="form-control" rows="3" v-model="form.memo"></textarea>
    </div>

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
<script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<script>window.__IMP_CODE__ = '<%= request.getAttribute("impCode") %>';</script>
<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
  import axios from 'https://cdn.jsdelivr.net/npm/axios@1.6.7/+esm';

  createApp({
    data() {
      return {
        sitterNo: parseInt('<%= request.getAttribute("sitterNo") %>'),
        petFirstPrice: parseInt('<%= request.getAttribute("petFirstPrice") %>'),
        petsList: JSON.parse('<%= ((String)request.getAttribute("petsJson")).replaceAll("\"", "\\\"") %>'),
        disabledHours: [],
        form: {
          pet_nos: [],
          location_detail: '',
          location_type: '',
          res_date: '',
          selectedTimes: [],
          memo: ''
        },
        timeSelectTemp: []
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
        if (this.timeSelectTemp.length === 0) {
          this.timeSelectTemp.push(hour);
          this.form.selectedTimes = [hour];
        } else if (this.timeSelectTemp.length === 1) {
          this.timeSelectTemp.push(hour);
          const [start, end] = this.timeSelectTemp.sort((a, b) => a - b);
          this.form.selectedTimes = [];
          for (let i = start; i <= end; i++) {
            if (!this.disabledHours.includes(i)) {
              this.form.selectedTimes.push(i);
            }
          }
        } else {
          // 세 번째 클릭 시 초기화 후 해당 시간부터 새 선택 시작
          this.timeSelectTemp = [hour];
          this.form.selectedTimes = [hour];
        }
      },
      async fetchDisabledHours() {
        if (!this.form.res_date) return;
        try {
          const res = await axios.get("/web/sitter/res/disabled_hours", {
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
        const IMP = window.IMP;
        IMP.init(window.__IMP_CODE__);

        const amount = this.totalPrice;
        const merchantUid = 'resv_' + new Date().getTime(); 

        IMP.request_pay({
          pg: "kakaopay", 
          pay_method: "card",
          merchant_uid: merchantUid,
          name: "pet4you 돌봄 예약",
          amount: amount,
          buyer_email: "test@example.com",
          buyer_name: "홍길동",
          buyer_tel: "010-1234-5678",
          buyer_addr: this.form.location_detail,
          buyer_postcode: "12345"
        }, async (rsp) => {
          if (rsp.success) {
            const payload = {
              sitter_no: this.sitterNo,
              pet_nos: this.form.pet_nos,
              res_date: this.form.res_date,
              start_time: this.startTime,
              end_time: this.endTime,
              location_type: this.form.location_type,
              location_detail: this.form.location_detail,
              total_price: this.totalPrice,
              pay_status: "결제완료",
              res_status: "요청",
              imp_uid: rsp.imp_uid,
              merchant_uid: rsp.merchant_uid
            };

            try {
              const res = await axios.post("/web/sitter/reserve_vue", payload, {
                withCredentials: true
              });
              if (res.data === "success") {
                alert("예약 및 결제 완료!");
                location.href = "/web/sitter/resList";
              } else {
                alert("예약 저장 실패");
              }
            } catch (err) {
              console.error("❌ 예약 등록 오류:", err);
              alert("오류 발생");
            }
          } else {
            alert("❌ 결제 실패: " + rsp.error_msg);
          }
        });
      }
    }
  }).mount("#app");
</script>
</body>
</html>
