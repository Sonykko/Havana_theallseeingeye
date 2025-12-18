{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set campaignManagementActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	     <h2 class="mt-4">Staff pick tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if editingPick %}
		<p>Here you can edit a Staff pick.</p>
		<form class="table-responsive col-md-4" method="post"><input type="hidden" name="sid" value="7">
			<div class="form-group">
				<label>Name</label>
				<input type="text" class="form-control" placeholder="" value="{{ EditStaffPick.getRoomName() }}{{ EditStaffPick.getGroupName() }}" readonly />
			</div>
			<div class="form-group">
				<label>Description</label>
				<input type="text" class="form-control" placeholder="" value="{{ EditStaffPick.getRoomDescription() }}{{ EditStaffPick.getGroupDescription() }}" readonly />
			</div>
			<div class="form-group">
				<label>Owner</label>
				<input type="text" class="form-control" placeholder="" value="{{ EditStaffPick.getRoomOwner() }}{{ EditStaffPick.getGroupOwner() }}" readonly />
			</div>
			<div class="form-group">
				<label>Pick ID</label>
				<input type="text" name="IdSave" class="form-control" id="IdSave" placeholder="Enter here the pick ID..." value="{{ EditStaffPick.getPickRecoId() }}" />
			</div>
			<div class="form-group">
				<label>Pick type</label>
				<select name="typeSave" id="typeSave" class="form-control">
					<option value="GROUP" {% if EditStaffPick.getType() == 'GROUP' %}selected{% endif %}>Group</option>
					<option value="ROOM" {% if EditStaffPick.getType()  == 'ROOM' %}selected{% endif %}>Room</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is Staff Pick?</label>
				<select name="isPickedSave" id="isPickedSave" class="form-control">
					<option value="1" {% if EditStaffPick.getIsPicked() == '1' %}selected{% endif %}>Yes</option>
					<option value="0" {% if EditStaffPick.getIsPicked() == '0' %}selected{% endif %}>No</option>
				</select>
			</div>
			<button type="submit" value="">Save Pick</input>
		</form>
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
		  {% if StaffPicksList|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Pick ID</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Owner</th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for staffPick in StaffPicksList %}
                <tr>
                  <td>{{ staffPick.getType() }}</td>
				  <td>{{ staffPick.getPickRecoId() }}</td>                               
				  <td>{{ staffPick.getGroupName() }}{{ staffPick.getRoomName() }}</td>                 
				  <td>{{ staffPick.getGroupDescription() }}{{ staffPick.getRoomDescription() }}</td>                 
				  <td>{{ staffPick.getGroupOwner() }}{{ staffPick.getRoomOwner() }}</td>                 			 
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/staff_picks?edit={{ staffPick.getPickRecoId() }}&type={{ staffPick.getType() }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/staff_picks?delete={{ staffPick.getPickRecoId() }}&type={{ staffPick.getType() }}" style="color:black;"><button type="button">Delete</button></a>
				</td>
                </tr>
			   {% set num = num + 1 %}
			   
			   {% endfor %}
              </tbody>
            </table>
      </div>
	  {% else %}
	  <p>No Staff Picks found to display.</p>
	  {% endif %}	  
	  {% endif %}
    </div>
  </div>
</body>
</html>