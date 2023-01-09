const ESCOMpras = {
	Find: function(id) { return document.getElementById(id); },
	SendRequest: function(url, data, func, onError) {
		let request = new XMLHttpRequest();
		//request.setRequestHeader(header, value);
		request.onreadystatechange = function() {
			if (request.readyState === 4) {
				if (request.status === 200) {
					console.log({ resultado : request.responseText});
					if (func) func(request.responseText);
				} else if (request.status === 204) {
					console.log("Success.");
					if (func) func();
				} else {
					console.log("AJAX Error " + request.status + ": " + request.responseText);
					if (onError) onError(request);
				}
			}
		};
		console.log("url: " + url);
		request.open("post", url, true);
		request.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
		request.send(data != null ? JSON.stringify(data) : "{}");
	},
	Hide: function(id) {
		document.getElementById(id).classList.add('d-none');
	},
	Show: function(id) {
		document.getElementById(id).classList.remove('d-none');
	},
	SetDefaultUserImg: function() {
		document.getElementById('userImg').outerHTML = '\n' +
			'<svg width="25" viewBox="0 0 16 16" id="userImg" fill="royalblue">\n' +
			'    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>\n' +
			'    <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 ' +
			'        0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 ' +
			'        0 8 1z"/>\n' +
			'</svg>'
	},
	Loading: "<span class=\"spinner-border text-secondary\"></span>Cargando."
}

function GetInputValue(name)
{
	return "&" + encodeURIComponent(name) +
		"=" + encodeURIComponent(document.getElementById(name).value);
}