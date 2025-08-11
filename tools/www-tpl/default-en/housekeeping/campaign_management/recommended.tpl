{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	     <h2 class="mt-4">Recommended groups tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if editingReco %}
		<p>Here you can edit a Recommended group.</p>
		{% for recommendedEdit in RecommendedEditList %}
		<form class="table-responsive col-md-4" method="post"><input type="hidden" name="sid" value="7">
			<div class="form-group">
				<label>Badge</label>
				<div><img src="{{ habboImagingPath }}/habbo-imaging/badge/{{ recommendedEdit.groupImage }}.gif" class="badge__group" /></div>
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
			<div class="form-group">
				<label>Set as Staff Pick?</label>
				<select name="setStaffPick" id="setStaffPick" class="form-control">
					<option value="1">Yes</option>
					<option value="0" selected>No</option>
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
				  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for recommended in RecommendedList %}
                <tr>
                  <td><img src="{{ habboImagingPath }}/habbo-imaging/badge/{{ recommended.groupImage }}.gif" class="badge__group" /></td>
				  <td>{{ recommended.ID }}</td>                               
				  <td>{{ recommended.groupName }}</td>                 
				  <td>{{ recommended.groupDescription }}</td>                 
				  <td>{{ recommended.groupOwner }}</td>                 			 
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
</body>
</html>