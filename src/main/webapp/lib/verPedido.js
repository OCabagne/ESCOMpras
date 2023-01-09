function Comentar(idTienda)
{
	ESCOMpras.Find('comentario').disabled = true;
	ESCOMpras.Find('comentarioBtn').innerHTML = ESCOMpras.Loading;
	ESCOMpras.Find('comentarioBtn').disabled = true;
	ESCOMpras.SendRequest(
		'Cliente/comentar?id=' + idTienda + GetInputValue('comentario'),
		null,
		function () { location.reload(); }
	);
}

function Reportar(idCliente)
{
	ESCOMpras.Find('reporte').disabled = true;
	ESCOMpras.Find('reporteBtn').innerHTML = ESCOMpras.Loading;
	ESCOMpras.Find('reporteBtn').disabled = true;
	ESCOMpras.SendRequest(
		'Tienda/reportar?id=' + idCliente + GetInputValue('reporte'),
		null,
		function () { location.reload(); }
	);
}

function EnviarClave(idPedido)
{
	ESCOMpras.SendRequest("Tienda/cargarClave?id=" + idPedido + GetInputValue("clavePedido"),
		null,
		function () {
			alert("Éxito.");
			location.reload();
		},
		function (msg) {
			alert(msg.responseText);
			location.reload();
		}
	);
}