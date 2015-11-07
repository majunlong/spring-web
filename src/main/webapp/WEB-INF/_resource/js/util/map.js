define(function(){
	function Map(){
		this.container = new Object();
	};
	Map.prototype.size = function() {
		var count = 0;
		for ( var key in this.container) {
			if (key == 'extend') {
				continue;
			}
			count++;
		}
		return count;
	};
	Map.prototype.put = function(key, value) {
		this.container[key] = value;
	};
	Map.prototype.get = function(key) {
		return this.container[key];
	};
	Map.prototype.remove = function(key) {
		delete this.container[key];
	};
	Map.prototype.keySet = function() {
		var keyset = new Array();
		var count = 0;
		for ( var key in this.container) {
			if (key == 'extend') {
				continue;
			}
			keyset[count] = key;
			count++;
		}
		return keyset;
	};
	Map.prototype.toString = function(){
		var str = "";
		for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
			str = str + keys[i] + "=" + this.container[keys[i]] + ";\n";
		}
		return str;
	};
	return Map;
});