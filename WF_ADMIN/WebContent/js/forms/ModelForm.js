window.ModelForm = Backbone.View.extend({

    initialize: function () {
    	// bind and listen
        _.bindAll(this, "saveSuccess", "saveError");
        this.model.bind("invalid", this.invalid, this);
        
        this.render();
    },

    render: function () {
    	// render template
        this.$el.html(this.template(this.model.toJSON()));
        
        // if the form was saved and re-rendered, show growl
        if (this.model.newlySaved) {
        	utils.growl("Success", "The model was successfully saved", "success");
        }
        
        if (!this.model.isNew()) {
	        this.$(".createdOn").text(utils.formatTimestamp(this.model.get("createdOn")));
	        this.$(".modifiedOn").text(utils.formatTimestamp(this.model.get("modifiedOn")));
        } else {
        	this.$("#revisionsLink").hide();
        }
        
        // insert form controls
        this.$(".form-controls").html(new FormControlsView({backUri: "#model/list"}).render().el);
        
        // insert form messages control
        this.messages = new FormMessagesView({model: this.model});
        this.$(".form-messages").html(this.messages.render().el);
        
        var files = new FileCollection();
        
        if (this.model.isNew()) {
        	// hide the delete button, if the model is new
        	this.$(".remove").hide();
        } else {
        	var file = new File({
        		fileName: this.model.get("fileName"),
        		url: utils.contextRoot + "/rs/model/" + this.model.get("modelId") + "/file"
        	});
        	files.add(file);
        }
        
        // insert file upload control
        var fileUpload = new FileListView({
        	model: files
        });
        this.listenTo(files, "change", this.changeFile);
        this.listenTo(files, "remove", this.removeFile);
        this.$(".file-list").append(fileUpload.el);
        
        return this;
    },
    
    events: {
    	"click .save"			: "save",
    	"click .remove"			: "remove",
    	"click .removeConfirm"	: "removeConfirm",
    	"click .removeCancel"	: "removeCancel",
    	"click .removeDone"		: "removeDone"
    },
    
    changeFile: function(file) {
    	this.model.set("fileName", file.get("fileName"));
    	this.model.set("fileType", file.get("fileType"));
    	this.model.set("tempName", file.get("tempName"));
    },
    
    removeFile: function() {
    	this.model.set("fileName", null);
    	this.model.set("fileType", null);
    	this.model.set("tempName", null);
    },
    
    invalid: function(model, errors) {
    	var self = this;
    	
    	// loop through errors
    	_.each(errors, function(error, idx) {
    		
    		// find the form group by input name
    		var group = this.$("input[name=" + error.attr + "]").parent();
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
    		name: this.$("#name").val()
    	}, {
    		success: this.saveSuccess,
    		error: this.saveError
    	});
    },
    
    saveSuccess: function (model, response, options) {
    	// set the model id, if newly created
    	if (this.model.isNew()) {
    		this.model.set("modelId", response.modelId);
    	}
    	
    	// mark as newly saved, for showing growl on re-render
    	this.model.newlySaved = true;
    	
    	// update URL and re-render
    	app.navigate("#model/" + this.model.get("modelId"), {replace: true});
    	this.render();
    },
    
    saveError: function (model, xhr, options) {
    	utils.growl("Error", "The model could not be saved", "error");
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
    	if (response.notifications != null && response.notifications.length > 0) {
    		model.trigger("notify", response.notifications);
        	this.$(".delete-confirm").hide();
        	this.$(".control-buttons").show();
    	} else {
	    	var alt = this.$(".delete-confirm");
	    	alt.removeClass("alert-warning");
	    	alt.addClass("alert-success");
	    	alt.html("<h4>Success</h4><p>The model was successfully removed</p><p><a class='btn btn-primary removeDone'>OK</a></p>");
	    	alt.show();
    	}
    },
    
    removeError: function (model, xhr, options) {
    	utils.growl("Error", "The model could not be removed", "error");
    },
    
    removeDone: function() {
    	app.navigate("#model/list", {trigger: true, replace: true});
    }

});