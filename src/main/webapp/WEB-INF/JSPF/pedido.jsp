<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="table table-striped px-0">
	<thead>
		<tr>
			<td>Producto</td>
			<td>Cantidad</td>
			<td>Detalles</td>
		</tr>
	</thead>
	<tbody>
		<%--@elvariable id="productos" type="java.util.List"--%>
		<%--@elvariable id="producto" type="escompras.model.dto.CompraDTO"--%>
		<c:forEach var="producto" items="${ productos }">
			<tr>
				<td>${ producto.producto }</td>
				<td>${ producto.cantidad }</td>
				<td>${ producto.detalles }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>