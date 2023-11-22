{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Remote alerting</h2>
		  <p>Send a remote alert to a given user.</p>
		  {% include "housekeeping/base/alert.tpl" %}
		  <form class="table-responsive col-md-4" method="post">	
		    <label for="user">User</label>
				<input type="text" name="user" class="form-control" id="user" placeholder="Enter here the Username..." value="" />
			<label for="message">Message</label>
				<textarea name="message" class="form-control" id="message" placeholder="Enter here the User Alert..." style="height:150px;"></textarea>										
				<br>			
			<button type="submit">Send User Alert</button>
		  </form>
		  
		  
		<h2 class="mt-4">View alerts</h2>
		  <p>Here can see the most recent logs of User Alerts created via RCON.</p>
			{% if nextremoteAlertLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousremoteAlertLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
          <div class="table-responsive" style="padding-left: 15px;">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>ID</th>
				  <th>User</th>
				  <th>Message</th>	
                  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/alert?page={{ page }}&sort=id">Created Date</a></th>            				  				  				  
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/alert?page={{ page }}&sort=user">Moderator</a></th>	
				  		  		  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for remoteAlertLog in remoteAlertLogs %}
                <tr>
				  <td>{{ remoteAlertLog.id }}</td>				  
				  <td>{{ remoteAlertLog.user }}</td>		
				  <td>{{ remoteAlertLog.message }}</td>		
				  <td>{{ remoteAlertLog.timestamp }}</td>				  
				  <td>{{ remoteAlertLog.moderator }}</td>				  
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