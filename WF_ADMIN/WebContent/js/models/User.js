window.User = Backbone.Model.extend({
	urlRoot: utils.contextRoot + "/rs/user",
	idAttribute: "userName"
});