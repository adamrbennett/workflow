// setup ajax
$.ajaxSetup({
	
	// check every ajax response for the Login header,
	// which indicates the user needs to re-authenticate
	complete: function(xhr) {
		var loginHeader = xhr.getResponseHeader("Login");
		if (loginHeader != null && loginHeader == "true") {
			
			// invalidate user session and redirect to app root
			$.get(utils.contextRoot + "/logout", function() {
				
				// send the user to the app root,
				// which will present the login page then redirect
				window.location = utils.contextRoot;
			});
		}
	}
});

//setup pub/sub
var topics = {};
jQuery.Topic = function(id) {
	var callbacks, topic = id && topics[id];
	
	if (!topic) {
		callbacks = jQuery.Callbacks();
		topic = {
			publish: callbacks.fire,
			subscribe: callbacks.add,
			unsubscribe: callbacks.remove
		};
		if (id) {
			topics[id] = topic;
		}
	}
	return topic;
};

// setup tooltips
$("[rel='tooltip']").tooltip();

// setup backbone
var AppRouter = Backbone.Router.extend({

    routes: {
    	""									:	"welcome",
        "process/list"						:	"processList",
        "process/add"						:	"processAdd",
        "process/:id"						:	"processRead",
        "process/:processId/task/:taskId"	:	"taskRead",
        "model/list"						:	"modelList",
        "model/add"							:	"modelAdd",
        "model/:id"							:	"modelRead"
    },

    initialize: function () {
    	var user = new User();
    	user.fetch({success: function() {
    		$(".user-name").text(user.get("fullName"));
    	}});
//    	var socket = new WebSocket("ws://" + utils.server + ":" + utils.port + utils.contextRoot + "/echo");
//    	socket.onmessage = function(event) {
//    		utils.growl("Server says", "Hello, I'll keep you up to date!", "info", "glyphicon glyphicon-bullhorn");
//    	};
    },
    
    welcome: function() {
    	if (!this.welcomeView) {
    		this.welcomeView = new WelcomeView();
    	}
    	$("#content").html(this.welcomeView.el);
    },
    
    processList: function() {
    	var col = new ProcessCollection();
    	col.fetch({success: function() {
    		$("#content").html(new ProcessListView({model: col}).el);
    	}});
    	utils.setActiveMenu("process-menu");
    },
    
    processAdd: function() {
    	var process = new Process();
    	$("#content").html(new ProcessForm({model: process}).el);
    	utils.setActiveMenu("process-menu");
    },
    
    processRead: function(id) {
    	var process = new Process({processId: id});
    	process.fetch({success: function() {
    		$("#content").html(new ProcessForm({model: process}).el);
    	}});
    	utils.setActiveMenu("process-menu");
    },

    modelList: function () {
    	var col = new ModelCollection();
		col.fetch({success: function(){
			$("#content").html(new ModelListView({model: col}).el);
		}});
    	utils.setActiveMenu("model-menu");
    },
    
    modelAdd: function() {
    	var model = new Model();
    	$('#content').html(new ModelForm({model: model}).el);
    	utils.setActiveMenu("model-menu");
    },
    
    modelRead: function(id) {
    	var model = new Model({modelId: id});
		model.fetch({success: function(){
			$("#content").html(new ModelForm({model: model}).el);
		}});
    	utils.setActiveMenu("model-menu");
    },
    
    taskRead: function(processId, taskId) {
    	var task = new Task({processId: processId, taskId: taskId});
    	task.fetch({success: function(model) {
    		$("#content").html(new TaskForm({model: model}).el);
    	}});
    	utils.setActiveMenu("process-menu");
    }

});

// load underscore templates
utils.loadTemplate(
	[
	 	["forms", "ProcessForm"],
	 	["forms", "ModelForm"],
	 	["forms", "TaskForm"],
	 	
	 	["views", "FileListView"],
	 	["views", "FileListItemView"],
	 	["views", "FormControlsView"],
	 	["views", "FormMessagesView"],
	 	["views", "ProcessListView"],
	 	["views", "ProcessListItemView"],
	 	["views", "ModelListView"],
	 	["views", "ModelListItemView"],
	 	["views", "TaskListView"],
	 	["views", "TaskListItemView"],
	 	["views", "WelcomeView"]
	],
	function() {
	    app = new AppRouter();
	    Backbone.history.start();
	}
);