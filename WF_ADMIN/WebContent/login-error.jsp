<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	response.setHeader("Login", "true");
%>
<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
	<meta charset="ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/style.css">
</head>
<body>

	<br><br><br>
	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="alert alert-danger">
					<strong>Login Error</strong><br>
					The user name or password entered is incorrect
				</div>
				<div class="panel panel-primary">
					<div class="panel-heading">Login to Workflow Admin</div>
					<div class="panel-body">
						<form role="form" method="post" action="j_security_check" onsubmit="submitHandler()">
							<div class="form-group">
								<label class="control-label" for="userName">User Name</label>
								<input type="text" name="j_username" class="form-control" id="userName" placeholder="User Name" autofocus="autofocus">
								<span class="help-block"></span>
							</div>
							<div class="form-group">
								<label class="control-label" for="password">Password</label>
								<input type="password" name="j_password" class="form-control" id="password" placeholder="Password">
								<span class="help-block"></span>
							</div>
							<div class="form-controls">
								<button type="submit" class="btn btn-primary">Login</button>
							</div>
						</form>
					</div>
					<div class="panel-footer">
						<div style="display:inline-block">
							<label>
								<input id="remember" type="checkbox">
								Remember me
							</label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery.js"></script>
	<script src="lib/jquery.cookie.js"></script>
	<script src="lib/bootstrap.min.js"></script>
	
	<script type="text/javascript">
		$.removeCookie("wf", {path: "/"});
		
		function submitHandler() {
			if ($("#remember").prop("checked")) {
				var val = $("#userName").prop("value") + ":" + $("#password").prop("value");
				$.cookie("wf", val, {expires: 30, path: "/"});
			}
		}
	</script>
</body>
</html>