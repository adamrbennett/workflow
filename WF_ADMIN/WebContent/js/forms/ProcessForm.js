window.ProcessForm = Backbone.View.extend({

    initialize: function () {
    	// bind and listen
        _.bindAll(this, "saveSuccess", "saveError");
        this.model.bind("invalid", this.invalid, this);
        
        this.render();
    },
    
    render: function () {
        
        var self = this;
        
    	// render template
        this.$el.html(this.template(this.model.toJSON()));
        
        // if the form was saved and re-rendered, show growl
        if (this.model.newlySaved) {
        	this.model.newlySaved = false;
        	utils.growl("Success", "The process was successfully saved", "success");
        }
        if (this.model.newlyCompleted) {
        	this.model.newlyCompleted = false;
        	utils.growl("Success", "The task was successfully completed", "success");
        }
        
        if (!this.model.isNew()) {
	        this.$(".createdOn").text(utils.formatTimestamp(this.model.get("createdOn")));
	        this.$(".modifiedOn").text(utils.formatTimestamp(this.model.get("modifiedOn")));
        } else {
        	this.$("#revisionsLink").hide();
        }
        
        // insert the form controls
        this.$(".form-controls").html(new FormControlsView({backUri: "#process/list"}).render().el);
        
        // insert form messages control
        this.messages = new FormMessagesView({model: this.model});
        this.$(".form-messages").html(this.messages.render().el);
        
        // populate the model dropdown
        var modelSel = this.$("#modelId");
        var modelCol = new ModelCollection();
        modelCol.fetch({success: function() {
        	modelCol.each(function(model) {
        		modelSel.append(new ModelOptionView({model: model}).render().el);
        	});
        	modelSel.val(self.model.get("modelId"));
        }});
        
        if (!this.model.isNew()) {
        	// insert the process tasks
	        var tasks = new TaskCollection([], {processId: this.model.get("processId")});
	        
	        // listen for task completion
	        this.listenTo(tasks, "complete", function() {
	        	self.model.newlyCompleted = true;
	        	self.render();
	        });
	        
	        // get tasks
	        tasks.fetch({success: function() {
	        	self.$(".process-tasks").html(new TaskListView({model: tasks}).el);
	        }});
	        
	        // disable model dropdown
	        modelSel.prop("disabled", true);
        } else {
        	// hide delete button
        	this.$(".remove").hide();
        }
        
        return this;
    },
    
    events: {
    	"click .save"			: "save",
    	"click .remove"			: "remove",
    	"click .removeConfirm"	: "removeConfirm",
    	"click .removeCancel"	: "removeCancel",
    	"click .removeDone"		: "removeDone"
    },
    
    invalid: function(model, errors) {
    	_.each(errors, function(error, idx) {
    		
    		// find the form group by input name
    		var group = this.$("input[name=" + error.attr + "], select[name=" + error.attr + "]").parent();
    		var i = 0;
    		while (!group.hasClass("form-group") && i < 5) {
    			group = group.parent();
    			i++;
    		}
    		
    		// add error class and insert message
    		group.addClass("has-error");
    		this.$(".help-block", group).html(error.message);
    	});
    	this.$(".form-messages")[0].scrollIntoView();
    },
    
    save: function() {
    	// remove error classes
    	this.$(".form-group, .has-error").removeClass("has-error");
    	
    	// save the model
    	this.model.save({
    		modelId: this.$("#modelId").val(),
    		name: this.$("#name").val()
    	}, {
    		success: this.saveSuccess,
    		error: this.saveError
    	});
    },
    
    saveSuccess: function (model, response, options) {
    	// set the model properties if newly created
    	if (this.model.isNew()) {
    		this.model.set("processId", response.processId);
    		this.model.set("name", response.name);
    	}
    	
    	// mark as newly saved, for showing growl on re-render
    	this.model.newlySaved = true;
    	
    	// update the URL and re-render
    	app.navigate("#process/" + this.model.get("processId"), {replace: true});
    	this.render();
    },
    
    saveError: function (model, xhr, options) {
    	utils.growl("Error", "The process could not be saved", "error");
    },
    
    remove: function() {
    	this.$(".control-buttons").hide();
    	this.$(".delete-confirm").show();
    },
    
    removeConfirm: function() {
    	this.model.destroy({success: this.removeSuccess, error: this.removeError});
    },
    
    removeCancel: function() {
    	this.$(".delete-confirm").hide();
    	this.$(".control-buttons").show();
    },
    
    removeSuccess: function (model, response, options) {
    	var alt = this.$(".delete-confirm");
    	alt.removeClass("alert-warning");
    	alt.addClass("alert-success");
    	alt.html("<h4>Success</h4><p>The process was successfully removed</p><p><a class='btn btn-primary removeDone'>OK</a></p>");
    	alt.show();
    },
    
    removeError: function (model, xhr, options) {
    	utils.growl("Error", "The process could not be removed", "error");
    },
    
    removeDone: function() {
    	app.navigate("#process/list", {trigger: true, replace: true});
    }

});