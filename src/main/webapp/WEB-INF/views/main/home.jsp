<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="${pageContext.request.contextPath}/assets/libs/swiper/swiper-bundle.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/assets/libs/swiper/swiper-bundle.css" />
<div class="container-fluid p-0 pb-4 wow fadeIn" data-wow-delay="0.1s">
    <div class="swiper-wrapper">
        <div class="swiper-slide py-5"  style="background-image:url('${pageContext.request.contextPath }/assets/images/main/main_vis1.jpg')">
            <div class="container py-5">
                <div class="row justify-content-start">
                    <div class="col-12">
                        <p class="display-4 text-light slideInDown">PET4U</p>
                        <p class="text-light fs-5 mb-4 pb-3">봄맞이 최대 30% 할인 쿠폰 이벤트</p>
                        <a href="" class="btn btn-primary rounded-pill py-3 px-5">자세히 보기</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="swiper-slide py-5"  style="background-image:url('${pageContext.request.contextPath }/assets/images/main/main_vis1.jpg')">
            <div class="container">
                <div class="row justify-content-start">
                    <div class="col-12">
                        <p class="display-4 text-light mb-4 slideInDown">PET4U</p>
                        <p class="text-light fs-5 mb-4 pb-3">봄맞이 최대 30% 할인 쿠폰 이벤트</p>
                        <a href="" class="btn btn-primary rounded-pill py-3 px-5">자세히 보기</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    //custom-list
    const swiper = new Swiper('.container-fluid', {
        loop: true,
        navigation: false,
        slidePerView:1,
        autoplay: 3000,
    });

</script>
<!-- Carousel End -->