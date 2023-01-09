<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>ESCOMpras - Ver pedido</title>
	<escompras:lib noPopper="true" estadoLib="true"/>
	<script src="lib/verPedido.js"></script>
</head>
<%--@elvariable id="tipoUsuario" type="java.lang.String"--%>
<c:set var="isCliente" value='${ tipoUsuario == "cliente" }' scope="page" />
<body>
	<escompras:container>
		<h2 class="fw-bold text-center py-3">
			Ver Pedido
		</h2>
		<%--@elvariable id="tienda" type="escompras.model.entity.Tienda"--%>
		<%--@elvariable id="cliente" type="escompras.model.entity.Cliente"--%>
		<div class="col-sm-10 col-md-6 container-lg">
			<h2 class="fw-bold text-center py-3">
				${ isCliente ? "Tienda" : "Cliente" }
			</h2>
			<div class="card mt-4">
				<div class="row">
					<div class="col-md-4 py-3">
						<img class="img-fluid"
							src="Imagen/${ isCliente ? 'tienda' : 'cliente'}/?id=
							${ isCliente ? tienda.idTienda : cliente.idCliente }"
							alt="${ isCliente ? tienda.nombre : cliente.nombre }"
						>
					</div>
					<div class="col-md-8 card-body ms-sm-2 ms-md-0">
						<h5 class="card-title">
							${ isCliente ? cliente.nombre : tienda.nombre }
						</h5>
						<c:if test="${ isCliente }">
							<div class="card-text mt-3">
								Tipo: ${ tienda.tipo.nombre }.
							</div>
						</c:if>
						<div class="card-text mt-2">
							Correo: ${ isCliente ? tienda.correo : cliente.correo }.
						</div>
						<c:if test="${ isCliente }">
							<div class="card-text mt-2">
								Ubicación: ${ tienda.ubicacion }.
							</div>
						</c:if>
						<c:if test="${ not isCliente }">
							<div class="card-text mt-2">
								Escuela: ${ cliente.escuela.nombre }.
							</div>
						</c:if>
					</div>
				</div>
			</div>
			<%--@elvariable id="comentarios" type="java.util.List<escompras.model.dto.ComentarioDTO>"--%>
			<c:if test="${ isCliente }">
				<h4 class="fw-bold text-center mt-4 mb-3">Comentarios</h4>
				<c:if test="${ not empty comentarios }">
					<div class="card overflow-auto px-3 pt-3" style="max-height: 250px"
					     id="comentarios">
						<c:forEach var="comentario" items="${ comentarios }">
							<div class="row">
								<b class="col-3">${ comentario.cliente }:</b>
								<p class="col-9">${ comentario.contenido }</p>
							</div>
						</c:forEach>
					</div>
				</c:if>
				<div class="input-group">
					<input type="text" id="comentario" maxlength="250"
					       class="form-control rounded" placeholder="Comentario"
					>
					<button class="btn btn-outline-secondary" onclick="Comentar('${ tienda.nombre }')"
						id="comentarioBtn"
					>
						Comentar
					</button>
				</div>
			</c:if>
			<%--@elvariable id="reportes" type="java.util.List<String>"--%>
			<c:if test="${ not isCliente }">
				<h3 class="fw-bold text-center mt-4 mb-3">Reportes al cliente</h3>
				<c:if test="${ not empty reportes }">
					<div class="card overflow-auto px-3 pt-3" style="max-height: 250px" id="reportes">
						<c:forEach var="reporte" items="${ reportes }">
							<div class="row">
								<p class="col-9">${ reporte }</p>
							</div>
						</c:forEach>
					</div>
				</c:if>
				<div class="input-group">
					<input type="text" id="reporte" maxlength="250"
					       class="form-control rounded" placeholder="Comentario"
					>
					<button class="btn btn-outline-secondary" id="reporteBtn"
						onclick="Reportar('${ cliente.idCliente }')"
					>
						Reportar
					</button>
				</div>
			</c:if>
		</div><%-- Datos de tienda --%>
		<div class="col-sm-10 col-md-6" id="vertical-line">
			<h2 class="fw-bold text-center py-3">
				Detalles
			</h2>
			<%--@elvariable id="pedido" type="escompras.model.entity.Orden"--%>
			<%--@elvariable id="tipoUsuario" type="java.lang.String"--%>
			<div class="container mt-4">
				<p>
					<strong>Fecha: </strong>${ pedido.fecha }<br>
					<c:if test="${ isCliente }">
						<strong>Tienda: </strong>${ tienda.nombre }<br>
					</c:if>
					<c:if test="${ not isCliente }">
						<strong>Cliente: </strong>${ cliente.nombre }<br>
					</c:if>
					<strong>Hora de entrega: </strong>${ pedido.horaEntrega }<br>
					<strong>Estado: </strong>
					<escompras:estado
						pedidoId="${ pedido.idOrden }"
						estado="${ pedido.estado.toString() }"
						isCliente='${ tipoUsuario == "cliente" }'
					/><br>
					<%--@elvariable id="clavePedido" type="java.lang.String"--%>
					<c:if test="${ not empty clavePedido and isCliente }">
						<strong>Clave:</strong> ${ clavePedido }<br>
					</c:if>
					<strong>Productos solicitados: </strong><br>
				</p>
				<%--@elvariable id="productos" type="java.util.List<escompras.model.entity.Compra>"--%>
				<table class="table table-striped mb-4 w-100">
					<thead>
						<tr>
							<th>Producto</th>
							<th>Cantidad</th>
							<th>Precio</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="producto" items="${ productos }">
							<tr>
								<td>${ producto.nombreProducto }</td>
								<td>${ producto.cantidad }</td>
								<td>
									${ producto.cantidad
									* producto.precioProducto / 100 }
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<p>
					<strong>Total: </strong><u>$ ${ pedido.montoTotal / 100 }</u><br>
					<strong>Escuela de entrega: </strong>${ pedido.escuela.nombre }<br>
				</p>
				<%--@elvariable id="pedirClave" type="java.lang.boolean"--%>
				<c:if test="${ pedirClave }">
					<div class="input-group">
						<input type="text" class="form-control" id="clavePedido"
						       placeholder="Clave de envío"
						>
						<button class="btn btn-success"
							onclick="EnviarClave('${ pedido.idOrden }')">
							Enviar
						</button>
					</div>
				</c:if>
			</div>
		</div>
	</escompras:container>
</body>
</html>