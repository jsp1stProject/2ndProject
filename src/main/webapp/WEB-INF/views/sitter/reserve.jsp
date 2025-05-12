<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>펫시터 예약하기</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div id="app" class="container mt-4">
  <h2 class="mb-4">펫시터 예약하기</h2>

  <form @submit.prevent="submitReservation">
    <div class="mb-3">
      <label>예약 일자</label>
      <input type="date" v-model="form.res_date" class="form-control" required>
    </div>

    <div class="row mb-3">
      <div class="col">
        <label>시작 시간</label>
        <select v-model="form.start_time" class="form-select">
          <option v-for="t in timeOptions" :key="t" :value="t">{{ t }}</option>
        </select>
      </div>
      <div class="col">
        <label>종료 시간</label>
        <select v-model="form.end_time" class="form-select">
          <option v-for="t in timeOptions" :key="t" :value="t">{{ t }}</option>
        </select>
      </div>
    </div>

    <div class="mb-3">
      <label>만남 장소</label><br>
      <div class="form-check form-check-inline" v-for="type in locationTypes" :key="type">
        <input class="form-check-input" type="radio" :value="type" v-model="form.location_type">
        <label class="form-check-label">{{ type }}</label>
      </div>
      <textarea v-if="form.location_type === '기타'" v-model="form.location_detail"
                class="form-control mt-2" placeholder="기타 장소 설명 입력"></textarea>
    </div>

    <div class="mb-3">
      <label>돌봄 받을 반려동물</label>
      <div v-for="pet in petsList" :key="pet.pet_no" class="form-check">
        <input class="form-check-input" type="checkbox" :value="pet.pet_no" v-model="form.pet_nos">
        <label class="form-check-label">{{ pet.pet_name }} ({{ pet.pet_type }}/{{ pet.pet_subtype }})</label>
      </div>
    </div>

    <div class="border p-3 bg-light mt-3">
      <h5>예약 요약</h5>
      날짜: {{ form.res_date || '-' }}<br>
      시간: {{ summaryTime }}<br>
      장소: {{ form.location_type }}<br>
      선택한 반려동물 수: {{ form.pet_nos.length }}<br>
      예상 총 가격: {{ formattedPrice }} 원
    </div>

    <button type="submit" class="btn btn-primary mt-4">예약하기</button>
  </form>
</div>

<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
  import axios from 'https://cdn.skypack.dev/axios';

  const pet_first_price = ${petFirstPrice};
  const petsList = JSON.parse('${petsJson}');
  const sitter_no = ${sitterNo};

  const app = createApp({
    data() {
      return {
        pet_first_price,
        petsList,
        locationTypes: ['신청자 집', '거리', '기타'],
        timeOptions: [...Array(14).keys()].flatMap(h => {
          const hour = h + 8;
          const hStr = hour < 10 ? '0' + hour : hour.toString();
          return [`${hStr}:00`, `${hStr}:30`];
        }),
        form: {
          res_date: '',
          start_time: '',
          end_time: '',
          location_type: '신청자 집',
          location_detail: '',
          pet_nos: []
        }
      }
    },
    computed: {
      summaryTime() {
        return this.form.start_time && this.form.end_time
          ? this.form.start_time + ' ~ ' + this.form.end_time
          : '-';
      },
      formattedPrice() {
        const count = this.form.pet_nos.length;
        const hours = this.calcHourDiff(this.form.start_time, this.form.end_time);
        const total = hours * count * this.pet_first_price;
        return isNaN(total) ? 0 : total.toLocaleString();
      }
    },
    methods: {
      calcHourDiff(start, end) {
        if (!start || !end) return 0;
        const [sh, sm] = start.split(':').map(Number);
        const [eh, em] = end.split(':').map(Number);
        const diff = (eh * 60 + em) - (sh * 60 + sm);
        return diff <= 0 ? 0 : Math.ceil(diff / 60);
      },
      submitReservation() {
        const payload = {
          ...this.form,
          sitter_no,
          total_price: this.formattedPrice.replace(/,/g, '')
        };

        axios.post("/sitter/reserve_vue", payload)
          .then(res => {
            alert("예약이 완료되었습니다.");
            location.href = "/sitter/resList";
          })
          .catch(err => {
            alert("예약에 실패했습니다.");
            console.error(err);
          });
      }
    }
  });

  app.mount('#app');
</script>
</body>
</html>
