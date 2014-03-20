window.FormMessagesView = Backbone.View.extend({
	
	className: "alert alert-danger alert-dismissable",
	
    initialize: function (options) {
        _.bindAll(this, "invalid", "notify", "add");
        this.model.bind("invalid", this.invalid);
        this.model.bind("notify", this.notify);
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        this.$el.hide();
        return this;
    },
    
    invalid: function(model, errors) {
    	// remove old errors
    	this.$("p").remove();
    	this.$el.hide();
    	
    	// set title
    	this.$("h4").text("Invalid Data");
    	
    	// add errors
    	var self = this;
    	_.each(errors, function(error, i) {
    		self.add(error.message);
    	});
    },
    
    notify: function(notifications) {
    	// remove old notifications
    	this.$("p").remove();
    	this.$el.hide();
    	
    	// set title
    	this.$("h4").text("Warning");
    	
    	// add notifications
    	var self = this;
    	_.each(notifications, function(notification, i) {
    		self.add(notification.message);
    	});
    },
    
    add: function(msg) {
    	this.$el.append("<p>" + msg + "</p>");
    	this.$el.show();
    }
    
});