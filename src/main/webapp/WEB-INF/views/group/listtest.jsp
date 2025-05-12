<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <div class="container pt-header">
        <div class="row pt-3">
            <div class="col-lg-4">
                <div class="filter-container">
                    <form action="" name="filterform" method="post">
                    <div class="d-flex justify-content-between pb-2">
                        <button type="button" class="cpsbtn">필터</button>
                        <button type="reset" class="btn btn-light resetbtn">초기화</button>
                    </div>
                        <div class="filter-inner mb-3">
                            <div class="filter-wrap p-3" id="filter">
                                <div class="filter-item"> <!--checkbox 타입-->
                                    <h6>태그</h6>
                                    <div class="checkbtn-wrap">
                                        <input type="checkbox" name="type" id="1">
                                        <label for="1">태그1</label>
                                        <input type="checkbox" name="type" id="2">
                                        <label for="2">태그2</label>
                                    </div>
                                </div>
                                <div class="filter-item"> <!--radio 타입-->
                                    <h6>조건</h6>
                                    <div class="radio-wrap row">
                                        <div class="col-3 col-lg-6">
                                            <input type="radio" name="enddate" value="false" id="enddate1" checked>
                                            <label for="enddate1">조건 1</label>
                                        </div>
                                        <div class="col-3 col-lg-6">
                                            <input type="radio" name="enddate" value="true" id="enddate2">
                                            <label for="enddate2">조건 2</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <h6>조건</h6>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault">
                                        <label class="form-check-label" for="flexSwitchCheckDefault">입장 가능</label>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <button type="button" class="btn btn-light w-100" onclick="filtersubmit();">결과 보기</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="d-flex justify-content-end">
                    <button type="button" class="btn btn-primary mb-2 ">
                        <iconify-icon icon="solar:users-group-rounded-broken" class="fs-6 align-middle"></iconify-icon>
                        새 그룹
                    </button>
                </div>
                <div class="card overflow-hidden">
                    <div class="row g-0">
                        <div class="position-relative col-sm-4 d-none d-sm-block">
                            <a href="javascript:void(0)">
                                <img src="../assets/images/blog/blog-img1.jpg" class="card-img-top h-100" alt="materialM-img" style="object-fit: cover">
                            </a>
                        </div>
                        <div class="card-body col-3 p-3">
                            <div class="d-flex gap-2">
                                <div class="d-flex align-items-center d-sm-none">
                                    <img src="../assets/images/blog/blog-img1.jpg" width="42" height="42" class="rounded-circle fs-1">
                                </div>
                                <div class="">
                                    <span class="badge text-bg-light fs-2 py-1 px-2 lh-sm">태그1</span>
                                    <span class="badge text-bg-light fs-2 py-1 px-2 lh-sm">태그2</span>
                                    <a class="d-block mt-1 mb-2 fs-5 text-dark fw-semibold link-primary" href="">강아지좋아하는사람들 모임</a>
                                </div>
                            </div>
                            <div class="d-flex align-items-center flex-wrap gap-2 mb-2 fs-2">
                                <div class="d-flex align-items-center gap-1">
                                    <span>방장</span><span class="text-dark">test12</span>
                                </div>
                                <div class="d-flex align-items-center gap-1">
                                    <span>참여자</span><span class="text-dark">1/10</span>
                                </div>
                                <div class="d-flex align-items-center gap-1">
                                    <span>개설일</span><span class="text-dark">2024.05.30</span>
                                </div>
                            </div>
                            <p class="mb-3">강아지 좋아하는 사람들 다 모이세요!! 서로 정보도 공유하고 품앗이도 가능한 방 입니다</p>
                            <div class="d-flex justify-content-end fs-3">
                                <button type="button" class="btn btn-outline-dark">입장하기</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card overflow-hidden">
                    <div class="row g-0">
                        <div class="position-relative col-sm-4 d-none d-sm-block">
                            <a href="javascript:void(0)">
                                <img src="../assets/images/blog/blog-img2.jpg" class="card-img-top h-100" alt="materialM-img" style="object-fit: cover">
                            </a>
                        </div>
                        <div class="card-body col-3 p-3">
                            <div class="d-flex gap-2">
                                <div class="d-flex align-items-center d-sm-none">
                                    <img src="../assets/images/blog/blog-img2.jpg" width="42" height="42" class="rounded-circle fs-1">
                                </div>
                                <div class="">
                                    <span class="badge text-bg-light fs-2 py-1 px-2 lh-sm">태그1</span>
                                    <span class="badge text-bg-light fs-2 py-1 px-2 lh-sm">태그2</span>
                                    <a class="d-block mt-1 mb-2 fs-5 text-dark fw-semibold link-primary" href="">강아지좋아하는사람들 모임</a>
                                </div>
                            </div>
                            <div class="d-flex align-items-center flex-wrap gap-2 mb-2 fs-2">
                                <div class="d-flex align-items-center gap-1">
                                    <span>방장</span><span class="text-dark">test12</span>
                                </div>
                                <div class="d-flex align-items-center gap-1">
                                    <span>참여자</span><span class="text-dark">1/10</span>
                                </div>
                                <div class="d-flex align-items-center gap-1">
                                    <span>개설일</span><span class="text-dark">2024.05.30</span>
                                </div>
                            </div>
                            <p class="mb-3">강아지 좋아하는 사람들 다 모이세요!! 서로 정보도 공유하고 품앗이도 가능한 방 입니다</p>
                            <div class="d-flex justify-content-end fs-3">
                                <button type="button" class="btn btn-outline-warning" disabled>가입 대기 중</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script>
    $(document).on("click",".cpsbtn",function(){
        var con=$(this).closest('.filter-container')
        if(con.hasClass('active')){
            con.removeClass('active');
        }else{
            con.addClass('active');
        }
    });
</script>