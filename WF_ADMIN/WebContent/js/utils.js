window.utils = {
	
	server: "localhost",
	port: "8080",
	contextRoot: "/wf/admin",

    // Asynchronously load templates located in separate .html files
    loadTemplate: function(views, callback) {

        var deferreds = [];

        $.each(views, function(index, view) {
        	var viewDir = view[0];
        	var viewName = view[1];
            if (window[viewName]) {
                deferreds.push($.get('templates/' + viewDir + '/' + viewName + '.html', function(data) {
                    window[viewName].prototype.template = _.template(data);
                }));
            } else {
                alert(viewName + " not found");
            }
        });

        $.when.apply(null, deferreds).done(callback);
    },
    
    growl: function(title, message, type, icon) {
    	$.pnotify({
			title: title,
			text: message,
			type: type,
			icon: icon,
			shadow: false,
			sticker: false,
			history: false
    	});
    },
    
    formatDate: function(arg) {
    	if (_.isNull(arg)) {
    		return "";
    	}
    	
    	var d = null;
    	var t = $.type(arg);
    	
    	if (t == "number") {
    		d = new Date(arg);
    	} else {
    		return "InvalidDateType";
    	}
    	
    	var month = d.getMonth()+1;
    	var day = d.getDate();
    	var year = d.getFullYear();
    	
    	var monthText = month.toString();
    	var dayText = day.toString();
    	var yearText = year.toString();
    	
    	if (monthText.length == 1)
    		monthText = "0" + monthText;
    	if (dayText.length == 1)
    		dayText = "0" + dayText;
    	
    	return monthText + "/" + dayText + "/" + yearText;
    },
	
	formatTime: function(arg) {
    	if (_.isNull(arg)) {
    		return "";
    	}
    	
    	var d = null;
    	var t = $.type(arg);
    	
    	if (t == "number") {
    		d = new Date(arg);
    	} else {
    		return "InvalidDateType";
    	}
    	
		var hours = d.getHours();
		var minutes = d.getMinutes();
		var period = "AM";
		
		if (hours >= 12)
			period = "PM";
		
		if (hours > 12)
			hours -= 12;
		
		var hoursText = hours.toString();
		var minutesText = minutes.toString();
		
		if (hoursText.length == 1)
			hoursText = "0" + hoursText;
		if (minutesText.length == 1)
			minutesText = "0" + minutesText;
		
		return hoursText + ":" + minutesText + " " + period;
	},
	
	formatTimestamp: function(arg) {
    	if (_.isNull(arg)) {
    		return "";
    	}
    	
		return utils.formatDate(arg) + " " + utils.formatTime(arg);
	},
    
    setActiveMenu: function(menuClass) {
    	$(".menu").removeClass("active");
    	$("." + menuClass).addClass("active");
    },
    
    uploadFile: function(file, onProgress, onSuccess, onComplete) {
		// create FormData for posting multipart and add file
		var data = new FormData();
		data.append("file", file);
		
		// create POST request for file upload
		var xhr = new XMLHttpRequest();
		xhr.open("POST", utils.contextRoot + "/upload", true);
		
		// progress event to update progress bar
		xhr.upload.onprogress = onProgress;
		
		// onload fires for successful tranfers
		xhr.upload.onload = onSuccess;
		
		// check for completion
		xhr.onreadystatechange = function () {
		    if (xhr.readyState === 4 && xhr.status === 200) {
		    	onComplete(xhr.responseText);
		    }
		};
    	
    	// send the POST request
		xhr.send(data);
    }
};