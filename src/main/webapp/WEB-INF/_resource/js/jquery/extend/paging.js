$.extend({
	pageSettings : {
		pageNumberId : "pageNumber",
		pageNumberName : "pageNumber",
		pageNumberClass : "pageNumber",
		pageNumberSelectedClass : "pageNumberSelected",
		pageSizes : [3, 6, 9],
		pageSizeId : "pageSize",
		pageSizeName : "pageSize",
		pageSizeClass : "pageSize",
		pageSizeSelectedClass : "pageSizeSelected",
		pagingSubmitId : "pagingSubmit",
		pagingLocationId : "pagingLocation",
		playPageClass : "playPage",
	},
	PAGING:function(param){
		
		var pages = parseInt(param.pages);
				
		if(pages < 1){
			return;
		}
		
		var pageNumber = parseInt(param.pageNumber);
		var pageSize = parseInt(param.pageSize);
		
		if(param != undefined){
			if(param.pageNumberId != undefined){
				jQuery.pageSettings.pageNumberId = param.pageNumberId;
			}
			if(param.pageNumberName != undefined){
				jQuery.pageSettings.pageNumberName = param.pageNumberName;
			}
			if(param.pageNumberClass != undefined){
				jQuery.pageSettings.pageNumberClass = param.pageNumberClass;
			}
			if(param.pageNumberSelectedClass != undefined){
				jQuery.pageSettings.pageNumberSelectedClass = param.pageNumberSelectedClass;
			}
			if(param.pageSizes != undefined){
				jQuery.pageSettings.pageSizes = param.pageSizes;
			}
			if(param.pageSizeId != undefined){
				jQuery.pageSettings.pageSizeId = param.pageSizeId;
			}
			if(param.pageSizeName != undefined){
				jQuery.pageSettings.pageSizeName = param.pageSizeName;
			}
			if(param.pageSizeClass != undefined){
				jQuery.pageSettings.pageSizeClass = param.pageSizeClass;
			}
			if(param.pageSizeSelectedClass != undefined){
				jQuery.pageSettings.pageSizeSelectedClass = param.pageSizeSelectedClass;
			}
			if(param.pagingSubmitId != undefined){
				jQuery.pageSettings.pagingSubmitId = param.pagingSubmitId;
			}
			if(param.pagingLocationId != undefined){
				jQuery.pageSettings.pagingLocationId = param.pagingLocationId;
			}
			if(param.playPageClass != undefined){
				jQuery.pageSettings.playPageClass = param.playPageClass;
			}
		}
		
		var array = new Array();
		var pageNumberId = jQuery.pageSettings.pageNumberId;
		var pageNumberName = jQuery.pageSettings.pageNumberName;
		var pageNumberClass = jQuery.pageSettings.pageNumberClass;
		var pageNumberSelectedClass = jQuery.pageSettings.pageNumberSelectedClass;
		var pageSizes = jQuery.pageSettings.pageSizes;
		var pageSizeId = jQuery.pageSettings.pageSizeId;
		var pageSizeName = jQuery.pageSettings.pageSizeName;
		var pageSizeClass = jQuery.pageSettings.pageSizeClass;
		var pageSizeSelectedClass = jQuery.pageSettings.pageSizeSelectedClass;
		var pagingSubmitId = jQuery.pageSettings.pagingSubmitId;
		var pagingLocationId = jQuery.pageSettings.pagingLocationId;
		var playPageClass = jQuery.pageSettings.playPageClass;
		var upPage = (pageNumber - 1 < 1 ? pages : pageNumber - 1);
		var nextPage = (pageNumber + 1 > pages ? 1 : pageNumber + 1);
		
		function toPageWithNumber(pageNumber){
			return pageNumberId + ".value=" + pageNumber + ";" + pagingSubmitId + ".submit();";
		}
		
		function toPageWithSize(pageSize){
			return pageNumberId + ".value=1;" + pageSizeId + ".value=" + pageSize + ";" + pagingSubmitId + ".submit();";
		}
		
		if(pages >= 10){
			var i = pageNumber - 4 <= 1 ? 1 : pageNumber - 4;
			var j = pageNumber + 4 >= pages ? pages : pageNumber + 4; 
			if(pageNumber + 4 > pages){
				i -= pageNumber + 4 - pages; 
			}
			if(pageNumber - 4 < 1){
				j += 5 - pageNumber;
			}
		}else{
			var i = 1;
			var j = pages;
		}
		
		for (; i <= j; i++) {
			if(i == pageNumber){
				array.push('<a class=');
				array.push('"' + pageNumberClass + " " + pageNumberSelectedClass + '"');
				array.push('onclick=');
				array.push('"' + toPageWithNumber(i) + '"');
				array.push('>');
				array.push(i);
				array.push('</a>');
			}else{
				array.push('<a class=');
				array.push('"' + pageNumberClass + '"');
				array.push('onclick=');
				array.push('"' + toPageWithNumber(i) + '"');
				array.push('>');
				array.push(i);
				array.push('</a>');
			}
		}
		
		array.push("&nbsp;&nbsp;");
	
		array.push('<a class="' + playPageClass + '"title="1"onclick="' + toPageWithNumber(1) + '">&lt;&lt;</a>');
		array.push('<a class="' + playPageClass + '"title="' + upPage + '"onclick="' + toPageWithNumber(upPage) + '">&lt;</a>');
		array.push('<a class="' + playPageClass + '"title="' + nextPage + '"onclick="' + toPageWithNumber(nextPage) + '">&gt;</a>');
		array.push('<a class="' + playPageClass + '"title="' + pages + '"onclick="' + toPageWithNumber(pages) + '">&gt;&gt;</a>');
	
		array.push("&nbsp;&nbsp;");
		
		for(var i=0; i < pageSizes.length; i++){
			if(pageSizes[i] == pageSize){
				array.push('<a class=');
				array.push('"' + pageSizeClass + " " + pageSizeSelectedClass + '"');
				array.push('onclick=');
				array.push('"' + toPageWithSize(pageSizes[i]) + '"');
				array.push('>');
				array.push(pageSizes[i]);
				array.push('</a>');
			}else{
				array.push('<a class=');
				array.push('"' + pageSizeClass + '"');
				array.push('onclick=');
				array.push('"' + toPageWithSize(pageSizes[i]) + '"');
				array.push('>');
				array.push(pageSizes[i]);
				array.push('</a>');
			}
		}
		
		array.push('<input type="hidden"id="' + pageNumberId + '"name="' + pageNumberName + '"value="' + pageNumber + '">')
		array.push('<input type="hidden"id="' + pageSizeId + '"name="' + pageSizeName + '"value="' + pageSize + '">')
		
		jQuery("#" + pagingLocationId).html(array.join(""));
		
		jQuery("body").keydown(function(e){
			var curKey = e.which;
			if(pageNumber > 0 && e.ctrlKey){
				if (curKey == 37) {
					jQuery("#" + pageNumberId).val(upPage);
					jQuery("#" + pagingSubmitId).submit();
				} else if (curKey == 38){
					for(i = 0; i < pageSizes.length; i++){
						if(pageSizes[i] == pageSize){
							var up = (i - 1 >= 0 ? pageSizes[i - 1] : pageSizes[pageSizes.length - 1]);
							jQuery("#" + pageNumberId).val(1);
							jQuery("#" + pageSizeId).val(up);
							jQuery("#" + pagingSubmitId).submit();
							return;
						}
					}
				} else if (curKey == 39){
					jQuery("#" + pageNumberId).val(nextPage);
					jQuery("#" + pagingSubmitId).submit();
				} else if (curKey == 40){
					for(i = 0; i < pageSizes.length; i++){
						if(pageSizes[i] == pageSize){
							var next = (i + 1 <= pageSizes.length - 1 ? pageSizes[i + 1] : pageSizes[0]);
							jQuery("#" + pageNumberId).val(1);
							jQuery("#" + pageSizeId).val(next);
							jQuery("#" + pagingSubmitId).submit();
							return;
						}
					}
				}
			}
		});
		
	}
})  
