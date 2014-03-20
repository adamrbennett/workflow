window.FileListItemView = Backbone.View.extend({

    tagName: "tr",

	model: File,
	
	initialize: function (options) {
		// bind
    	_.bindAll(this, "fileProgress", "fileSuccess", "fileComplete");
        this.model.bind("change", this.render, this);

        if (options.readonly) {
        	this.readonly = true;
        }
        
        this.render();
    },

    render: function () {
    	
    	// render template
        this.$el.html(this.template(this.model.toJSON()));
        
        var progressBar = this.$(".file-progress");
        var complete = this.$(".file-complete");
        var remove = this.$(".file-remove");
        
        var fileName = this.model.get("fileName");
        var tempName = this.model.get("tempName");
        var percentLoaded = this.model.get("percentLoaded");
        
        // hide link if there is no file url
        if (_.isNull(this.model.get("url"))) {
        	this.$(".file-link").hide();
        	this.$(".file-label").show();
        }
        
        // if the file exists
        if (fileName || tempName) {
        	complete.show();
        	if (!this.readonly)
        		remove.show();
        } else if (percentLoaded > 0) {
        	progressBar.show();
        }
        
        return this;
    },
    
    events: {
    	"change #file"			: "change",
    	"click .file-remove"	: "removeFile"
    },
    
    change: function(event) {
		var self = this;
		
    	// get the file
    	this.file = event.currentTarget.files[0];
		
    	// set the file name on the model
		this.model.set("fileName", this.file.name);
    	
		// update view
    	this.$(".file-progress").show();
		
    	// upload the file
		utils.uploadFile(this.file, this.fileProgress, this.fileSuccess, this.fileComplete);
    },
    
    fileProgress: function(event) {
		var percentage = 100;
		if (event.lengthComputable) {
			percentage = Math.round((event.loaded * 100) / event.total);
		}
		this.$(".progress-bar").attr("aria-valuenow", percentage);
		this.$(".progress-bar").width(percentage + "%");
	},
	
	fileSuccess: function(event) {
		this.$(".file-progress").hide();
		this.$(".file-complete").show();
	},
	
	fileComplete: function(responseText) {
    	// parse the temporary file name and mime type
    	var resp = responseText.split(";");
    	
    	// set temporary name and mime type on the model
    	this.model.set("tempName", resp[0]);
    	this.model.set("fileType", resp[1]);
	},
    
    removeFile: function() {
    	// destroy the model without sending request to server
    	this.model.trigger("destroy", this.model);
    	
    	// remove the view
    	this.remove();
    }

});