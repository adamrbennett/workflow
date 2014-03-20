/**
 * @name TaskForm
 */
window.TaskForm = Backbone.View.extend({

	/**
	 * @memberOf TaskForm
	 */
    initialize: function () {
        // bind and listen
    	_.bindAll(this, "saveSuccess", "saveError");
        this.model.bind("invalid", this.invalid, this);
        
        this.render();
    },
    
    render: function() {
    	this
    		.renderTemplate()
    		.renderRevisionHistory()
    		.renderTaskForm()
    		.renderInputs()
    		.renderFormControls()
    		.renderFormMessages();
    	
    	return this;
    },
    
    renderTemplate: function() {
        this.$el.html(this.template(this.model.toJSON()));
        return this;
    },
    
    renderRevisionHistory: function() {
        this.$(".createdOn").text(utils.formatTimestamp(this.model.get("createdOn")));
        this.$(".modifiedOn").text(utils.formatTimestamp(this.model.get("modifiedOn")));
        return this;
    },
    
    renderTaskForm: function() {
        // insert the task form
        this.$("#form").html(this.model.get("form"));
        
        // reset the task form
        this.$("#form")[0].reset();
        
        return this;
    },
    
    renderInputs: function() {
    	var self = this;
    	
        // populate the task form inputs
        var inputs = this.model.get("inputs");
        if (inputs) {
	        $.each(inputs, function(key, value) {
	        	var input = self.$("input[name='" + key + "'], textarea[name='" + key + "'], select[name='" + key + "']");
	        	var type = input.prop("type");
	        	if (
	        		input.length == 1 &&
	        		type != "radio" &&
	        		type != "checkbox" &&
	        		type != "file"
	        	) {
	        		input.val(value.value);
	        		if (input.attr("data-list")) {
	        			input.hide();
	        			if (!_.isNull(value.value) && value.value != "") {
		        			var format = input.attr("data-list-format");
		        			var list = new InputListView({data: value.value, format: format});
		        			input.after(list.el);
	        			}
	        		}
	        	} else if (
	        		type == "radio" ||
		        	type == "checkbox"
		        ) {
	        		input.filter("[value='" + value.value + "']").prop("checked", true);
	        	} else if (type == "file") {
	        		var files = new FileCollection();
	        		_.each(value.files, function(inputFile) {
	        			var file = new File({
	        				id: inputFile.fileId,
	        				fileName: inputFile.fileName,
	        				fileType: inputFile.fileType,
	                		url: utils.contextRoot + "/rs/process/" + self.model.get("processId") + "/task/" + self.model.get("taskId") + "/input/" + value.inputId + "/file/" + inputFile.fileId
	        			});
	        			files.add(file);
	        		});
	        		var fileUpload = new FileListView({
	        			model: files,
	        			readonly: input.attr("readonly") == "readonly",
	        			multiple: input.attr("multiple") == "multiple"
	        		});
	        		input.data("files", files);
	        		input.hide();
	        		input.after(fileUpload.el);
	        	}
	        });
    	}
        
        return this;
    },
    
    renderFormControls: function() {
        var isComplete = this.model.get("completedOn") != null;
        
        // insert the form controls
        var controls = new FormControlsView({
        	backUri: "#process/" + this.model.get("processId"),
        	buttons: this.model.get("buttons"),
        	showSave: !isComplete,
        	showComplete: !isComplete,
        	showDelete: !isComplete,
        	showButtons: !isComplete
        });
        controls.setElement(this.$(".form-controls")).render();
        
        return this;
    },
    
    renderFormMessages: function() {
        // insert form messages control
        this.messages = new FormMessagesView({model: this.model});
        this.$(".form-messages").html(this.messages.render().el);
        
        // hide the delete button if the model is new
        if (this.model.isNew()) {
        	this.$(".remove").hide();
        }
        
        return this;
    },
    
    getInputs: function() {
    	var inputs = this.model.get("inputs");
    	$.each(inputs, function(key, value) {
        	var input = self.$("input[name='" + key + "'], textarea[name='" + key + "'], select[name='" + key + "']");
        	var type = input.prop("type");
        	if (
        		input.length == 1 &&
        		type != "radio" &&
        		type != "checkbox" &&
        		type != "file"
        	) {
        		value.value = input.val();
        	} else if (
        		type == "radio" ||
        		type == "checkbox"
        	) {
        		value.value = input.filter(":checked").val();
        	} else if (type == "file") {
        		var files = input.data("files");
        		var inputFiles = [];
        		_.each(files.models, function(file) {
        			var inputFile = new InputFile();
        			inputFile.set("processId", value.processId);
        			inputFile.set("taskId", value.taskId);
        			inputFile.set("inputId", value.inputId);
        			inputFile.set("fileId", file.get("id"));
        			inputFile.set("fileName", file.get("fileName"));
        			inputFile.set("fileType", file.get("fileType"));
        			inputFile.set("tempName", file.get("tempName"));
        			inputFiles.push(inputFile);
        		});
        		value.files = inputFiles;
        	}
    		inputs[key] = value;
    	});
    	return inputs;
    },
    
    events: function() {
    	var e = {
        	"click .save"			: "save",
        	"click .complete"		: "complete",
        	"click .remove"			: "remove",
        	"click .removeConfirm"	: "removeConfirm",
        	"click .removeCancel"	: "removeCancel",
        	"click .removeDone"		: "removeDone"
    	};
        
        // add events for custom buttons
        var buttons = $.parseJSON(this.model.get("buttons"));
        if (buttons) {
        	_.each(buttons, function(button) {
        		e["click ." + button.label] = "complete";
        	});
        }
    	
    	return e;
    },
    
    invalid: function(model, errors) {
    	_.each(errors, function(error, idx) {
    		
    		// find the form group by input name
    		var group = this.$("input[name=" + error.attr + "], textarea[name='" + error.attr + "']").parent();
    		var i = 0;
    		while (!group.hasClass("form-group") && i < 5) {
    			group = group.parent();
    			i++;
    		}
    		
    		// add the error class and insert message
    		group.addClass("has-error");
    		this.$(".help-block", group).html(error.message);
    	});
    	this.$(".form-messages")[0].scrollIntoView();
    },
    
    complete: function(event) {
    	var handler = $(event.currentTarget).data("handler");
    	if (!_.isNull(handler)) {
        	this.model.set("handler", handler);
    	}
    	
    	// set action and save
    	this.model.set("action", "complete");
    	this.save();
    },
    
    save: function() {
    	var self = this;
    	
    	// remove error classes
    	this.$(".form-group, .has-error").removeClass("has-error");
    	
    	// update task inputs
    	var inputs = this.getInputs();
    	
        // get the task validate method
    	this.model.validate = window.taskValidator;
    	
    	// save the model
    	this.model.save({inputs: inputs}, {
    		success: this.saveSuccess,
    		error: this.saveError
    	});
    },
    
    saveSuccess: function (model, response, options) {
    	if (model.get("action") == "complete")
    		utils.growl("Success", "The task was successfully completed", "success");
    	else
    		utils.growl("Success", "The task was successfully saved", "success");
    	
    	this
    		.renderRevisionHistory()
    		.renderFormControls()
    		.renderInputs();
    },
    
    saveError: function (model, xhr, options) {
    	utils.growl("Error", "The task could not be saved", "error");
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
    	alt.html("<h4>Success</h4><p>The task was successfully removed</p><p><a class='btn btn-primary removeDone'>OK</a></p>");
    	alt.show();
    },
    
    removeError: function (model, xhr, options) {
    	utils.growl("Error", "The task could not be removed", "error");
    },
    
    removeDone: function() {
    	app.navigate("#process/" + this.model.get("processId"), {trigger: true, replace: true});
    }

});