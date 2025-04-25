<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Festeller</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport">
	<meta content="" name="keywords">
	<meta content="" name="description">

	<link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath }/assets/images/logos/favicon.png" />
	<link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/styles.css" />
	
	<!-- Libraries Stylesheet -->
<%--	<link href="${pageContext.request.contextPath }/assets/plugin/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">--%>
<%--	<link href="${pageContext.request.contextPath }/assets/plugin/swiper/swiper-bundle.css" rel="stylesheet">--%>

<%--	<script src="${pageContext.request.contextPath }/assets/plugin/swiper/swiper-bundle.min.js"></script>--%>

<%--	<script src="${pageContext.request.contextPath }/assets/plugin/wow/wow.min.js"></script>--%>
	<script src="${pageContext.request.contextPath }/assets/libs/jquery/dist/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath }/assets/libs/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath }/assets/js/sidebarmenu.js"></script>
	<script src="${pageContext.request.contextPath }/assets/js/app.min.js"></script>
	<script src="${pageContext.request.contextPath }/assets/libs/apexcharts/dist/apexcharts.min.js"></script>
	<script src="${pageContext.request.contextPath }/assets/libs/simplebar/dist/simplebar.js"></script>
	<script src="${pageContext.request.contextPath }/assets/js/dashboard.js"></script>
	<!-- solar icons -->
	<script src="https://cdn.jsdelivr.net/npm/iconify-icon@1.0.8/dist/iconify-icon.min.js"></script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<jsp:include page="${main_jsp }"></jsp:include>
	<jsp:include page="footer.jsp"></jsp:include>



</body>
</html>