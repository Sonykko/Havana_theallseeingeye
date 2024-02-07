{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	     <h2 class="mt-4">Staff pick tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if editingPick %}
		<p>Here you can edit a Staff pick.</p>
		{% for staffPickEdit in EditStaffPick %}
		<form class="table-responsive col-md-4" method="post"><input type="hidden" name="sid" value="7">
			<div class="form-group">
				<label>Name</label>
				<input type="text" class="form-control" placeholder="" value="{{ staffPickEdit.name }}" readonly />
			</div>
			<div class="form-group">
				<label>Description</label>
				<input type="text" class="form-control" placeholder="" value="{{ staffPickEdit.description }}" readonly />
			</div>
			<div class="form-group">
				<label>Owner</label>
				<input type="text" class="form-control" placeholder="" value="{{ staffPickEdit.owner }}" readonly />
			</div>
			<div class="form-group">
				<label>Pick ID</label>
				<input type="text" name="IdSave" class="form-control" id="IdSave" placeholder="Enter here the pick ID..." value="{{ staffPickEdit.pickId }}" />
			</div>
			<!--<div class="form-group">
				<label>Pick ID</label>
				<input type="text" name="typeSave" class="form-control" id="typeSave" placeholder="Enter here the pick type..." value="{{ EditStaffPick.type }}" />
			</div>
			<div class="form-group">
				<label>Is Picked?</label>
				<input type="text" name="isPicked" class="form-control" id="isPicked" placeholder="Enter here if it are Staff Pick..." value="{{ EditStaffPick.isPicked }}" />
			</div>-->
			<div class="form-group">
				<label>Pick type</label>
				<select name="typeSave" id="typeSave" class="form-control">
					<option value="GROUP" {% if staffPickEdit.type == 'GROUP' %}selected{% endif %}>Group</option>
					<option value="ROOM" {% if staffPickEdit.type == 'ROOM' %}selected{% endif %}>Room</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is Staff Pick?</label>
				<select name="isPickedSave" id="isPickedSave" class="form-control">
					<option value="1" {% if staffPickEdit.isPicked == '1' %}selected{% endif %}>Yes</option>
					<option value="0" {% if staffPickEdit.isPicked == '0' %}selected{% endif %}>No</option>
				</select>
			</div>
			<button type="submit" value="">Save Pick</input>
		</form>
		{% endfor %}
		{% else %}
		<p>Here you can add a Staff pick.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Pick ID</label>
				<input type="text" name="create" class="form-control" id="create" placeholder="Enter here the pick ID..." value="" />
			</div>
			<div class="form-group">
				<label>Pick type</label>
				<select name="type" id="type" class="form-control">
					<option value="GROUP">Group</option>
					<option value="ROOM">Room</option>
				</select>
			</div>
			<button type="submit">Create Pick</button>
		</form>
          <h2 class="mt-4">Edit Staff picks</h2>
		  <p>The Staff Picks list is seen below.</p>
          <div class="table-responsive" style="padding-left: 15px;">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Pick ID</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Owner</th>
				  <th>Is Picked</th>
				  <th>Action</th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for staffPick in StaffPicksList %}
                <tr>
                  <td>{{ staffPick.type }}</td>
				  <td>{{ staffPick.ID }}</td>                               
				  <td>{{ staffPick.groupName }}{{ staffPick.roomName }}</td>                 
				  <td>{{ staffPick.groupDescription }}{{ staffPick.roomDescription }}</td>                 
				  <td>{{ staffPick.groupOwner }}{{ staffPick.roomOwner }}</td>                 			 
				  <td>{% if staffPick.isPicked == 1 %}Yes{% else %}No{% endif %}</td>
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/staff_picks?edit={{ staffPick.ID }}&type={{ staffPick.type }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/staff_picks?delete={{ staffPick.ID }}&type={{ staffPick.type }}" style="color:black;"><button type="button">Delete</button></a>
				</td>
                </tr>
			   {% set num = num + 1 %}
			   
			   {% endfor %}
              </tbody>
            </table>
      </div>
	  {% endif %}
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