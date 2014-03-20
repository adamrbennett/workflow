window.InputListView = Backbone.View.extend({
	
	tagName: "ul",
	
	className: "list-group",
	
	initialize: function (options) {
        
        if (options.data) {
        	this.data = options.data;
        }
        if (options.format) {
        	this.format = options.format;
        }
        
		this.render();
    },

    render: function () {
    	var self = this;
    	
    	if (this.data != null) {
	    	var items = JSON.parse("[" + this.data + "]");
	    	_.each(items, function(item) {
	    		self.$el.append(new InputListItemView({item: item, format: self.format}).el);
	    	});
    	}
    	
    	return this;
    }
    
});