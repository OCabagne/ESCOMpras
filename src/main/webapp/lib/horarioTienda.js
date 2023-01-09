function LoadHorarios()
{
	let url = "Tienda/horariosAJAX?" + GetInputValue("escuela").substring(1);
	ESCOMpras.Find('horarios').innerHTML = ESCOMpras.Loading;
	ESCOMpras.SendRequest(url, null, function (resp) {
		ESCOMpras.Find('horarios').innerHTML = resp;
	});
}

function Guardar()
{
	ESCOMpras.SendRequest(
		"Tienda/horariosAJAX?horarioAction=update" + GetInputValue("escuela"),
		{
			day: ESCOMpras.Find('day').value,
			entrega: ESCOMpras.Find('ent').value
		},
		function (resp) {
			ESCOMpras.Find('horarios').innerHTML = resp;
		}
	);
}

function Modificar(id)
{
	ESCOMpras.SendRequest(
		"Tienda/horariosAJAX?horarioAction=update&escuela=" + ESCOMpras.Find("escuela").value,
		{
			day: ESCOMpras.Find('day' + id).value,
			entrega: ESCOMpras.Find('ent' + id).value,
			id: id
		}
	);
}

function Borrar(id)
{
	let url = "Tienda/horariosAJAX?horarioAction=delete&escuela=" + ESCOMpras.Find("escuela").value;
	let values = { id: id };
	ESCOMpras.Find('horario' + id).innerHTML = ESCOMpras.Loading;
	ESCOMpras.SendRequest(url, values,
		function (resp) { ESCOMpras.Find('horarios').innerHTML = resp; }
	);
}