window.FormControlsView = Backbone.View.extend({
	
    initialize: function (options) {
    	
    	this.showSave = true;
    	this.showComplete = false;
    	this.showDelete = true;
    	this.showButtons = true;
    	
    	if (options != null) {
    		if (!_.isUndefined(options.showSave)) {
    			this.showSave = options.showSave;
    		}
    		if (!_.isUndefined(options.showComplete)) {
    			this.showComplete = options.showComplete;
    		}
    		if (!_.isUndefined(options.showDelete)) {
    			this.showDelete = options.showDelete;
    		}
    		if (!_.isUndefined(options.showButtons)) {
    			this.showButtons = options.showButtons;
    		}
    		this.options = options;
    	}
    	
        this.render();
    },

    render: function () {
    	var self = this;
    	
        this.$el.html(this.template(this.options));
        
        if (this.options.buttons && this.showButtons) {
        	this.showComplete = false;
        	var buttons = $.parseJSON(this.options.buttons);
        	_.each(buttons, function(button) {
        		var className = "btn-primary";
        		if (button.type) {
        			className = "btn-" + button.type;
        		}
    	    	var btn = $("<a/>", {
    	    		"class": "btn " + className + " " + button.label
    	    	}).text(button.label);
    	    	btn.data("handler", button.handler);
    	    	self.$(".control-buttons").append(btn).append("&nbsp;");
        	});
        }
        
        if (!this.showSave) {
        	this.$(".save").hide();
        }
        
        if (!this.showComplete) {
        	this.$(".complete").hide();
        }
        
        if (!this.showDelete) {
        	this.$(".remove").hide();
        }
        
        return this;
    }
    
});