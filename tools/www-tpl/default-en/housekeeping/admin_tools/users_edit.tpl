{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Edit User</h2>		
		<p>Here you can edit user details.</p>
		{% include "housekeeping/base/alert.tpl" %}
		{% autoescape 'html' %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Username:</label>
				<input type="text" class="form-control" id="text" name="username"  value="{{ playerUsername }}">
			</div>
			<div class="form-group">
				<label for="pwd">Email:</label>
				<input type="email" class="form-control" name="email" value="{{ playerEmail }}">
			</div>
			<div class="form-group">
				<label for="pwd">Look/figure:</label>
				<input type="text" class="form-control" name="figure" value="{{ playerFigure }}">
			</div>
			<div class="form-group">
				<label for="pwd">Motto:</label>
				<input type="text" class="form-control" name="motto" value="{{ playerMotto }}">
			</div>
			<div class="form-group">
				<label for="pwd">Credits:</label>
				<input type="text" class="form-control" name="credits" value="{{ playerCredits }}">
			</div>
			<div class="form-group">
				<label for="pwd">Pixels:</label>
				<input type="text" class="form-control" name="pixels" value="{{ playerPixels }}">
			</div>
			<div class="form-group"> 
				<input type="hidden" id="text" name="id" value="{{ playerId }}">
				<button type="submit">Save Details</button>
			</div>
		</form>
		{% endautoescape %}
      </div>
    </div>
  </div>
</body>
</html>