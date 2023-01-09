<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<title>ESCOMpras - Mi Perfil</title>
	<escompras:lib noPopper="true"/>
</head>
<body>
	<escompras:container>
		<h2 class="fw-bold text-center py-3">
			Mi Perfil
		</h2>
		<div class="col-sm-10 col-md-6">
			<h2 class="fw-bold text-center py-3">
				Cambiar imagen
			</h2>
			<div class="container-lg p-3">
				<form method="post" action="Usuario/cambiarImagen" enctype="multipart/form-data"
				      class="row border-1 min-vh-50 col -md-6" id="imagenForm">
					<label for="imagen" class="col-auto col-form-label">
						Imagen de Perfil:
					</label>
					<div class="col-md-4 py-3">
						<c:set scope="page" var="isCliente"
						       value='${ sessionScope.tipoUsuario == "cliente" }'
						/>
						<img src="Imagen/${ sessionScope.tipoUsuario }?id=${ sessionScope.id }"
							alt="${ sessionScope.usuario }"
							class="img-fluid"
						>
					</div>
					<div class="form-group row mb-4">
						<div class="col-sm-10">
							<input type="file" class="form-control"
							       id="imagen" name='imagen' accept="image/*"
							>
						</div>
					</div>
					<button class="mb-4 btn btn-primary d-grid gap-2 col-6 mx-auto" type="submit">
						Cambiar
					</button>
				</form>
			</div>
			<h2 class="fw-bold text-center py-3">
				Cambiar Contraseña
			</h2>
			<div class="container-lg p-3">
				<form method="post" action="Usuario/cambiarPassword"
					class="row border-1 min-vh-50 col -md-6">
					<div class="mb-4 form-group row">
						<div class="col-sm-10">
							<input type="password" class="form-control "
							       id="password" name="password"
							       placeholder="Contraseña Actual"
							       required="required">
						</div>
					</div><!-- Contraseña Actual-->
					<div class="mb-4 form-group row">
						<div class="col-sm-10">
							<input type="password" class="form-control"
							       id="newPassword" name="newPassword"
							       placeholder="Nueva Contraseña"
							       required="required">
						</div>
					</div><!-- Nueva Contraseña -->
					<div class="mb-4 form-group row">
						<div class="col-sm-10">
							<input type="password" class="form-control"
							       id="newPasswordConfirm"
							       placeholder="Confirmar Contraseña"
							       required="required">
						</div>
					</div><!-- Confirmación -->
					<button type="submit"
						class="mb-4 btn btn-primary d-grid gap-2 col-6 mx-auto">
						Cambiar
					</button><!-- BTN Cambiar Contraseña -->
				</form>
			</div>
		</div><!-- Contraseña -->
		<div class="col-sm-10 col-md-6" id="vertical-line">
			<h2 class="fw-bold text-center py-3">
				Cambiar Correo
			</h2>
			<div class="container-lg p-3">
				<form method="post" action="Usuario/changePassword"
				      class="row border-1 min-vh-50 col -md-6">
					<div class="form-group row mb-4">
						<div class="col-sm-10">
							<input type="password" class="form-control"
							       id="email" name="email"
							       placeholder="Correo Actual"
							       required="required">
						</div>
					</div><!-- Correo Actual-->
					<button type="button" onclick=""
						class="btn btn-primary mb-4 d-grid gap-2 col-6 mx-auto">
						Enviar código
					</button><!-- BTN Enviar Código -->
					<div class="form-group row mb-4">
						<div class="col-sm-10">
							<input type="password" class="form-control"
							       id="new_email"
							       placeholder="Código"
							       required="required">
						</div>
					</div><!-- Nueva Contraseña -->
					<div class="form-group row mb-4">
						<div class="col-sm-10">
							<input type="password" class="form-control"
							       id="new_email_confirm"
							       placeholder="Correo Nuevo"
							       required="required">
						</div>
					</div><!-- Confirmar Nueva Contraseña -->
					<button type="submit"
						class="btn btn-primary mb-4 d-grid gap-2 col-6 mx-auto">
						Cambiar
					</button><!-- BTN Cambiar Contraseña -->
				</form>
			</div>
		</div><!-- Correo -->
	</escompras:container>
</body>
</html>