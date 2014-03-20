window.InputFile = Backbone.Model.extend({
	idAttribute: "fileId",
	defaults: {
		processId: null,
		taskId: null,
		inputId: null,
		fileId: null,
		fileName: null,
		fileType: null,
		fileData: null,
		tempName: null
	}
});