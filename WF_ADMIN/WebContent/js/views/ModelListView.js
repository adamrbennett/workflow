window.ModelListView = Backbone.View.extend({

    initialize:function () {
        this.render();
    },

    render:function () {
    	var models = this.model;
    	
    	// render template
        this.$el.html(this.template());
        
        // get the list table
        var tbl = this.$("table");
        
        // add the models to the table
        models.each(function(model) {
        	tbl.append(new ModelListItemView({model: model}).render().el);
        });
        
        return this;
    }

});

window.ModelListItemView = Backbone.View.extend({

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

window.ModelOptionView = Backbone.View.extend({
	
	tagName: "option",
	
	initialize: function() {
        this.model.bind("change", this.render, this);
        this.model.bind("destroy", this.close, this);
	},
	
	render: function() {
		this.$el.attr("value", this.model.get("modelId"));
		this.$el.html(this.model.get("name"));
		
		return this;
	}
});