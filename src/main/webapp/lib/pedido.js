let isCliente;

function RequestPedidos(isClienteParam)
{
	isCliente = isClienteParam;
	ESCOMpras.Find("tBody").innerHTML = ESCOMpras.Loading;
	ESCOMpras.SendRequest("Usuario/verPedidosJSON", null, function (result) {
		ESCOMpras.Find("tBody").innerHTML = "";
		let lista = JSON.parse(result).lista;
		console.log("data obtained: ", lista);
		for (let pedido of lista) FillPedidoTemplate(pedido);
	});
}

function FillPedidoTemplate(pedido)
{
	const row = ESCOMpras.Find("pedidoTemplate").content.cloneNode(true);
	row.id = pedido.id;
	let cells = row.querySelectorAll("td");
	cells[0].id = "pedidoUsuario" + pedido.id;
	cells[0].innerText = isCliente ? pedido.tienda : pedido.cliente;
	cells[1].id = "pedidoFecha" + pedido.id;
	cells[1].innerText = pedido.fecha;
	cells[2].id = "pedidoEscuela" + pedido.id;
	cells[2].innerText = pedido.escuela;
	cells[3].id = "horaEntrega" + pedido.horaEntrega;
	cells[3].innerText = pedido.horaEntrega;
	cells[4].id = "pedidoTotal" + pedido.id;
	cells[4].innerText = "$ " + (pedido.total / 100).toFixed(2);
	cells[5].id = "pedidoEstado" + pedido.id;
	cells[5].innerText = pedido.estado;
	let id = pedido.id;
	cells[6].querySelector("a").href = "Usuario/verPedido?pedido=" + pedido.id;
	document.querySelector("tbody").appendChild(row);
}