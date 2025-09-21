{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
    <h2 class="mt-4">Search Room Chatlogs</h2>
    {% include "housekeeping/base/alert.tpl" %}
    <p>Here you can search room chatlogs by the field of your choice, and the requested input by you.</p>
    <form class="table-responsive col-md-4" method="post">
        <div class="form-group">
            <label for="field">Field</label>
            <select name="searchField" class="form-control" id="field">
                <option value="chatlog.id">Chatlog ID</option>
                <option value="message">Message</option>
                <option value="room_id">Room ID</option>
                <option value="name">Room Name</option>
            </select>
        </div>
        <div class="form-group">
            <label for="field">Search type</label>
            <select name="searchType" class="form-control" id="field">
                <option value="contains">Contains</option>
                <option value="starts_with">Starts with</option>
                <option value="ends_with">Ends with</option>
                <option value="equals">Equals</option>
            </select>
        </div>
        <div class="form-group">
            <label for="searchFor">Search data</label>
            <input type="text" name="searchQuery" class="form-control" id="searchFor" placeholder="Looking for...">
        </div>
        <button type="submit">Perform Search</button>
    </form>
    <br>
    {% if searchChatlogs|length > 0 %}
	<hr/>
	<p style="font-size:16px;"><b>Search results</b></p>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
					<th>Created Date</th>
                    <th>Username</th>
                    <th>Message</th>
                    <th>Room</th>                  
                </tr>
            </thead>
            <tbody>
                {% set num = 1 %}
                {% for searchChatlogs in searchChatlogs %}
                <tr>
                    <td>{{ searchChatlogs.getId() }}</td>
					<td>{{ (searchChatlogs.getDate() * 1000)| date("HH:mm d/MM/yyyy") }}</td>
                    <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ searchChatlogs.getUserId() }}" style="color:black;"><b>{{ searchChatlogs.getUserName() }}</b></a></td>
                    <td>{{ searchChatlogs.getMessage() }}</td>
					<td>{{ searchChatlogs.getRoomName() }} (id: {{ searchChatlogs.getRoomId() }})</td> 
                </tr>
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
		
          <h2 class="mt-4">Room Chatlogs</h2>
		  <p>The recently chatlogs in rooms list is seen below.</p>
			<div class="pagination-buttons-box">
			{% if nextChatlogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousChatlogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
		  {% if chatlogs|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>                				  
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=timestamp">Created Date</a></th>				  
				  <th>User</th>
				  <th>Message</th>
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=room_id">Room</a></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for chatlog in chatlogs %}
                <tr>
				  <td>{{ (chatlog.getDate() * 1000)| date("HH:mm d/MM/yyyy") }}</td>				  				  
				  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ chatlog.getUserId() }}" style="color:black;"><b>{{ chatlog.getUserName() }}</b></a></td>
				  <td>{{ chatlog.getMessage() }}</td>
				  <td>{{ chatlog.getRoomName() }} (id: {{ chatlog.getRoomId() }})</td>
                </tr>
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
		  </div>
		  {% else %}
		  <p><i>Nothing found to display.</i></p>
		  {% endif %} 
    </div>
  </div>
</body>
</html>