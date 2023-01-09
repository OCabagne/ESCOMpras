let IS_CLIENTE;
let estados;

function SetIsCliente(isCliente, pedidoId)
{
	IS_CLIENTE = isCliente;
	estados = {
		"SOLICITADO" : {
			next : IS_CLIENTE ? ["CANCELADO"]
				: ["RECHAZADO", "APROBADO"],
			style : "badge bg-info text-dark"
		},
		"APROBADO" : {
			next : IS_CLIENTE ? ["CANCELADO"]
				: ["ENVIADO"],
			style : "badge bg-success"
		},
		"ENVIADO" : {
			next : IS_CLIENTE ? ["RECIBIDO"]
				: [],
			style : "badge bg-primary"
		},
		"RECIBIDO" : {
			next : IS_CLIENTE ? [] : [],
			style : "badge bg-secondary"
		},
		"RECHAZADO" : {
			next : IS_CLIENTE ? [] : [],
			style : "badge bg-warning text-dark"
		},
		"CANCELADO" : {
			next : IS_CLIENTE ? [] : [],
			style : "badge bg-danger"
		}
		
	};
	LoadEstado(pedidoId);
}

function LoadEstado(pedidoId)
{
	let estado = estados[ESCOMpras.Find("estado" + pedidoId).value];
	ESCOMpras.Find("estadoLoading" + pedidoId).classList.add("d-none");
	ESCOMpras.Find("estadoLoading" + pedidoId).innerHTML = "";
	ESCOMpras.Find("estado" + pedidoId).disabled = !(estado.next.length > 0);
	ESCOMpras.Find("estado" + pedidoId).className = estado.style;
	let options = ESCOMpras.Find("estado" + pedidoId).querySelectorAll("option");
	for (const option of options)
		option.disabled = !(estado.next.includes(option.value));
}

function ChangeEstado(pedidoId)
{
	ESCOMpras.Find("estado" + pedidoId).disabled = true;
	ESCOMpras.Find("estadoLoading" + pedidoId).innerHTML = ESCOMpras.Loading;
	ESCOMpras.Find("estadoLoading" + pedidoId).classList.remove("d-none");
	let url = "Usuario/cambiarEstadoPedido"
		+ "?pedido=" + pedidoId
		+ "&estado=" + ESCOMpras.Find("estado" + pedidoId).value;
	ESCOMpras.SendRequest(url, null, function() {
		LoadEstado(pedidoId);
	}, function() {
		alert("Ocurrió un error al intentar cambiar el estado del pedido.");
		location.reload();
	});
}