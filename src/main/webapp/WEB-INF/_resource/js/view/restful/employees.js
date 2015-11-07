define(function() {require(["jquery", "restful", "paging"], function($) {
	$(function(){
		$.PAGING({
			pageNumber : $('input#initPageNumber').val(),
			pageSize : $('input#initPageSize').val(),
			pages : $('input#initPages').val(),
			pagingSubmitId : "employee",
			/* 			
			pageNumberId : "pageNumber",
			pageNumberName : "pageNumber",
			pageNumberClass : "pageNumber",
			pageNumberSelectedClass : "pageNumberSelected",
			pageSizes : [3, 6, 9],
			pageSizeId : "pageSize",
			pageSizeName : "pageSize",
			pageSizeClass : "pageSize",
			pageSizeSelectedClass : "pageSizeSelected",
			pagingLocationId : "pagingLocation",
			playPageClass : "playPage",
			*/			
		});
		$('a.delete').click(function(){
			$.DELETE(this.href);
			return false;
		});
	});
});});