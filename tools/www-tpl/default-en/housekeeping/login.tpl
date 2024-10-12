<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">
		<link rel="icon" href="../../favicon.ico">
		<title>{{ site.siteName }} Housekeeping (ES)</title>
		<link href="{{ site.staticContentPath }}/public/hk/css/bootstrap.login.css" rel="stylesheet">
		<link href="{{ site.staticContentPath }}/public/hk/css/bootstrap.login.override.css" rel="stylesheet">
		<link href="{{ site.staticContentPath }}/public/hk/css/sticky-footer.css" rel="stylesheet">
		{% if site.hkNewStyle %}
		<link href="{{ site.staticContentPath }}/public/hk/css/hk_scale.css" rel="stylesheet">
		{% endif %}
	</head>
	<body>	
		<div class="page-container">
			<div class="login-container">
				<div class="hkw-text">
					<h4 class="hkw-title">Welcome Hobbas</h4>
					<p>This service is meant for registered Hobbas. The service is monitored closely and we will pursue and charge any unauthorised users.</p>
					<p class="hkw-end">Your username and password to the tool are personal. <b>Never</b> give them to anyone under <b>any</b> situation.</p>
				</div>
				<div class="login-data-box">
					<text class="login-title">Log in</text>	
					<div class="box">
						<form class="form-signin login-box" action="/ase/housekeeping/es/login" method="post">
							<label for="inputUsername" class="sr-only">Username</label>Username:
							<input type="text" name="hkusername" id="inputUsername" class="form-control">
							<label for="inputPassword" class="sr-only">Password</label>Password:
							<input type="password" name="hkpassword" id="inputPassword" class="form-control">
							<button type="submit">Log in</button>				
						</form>
					</div>
				</div>		
			</div>
			{% include "housekeeping/base/alert.tpl" %}
		</div>
		<script src="{{ site.staticContentPath }}/public/hk/js/ie10-viewport-bug-workaround.js"></script>
		{% include "housekeeping/base/footer.tpl" %}
	</body>
</html>
