{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Room Admin</h2>		
		<p>Here you can edit room details.</p>
		{% include "housekeeping/base/alert.tpl" %}
		{% for room in RoomAdminData %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>ID:</label>
				<input type="text" class="form-control" id="text" name="roomId"  value="{{ room.getData().getId() }}" readonly>
			</div>
			<div class="form-group">
				<label>Category:</label>
				<select name="category" id="category" class="form-control">
					<option value="2" {% if room.getData().getCategoryId() == 2 %}selected{% endif %}>Sin categoría</option>
					<option value="101" {% if room.getData().getCategoryId() == 101 %}selected{% endif %}>Selección Staff HQ</option>
					<option value="112" {% if room.getData().getCategoryId() == 112 %}selected{% endif %}>5 Planta: Fiestas</option>
					<option value="113" {% if room.getData().getCategoryId() == 113 %}selected{% endif %}>3 Planta: Cambios</option>
					<option value="114" {% if room.getData().getCategoryId() == 114 %}selected{% endif %}>1 Planta: Charla</option>
					<option value="115" {% if room.getData().getCategoryId() == 115 %}selected{% endif %}>7 Planta: Salones de Belleza & Modelaje</option>
					<option value="116" {% if room.getData().getCategoryId() == 116 %}selected{% endif %}>6 Planta: Laberintos & Parques Temáticos</option>
					<option value="117" {% if room.getData().getCategoryId() == 117 %}selected{% endif %}>2 Planta: Juegos</option>
					<option value="118" {% if room.getData().getCategoryId() == 118 %}selected{% endif %}>4 Planta: Ayuda</option>
					<option value="120" {% if room.getData().getCategoryId() == 120 %}selected{% endif %}>Varios</option>
					<option value="121" {% if room.getData().getCategoryId() == 121 %}selected{% endif %}>Puzzle del Poder de las Flores</option>
				</select>
			</div>
			<div class="form-group">
				<label for="pwd">Name:</label>
				<input type="text" class="form-control" name="name" value="{{ room.getData().getName() }}">
			</div>
			<div class="form-group">
				<label for="pwd">Description:</label>
				<textarea class="form-control" name="description" placeholder="Enter here the room description...">{{ room.getData().getDescription() }}</textarea>
			</div>
			<div class="form-group">
				<label for="pwd">Access type:</label>
				<select name="accesstype" id="accesstype" class="form-control">
					<option value="0" {% if room.getData().getAccessTypeId() == 0 %}selected{% endif %}>Open - anyone can enter</option>
					<option value="1" {% if room.getData().getAccessTypeId() == 1 %}selected{% endif %}>Visitors have to ring the doorbell</option>
					<option value="2" {% if room.getData().getAccessTypeId() == 2 %}selected{% endif %}>Password is required to enter this room</option>
				</select>
			</div>
			<div class="form-group">
				<label for="pwd">Password (only if the access type is Password):</label>
				<input type="password" class="form-control" name="password" value="{{ room.getData().getPassword() }}">
			</div>
			<div class="form-group">
				<label for="pwd">Owner:</label>
				<input type="text" class="form-control" name="ownerId" value="{{ room.getData().getOwnerName() }} (id: {{ room.getData().getOwnerId() }})" readonly>
			</div>
			<button type="submit">Save Details</button>
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