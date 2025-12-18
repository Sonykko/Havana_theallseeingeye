{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Room admin</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Here you can search rooms by the field of your choice, and the requested input by you</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label for="field">Field</label>
				<select name="searchField" class="form-control" id="field">
					<option value="id">Room ID</option>
					<option value="name">Room Name</option>
					<option value="description">Description</option>
					<option value="owner_id">Owner ID</option>
					<option value="ownerName">Owner Name (Need to type exact owner name)</option>
				</select>
			</div>
			<div class="form-group">
				<label for="field">Search type</label>
				<select name="searchType" class="form-control" id="field">
					<option value="contains">Contains</option>
					<option value="starts_with">Starts with</option>
					<option value="ends_with">Ends with</option>
					<option value="equals">Equals</option>
					<option value="ownerName">Owner Name</option>
				</select>
			</div>
			<div class="form-group">
				<label for="searchFor"><b>Search data</b></label>
				<input type="text" name="searchQuery" class="form-control" id="searchFor" placeholder="Looking for...">
			</div>
			<button type="submit">Perform Search</button>
		</form>
		<br>
		{% if roomsAdmin|length > 0 %}
		<hr/>
		<p style="font-size:16px;"><b>Search results</b></p>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>Category</th>
                  <th>Room</th>                 
				  <th>Description</th>
				  <th>Status</th>
				  <th>Owner</th>			  
				  <th>Action</th>			  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for roomAdmin in roomsAdmin %}
                <tr>
                  <td>{{ roomAdmin.categoryName }}</td>
				  <td>{{ roomAdmin.name }} (id: {{ roomAdmin.roomId }})</td>				  
				  <td>{{ roomAdmin.description }}</td>
				  <td>{% if roomAdmin.status == 0 %}Opened{% endif %}{% if roomAdmin.status == 1 %}Doorbell{% endif %}{% if roomAdmin.status == 2 %}Password{% endif %}</td>
                  <td>{{ roomAdmin.ownerName }} (id: {{ roomAdmin.ownerId }})</td>
				  <td>
				    <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/rooms/edit?id={{ roomAdmin.roomId }}"><button type="button">Admin Room</button></a>
                    <form method="post">
                        <input type="hidden" name="roomId" id="roomId" value="{{ roomAdmin.roomId }}" />
                        <button type="submit" name="action" value="copyRoom">Copy/Clone</button>
                    </form>
				  </td>                </tr>
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
          </div>
		{% endif %}
		{% if noResults %}
		<hr/>
		<p style="font-size:16px;"><b>Search results</b></p>
		<p><i>No results found.</i></p>
		{% endif %}	
      </div>
    </div>
  </div>
</body>
</html>