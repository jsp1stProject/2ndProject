<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <div class="container pt-header">
        <div class="row pt-3">
            <div class="col-lg-4">
                <div class="card w-100">
                    <div class="card-body accordion">
                        <div class="d-flex justify-content-between align-items-center">
                            <button class="mb-0 card-title accordion-button w-auto p-0 inline-accordion" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                <span class="me-2">그룹 일정</span>
                            </button>
                            <div class="dropdown">
                                <button id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false" class="rounded-circle btn-transparent rounded-circle btn-sm px-1 btn shadow-none">
                                    <i class="ti ti-dots-vertical fs-6"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton1">
                                    <li><a class="dropdown-item" href="javascript:void(0)">일정 추가</a></li>
                                </ul>
                            </div>
                        </div>
                        <ul class="list-unstyled mb-0accordion-collapse collapse show" id="collapseOne">
                            <li class="py-10 border-bottom">
                                <h6 class="mb-1 fs-3">초코 산책 품앗이</h6>
                                <div class="fs-2 d-flex gap-2">
                                    <div class="d-flex align-items-center gap-1">
                                        <iconify-icon icon="solar:clock-circle-broken" class="fs-4 text-primary"></iconify-icon>
                                        <span>2025.05.12</span>
                                    </div>

                                    <div class="d-flex align-items-center gap-1">
                                        <iconify-icon icon="solar:users-group-rounded-broken" class="fs-4 text-primary"></iconify-icon>
                                        <span>유저1, 유저2</span>
                                    </div>
                                </div>
                            </li>
                            <li class="py-10 border-bottom">
                                <h6 class="mb-1 fs-3">그룹 정모</h6>
                                <div class="fs-2 d-flex gap-2">
                                    <div class="d-flex align-items-center gap-1">
                                        <iconify-icon icon="solar:clock-circle-broken" class="fs-4 text-primary"></iconify-icon>
                                        <span>2025.05.12</span>
                                    </div>

                                    <div class="d-flex align-items-center gap-1">
                                        <iconify-icon icon="solar:users-group-rounded-broken" class="fs-4 text-primary"></iconify-icon>
                                        <span>유저1, 유저2, 유저3</span>
                                    </div>
                                </div>
                            </li><li class="py-10 border-bottom">
                            <h6 class="mb-1 fs-3">초코 산책 품앗이</h6>
                            <div class="fs-2 d-flex gap-2">
                                <div class="d-flex align-items-center gap-1">
                                    <iconify-icon icon="solar:clock-circle-broken" class="fs-4 text-primary"></iconify-icon>
                                    <span>2025.05.12</span>
                                </div>

                                <div class="d-flex align-items-center gap-1">
                                    <iconify-icon icon="solar:users-group-rounded-broken" class="fs-4 text-primary"></iconify-icon>
                                    <span>유저1, 유저2</span>
                                </div>
                            </div>
                        </li>
                            <a href="javascript:void(0)" class="fs-3 mt-3 text-center d-block">더보기</a>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="d-flex justify-content-end">
                    <button type="button" class="btn btn-primary mb-2">
                        <iconify-icon icon="solar:pen-2-broken" class="fs-6 align-middle"></iconify-icon>
                        새 글
                    </button>
                </div>
                <div class="card overflow-hidden">
                    <div class="row g-0">
                        <div class="card-body col-3 p-3">
                            <div class="d-flex gap-2">
                                <div class="d-flex align-items-center">
                                    <img src="../assets/images/profile/user-2.jpg" width="42" height="42" class="rounded-circle fs-1">
                                </div>
                                <div class="">
                                    <a class="d-block fs-4 text-dark fw-semibold link-primary" href="">다음주 금요일에 돌봄 가능하신 분!</a>
                                    <div class="d-flex align-items-center flex-wrap gap-2 fs-3">
                                        <div class="d-flex align-items-center gap-1">
                                            <span>작성자</span><span class="text-dark">test12</span>
                                        </div>
                                        <div class="d-flex align-items-center gap-1">
                                            <span>작성일</span><span class="text-dark">2024.05.30</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="feed-content my-3">
                                <p>강아지 좋아하는 사람들 다 모이세요!! 서로 정보도 공유하고 품앗이도 가능한 방 입니다</p>
                                <p>강아지 좋아하는 사람들 다 모이세요!! 서로 정보도 공유하고 품앗이도 가능한 방 입니다</p>
                            </div>
                            <div class="d-flex justify-content-end fs-3">
                                <button type="button" class="btn btn-link">더보기</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card overflow-hidden">
                    <div class="row g-0">
                        <div class="card-body col-3 p-3">
                            <div class="d-flex gap-2">
                                <div class="d-flex align-items-center">
                                    <img src="../assets/images/profile/user-2.jpg" width="42" height="42" class="rounded-circle fs-1">
                                </div>
                                <div class="">
                                    <a class="d-block fs-4 text-dark fw-semibold link-primary" href="">다음주 금요일에 돌봄 가능하신 분!</a>
                                    <div class="d-flex align-items-center flex-wrap gap-2 fs-3">
                                        <div class="d-flex align-items-center gap-1">
                                            <span>작성자</span><span class="text-dark">test12</span>
                                        </div>
                                        <div class="d-flex align-items-center gap-1">
                                            <span>작성일</span><span class="text-dark">2024.05.30</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="feed-content my-3">
                                <p>강아지 좋아하는 사람들 다 모이세요!! 서로 정보도 공유하고 품앗이도 가능한 방 입니다</p>
                                <p>강아지 좋아하는 사람들 다 모이세요!! 서로 정보도 공유하고 품앗이도 가능한 방 입니다</p>
                            </div>
                            <div class="d-flex justify-content-end fs-3">
                                <button type="button" class="btn btn-link">더보기</button>
                            </div>
                        </div>
                        <img src="../assets/images/blog/blog-img2.jpg" class="card-img-bottom h-100" alt="materialM-img" style="object-fit: cover">
                    </div>
                </div>
            </div>
        </div>
    </div>