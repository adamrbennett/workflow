window.FileListView = Backbone.View.extend({

	/**
	 * @memberOf FileListView
	 */
	model: FileCollection,
	
	initialize: function (options) {
		// bind
		this.model.bind("remove", this.render, this);
        this.model.bind("change", this.render, this);
        
        if (options.multiple) {
        	this.multiple = true;
        }
        if (options.readonly) {
        	this.readonly = true;
        }
        
		this.render();
    },

    render: function () {
    	
    	var self = this;
    	
    	var files = this.model;
    	
    	// render template
        this.$el.html(this.template(this.model.toJSON()));
        
        // get the file table
        var tbl = this.$("table");
        
        // add files to the file table
        files.each(function(file) {
        	tbl.append(new FileListItemView({model: file, readonly: self.readonly}).render().el);
        });
        
        var input = this.$(".file-input");
        var choose = this.$(".file-choose");
        var count = this.$(".file-count");
    	
        // set single/multiple behavior
    	var label = "Choose File";
    	if (this.multiple) {
    		label = "Choose Files";
    		input.attr("multiple", "multiple");
    		count.show();
    	}
    	choose.text(label);
    	
    	if (this.readonly) {
    		choose.hide();
    		count.hide();
    	}
        
        return this;
    },
    
    events: {
    	"change .file-input"	: "changeFile",
    	"click .file-choose"	: "chooseFile"
    },
    
    chooseFile: function(event) {
    	this.$(".file-input").click();
    	event.preventDefault();
    },
    
    changeFile: function(event) {
    	
    	// if single-select, clear any existing files
    	if (!this.multiple) {
    		this.model.reset();
    	}
    	
    	// if the user selected one or more files
    	var size = _.size(event.currentTarget.files);
    	if (size > 0) {
	    	var self = this;
	        
	        // get the file table
	        var tbl = this.$("table");
			
	    	// get each file and add to the table
			_.each(event.currentTarget.files, function(f, idx) {
				// add the file to the collection
		        var file = new File({file: f});
		        file = self.model.add(file);
		        
		        // create a new file item
		        var view = new FileListItemView({model: file});
		        
		        // add the file item view
		        tbl.append(view.el);
		        
		        // upload the file for temporary storage
		        file.upload();
			});
    	}
    }
    
});