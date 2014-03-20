window.Process = Backbone.Model.extend({
	urlRoot: utils.contextRoot + "/rs/process",
	idAttribute: "processId",
	
	defaults: {
		processId: null,
		modelId: null,
		createdOn: null,
		createdBy: null,
		modifiedOn: null,
		modifiedBy: null,
		name: null,
		data: null,
		tasks: null
	},
	
	validate: function(attrs, options) {
		var errors = [];
		
		if (attrs.modelId == null || attrs.modelId.length <= 0) {
			errors.push({
				attr: "modelId",
				message: "You must select a model"
			});
		}
		
		if (!this.isNew() && (attrs.name == null || attrs.name.length <= 0)) {
			errors.push({
				attr: "name",
				message: "Name cannot be blank"
			});
		}
		
		if (errors.length) {
			return errors;
		}
	}
});

window.ProcessCollection = Backbone.Collection.extend({
    model: Process,
    url: utils.contextRoot + "/rs/process"
});