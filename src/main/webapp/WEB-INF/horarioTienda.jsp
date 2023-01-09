<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>ESCOMpras - Horarios</title>
	<escompras:lib noPopper="true"/>
	<script src="lib/horarioTienda.js"></script>
</head>
<body>
	<escompras:container>
		<h2 class="fw-bold text-center">Horarios de entrega</h2>
		<h3 class="fw-bold text-center pb-4">Tienda ${ sessionScope.usuario }</h3>
		<div class="form-row align-items-center mb-4 d-flex">
			<label for="escuela" class="d-flex me-1">Escuela:</label>
			<select id="escuela" class="btn btn-secondary flex-fill" onchange="LoadHorarios()">
				<option value="0" disabled selected>Elija una escuela</option>
				<%--@elvariable id="escuelas" type="java.util.List"--%>
				<c:forEach var="escuela" items="${ escuelas }">
					<option>${escuela}</option>
				</c:forEach>
			</select>
		</div><!-- Escuela -->
		<div class="mb-4 px-3 px-sm-4 px-md-5" id="horarios"></div><!-- Datos -->
	</escompras:container>
</body>
</html>