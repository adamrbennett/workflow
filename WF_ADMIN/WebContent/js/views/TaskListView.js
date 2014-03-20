window.TaskListView = Backbone.View.extend({

    initialize:function () {
        this.render();
    },

    render:function () {
    	var tasks = this.model;
    	
    	// render template
        this.$el.html(this.template());
        
        // get the task table
        var tbl = this.$("table");
        
        // add tasks to table
        tasks.each(function(task) {
        	tbl.append(new TaskListItemView({model: task}).render().el);
        });
        
        return this;
    },
    
    events: {
    	"click .complete"	: "complete"
    },
    
    complete: function(event) {
    	var target = $(event.target);
    	var processId = target.attr("data-process-id");
    	var taskId = target.attr("data-task-id");
    	
    	var self = this;
    	
    	this.model.get(taskId).save({action: "complete"}, {
    		success: function() {
    			self.model.trigger("complete");
    		},
    		error: function() {
    			utils.growl("Error", "The task could not be completed", "error");
    		}
    	});
    }

});