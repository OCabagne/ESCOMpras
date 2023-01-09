<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<title>Lista de productos</title>
	<escompras:lib noPopper="true"/>
	<script src="lib/productosEnTienda.js"></script>
</head>
<body onload="LoadProductos()">
	<escompras:container>
		<h2 class="fw-bold text-center">Productos registrados</h2>
		<h3 class="fw-bold text-center pb-4">Tienda ${ sessionScope.usuario }</h3>
		<div class="mb-4 px-2 px-sm-3 px-md-4" id="productos"></div>
	</escompras:container>
</body>
</html>
