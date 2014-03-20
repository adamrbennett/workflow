window.TaskListItemView = Backbone.View.extend({

    tagName: "tr",

    initialize: function () {
        _.bindAll(this, "setName", "setDueOn", "setOptions", "setActions");
        this.model.bind("change", this.render, this);
        this.model.bind("destroy", this.close, this);
    },

    render: function () {
    	// render template
        this.$el.html(this.template(this.model.toJSON()));
        
        // execute view logic
        this.setName();
        this.setDueOn();
        this.setOptions();
        this.setActions();
        
        return this;
    },
    
    events: {
    	"change select" : "changeOption"
    },
    
    setName: function() {
    	var name = this.model.get("name");
    	var inputs = this.model.get("inputs");
    	var completedOn = this.model.get("completedOn");
    	
    	if (completedOn != null && inputs != null && _.size(inputs) > 0) {
	    	name = $("<a/>", {
	    		"href": "#process/" + this.model.get("processId") + "/task/" + this.model.get("taskId")
	    	}).text(this.model.get("name"));
    	}
    	
    	this.$(".name").append(name);
    },
    
    setDueOn: function() {
    	this.$(".dueOn").text(utils.formatDate(this.model.get("dueOn")));
    },
    
    setOptions: function() {
    	var self = this;
    	var opts = this.$(".options");
        var sel = this.$("select");
        
        var completedOn = this.model.get("completedOn");
        var options = this.model.get("options");
        var decision = this.model.get("decision");

        // show and populate options dropdown, if appropriate
    	if (completedOn == null && options != null && options != "") {
    		sel.show();
            if (options.length > 0) {
            	var jopts = $.parseJSON(options);
            	_.each(jopts, function(opt, i) {
            		sel.append(new TaskOptionView({label: opt.label.trim(), value: opt.value.trim()}).render().el);
            	});
            	sel.val(decision);
            }
        // show decision, if appropriate
    	} else if (completedOn != null && decision != null && decision != "") {
    		var jopts = $.parseJSON(options);
    		opts.append($.grep(jopts, function(obj) {return obj.value == decision;})[0].label);
    	}
    },
    
    setActions: function() {
    	var completedOn = this.model.get("completedOn");
    	var inputs = this.model.get("inputs");
    	var options = this.model.get("options");
    	var decision = this.model.get("decision");
    	
    	// if task has been completed
    	if (completedOn != null) {
    		this.$(".actions").append(utils.formatDate(completedOn));
    		return;
    	}
    	
    	// if task has inputs
    	if (inputs != null && _.size(inputs) > 0) {
    		this.$(".actions a.edit").show();
    		return;
    	}
    	
    	// if task has no options, or a decision has been made
    	if (options == null || decision != null) {
    		this.$(".actions a.complete").show();
    		return;
    	}
    },
    
    changeOption: function(event) {
    	this.model.set("decision", $(event.target).val());
    }

});

window.TaskOptionView = Backbone.View.extend({
	
	tagName: "option",
	
	initialize: function(options) {
    	this.options = options || {};
	},
	
	render: function() {
		this.$el.attr("value", this.options.value);
		this.$el.html(this.options.label);
		
		return this;
	}
});