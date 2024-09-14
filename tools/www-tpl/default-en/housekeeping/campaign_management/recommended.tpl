{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	     <h2 class="mt-4">Recommended groups tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if editingReco %}
		<p>Here you can edit a Staff pick.</p>
		{% for recommendedEdit in RecommendedEditList %}
		<form class="table-responsive col-md-4" method="post"><input type="hidden" name="sid" value="7">
			<div class="form-group">
				<label>Badge</label>
				<div><img src="{{ site.habboImagingPath }}/habbo-imaging/badge/{{ recommendedEdit.groupImage }}.gif" class="badge__group" /></div>
			</div>
			<div class="form-group">
				<label>Name</label>
				<input type="text" class="form-control" placeholder="" value="{{ recommendedEdit.groupName }}" readonly />
			</div>
			<div class="form-group">
				<label>Description</label>
				<input type="text" class="form-control" placeholder="" value="{{ recommendedEdit.groupDescription }}" readonly />
			</div>
			<div class="form-group">
				<label>Owner</label>
				<input type="text" class="form-control" placeholder="" value="{{ recommendedEdit.groupOwner }}" readonly />
			</div>
			<div class="form-group">
				<label>ID</label>
				<input type="text" name="IdSave" class="form-control" id="IdSave" placeholder="Enter here the group ID..." value="{{ recommendedEdit.groupId }}" />
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
				<label>Type</label>
				<select name="typeSave" id="typeSave" class="form-control">
					<option value="GROUP" {% if recommendedEdit.type == 'GROUP' %}selected{% endif %}>Group</option>
					<option value="ROOM" {% if recommendedEdit.type == 'ROOM' %}selected{% endif %}>Room</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is Staff Pick?</label>
				<select name="isPickedSave" id="isPickedSave" class="form-control">
					<option value="1" {% if recommendedEdit.isPicked == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if recommendedEdit.isPicked == 0 %}selected{% endif %}>No</option>
				</select>
			</div>
			<button type="submit" value="">Save Pick</input>
		</form>
		{% endfor %}
		{% else %}
		<p>Here you can add a Recommended group.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Group ID</label>
				<input type="text" name="create" class="form-control" id="create" placeholder="Enter here the group ID..." value="" />
			</div>
			<button type="submit">Add Recommended</button>
		</form>
          <h2 class="mt-4">Edit Recommended groups</h2>
		  <p>The Recommended list is seen below.</p>
		  {% if RecommendedList|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Badge</th>
                  <th>Group ID</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Owner</th>
				  <th>Is Picked</th>
				  <th>Action</th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for recommended in RecommendedList %}
                <tr>
                  <td><img src="{{ site.habboImagingPath }}/habbo-imaging/badge/{{ recommended.groupImage }}.gif" class="badge__group" /></td>
				  <td>{{ recommended.ID }}</td>                               
				  <td>{{ recommended.groupName }}</td>                 
				  <td>{{ recommended.groupDescription }}</td>                 
				  <td>{{ recommended.groupOwner }}</td>                 			 
				  <td>{% if recommended.isPicked == 1 %}Yes{% else %}No{% endif %}</td>
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/recommended?edit={{ recommended.ID }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/recommended?delete={{ recommended.ID }}" style="color:black;"><button type="post">Delete</button></a>
				</td>
                </tr>
			   {% set num = num + 1 %}
			   
			   {% endfor %}
              </tbody>
            </table>
      </div>
	  {% else %}
	  <p>No Recommended groups found to display.</p>
	  {% endif %}
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