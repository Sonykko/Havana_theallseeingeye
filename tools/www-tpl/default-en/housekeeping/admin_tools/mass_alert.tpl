{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Hotel Alert tool</h2>
		  <p>Send a mass Hotel Alert to all online players.</p>
		  
		  <div class="alert__tool">
			  <form class="alert__tool__form" method="post">	
				<div class="alert__tool__custommessage">
					<label for="message" style="padding-right: 10px;">Message</label>
						<input name="message" class="" id="message" placeholder="Enter here the Hotel Alert..." />
				</div>					
				<div class="alert__tool__recipient">
					<label for="sender">Sender</label>
					<input type="text" name="sender" class="" id="sender" placeholder="{{ playerDetails.getName() }}" value="{{ playerDetails.getName() }}" readonly>
				</div>		
				<div class="" style="width: 300px;gap: 10px;display: flex;padding-bottom: 10px;">					
						<input type="checkbox" name="showSender" value="true" checked />Show sender in the alert
					</div>
				<div class="alert__tool__submit">		
					<button type="submit">Send Hotel Alert</button>
				</div>
			  </form>
		  </div>
		  {% include "housekeeping/base/alert.tpl" %}
		  
		<h2 class="mt-4">Hotel Alert log</h2>
		<div class="pagination-buttons-box">
		  <p>Here can see the most recent logs of Hotel Alerts created via RCON.</p>
			{% if nexthotelAlertLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previoushotelAlertLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_alert?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
          <div class="table-responsive">
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