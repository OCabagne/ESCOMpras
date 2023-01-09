<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="utf-8" %>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Día</th>
			<th>Hora de entrega</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<%--@elvariable id="horarios" type="java.util.List<escompras.model.dto.HoraServicioDTO>"--%>
		<c:forEach var="horario" items="${ horarios }">
			<%@include file="horarioTablaIndividual.jspf"%>
		</c:forEach>
		<c:set var="horario" value="${ null }"/>
		<%@include file="horarioTablaIndividual.jspf"%>
	</tbody>
</table>
