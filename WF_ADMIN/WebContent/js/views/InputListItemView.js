window.InputListItemView = Backbone.View.extend({

    tagName: "li",
    
    className: "list-group-item",

	initialize: function (options) {
		this.options = options || {};
        this.render();
    },

    render: function () {
    	
    	var self = this;
    	
    	var itemText = this.options.format;
		_.each(this.options.item, function(value,key) {
			itemText = itemText.replace(new RegExp("{" + key + "}", "g"), value);
		});
		
		self.$el.append(itemText);
		
        return this;
    }
    
});