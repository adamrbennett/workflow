window.ProcessListView = Backbone.View.extend({

    initialize:function () {
        this.render();
    },

    render:function () {
    	var processes = this.model;
    	
    	// render template
        this.$el.html(this.template());
        
        // get the list table
        var tbl = this.$("table");
        
        // add the processes to the table
        processes.each(function(process) {
        	tbl.append(new ProcessListItemView({model: process}).render().el);
        });
        
        return this;
    }

});

window.ProcessListItemView = Backbone.View.extend({

    tagName: "tr",

    initialize: function () {
        this.model.bind("change", this.render, this);
        this.model.bind("destroy", this.close, this);
    },

    render: function () {
        this.$el.html(this.template(this.model.toJSON()));
        
        this.$(".modifiedOn").text(utils.formatTimestamp(this.model.get("modifiedOn")));
        
        return this;
    }

});