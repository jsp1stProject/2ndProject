<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<div class="container pt-header">
<div id="member-manage" class="container pt-4">
  <div class="text-center mb-4">
    <h3 class="fw-bold">
      <span style="font-size: 1.8rem;">📋</span> 그룹 가입 신청 현황
    </h3>
    <p class="text-muted small mt-2">참여 신청한 멤버를 승인하거나 거절할 수 있습니다.</p>
  </div>

  <div class="table-responsive shadow-sm rounded" style="min-height: 400px;">
    <table class="table table-bordered text-center align-middle">
      <thead class="table-light">
        <tr>
          <th>신청자 닉네임</th>
          <th>신청일</th>
          <th>상태</th>
          <th>처리</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="reqlist.length === 0">
          <td colspan="4" class="text-center text-muted">현재 대기 중인 신청이 없습니다.</td>
        </tr>
        <tr v-for="req in reqlist" :key="req.request_no">
          <td>{{ req.user_nickname }}</td>
          <td>{{ req.dbday }}</td>
          <td>
            <span v-if="req.status === 'WAITING'" class="badge bg-warning text-dark">대기 중</span>
            <span v-else-if="req.status === 'APPROVED'" class="badge bg-success">승인됨</span>
            <span v-else class="badge bg-danger">거절됨</span>
          </td>
          <td>
            <span v-if="req.status === 'WAITING'">
            <button class="btn btn-success btn-sm me-1" @click="approve(req,'APPROVED')">승인</button>
            <button class="btn btn-danger btn-sm" @click="reject(req,'REJECTED')">거절</button>
            </span>
            <span v-else>
            <button class="btn btn-secondary btn-sm" disabled>처리 완료</button>
            </span>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</div>
<script type="module">
  import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';

  createApp({
    data() {
      return {
        requests: [],
		reqlist:[],
		status:'',
		user_no:0
      };
    },
    mounted() {
	  const params = new URLSearchParams(window.location.search);
      const userNoParam = params.get('user_no');
      console.log(userNoParam)
  	  if (userNoParam) {
    		this.user_no = parseInt(userNoParam);
  	   }
	  this.dataRecv()
	  
      
    },
    methods: {
	  async dataRecv() {
          await axios.get('../api/groups/'+this.user_no+'/join_requests')
          .then(res => {
			  console.log(res.data)
		  	  this.reqlist = res.data.data.reqlist;
		       console.log("list는 "+this.reqlist)
		  })
		  .catch(err => {
            console.error("데이터 조회 실패", err);
          });
      },
      approve(req,status) {
        axios.post('../api/groups/'+req.request_no+'/join_requests_result',{
				status: status,
				user_nickname : req.user_nickname,
				group_no : req.group_no,
				user_no : req.user_no
			})
          .then(() => {
            alert("승인 완료");
            this.dataRecv()
          })
          .catch(err => {
            alert("승인 실패");
            console.error(err);
          });
      },
      reject(req,status) {
        axios.post('../api/groups/'+req.request_no+'/join_requests_result',{
				status: status,
				user_nickname : req.user_nickname,
				group_no : req.group_no,
				user_no : req.user_no
			})
          .then(() => {
            alert("거절 완료");
            this.dataRecv()
          })
          .catch(err => {
            alert("거절 실패");
            console.error(err);
          });
      }
    }
  }).mount('#member-manage');
</script>
