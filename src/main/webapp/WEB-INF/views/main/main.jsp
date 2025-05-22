<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>펫포유</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport">
	<meta content="" name="keywords">
	<meta content="" name="description">

	<link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath }/assets/images/logos/favicon.png" />
	<link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/styles.css" />

	<script src="${pageContext.request.contextPath }/assets/libs/jquery/dist/jquery.js"></script>
	<script src="${pageContext.request.contextPath }/assets/libs/bootstrap/dist/js/bootstrap.bundle.js"></script>
	<script src="${pageContext.request.contextPath }/assets/js/sidebarmenu.js"></script>
	<script src="${pageContext.request.contextPath }/assets/js/app.min.js"></script>
	<script src="${pageContext.request.contextPath }/assets/libs/apexcharts/dist/apexcharts.js"></script>
	<script src="${pageContext.request.contextPath }/assets/libs/simplebar/dist/simplebar.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

	<script src="https://cdn.jsdelivr.net/npm/iconify-icon@1.0.8/dist/iconify-icon.min.js"></script>

	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
	<script>
		function toast(msg) {
			Toastify({
				text: msg,
				duration: 3000,
				newWindow: true,
				close: true,
				gravity: "bottom", // `top` or `bottom`
				position: "right", // `left`, `center` or `right`
				stopOnFocus: true, // Prevents dismissing of toast on hover
				style: {
					background: "linear-gradient(to right, #00b09b, #96c93d)",
				},
				onClick: function () {
				} // Callback after click
			}).showToast();
		}
	</script>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="${main_jsp }"></jsp:include>
<jsp:include page="footer.jsp"></jsp:include>



</body>
</html>