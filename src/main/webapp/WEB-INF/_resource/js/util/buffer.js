define(function(){
	function Buffer(){
		this.container = new Array();
	};
	Buffer.prototype.append = function(str){
		this.container.push(str);
	};
	Buffer.prototype.toString = function(){
		return this.container.join("");
	};
	Buffer.prototype.length = function(){
		return this.container.length;
	};
	return Buffer;
});