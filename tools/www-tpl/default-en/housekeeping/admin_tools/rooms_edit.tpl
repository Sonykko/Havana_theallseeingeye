{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Room Admin</h2>		
		<p>Here you can edit room details.</p>
		{% include "housekeeping/base/alert.tpl" %}
		{% for RoomAdminData in RoomAdminData %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>ID:</label>
				<input type="text" class="form-control" id="text" name="roomId"  value="{{ RoomAdminData.id }}">
			</div>
			<div class="form-group">
				<label>Category:</label>
				<input type="text" class="form-control" id="text" name="roomId"  value="{{ RoomAdminData.category }}">
			</div>
			<div class="form-group">
				<label for="pwd">Name:</label>
				<input type="text" class="form-control" name="name" value="{{ RoomAdminData.name }}">
			</div>
			<div class="form-group">
				<label for="pwd">Description:</label>
				<input type="text" class="form-control" name="description" value="{{ RoomAdminData.description }}">
			</div>
			<div class="form-group">
				<label for="pwd">Access type:</label>
				<input type="text" class="form-control" name="accesstype" value="{{ RoomAdminData.accesstype }}">
			</div>
			<div class="form-group">
				<label for="pwd">Onwer ID:</label>
				<input type="text" class="form-control" name="ownerId" value="{{ RoomAdminData.ownerId }}">
			</div>
			<div class="form-group"> 
				<input type="hidden" id="text" name="id" value="{{ playerId }}">
				<button type="submit">Save Details</button>
			</div>
		</form>
		{% endfor %}
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

{% include "housekeeping/base/footer.tpl" %}