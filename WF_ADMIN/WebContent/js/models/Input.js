window.Input = Backbone.Model.extend({
	
	idAttribute: "inputId",
	
	defaults: {
		processId: null,
		taskId: null,
		inputId: null,
		name: "",
		value: "",
		files: null
	},
	
	validate: function(attrs, options) {
		var errors = [];
		
		if (errors.length) {
			return errors;
		}
	}
});