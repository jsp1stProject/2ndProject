<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<div class="container pt-header mt-4" id="ScheduleListApp">
  <div class="row mt-5 mb-4 text-center">
	  <h3 class="fw-bold"><i class="bi bi-calendar-check"></i> 전체 일정 목록</h3>
	  <p class="text-muted small">등록한 전체 일정들을 조회할 수 있습니다.</p>
	</div>
	
	<div class="input-group mb-3 w-50 mx-auto">
	  <input type="text" class="form-control" v-model="searchText" placeholder="제목 검색">
	  <button class="btn btn-outline-primary" @click="search">검색</button>
	</div>

  <table class="table table-bordered text-center align-middle shadow-sm">
    <thead class="table-light">
      <tr>
        <th>제목</th>
        <th>시작일</th>
        <th>종료일</th>
        <th>중요</th>
        <th>유형</th>
      </tr>
    </thead>
    <tbody class="min-fixed-body">
      <tr v-if="list.length === 0">
        <td colspan="5" class="text-muted text-center align-middle" style="height: 300px; vertical-align: middle;">일정이 없습니다.</td>
      </tr>
      <tr v-for="s in list" :key="s.sche_no">
        <td>{{ s.sche_title }}</td>
        <td>{{ s.sche_start_str }}</td>
        <td>{{ s.sche_end_str }}</td>
        <td>{{ s.is_important === 1 ? '⭐' : '' }}</td>
        <td>{{ s.type === 0 ? '개인' : '그룹' }}</td>
      </tr>
    </tbody>
  </table>

  <nav v-if="totalpage > 1" class="mt-4">
    <ul class="pagination justify-content-center">
      <li class="page-item" :class="{ disabled: curpage === 1 }">
        <a class="page-link" href="#" @click.prevent="changePage(curpage - 1)">이전</a>
      </li>
      <li
        class="page-item"
        v-for="p in pageRange"
        :key="p"
        :class="{ active: p === curpage }"
      >
        <a class="page-link" href="#" @click.prevent="changePage(p)">{{ p }}</a>
      </li>
      <li class="page-item" :class="{ disabled: curpage === totalpage }">
        <a class="page-link" href="#" @click.prevent="changePage(curpage + 1)">다음</a>
      </li>
    </ul>
  </nav>
</div>
<script type="module">
import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';

createApp({
  data() {
    return {
      user_no: 0,
      list: [],
	  page:1,
      curpage: 1,
      totalpage: 0,
      startPage: 1,
      endPage: 1,
      searchText: ''
    };
  },
  computed: {
    pageRange() {
      const range = [];
      for (let i = this.startPage; i <= this.endPage; i++) {
        range.push(i);
      }
      return range;
    }
  },
  mounted() {
    this.loadData(1);
  },
  methods: {
    loadData(page) {
      axios.get('../api/schedules/list?page='+page+'&search='+this.searchText)
        .then(res => {
		  console.log(res.data)
          this.list = res.data.list;
          this.curpage = res.data.curpage;
          this.totalpage = res.data.totalpage;
          this.startPage = res.data.startPage;
          this.endPage = res.data.endPage;
        })
        .catch(err => {
          console.error("목록 로딩 실패", err);
        });
    },
    changePage(page) {
      if (page < 1 || page > this.totalpage) return;
      this.loadData(page);
    },
    search() {
      this.loadData(1); // 검색은 항상 첫 페이지로 이동
    }
  }
}).mount('#ScheduleListApp');
</script>