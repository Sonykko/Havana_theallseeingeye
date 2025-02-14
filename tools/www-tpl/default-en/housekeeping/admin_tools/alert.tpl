{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Remote alerting</h2>
		  <p>This tool allows you to send an alert to a Habbo if you are not in the same room (you don't even need to be in the hotel). You can use this to answer Calls for help that aren't urgent (eg 'How do I dance?' or 'How do I become a Hobba?'), so that you don't have to go to the room.</p>
		  <div class="alert__tool">
			  <form class="alert__tool__form" method="post">	
				<div class="alert__tool__recipient">
					<label for="user">The recipient</label>
					<input type="text" name="user" class="" id="user" value="" />
				</div>
				<div class="alert__tool__commonmessage">
					<select name="commonMessage" id="commonMessage">
						<option value="">Choose a common message</option>
						{% for CFHTopics in CFHTopics %}
						<option value="{{ CFHTopics.sanctionReasonDesc }}">{{ CFHTopics.sanctionReasonValue }}</option>
						{% endfor %}
					</select>
				</div>								
				<div class="alert__tool__custommessage">
					<label for="message">Message</label>
					<input type="text" name="customMessage" class="" id="customMessage" value="" />					
				</div>	
				<div class="alert__tool__submit">
					<button type="submit">Alert</button>
				</div>
			  </form>
		  </div>
		  {% include "housekeeping/base/alert.tpl" %}
		<h2 class="mt-4">View alerts</h2>
		  <p>Here can see the most recent logs of User Alerts created via RCON.</p>
		  <div class="pagination-buttons-box">
			{% if nextremoteAlertLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousremoteAlertLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
		  {% if remoteAlertLogs|length > 0 %}
          <div class="table-responsive">
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
				{% autoescape 'html' %}				  
				  <td>{{ remoteAlertLog.message }}</td>	
				{% endautoescape %}
				  <td>{{ remoteAlertLog.timestamp }}</td>				  
				  <td>{{ remoteAlertLog.moderator }}</td>				  
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