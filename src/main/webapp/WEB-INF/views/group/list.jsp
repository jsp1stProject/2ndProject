<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>그룹리스트</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script> 
  <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
  <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
  <style>
  body {
    background-color: #f6f7f8;
    font-family: 'Segoe UI', sans-serif;
    padding: 20px;
  }

  /* ✅ 전체 너비를 70%로 제한 */
  .custom-container {
    width: 70%;
    margin: 0 auto;
  }

  /* ✅ 일정 패널 높이 조정 */
  .panel-info {
    min-height: 800px; /* 일정 영역 더 길게 */
  }

  .post {
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-bottom: 20px;
    padding: 15px;
  }

  .post-title {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .post-meta {
    color: #999;
    font-size: 12px;
    margin-bottom: 10px;
  }

  .post-body {
    margin-top: 15px;
  }

  .post-footer .btn {
    margin-right: 10px;
    padding: 2px 8px;
    font-size: 12px;
  }

  .carousel {
    margin-top: 15px;
  }

  .carousel-inner > .item > img {
    width: 100%;
    height: 500px;
    object-fit: cover;
    border-radius: 8px;
  }

  .carousel-control.left, .carousel-control.right {
    background-image: none;
    color: #333;
  }

  @media (max-width: 768px) {
    .carousel-inner > .item > img {
      height: 300px;
    }

    .custom-container {
      width: 95%;
    }
  }
</style>
</head>
<body>
<div class="container-fluid custom-container">
  <div class="row">
    
    <!-- 왼쪽 필드 영역 -->
    <div class="col-sm-3">
      <div class="panel panel-info">
        <div class="panel-heading">📅 오늘의 일정</div>
        <div class="panel-body">
          <ul class="list-unstyled">
            <li><strong>그룹에 대한 필터 예정</strong></li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 중앙 피드 영역 -->
    <div class="col-sm-9">
      
      
      <!-- 상단 새 피드 쓰기 버튼 -->
      <div class="text-left" style="margin-bottom: 15px;">
        <button class="btn btn-primary" data-toggle="modal" data-target="#newPostModal">
          <span class="glyphicon glyphicon-pencil"></span> 새 그룹
        </button>
      </div>
      <!--  상단 새 일정 쓰기 버튼 -->
      <!-- <div class="text-right" style="margin-bottom: 15px;">
        <button class="btn btn-primary" data-toggle="modal" data-target="#newPostModal">
          <span class="glyphicon glyphicon-pencil"></span> 일정 추가
        </button>
      </div> -->

	  <div id="newPostModal" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">

      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">새 그룹</h4>
      </div>

      <div class="modal-body">
        <form @submit.prevent="addPost">
          <div class="form-group">
            <label for="title">그룹명</label>
            <input type="text" id="title" class="form-control" v-model="newPost.group_name" required><!--   -->
          </div>

          <div class="form-group">
            <label for="content">그룹설명</label>
            <textarea id="content" class="form-control" rows="4" v-model="newPost.description" required></textarea><!--  -->
          </div>
          
          <div class="form-group">
            <label for="content">최대인원</label>
            <textarea id="content" class="form-control" rows="4" v-model="newPost.capacity" required></textarea><!--  -->
          </div>

          <div class="form-group">
            <label for="images">프로필사진</label>
            <input type="text" id="images" class="form-control" v-model="newPost.profile_img"><!--   -->
          </div>

          <button type="submit" class="btn btn-success">생성하기</button>
        </form>
      </div>

    </div>
  </div>
</div>

      <!-- 게시글 반복 렌더링 -->
    <div class="post" v-for="(vo, index) in list" :key="vo.group_id"> 
        <div class="panel panel-default" style="margin-bottom: 20px;">
        <div class="panel-body" @click="detail(vo.group_id)">
          <div class="row">
            <!-- 그룹 이미지 -->
            <div class="col-sm-2">
              <img :src="vo.profile_img" class="img-responsive img-circle" style="width: 100px; height: 100px; object-fit: cover;">
            </div>
            <!-- 그룹 설명 -->
            <div class="col-sm-10">
              <h3>{{vo.group_name}}</h3>
              <p class="text-muted">{{vo.description}}</p>
              <span class="label label-default">{{vo.is_public}}</span>  <!-- v-if="group.is_public === 'N'" -->
              <p>최대 인원: {{vo.capacitya}}명</p>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</div>
</div>
<script type="module">
  import { createApp, ref } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

  createApp({
	data(){
      return {
         list:[],
		 newPost: {
         			group_name: '',
         			profile_img: '',
         			description: '',
					capacity:''
      			   },
		 
      }
    },
	mounted(){
		this.dataRecv()
	},
    methods:{
		detail(group_id) {
			location.href='../board/reddit.do?group_id='+group_id
		},
		addPost() {
			const res = axios.post('../board/feed_insert.do',{
					title : this.newPost.title,
					content : this.newPost.content,
					images : this.newPost.imageInput
			}).then(response=> {
				console.log("데이터 등록 성공")
				
			})
			
			
		},
		async dataRecv(){
			console.log("dataRecv 실행")
			const res = await axios.get('../board/groups',{
			})
            this.list=res.data.list 
			console.log(res)
		}

	}
  }).mount('.container-fluid')
</script>
</body>
</html>