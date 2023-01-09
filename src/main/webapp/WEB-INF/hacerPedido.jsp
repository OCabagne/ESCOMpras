<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
	<title>ESCOMpras - Hacer Pedido</title>
	<escompras:lib noPopper="true"/>
	<script src="lib/hacerPedido.js"></script>
</head>
<%--@elvariable id="productoId" type="java.lang.Integer"--%>
<body onload="AddProducto('${productoId}')">
	<escompras:container>
		<h2 class="fw-bold text-center py-3">
			Realizar Pedido
		</h2>
		<%--@elvariable id="tienda" type="escompras.model.entity.Tienda"--%>
		<div class="col-sm-10 col-md-6 container-lg">
			<h2 class="fw-bold text-center py-3">
				Tienda
			</h2>
			<div class="card mt-4">
				<div class="row">
					<div class="col-md-4 py-3">
						<img src="Imagen/tienda?id=${ tienda.idTienda }"
						     class="img-fluid" alt="${ tienda.nombre }">
					</div>
					<div class="col-md-8 card-body ms-sm-2 ms-md-0">
						<h5 class="card-title">
							${ tienda.nombre }
						</h5>
						<div class="card-text mt-3">
							Tipo: ${ tienda.tipo.nombre }.
						</div>
						<div class="card-text mt-2">
							Correo: ${ tienda.correo }.
						</div>
						<div class="card-text mt-2">
							Ubicación: ${ tienda.ubicacion }.
						</div>
					</div>
				</div>
			</div>
			<%--@elvariable id="comentarios" type="java.util.List<escompras.model.dto.ComentarioDTO>"--%>
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
		</div><%-- Datos de tienda --%>
		<div class="col-sm-10 col-md-6" id="vertical-line">
			<h2 class="fw-bold text-center py-3">
				Detalles
			</h2>
			<%--@elvariable id="horarios" type="java.util.List<escompras.model.dto.HoraServicioDTO>"--%>
			<div class="container mt-4">
				<p>
					<%--@elvariable id="fecha" type="java.lang.String"--%>
					<strong>Fecha: </strong>${ fecha }<br>
					<strong>Tienda: </strong>${ tienda.nombre }<br>
					<strong>Productos solicitados: </strong>
				</p>
				<table class="table table-striped mb-4 w-100">
					<thead>
						<tr>
							<th>Producto</th>
							<th>Cantidad</th>
							<th>Precio</th>
						</tr>
					</thead>
					<tbody id="tabla">
						<%--@elvariable
							id="listaProductos"
							type="java.util.List<escompras.model.dto.ProductoConID>"
						--%>
						<tr id="lastRow">
							<td>
								<label for="listaProductos">
									Disponibles:
								</label>
							</td>
							<td>
								<select id="listaProductos" class="form-select">
									<option disabled selected>
										Lista de productos
									</option>
									<c:forEach var="prod" items="${ listaProductos }">
										<option value="${ prod.id }"
											id="${ prod.id }">
											${ prod.nombre }
										</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<button type="button" class="btn btn-success"
									id="addButton"
									onclick="Select()">
									Añadir
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				<%--@elvariable id="escuela" type="escompras.model.entity.Escuela"--%>
				<p>
					<strong>Total: </strong><u id="total">$ 0.00</u><br>
					<strong>Escuela: </strong>${ escuela }<br>
				</p>
				<select class="form-select" class="form-control" id="horario" name="horario"
					form="pedido" required>
					<option value="" selected disabled>¿Hora de entrega?</option>
					<c:forEach var="horario" items="${ horarios }">
						<option value="${ horario.id }" class="dropdown-item">
							${ horario.toString() }
						</option>
					</c:forEach>
				</select>
			</div>
		
		</div>
		<form class="my-4" id="pedido" method="post"
		      action="Cliente/guardarPedido?tienda=${tienda.idTienda}">
			<button type="submit" class="btn btn-primary d-grid col-5 mx-auto">
				Finalizar pedido
			</button>
		</form>
	</escompras:container>
</body>
</html>
