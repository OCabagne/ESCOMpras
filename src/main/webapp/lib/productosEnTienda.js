function LoadProductos()
{
	let url = "Tienda/productosAJAX?";
	ESCOMpras.Find('productos').innerHTML = ESCOMpras.Loading;
	ESCOMpras.SendRequest(url, null, function (resp) {
		ESCOMpras.Find('productos').innerHTML = resp;
	});
}

function Guardar()
{
	ESCOMpras.SendRequest(
		"Tienda/productosAJAX?productoAction=update",
		{
			cantidad: ESCOMpras.Find('cantidad').value,
			nombre: ESCOMpras.Find('nombre').value,
			precio: ESCOMpras.Find('precio').value,
			unidad: ESCOMpras.Find('unidad').value,
			descripcion: ESCOMpras.Find('descripcion').value,
			promocion: ESCOMpras.Find('promocion').value
		},
		function(resp) {
			ESCOMpras.Find('productos').innerHTML = resp;
		}
	);
}

function Modificar(id)
{
	ESCOMpras.SendRequest(
		"Tienda/productosAJAX?productoAction=update",
		{
			cantidad: ESCOMpras.Find('cantidad' + id).value,
			nombre: ESCOMpras.Find('nombre' + id).value,
			precio: ESCOMpras.Find('precio' + id).value,
			unidad: ESCOMpras.Find('unidad' + id).value,
			descripcion: ESCOMpras.Find('descripcion' + id).value,
			promocion: ESCOMpras.Find('promocion' + id).value,
			id: id
		}
	);
}

function Borrar(id)
{
	ESCOMpras.Find('producto' + id).innerHTML = ESCOMpras.Loading;
	ESCOMpras.SendRequest(
		"Tienda/productosAJAX?productoAction=delete",
		{ id: id },
		function (resp) { ESCOMpras.Find('productos').innerHTML = resp; }
	);
}

function CambiarImagen(id)
{
	ESCOMpras.Find('status' + id).classList.remove('text-danger');
	ESCOMpras.Find('status' + id).classList.remove('text-success');
	ESCOMpras.Find('upload' + id).disabled = true;
	ESCOMpras.Find('status' + id).innerHTML = ESCOMpras.Loading;
	let formData = new FormData();
	formData.append("imagen", document.getElementById("image" + id).files[0]);
	fetch('Tienda/setImagenProducto?id=' + id, { method: "POST", body: formData }).then(r => {
		let msg = ESCOMpras.Find('status' + id);
		if (r.ok) {
			r.text().then(value => {
				SetSuccessMessage(msg, value)
				let img = ESCOMpras.Find("img" + id + "src");
				img.src = img.src.substring(0, img.src.lastIndexOf('?')) + '?' + Math.random();
			});
		} else {
			r.text().then(value => {
				SetDangerMessage(msg, value);
			});
		}
		ESCOMpras.Find('upload' + id).disabled = false;
	});
}

function SetDangerMessage(area, message)
{
	area.classList.add('text-danger');
	area.classList.remove('text-success');
	area.innerHTML = message;
}

function SetSuccessMessage(area, message)
{
	area.classList.remove('text-danger');
	area.classList.add('text-success');
	area.innerHTML = message;
}