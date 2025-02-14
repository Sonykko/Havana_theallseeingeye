{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
          <h2 class="mt-4">Calls for help</h2>
		  <p>Here can moderate all Calls for help. You don't even need to be in the hotel.</p>
			<div class="pagination-buttons-box">
			{% if nextCFHlogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_mods?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousCFHlogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_mods?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
		  {% if cfhlogs|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Caller</th>
				  <th>Time</th>
				  <th>Room name</th>
				  <th>Message</th>
				  <th></th>			
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for cfhlog in cfhlogs %}
                <tr>
				  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ cfhlog.userId }}">{{ cfhlog.username }} (id: {{ cfhlog.userId }})</a></td>
				  <td>{{ cfhlog.createdTime }}</td>	
				  <td>{{ cfhlog.roomName }} (id: {{ cfhlog.roomId }})</td>				  
				  <td>{{ cfhlog.reason }}</td>				  
				  {% if cfhlog.status != "1" %}
				  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/api/cfh.pick?cryId={{ cfhlog.cryId }}">Pick Up</a></td>
				  {% else %}
				  <td>-</td>
				  {% endif %}
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