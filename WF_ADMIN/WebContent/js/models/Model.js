window.Model = Backbone.Model.extend({
	urlRoot: utils.contextRoot + "/rs/model",
	idAttribute: "modelId",
	
	defaults: {
		modelId: null,
		createdOn: null,
		createdBy: null,
		modifiedOn: null,
		modifiedBy: null,
		name: null,
		versionNumber: 1,
		fileName: null,
		fileType: null,
		fileData: null,
		tempName: null
	},
	
	validate: function(attrs, options) {
		var errors = [];
		
		if (attrs.name == null || attrs.name.length <= 0) {
			errors.push({
				attr: "name",
				message: "You must enter a name"
			});
		}
		
		if (attrs.fileName == null || attrs.fileName == "") {
			errors.push({
				attr: "file",
				message: "You must upload a model definition"
			});
		}
		
		if (errors.length) {
			return errors;
		}
	}
});

window.ModelCollection = Backbone.Collection.extend({
    model: Model,
    url: utils.contextRoot + "/rs/model"
});