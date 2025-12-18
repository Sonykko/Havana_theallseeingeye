{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set adminToolsActive = "active" %}
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
					{% for roomCat in roomCats %}
					<option value="{{ roomCat.id }}" {% if room.getData().getCategoryId() == roomCat.id %}selected{% endif %}>{{ roomCat.categoryName }}</option>
					{% endfor %}
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
			<div class="form-group">
				<label style="display: flex;align-items: center;user-select: none;">
					<input type="checkbox" class="form-control" name="showOwnerName" {% if room.getData().showOwnerName() %}checked{% endif %} style="width: fit-content;box-shadow: none;margin-right: 5px;">Show owner.</input>
				</label>
			</div>
			<button type="submit">Save Details</button>
		</form>
		{% endfor %}
      </div>
    </div>
  </div>
</body>
</html>