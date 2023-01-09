<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Lista de pedidos</title>
	<escompras:lib noPopper="true"/>
	<script src="lib/pedido.js"></script>
</head>
<%--@elvariable id="tipoUsuario" type="java.lang.String"--%>
<c:set var="isCliente" value='${ tipoUsuario == "cliente" }' scope="page" />
<body onload="RequestPedidos(${ isCliente })">
	<escompras:container>
		<table class="table table-striped">
			<thead>
				<tr>
					<td>
						<c:if test='${ isCliente }'>Tienda</c:if>
						<c:if test='${ not isCliente }'>Cliente</c:if>
					</td>
					<td>Fecha de compra</td>
					<td>Escuela</td>
					<td>Hora de entrega</td>
					<td>Total</td>
					<td>Estado</td>
					<td></td>
				</tr>
			</thead>
			<tbody id="tBody"></tbody>
		</table>
		<div id="productos"></div>
	</escompras:container>
	<template id="pedidoTemplate">
		<tr>
			<td id="pedidoUsuario"></td>
			<td id="pedidoFecha"></td>
			<td id="pedidoEscuela"></td>
			<td id="pedidoTotal"></td>
			<td id="horaEntrega"></td>
			<td id="pedidoEstado"></td>
			<td id="pedidoVer" class="py-1">
				<a  class="btn btn-info">Ver</a>
			</td>
		</tr>
	</template>
</body>