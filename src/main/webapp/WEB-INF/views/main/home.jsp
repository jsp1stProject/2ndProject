<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
	<div class="container-fluid p-0 pb-4 wow fadeIn" data-wow-delay="0.1s">
		<div class="owl-carousel-inner">
            <div class="container">
                <div class="row justify-content-start">
                    <div class="col-12">
						<p class="display-4 text-light mb-4 slideInDown">여행가기 좋은 계절,<br><span class="impact">Festeller</span>와 함께 가요</p>
                        <p class="text-light fs-5 mb-4 pb-3">봄맞이 최대 30% 할인 쿠폰 이벤트</p>
                        <a href="" class="btn btn-primary rounded-pill py-3 px-5">자세히 보기</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="owl-carousel header-carousel position-relative">
            <div class="owl-carousel-item position-relative">
            	<div class="backimg" style="background-image:url('${pageContext.request.contextPath }/assets/img/main_visual_21.png')"></div>
            </div>
            <div class="owl-carousel-item position-relative">
            	<div class="backimg" style="background-image:url('${pageContext.request.contextPath }/assets/img/main_visual_22.png')"></div>
            </div>
            <div class="owl-carousel-item position-relative">
            	<div class="backimg" style="background-image:url('${pageContext.request.contextPath }/assets/img/main_visual_23.png')"></div>
            </div>
        </div>
    </div>
    <!-- Carousel End -->

</body>
</html>