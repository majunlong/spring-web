$.extend({   
	INPUT:function(URL){
		var restform = document.createElement("form");
		restform.action = URL;
		restform.method = "post";
		restform.style.display = "none";
			var restHidden = document.createElement("input");
			restHidden.type = "hidden";
			restHidden.name = "_method";
			restHidden.value = "input";
			restHidden.style.display = "none";
		restform.appendChild(restHidden);
		document.body.appendChild(restform);
		restform.submit();
		return restform;
	},
	DELETE:function(URL){
		var restform = document.createElement("form");
		restform.action = URL;
		restform.method = "post";
		restform.style.display = "none";
		var restHidden = document.createElement("input");
		restHidden.type = "hidden";
		restHidden.name = "_method";
		restHidden.value = "delete";
		restHidden.style.display = "none";
		restform.appendChild(restHidden);
		document.body.appendChild(restform);
		restform.submit();
		return restform;
	},
})