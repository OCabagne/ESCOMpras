<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="escompras" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>ESCOMpras - Registrarse</title>
	<escompras:lib noPopper="true"/>
	<script src="lib/index.js"></script>
</head>
<body onload="ActivarClienteForm(); init()">
	<escompras:container>
		<div class="col-sm-10 col-md-6">
			<h2 class="fw-bold text-center py-5">Crear una cuenta nueva</h2>
			<form method="post" enctype="multipart/form-data" action="Usuario/register">
				<escompras:input name="nombre" placeholder="Nombre(s)" required="true"/>
				<escompras:input name="apellidos" placeholder="Apellidos"/>
				<escompras:input name="ubicacion" placeholder="Ubicación" length="50"/>
				<escompras:input name="correo" placeholder="Correo" type="email" required="true"/>
				<escompras:input name="password" placeholder="Contraseña"
						 type="password" required="true"
				/>
				<escompras:input name="passwordConfirm" placeholder="Confirmar contraseña"
						 type="password" required="true"
						 withValidity="true" validityMSG="Las contraseñas no coinciden."
				/>
				<%--@elvariable id="escuelas" type="java.util.List"--%>
				<escompras:select name="escuela" placeholder="¿En qué escuela estás?"
						  items="${ escuelas }"
				/>
				<%--@elvariable id="tiposTienda" type="java.util.List"--%>
				<escompras:select name="tipoTienda" placeholder="Tipo de tienda:"
						  items="${ tiposTienda }"
				/>
				<div class="mb-4">
					<div class="form-group row">
						<div class="col-sm-10">
							<label for="imagen" class="col-auto col-form-label">
								Foto de Perfil:
							</label>
							<input type="file" class="form-control"
							       id="imagen" name='imagen' accept="image/*"
							>
						</div>
					</div>
				</div><!-- Foto de Perfil -->
				<div class="mb-4">
					<div class="form-check">
						<input class="form-check-input" type="radio"
						       name="tipo" id="cliente" value="cliente" checked
						       onclick="ActivarClienteForm()">
						<label class="form-check-label" for="cliente">
							Soy Cliente
						</label>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="radio"
						       name="tipo" id="tienda" value="tienda"
						       onclick="ActivarTiendaForm()">
						<label class="form-check-label" for="tienda">
							Soy Vendedor
						</label>
					</div>
				</div><!-- Tipo Usuario -->
				<div class="mb-4">
					<div class="form-check">
						<input class="form-check-input" type="checkbox"
						       id="acepto" required="required">
						<label class="form-check-label" for="acepto">
							Acepto los <a href="./?action=terminosYCondiciones">
								Términos y Condiciones
							</a>
						</label>
					</div>
				</div><!-- Términos y Condiciones -->
				<div class="mb-4">
					<button type="submit" class="btn btn-primary btn-lg" id="registerButton">
						Registrarme
					</button>
				</div><!-- Registrarme -->
				<label class="mb-4">
					¿Ya tienes cuenta? Haz click <a href="#">aquí</a>
				</label><!-- Enlace al ingreso -->
			</form>
		</div><!-- Lado izquierdo: Registro -->
		<div class="col-sm-10 col-md-6" id="vertical-line">
			<div class="col">
				<h2 class="fw-bold text-center py-5">Usar redes sociales</h2>
			</div>
			<div class="container w-100 my-5">
				<div class="col">
					<button class="btn btn-outline-primary w-100 my-1">
						<div class="row align-items-center">
							<div class="col-2 d-none d-md-block">
								<img src="res/fb.png" width="32" alt="">
							</div>
							<div class="col-sm-10">
								Facebook
							</div>
						</div>
					</button>
				</div><!--Facebook -->
				<div class="col">
					<button class="btn btn-outline-danger w-100 my-1">
						<div class="row align-items-center">
							<div class="col-2 d-none d-md-block">
								<img src="res/google.png" width="32" alt="">
							</div>
							<div class="col-sm-10">
								Google
							</div>
						</div>
					</button>
				</div><!-- Google -->
			</div>
			<hr>
			<div class="container w-50">
				<div class="col">
					<h2 class="fw-bold text-center py-5">
						¡Llévanos siempre contigo!
					</h2>
				</div>
				<div class="col-sm-10 col-md-6">
					<button class="btn">
						<img src="res/google_play.png" width="250">
					</button>
				</div><!-- Google Play -->
				<div class="col-sm-10 col-md-6">
					<button class="btn">
						<img src="res/appstore.png" width="250">
					</button>
				</div><!-- AppStore -->
			</div>
		</div><!-- Lado derecho: Redes sociales -->
	</escompras:container>
</body>
</html>