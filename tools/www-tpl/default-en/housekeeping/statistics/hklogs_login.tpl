{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set dashboardActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_statistics.tpl" %}
		<h2 class="mt-4">Housekeeping login logs</h2>
		  <p>The recently login Staff logs list from Housekeeping is seen below.</p>
		  {% include "housekeeping/base/alert.tpl" %}

			{% if nextLoginLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.login?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousLoginLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.login?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
			<br><br>
			</div>
          <div class="table-responsive" style="padding-left: 15px;">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>             				  				  				  
				  <th>User</th>
				  <th>Date</th>				  
				  <th>User IP</th>				  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for loginLog in LoginLogs %}
                <tr>
				  <td>{{ loginLog.id }}</td>
				  <td>{{ loginLog.userName }} (id: {{ loginLog.userId }})</td>
				  <td>{{ loginLog.date }}</td>
				  <td>{{ loginLog.userIp }}</td>
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