<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<%--@elvariable id="tipo" type="escompras.model.entity.Tipo"--%>
	<title>ESCOMpras - Productos ${ not empty tipo ? tipo.nombre : "" }</title>
	<escompras:lib noPopper="true"/>
	<script src="lib/index.js"></script>
</head>
<body>
	<escompras:container>
		<div class="row g-3">
			<c:set scope="page" var="isCliente" value='${ sessionScope.tipoUsuario == "cliente" }'/>
			<%--@elvariable id="productos" type="java.util.List<escompras.model.dto.ProductoDTO>"--%>
			<c:forEach var="prod" items="${ productos }">
				<div class="col-12 col-md-6 col-lg-4">
					<div class="card mb-3" >
						<a href="Cliente/hacerPedido?id=${prod.id}"
						   class="d-flex justify-content-center">
							<img src="Imagen/producto?id=${ prod.id }"
							     alt="Image Not Found"
							>
						</a>
						<div class="card-body">
							<a href="Cliente/hacerPedido?id=${prod.id}">
								<h5 class="card-title">
									${ prod.nombre }
								</h5>
							</a>
							<p class="card-text">
								Descripción: ${ prod.descripcion }
							</p>
							<p class="card-text">
								Costo: $ ${ prod.precio }
								por 1 ${ prod.unidad }.
							</p>
							<p class="card-text">
								En inventario: ${ prod.cantidad }.
							</p>
							<c:if test="${ not empty prod.promocion }">
								<p class="card-text">
									Promoción actual: ${ prod.promocion }
								</p>
							</c:if>
							<p class="card-text d-flex justify-content-between">
								<small class="text-muted">
									Producto de <i>
										<a href="Cliente/verTienda?tienda=${ prod.tienda }">
											${ prod.tienda }
										</a>
									</i>.
								</small>
								<c:if test="${ isCliente }">
								<text class="p-0" onclick="Like('${ prod.id }')">
									<svg width="20" height="20" fill="royalblue"
										${ prod.like ? '' : 'class="d-none"' }
									     viewBox="0 0 16 16" id="unlike${ prod.id }">
										<path fill-rule="evenodd" d="M8
										1.314C12.438-3.248 23.534 4.735 8
										15-7.534 4.736 3.562-3.248 8 1.314z"
										></path>
									</svg>
									<svg width="20" height="20" fill="royalblue"
									     ${ prod.like ? 'class="d-none"' : ''}
									     viewBox="0 0 16 16" id="like${ prod.id }">
										<path d="m8 2.748-.717-.737C5.6.281
										2.514.878 1.4 3.053c-.523 1.023-.641
										2.5.314 4.385.92 1.815 2.834 3.989
										6.286 6.357 3.452-2.368 5.365-4.542
										6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878
										10.4.28 8.717 2.01L8 2.748zM8 15C-7.333
										4.868 3.279-3.04 7.824
										1.143c.06.055.119.112.176.171a3.12 3.12
										0 0 1 .176-.17C12.72-3.042 23.333 4.867
										8 15z"></path>
									</svg>
									<span class="spinner-border text-secondary d-none"
									      id="loadLike${ prod.id }"
									></span>
								</text>
								</c:if>
							</p>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</escompras:container>
</body>
</html>