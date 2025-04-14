<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>
<a href="/assets/index.html"></a>
<a href="./assets/index.html"></a>
<a href="../assets/index.html"></a>
<a href="assets/index.html"></a>
<P>  The time on the server is ${serverTime}. ${test }, ${test.name }</P>
</body>
</html>
