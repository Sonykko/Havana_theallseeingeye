{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Search Users</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Here you can search users by the field of your choice, and the requested input by you</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label for="field">Field</label>
				<select name="searchField" class="form-control" id="field">
					<option value="username">Username</option>
					<option value="id">ID</option>
					<option value="credits">Credits</option>
					<option value="pixels">Pixels</option>
					<option value="mission">Mission</option>
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
		{% if players|length > 0 %}
		<hr/>
		<p style="font-size:16px;"><b>Search results</b></p>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
				  <th>Email</th>
				  <th>Look</th>
                  <th>Mission</th>
                  <th>Credits</th>
                  <th>Duckets</th>
				  <th>Date joined</th>
				  <th>Last online</th>				  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for player in players %}
                <tr>
                  <td>{{ player.id }}</td>
                  <td>{{ player.name }}</td>
				  <td>{{ player.email }}</td>
				  <td><img src="{{ site.habboImagingPath }}/habbo-imaging/avatarimage?figure={{ player.figure }}&size=s"></td>
				  {% autoescape 'html' %}
                  <td>{{ player.motto }}</td>
				  {% endautoescape %}
                  <td>{{ player.credits }}</td>
                  <td>{{ player.pixels }}</td>
				  <td>{{ player.formatJoinDate("dd-MM-yyyy HH:mm:ss") }}</td>
				  <td>{{ player.formatLastOnline("dd-MM-yyyy HH:mm:ss") }}</td>	
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
      </div>
    </div>
  </div>
</body>
</html>