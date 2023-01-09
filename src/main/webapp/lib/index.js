/*function AsignarDropDown(dropDown, tipo)
{
	ESCOMpras.find(dropDown + 'Elegido').setAttribute('value', tipo);
	ESCOMpras.find(dropDown + 'DD').innerText = tipo;
}*/

function ActivarTiendaForm()
{
	ESCOMpras.Hide('escuelaDiv');
	ESCOMpras.Hide('apellidosDiv');
	ESCOMpras.Show('ubicacionDiv');
	ESCOMpras.Show('tipoTiendaDiv');
}

function ActivarClienteForm()
{
	ESCOMpras.Show('escuelaDiv');
	ESCOMpras.Show('apellidosDiv');
	ESCOMpras.Hide('ubicacionDiv');
	ESCOMpras.Hide('tipoTiendaDiv');
}

function CompararPassword()
{
	let confirm = ESCOMpras.Find('passwordConfirm')
	if (ESCOMpras.Find('password').value !== ESCOMpras.Find('passwordConfirm').value) {
		confirm.classList.add('is-invalid');
		confirm.classList.remove('is-valid');
		ESCOMpras.Find('registerButton').disabled = true;
	} else {
		confirm.classList.add('is-valid');
		confirm.classList.remove('is-invalid');
		ESCOMpras.Find('registerButton').disabled = false;
	}
}

function init()
{
	ESCOMpras.Find('password').addEventListener('change', CompararPassword);
	ESCOMpras.Find('passwordConfirm').addEventListener('change', CompararPassword);
}

function Like(prodId)
{
	let like = ESCOMpras.Find("like" + prodId).classList;
	let unlike = ESCOMpras.Find("unlike" + prodId).classList;
	let loadLike = ESCOMpras.Find("loadLike" + prodId).classList;
	let isLike = unlike.contains("d-none");
	if (isLike && like.contains("d-none")) return;
	(isLike ? like : unlike).add('d-none');
	loadLike.remove('d-none');
	ESCOMpras.SendRequest("Cliente/like?id=" + prodId + (isLike ? '' : '&remove'), null,
		function (resp) {
			if (!resp) {
				(isLike ? unlike : like).remove('d-none');
			} else {
				console.log("result: " + resp);
			}
			loadLike.add('d-none');
		},
		function (error) {
			(isLike ? like : unlike).remove('d-none');
			console.log("error liking producto: " + error);
			loadLike.add('d-none');
		}
	);
}