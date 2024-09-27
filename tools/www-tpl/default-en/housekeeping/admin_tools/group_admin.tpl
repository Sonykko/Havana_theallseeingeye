{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% set ruffleActive = "active" %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Group admin</h2>				  		
		  {% include "housekeeping/base/alert.tpl" %}
		  <br />
		  <p><b>Find group</b></p>
		  <p>This tool allows you to search a groups with name starting or owned by.</p>
		<form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
			<div class="">
				<label>Search</label>
				{% autoescape 'html' %}
				<input type="text" name="searchStr" id="searchStr" />
				{% endautoescape %}
			</div>
			<button type="submit" name="action" value="searchGroup">Submit</button>
		</form>		  
		  {% if searchGroupDetails|length > 0 %}
		  <br />
		  <p>Groups with name starting with '{{ query }}' or owned by '{{ query }}'</p>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Owner</th>
							<th>Group name and description</th>							
							<th>Room ID</th>
							<th>Badge</th>
							<th>Alias</th>
							<th>Type</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for group in searchGroupDetails %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="groupId" value="{{ group.getId() }}" />
								<td>{{ group.getOwnerName()}}</td>
								<td>
									<text><b>Name</b></text>
									<br />
									<input type="text" name="groupName" value="{{ group.getName() }}" style="width: 100%" />
									<br />
									<text><b>Description</b></text>
									<br />
									<input type="text" name="groupDesc" value="{{ group.getDescription() }}" style="width: 100%" />
								</td>
								<td><input type="text" name="groupRoom" value="{{ group.getRoomId() }}" /></td>
								<td style="text-wrap: nowrap;">
									<img src="{{ site.sitePath }}/habbo-imaging/badge/{{ group.getBadge() }}.gif" class="badge-preview" /> - <a href="#" class="edit-badge" data-id="{{ group.getId }}" data-toggle="modal" data-target="#badgeEditorModal">Edit</a>
								</td>
								<td><input type="text" name="groupAlias" value="{{ group.getAlias() }}" /></td>
								<td>	
									<select name="groupType" id="groupType">
										<option value="0" {% if group.getGroupType == 0 %}selected{% endif %}>Regular</option>
										<option value="1" {% if group.getGroupType == 1 %}selected{% endif %}>Exclusive</option>
										<option value="2" {% if group.getGroupType == 2 %}selected{% endif %}>Private</option>
									</select>
								</td>									
								<td><button type="submit" name="action" value="groupSave">Save</button></td>
							</form>
						{% endautoescape %}
						</tr>
						{% set num = num + 1 %}
						{% endfor %}
					</tbody>
				</table>
				{% endif %}
			{% if searchEmpty %}
			<br>
			<p><i>No results found to display.</i></p>
			{% endif %}
		  <hr />
		  <p><b>List of current groups</b></p>
		  <p>This tool allow you to see the complete list of all current groups in the Hotel.</p>
		  <div class="pagination-buttons-box">
			{% if nextAllGroupDetails|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/group_admin?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousAllGroupDetails|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/group_admin?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>			
		<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Owner</th>
							<th>Group name and description</th>							
							<th>Room ID</th>
							<th>Badge</th>
							<th>Alias</th>
							<th>Type</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for group in allGroupDetails %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="groupId" value="{{ group.getId() }}" />
								<td>{{ group.getOwnerName()}}</td>
								<td>
									<text><b>Name</b></text>
									<br />
									<input type="text" name="groupName" value="{{ group.getName() }}" style="width: 100%" />
									<br />
									<text><b>Description</b></text>
									<br />
									<input type="text" name="groupDesc" value="{{ group.getDescription() }}" style="width: 100%" />
								</td>
								<td><input type="text" name="groupRoom" value="{{ group.getRoomId() }}" /></td>
								<td style="text-wrap: nowrap;">
									<img src="{{ site.sitePath }}/habbo-imaging/badge/{{ group.getBadge() }}.gif" class="badge-preview" /> - <a href="#" class="edit-badge" data-id="{{ group.getId }}" data-toggle="modal" data-target="#badgeEditorModal">Edit</a>
								</td>
								<td><input type="text" name="groupAlias" value="{{ group.getAlias() }}" /></td>
								<td>	
									<select name="groupType" id="groupType">
										<option value="0" {% if group.getGroupType == 0 %}selected{% endif %}>Regular</option>
										<option value="1" {% if group.getGroupType == 1 %}selected{% endif %}>Exclusive</option>
										<option value="2" {% if group.getGroupType == 2 %}selected{% endif %}>Private</option>
									</select>
								</td>									
								<td><button type="submit" name="action" value="groupSave">Save</button></td>
							</form>
						{% endautoescape %}	
						</tr>
						{% set num = num + 1 %}
						{% endfor %}
					</tbody>
				</table>
			</div>		  
      </div>
    </div>
  </div>
 <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
  <script src="https://blackrockdigital.github.io/startbootstrap-simple-sidebar/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  </script>
  
<!-- Bootstrap JS (compatible con jQuery) -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

<!-- SWFObject -->
<script src="https://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js"></script>

<script>
  $(document).on('click', '.edit-badge', function(e) {
    e.preventDefault();
    
    var groupId = $(this).data('id');

    $.ajax({
      url: '{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/api/groups/show_badge_editor',
      type: 'POST',
      data: { groupId: groupId },
      success: function(response) {
        $('#badgeEditorContent').html(response);
        $('#badgeEditorModal').modal('show');
		loadBadgeEditor();
      },
      error: function() {
        $('#badgeEditorContent').html('<p>There was an error loading the badge editor. Please try again later.</p>');
      }
    });
  });
  
  function closeBadgeEditor(event) {
    $('#badgeEditorModal').modal('hide');
	}
</script>
  
<!-- Modal -->
<div class="modal fade" id="badgeEditorModal" tabindex="-1" role="dialog" aria-labelledby="badgeEditorLabel" aria-hidden="true">
  <div class="modal-dialog" role="document" style="width: fit-content;width: -moz-fit-content;">
    <div class="modal-content" style="width: fit-content;width: -moz-fit-content;">
      <div class="modal-header">
        <h5 class="modal-title" id="badgeEditorLabel">Edit Badge</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true" style="height: 15px;width: 15px;position: absolute;right: 13px;top: 9px;text-indent: -10000px;background: transparent url({{ site.staticContentPath }}/web-gallery/v2/images/close_x.gif) no-repeat;"><img src="{{ site.staticContentPath }}/web-gallery/v2/images/close_x.gif" width="15" height="15" alt=""></span>
        </button>
      </div>
      <div class="modal-body" id="badgeEditorContent">
      </div>
    </div>
  </div>
</div>
</body>
</html>