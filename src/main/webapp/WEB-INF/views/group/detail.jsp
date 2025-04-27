<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Reddit 스타일 게시글 + 큰 이미지 슬라이드</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
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

.carousel-inner>.item>img {
	width: 100%;
	height: 500px;
	object-fit: cover;
	border-radius: 8px;
}

.carousel-control.left, .carousel-control.right {
	background-image: none;
	color: #333;
}

.preview-img {
  max-width: 100px;
  max-height: 100px;
  margin: 5px;
  border-radius: 6px;
  object-fit: cover;
}

@media ( max-width : 768px) {
	.carousel-inner>.item>img {
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

			<!-- 왼쪽 일정 영역 -->
			<div class="col-sm-3">
				<div class="panel panel-info">
					<div class="panel-heading">📅 오늘의 일정</div>
					<div class="panel-body">
						<ul class="list-unstyled">
							<li><strong>10:00</strong> 회의</li>
							<li><strong>13:30</strong> 디자인 피드백</li>
							<li><strong>16:00</strong> 개발 회의</li>
						</ul>
					</div>
				</div>
			</div>

			<!-- 중앙 피드 영역 -->
			<div class="col-sm-9">

				<div class="panel panel-default" style="margin-bottom: 20px;">
					<div class="panel-body">
						<div class="row">
							<!-- 그룹 이미지 -->
							<div class="col-sm-2">
								<img
									src="data:image/jpeg;base64,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"
									class="img-responsive img-circle"
									style="width: 100px; height: 100px; object-fit: cover;">
							</div>
							<!-- 그룹 설명 -->
							<div class="col-sm-10">
								<h3>{{ gvo.group_name }}</h3>
								<p class="text-muted">{{ gvo.description }}</p>
								<span class="label label-default">비공개 그룹</span>
								<!-- v-if="group.is_public === 'N'" -->
								<p>최대 인원: {{gvo.capacity}}명</p>
							</div>
						</div>
					</div>
				</div>
				<!-- 상단 새 피드 쓰기 버튼 -->
				<div class="text-left" style="margin-bottom: 15px;">
					<button class="btn btn-primary" data-toggle="modal"
						data-target="#newPostModal">
						<span class="glyphicon glyphicon-pencil"></span> 새 글 쓰기
					</button>
				</div>
				<!--  상단 새 일정 쓰기 버튼 -->
				<!-- <div class="text-right" style="margin-bottom: 15px;">
        <button class="btn btn-primary" data-toggle="modal" data-target="#newPostModal">
          <span class="glyphicon glyphicon-pencil"></span> 일정 추가
        </button>
      </div> -->

				<div id="newPostModal" class="modal fade" tabindex="-1"
					role="dialog">
					<div class="modal-dialog">
						<div class="modal-content">

							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title">새 글 작성</h4>
							</div>

							<div class="modal-body">
								<form @submit.prevent="addPost">
									<div class="mb-3">
									    <label class="form-label">제목</label>
									    <input type="text" class="form-control" v-model="newPost.title" required>
									  </div>
									
									  <div class="mb-3">
									    <label class="form-label">내용</label>
									    <textarea class="form-control" rows="4" v-model="newPost.content" required></textarea>
									  </div>
									
									<!-- 파일 선택 -->
									<div class="mb-3">
									    <label class="form-label">이미지 첨부</label>
									    <input type="file" multiple @change="handleFileChange" accept="image/*" />
									  </div>
										
									<!-- 미리보기부분 -->
									<div class="row">
									    <div class="col-xs-3" v-for="(preview, index) in imagePreviews" :key="index">
									      <img :src="preview" class="img-thumbnail img-fluid" style="max-height: 120px;">
									    </div>
									  </div>
									
									  <button type="submit" class="btn btn-primary mt-3">게시하기</button>
									</form>
							</div>

						</div>
					</div>
				</div>

				<!-- 게시글 반복 렌더링 -->
				<div class="post" v-for="(vo, index) in list" :key="vo.feed_no">
					<div class="post-title">{{ vo.title }}</div>
					<div class="post-meta">{{ vo.regdate }} vo.regdate</div>

					이미지 슬라이드
					<!--  <div :id="'carousel-' + index" class="carousel slide" data-ride="carousel">
          <ol class="carousel-indicators">
            <li  :data-target="'#carousel-' + index" :data-slide-to="imgIndex" :class="{ active: imgIndex === 0 }"></li>
          </ol>
          <div class="carousel-inner">
            <div class="item" v-for="(image, imgIndex) in post.images" :class="{ active: imgIndex === 0 }">
              <img src="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzAyMDhfMTg2%2FMDAxNjc1ODI1NDU4ODAy.r-OTedxUBE0S-kO_cmPOp1RARfs_DyOsuhMFsJiSu5Ag.lcSPhivCsVpE0p3GiqYndeHF5c5I_hoXZv3W0luTFUMg.JPEG.gywls55566%2F694D01C0-B082-47B3-882C-91CB1421A4C0.jpg&type=ofullfill340_600_png" class="img-responsive" style="width: 100%; height: 500px; object-fit: cover;">
            </div>
          </div>
          <a class="left carousel-control" :href="'#carousel-' + index" data-slide="prev">
            <span class="glyphicon glyphicon-chevron-left"></span>
          </a>
          <a class="right carousel-control" :href="'#carousel-' + index" data-slide="next">
            <span class="glyphicon glyphicon-chevron-right"></span>
          </a>
        </div> -->
					<div class="row g-2 mt-2" v-if="vo.images && vo.images.length">
					  <div class="col-6 col-md-4 col-lg-3" v-for="(img, imgIdx) in vo.images" :key="imgIdx">
					    <img :src="'/images/' + img" class="img-fluid rounded border" style="height: 200px; object-fit: cover;">
					  </div>
					</div>
					<div class="post-body" style="margin-top: 10px;">{{
						vo.content }}</div>

					<div class="post-footer">
						<button class="btn btn-default btn-sm">
							<span class="glyphicon glyphicon-comment"></span> 댓글
						</button>
						<button class="btn btn-default btn-sm">
							<span class="glyphicon glyphicon-share"></span> 공유
						</button>
						<button class="btn btn-default btn-sm">
							<span class="glyphicon glyphicon-star"></span> 저장
						</button>
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
		 gvo:{},
		 group_no:1,
		 
		 newPost: {
         			title: '',
         			content: '',
      			   },
		 selectedFiles: [],
    	 imagePreviews: []
		 
      }
    },
	mounted(){
		const params = new URLSearchParams(window.location.search);
  const groupNoParam = params.get('group_no');

  if (groupNoParam) {
    this.group_no = parseInt(groupNoParam);
  }
		this.dataRecv()
	},
    methods:{
		handleFileChange(event) {
  		  const files = Array.from(event.target.files);
  		  this.selectedFiles = files;
  		  this.imagePreviews = [];

  		  files.forEach(file => {
   		   const reader = new FileReader();
  	 	   reader.onload = (e) => {
   		     this.imagePreviews.push(e.target.result);
   		   };
   		   reader.readAsDataURL(file);
   		 	});
 		 },

		resetForm() {
	    this.newPost.title = '';
  		this.newPost.content = '';
   	 	this.selectedFiles = [];
   	 	this.imagePreviews = [];
  		},
		addPost() {
			const formData = new FormData();
 			formData.append('title', this.newPost.title);
  			formData.append('content', this.newPost.content);
  			this.selectedFiles.forEach(file => {
    			formData.append('files', file);
  			});

  			axios.post('../board/feed_insert.do', formData, {
    			headers: {
      			'Content-Type': 'multipart/form-data'
    			}
  			}).then(response => {
    			alert("게시글이 등록되었습니다!");
    			this.resetForm();
    			this.dataRecv();
  			});		    

			/*
			console.log("등록하는 파일들"+this.newPost.files)
			const res = axios.post('../board/feed_insert.do',{
					title : this.newPost.title,
					content : this.newPost.content,
					files : this.newPost.files

			}).then(response=> {
				console.log("데이터 등록 성공")
				
			})
			*/
			
		},
		async dataRecv(){
			console.log("dataRecv 실행")
			console.log(this.group_no)
			const res = await axios.get('../group/feeds',{
					params:{
							group_no:this.group_no
					}
			})
            this.list=res.data.list
			this.gvo=res.data.gvo
			console.log(res)
		}

	}
  }).mount('.container-fluid')
</script>
	
</body>

<!-- <script type="text/javascript">
		let curImageCount = 0; //현재 들어온 이미지 수
		/*
			"file" multiple은 여러개 파일을 받을 순 있는데 
			한 번에 여러개를 선택해야 여러개가 들어오고 하나씩 선택하면 누적되지 않아
			
			file 내부적으로 FileList라는 걸 가지고 있는데 이 리스트에 0,1,2,번에 저장되는 방식이야
			예를들어 사진1,사진2,사진3을 한 번에 선택하면{0:사진1, 1:사진2, 2:사진3} 이런식으로 저장된다
					사진1,2,3을 따로 올리면 {0:사진1},  {0:사진2} 이런식으로 초기화되서 다시 들어와
				
			그래서 이미지 정보를 담고 있는 누적 이미지수와 배열을 따로 선언하는 것 
			*/
		let imgSubmitOn = false; // submit버튼 제어를 위한 이미지 등록 여부 체크 변수
		let fileArray = []; //등록된 이미지 저장 배열

		$(function() {
			const upload = document.getElementById("upload");
			//이미지
			const imagePreviews = document.getElementById("imagePreviews");
			const uploadUL = document.getElementById("forUpload-ul");
			let fileArrayIndex = 0; //등록된 사진을 한 장씩 삭제하기 위해서 개별 파일에 인덱스 부여하기 위해 선언
			let maxImageCount = 4; //최대 등록 파일 개수설정

			upload.addEventListener("change", function() {
				console.log("사진업로드버튼 클릭?")
				let files = this.files; 

				curImageCount += files.length;

				if (curImageCount > maxImageCount) {
					curImageCount = uploadUL.childElementCount;
					alert(maxImageCount + '개까지만 등록 가능합니다.');
					return;
				}
				if (uploadUL.childElementCount > maxImageCount - 1) {
					alert(maxImageCount + '개까지만 등록 가능합니다.');
					return;
				}
				for (let i = 0; i < files.length; i++) {
					let file = files[i];
					let reader = new FileReader();
					reader.readAsDataURL(file);

					reader.onload = function() {
						uploadUL.style.display = "";
						let imageSrc = reader.result;

						let imgLi = document.createElement("li");
						uploadUL.appendChild(imgLi);

						let selectedImg = document.createElement("img");
						selectedImg.src = imageSrc;
						selectedImg.className = "preview-img";
						imgLi.appendChild(selectedImg);

						let delBtn = document.createElement("input");
						delBtn.type = "button";
						delBtn.className = "preview-del-btn";
						delBtn.value = "X";
						delBtn.dataset.fileIndex = fileArrayIndex;
						fileArrayIndex++;
						imgLi.appendChild(delBtn);

						fileArray.push(file);
					}
				}
				checkImg();
			});
			
			
				imagePreviews.addEventListener("click", function(event) {
					if (event.target.classList.contains("preview-del-btn")) {
						var fileIndex = event.target.dataset.fileIndex;
						var imagePreview = event.target
								.closest("#forUpload-ul > li");
						imagePreview.remove();
						fileArray.splice(fileIndex, 1);
						fileArrayIndex--;
						curImageCount--;
						checkImg();
					}
				});
			
			function checkImg() {
				if (curImageCount > 0) {
					imgSubmitOn = true;
				} else {
					imgSubmitOn = false;
				}
				if (imgSubmitOn) {  /* titleSubmitOn &&  */
					$('#gallery_submit').css('background-color', 'coral');
					$('#gallery_submit').attr("disabled", false);
				} else {
					$('#gallery_submit').css('background-color', '#ccc');
					$('#gallery_submit').attr("disabled", true);
				}
			}
		});
	</script> -->
	
</html>