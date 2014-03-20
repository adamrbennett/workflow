window.Task = Backbone.Model.extend({
	
	urlRoot: function() {
		return utils.contextRoot + "/rs/process/" + this.get("processId") + "/task";
	},
	
	idAttribute: "taskId",
	
	defaults: {
		processId: null,
		taskId: null,
		scriptId: null,
		createdOn: null,
		createdBy: null,
		modifiedOn: null,
		modifiedBy: null,
		name: null,
		role: null,
		buttons: null,
		options: null,
		decision: null,
		dueOn: null,
		completedOn: null
	},
	
	validate: function(attrs, options) {
		var errors = [];
		
		if (errors.length) {
			return errors;
		}
	}
});

window.TaskCollection = Backbone.Collection.extend({
    model: Task,
    
    initialize: function(models, options) {
    	this.options = options || {};
    },
    
    url: function() {
    	return utils.contextRoot + "/rs/process/" + this.options.processId + "/task";
    }
});