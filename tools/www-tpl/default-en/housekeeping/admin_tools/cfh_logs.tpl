{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
    <!--<h2 class="mt-4">Search Room Chatlogs</h2>
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
    {% if chatLogs|length > 0 %}
    <h5>Search Results</h5>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Message</th>
                    <th>Room ID</th>
                    <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=room_id">Room Name</a></th>
                    <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=timestamp">Date</a></th>
                </tr>
            </thead>
            <tbody>
                {% set num = 1 %}
                {% for searchChatlogs in chatLogs %}
                <tr>
                    <td>{{ searchChatlogs.id }}</td>
                    <td>{{ searchChatlogs.username }}</td>
                    <td>{{ searchChatlogs.message }}</td>
                    <td>{{ searchChatlogs.roomId }}</td>
                    <td>{{ searchChatlogs.roomName }}</td>
                    <td>{{ (searchChatlogs.timestamp * 1000)| date("dd-MM-yyyy HH:mm:ss") }}</td>
                </tr>
                {% set num = num + 1 %}
                {% endfor %}
            </tbody>
        </table>
    </div>
    {% endif %}
		    <h2 class="mt-4">Search CFH logs</h2>
    {% include "housekeeping/base/alert.tpl" %}
    <p>Here you can search room chatlogs by the field of your choice, and the requested input by you.</p>
    <form class="table-responsive col-md-4" method="post">
        <div class="form-group">
            <label for="field">Field</label>
            <select name="searchField" class="form-control" id="field">
                <option value="user">User</option>
                <option value="message">Message</option>
                <option value="room">Room Name</option>
                <option value="room_id">Room ID</option>
				<option value="status">Status (0,1)</option>
				<option value="moderator">Moderator</option>
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
	{% if cfhlogSearch|length > 0 %}
	          <h2 class="mt-4">Results CFH Logs</h2>
		  <p>The recently chatlogs in rooms list is seen below.</p>
			</div>
          <div class="table-responsive" style="padding-left: 15px;">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Created Date</th>
                  <th>User</th>
				  <th>Message</th>
				  <th>Room</th>
				  <th>Status</th>
				  <th>Moderator</th>
				  <th>Picked Up Date</th>
				  
				  <!--<th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=room_id">Room ID</a></th>
				  <th>Room Name</th>
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=timestamp">Date</a></th>-->
                <!--</tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for cfhlogSearch in cfhlogSearchs %}
                <tr>
                  <td>{{ cfhlogSearch.createdTime }}</td>
                  <!--<td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ chatlog.userId }}" style="color:black;"><b>{{ chatlog.username }}<b></a></td>-->
				  <!--<td><b><u>{{ cfhlogSearch.username }}</u></b></td>
				  <td>{{ cfhlogSearch.message }}</td>
				  <td>{{ cfhlogSearch.roomName }} (id: {{ cfhlogSearch.roomId }})</td>
				  {% if cfhlogSearch.status != "1" %}
				  <td style="color:red;"><b>Not Picked Up</b></td>
				  {% else %}
				  <td style="color:limegreen;"><b>Picked Up</b></td>
				  {% endif %}
				  <td>{% if cfhlogSearch.status != "1" %}-{% else %}{{ cfhlogSearch.moderator }}{% endif %}</td>
				  <td>{% if cfhlogSearch.status != "1" %}-{% else %}{{ cfhlogSearch.pickedTime }}{% endif %}</td>
                </tr>
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
      </div>
	  {% endif %}-->
	
	
	
	
	

          <h2 class="mt-4">CFH logs</h2>
		  <p>The recently CFH logs list is seen below.</p>

			{% if nextCFHlogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_logs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousCFHlogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_logs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
          <div class="table-responsive" style="padding-left: 15px;">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Created Date</th>
                  <th>User</th>
				  <th>Message</th>
				  <th>Room</th>
				  <th>Picked Up Date</th>
				  <th>Moderator</th>
				  <th>Status</th>
				  
				  <!--<th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=room_id">Room ID</a></th>
				  <th>Room Name</th>
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs?page={{ page }}&sort=timestamp">Date</a></th>-->
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for cfhlog in cfhlogs %}
                <tr>
                  <td>{{ cfhlog.createdTime }}</td>
                  <!--<td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ chatlog.userId }}" style="color:black;"><b>{{ chatlog.username }}<b></a></td>-->
				  <td><b><u>{{ cfhlog.username }}</u></b></td>
				  <td>{{ cfhlog.message }}</td>
				  <td>{{ cfhlog.roomName }} (id: {{ cfhlog.roomId }})</td>
				  <td>{% if cfhlog.status != "1" %}-{% else %}{{ cfhlog.pickedTime }}{% endif %}</td>
				  <td>{% if cfhlog.status != "1" %}-{% else %}{{ cfhlog.moderator }}{% endif %}</td>
				  {% if cfhlog.status != "1" %}
				  <td style="color:red;"><b>Not Picked Up</b></td>
				  {% else %}
				  <td style="color:limegreen;"><b>Picked Up</b></td>
				  {% endif %}
                </tr>
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
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