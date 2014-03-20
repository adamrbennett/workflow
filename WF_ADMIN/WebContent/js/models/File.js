window.File = Backbone.Model.extend({
	defaults: {
		fileName: null,
		fileType: null,
		tempName: null,
		url: null,
		percentLoaded: 0
	},
	
	initialize: function() {
		_.bindAll(this, "fileProgress", "fileSuccess", "fileComplete");
	},
	
	events: {
		
	},

	upload: function() {
		var file = this.get("file");
		
		// save the file name
		this.fileName = file.name;
		
		// upload the file
		utils.uploadFile(file, this.fileProgress, this.fileSuccess, this.fileComplete);
	},
	
	fileProgress: function(event) {
		var percentage = 100;
		if (event.lengthComputable) {
			percentage = Math.round((event.loaded * 100) / event.total);
		}
		this.set("percentLoaded", percentage);
	},
	
	fileSuccess: function(event) {
	},
	
	fileComplete: function(responseText) {
    	// parse the temporary file name and mime type
    	var resp = responseText.split(";");
    	
    	// set model data
    	this.set("tempName", resp[0]);
    	this.set("fileType", resp[1]);
		this.set("fileName", this.fileName);
	}
});

window.FileCollection = Backbone.Collection.extend({
    model: File
});