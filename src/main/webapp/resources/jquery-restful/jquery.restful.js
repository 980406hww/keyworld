(function($){
    function restfulService(){

    }
    restfulService.prototype.get = function(url, options){
        var defer = $.Deferred();
        var ajaxOptions = $.extend({
            type : 'GET',
            url : url,
            success : function(data) {
                defer.resolve(data);
            },
            error: function(e) { 
            	defer.reject(e.responseJSON); 
        	} 
        }, options);
        $.ajax(ajaxOptions);
        return defer.promise();
    };
    
    restfulService.prototype.post = function(url, model, options){
        var defer = $.Deferred();
        var ajaxOptions = $.extend({
            type : 'POST',
            url : url,
            contentType : "application/json",
            data : JSON.stringify(model),
            success : function(data) {
                defer.resolve(data);
            },
            error: function(e) { 
            	defer.reject(e.responseJSON); 
        	} 
        },options);
        $.ajax(ajaxOptions);
        return defer.promise();
    };
    
    restfulService.prototype.put = function(url, model, options){
        var defer = $.Deferred();
        var ajaxOptions = $.extend({
            type : 'PUT',
            url : url,
            contentType : "application/json",
            data : JSON.stringify(model),
            success : function(data) {
                defer.resolve(data);
            },
            error: function(e) { 
            	defer.reject(e.responseJSON); 
        	} 
        },options);
        $.ajax(ajaxOptions);
        return defer.promise();
    };
    
    restfulService.prototype.del = function(url, options){
        var defer = $.Deferred();
        var ajaxOptions = $.extend({
            type : 'DELETE',
            url : url,
            success : function(data) {
                defer.resolve(data);
            },
            error: function(e) { 
            	defer.reject(e.responseJSON); 
        	} 
        }, options);
        $.ajax(ajaxOptions);
        return defer.promise();
    };

    $.restful = new restfulService();
})(jQuery);