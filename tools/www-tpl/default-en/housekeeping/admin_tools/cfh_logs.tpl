{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
          <h2 class="mt-4">CFH action log</h2>
		  <p>The recently CFH logs list is seen below.</p>
			<div class="pagination-buttons-box">
			{% if nextCFHlogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_logs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousCFHlogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_logs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Moderator</th>
                  <th>Habbo</th>
				  <th>Created Date</th>
				  <th>Action</th>
				  <th>Pick Up Date</th>
				  <th>Message to user</th>
				  <th>Reason for action</th>
				  <th>Room</th>
				  <th>Status</th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for cfhlog in cfhlogs %}
                <tr>
                  <td>{% if cfhlog.status != "1" %}-{% else %}{{ cfhlog.moderator }}{% endif %}</td>
				  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ cfhlog.userId }}" style="color: black;"><b><u>{{ cfhlog.username }}</u></b> (id: {{ cfhlog.userId }})</a></td>
				  <td>{{ cfhlog.createdTime }}</td>
				  <td>{{ cfhlog.action }}</td>
				  <td>{% if cfhlog.status != "1" %}-{% else %}{{ cfhlog.pickedTime }}{% endif %}</td>
				  <td>{{ cfhlog.messageToUser }}</td>
				  <td>{{ cfhlog.reason }}</td>
				  <td>{{ cfhlog.roomName }} (id: {{ cfhlog.roomId }})</td>
				  {% if cfhlog.status != "1" %}
				  <td style="color:red;display:flex;flex-direction:column;"><b>Not Picked</b><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/api/cfh.pick?cryId={{ cfhlog.cryId }}&moderator={{ playerDetails.getName() }}"><button>Pick Up</button></a></td>
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