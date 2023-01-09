let removed = {};

function Select()
{
	AddProducto(ESCOMpras.Find('listaProductos').value);
}

function AddProducto(id)
{
	ESCOMpras.Find("lastRow").insertAdjacentHTML('beforebegin', "<tr id='row" + id + "'></tr>");
	ESCOMpras.Find("row" + id).innerHTML = ESCOMpras.Loading;
	ESCOMpras.SendRequest("Cliente/costoProducto?id=" + id, null, function (resp) {
		ESCOMpras.Find("row" + id).innerHTML = resp;
		ContarTotal();
	});
	removed[id] = ESCOMpras.Find(id).outerHTML;
	ESCOMpras.Find(id).remove();
	if (ESCOMpras.Find("listaProductos").childElementCount <= 1) {
		ESCOMpras.Find("addButton").setAttribute("disabled", "disabled");
	}
}

function ContarTotal()
{
	let total = 0;
	for (let fila of ESCOMpras.Find("tabla").rows) {
		if (fila.id === "lastRow") continue;
		let id = fila.id.substring(3);
		if (ESCOMpras.Find("precio" + id) !== null)
			total += ESCOMpras.Find("precio" + id).innerText
				* ESCOMpras.Find("cantidad" + id).value;
	}
	ESCOMpras.Find("total").innerText = "$ " + total.toFixed(2);
}