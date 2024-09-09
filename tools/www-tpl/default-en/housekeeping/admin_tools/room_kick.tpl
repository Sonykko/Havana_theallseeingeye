{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Remote room alerting & kicking</h2>
		  <p>This tool allows you to send an alert to a room if you are not in the same room (you don't even need to be in the hotel). You can use this to answer Calls for help that aren't urgent (eg 'How do I dance?' or 'How do I become a Hobba?'), so that you don't have to go to the room.</p>
		  {% include "housekeeping/base/alert.tpl" %}
		  <div class="alert__tool">
			  <form class="alert__tool__form" method="post">	
				<div class="alert__tool__recipient">
					<label for="roomId">The room ID</label>
					<input type="text" name="roomId" class="" id="roomId" value="" />
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
					<label for="customMessage">Message</label>
					<input type="text" name="customMessage" class="" id="customMessage" value="" />					
				</div>	
				<div class="" style="width: 300px;gap: 10px;display: flex;padding-bottom: 10px;">					
					<input type="checkbox" name="unacceptable" value="true" />Set room as Unacceptable
				</div>				
				<div class="alert__tool__submit">					
					<button type="submit" name="action" value="roomAlert">Alert</button>
					<button type="submit" name="action" value="roomKick">Kick</button>
				</div>
			  </form>
		  </div>		  
		  
		  
		<h2 class="mt-4">View alerts</h2>
		  <p>Here can see the most recent logs of User Alerts created via RCON.</p>
		  <div class="pagination-buttons-box">
			{% if nextremoteRoomKickLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_kick?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousremoteRoomKickLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_kick?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr style="text-wrap: nowrap;">
				  <th>ID</th>
				  <th>Type</th>
				  <th>Room ID</th>
				  <th>Message</th>	
                  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_kick?page={{ page }}&sort=id">Created Date</a></th>            				  				  				  
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_kick?page={{ page }}&sort=user">Moderator</a></th>	
				  		  		  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for remoteRoomKickLog in remoteRoomKickLogs %}
                <tr>
				  <td>{{ remoteRoomKickLog.id }}</td>				  
				  <td>{{ remoteRoomKickLog.type }}</td>		
				  <td><a href="{{ site.sitePath }}/ase/habbo/es/housekeeping/extra/hobba/admin_tools/rooms/edit?id={{ remoteRoomKickLog.user }}">{{ remoteRoomKickLog.user }}</a></td>		
				  <td>{{ remoteRoomKickLog.message }}</td>		
				  <td>{{ remoteRoomKickLog.timestamp }}</td>				  
				  <td>{{ remoteRoomKickLog.moderator }}</td>				  
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