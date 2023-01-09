<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="utf-8" %>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Cantidad</th>
			<th>Nombre</th>
			<th>Precio</th>
			<th>Unidad</th>
			<th>Descripción</th>
			<th>Promoción</th>
			<th>Imagen</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<%--@elvariable id="productos" type="java.util.List<escompras.model.dto.ProductoDTO>"--%>
		<c:forEach var="producto" items="${ productos }">
			<%@include file="productosTablaIndividual.jspf"%>
		</c:forEach>
		<c:set var="producto" value="${ null }"/>
		<%@include file="productosTablaIndividual.jspf"%>
	</tbody>
</table>