<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag display-name="container" pageEncoding="UTF-8" %>

<div class="container-fluid">
	<nav class="navbar navbar-expand-lg navbar-light bg-light container-fluid">
		<c:if test="${ not empty sessionScope.id }">
			<%@include file="tagf/navbarLogged.tagf"%>
		</c:if>
		<c:if test="${ empty sessionScope.id }">
			<div class="container-fluid">
				<a href="." class="navbar-brand text-primary">
					<img src="favicon.ico" alt="ícono" width="25"/>
					ESCOMpras
				</a>
				<form method="post" action="Usuario/login" class="d-flex input-group w-50">
					<input type="text" class="form-control rounded"
					       id="login_usuario" name="login_usuario"
					       placeholder="Correo" autofocus>
					<input type="password" class="form-control rounded"
					       id="login_password" name="login_password"
					       placeholder="Contraseña">
					<button type="submit" class="btn btn btn-primary">
						Acceder
					</button>
				</form>
			</div>
		</c:if>
	</nav>
</div><!-- navbar -->
<div class="container w-75 mt-5">
	<%@include file="tagf/error.tagf"%>
	<div class="row">
		<jsp:doBody/>
	</div>
</div>
<%@include file="tagf/footer.tagf" %>