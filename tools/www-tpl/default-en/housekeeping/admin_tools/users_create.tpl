{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Create User</h2>		
		<p>Enter the details to create a new user.</p>
		{% include "housekeeping/base/alert.tpl" %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Username:</label>
				<input type="text" class="form-control" id="text" placeholder="Enter username" name="username">
			</div>
			<div class="form-group">
				<label>Password:</label>
				<input type="password" class="form-control" placeholder="Enter password" name="password">
			</div>
			<div class="form-group">
				<label>Confirm Password:</label>
				<input type="password" class="form-control" placeholder="Enter password" name="confirmpassword">
			</div>
			<div class="form-group">
				<label for="pwd">Email:</label>
				<input type="email" class="form-control" placeholder="Enter email" name="email">
			</div>
			<div class="form-group">
				<label for="pwd">Look/figure:</label>
				<input type="text" class="form-control" name="figure" value="{{ defaultFigure }}">
			</div>
			<div class="form-group">
				<label for="pwd">Mission:</label>
				<input type="text" class="form-control" placeholder="Enter mission" name="mission" value="{{ defaultMission }}">
			</div>
			<div class="form-group"> 
					<button type="submit">Submit</button>
			</div>
		</form>
      </div>
    </div>
  </div>
  <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"></script>
  <script src="https://blackrockdigital.github.io/startbootstrap-simple-sidebar/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  </script>
</body>
</html>