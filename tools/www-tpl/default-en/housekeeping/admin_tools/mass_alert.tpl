{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Hotel Alert tool</h2>
		  <p>Send a mass Hotel Alert to all online players.</p>
		  {% include "housekeeping/base/alert.tpl" %}
		  <form class="table-responsive col-md-4" method="post">	
			<label for="message">Message</label>
				<textarea name="message" class="form-control" id="message" placeholder="Enter here the Hotel Alert..." style="height:150px;"></textarea>
								
			<label for="sender">Sender</label>
				<input type="text" name="sender" class="form-control" id="sender" placeholder="{{ playerDetails.getName() }}" value="{{ playerDetails.getName() }}" readonly>
				<br>			
			<button type="submit">Send Hotel Alert</button>
		  </form>
		  
		  
		<h2 class="mt-4">Hotel Alert log</h2>
		  <p>Here can see the most recent logs of Hotel Alerts created via RCON.</p>
			{% if nexthotelAlertLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previoushotelAlertLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			<br><br>
			</div>
          <div class="table-responsive" style="padding-left: 15px;">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>ID</th>
                  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_alert?page={{ page }}&sort=id">Created Date</a></th>            				  				  				  
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_alert?page={{ page }}&sort=user">Moderator</a></th>	
				  <th>Message</th>			  		  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for massAlertLog in hotelAlertLogs %}
                <tr>
				  <td>Hotel Alert ID:{{ massAlertLog.id }}</td>
				  <td>{{ massAlertLog.timestamp }}</td>				  
				  <td>{{ massAlertLog.moderator }}</td>
				  <td>{{ massAlertLog.message }}</td>		 
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