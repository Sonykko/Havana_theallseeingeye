{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set statisticsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_statistics.tpl" %}
		<h2 class="mt-4">Housekeeping login logs</h2>
		  <p>The recently login Staff logs list from Housekeeping is seen below.</p>
		  {% include "housekeeping/base/alert.tpl" %}
			<div class="pagination-buttons-box">
			{% if nextLoginLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.login?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousLoginLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.login?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
		  {% if LoginLogs|length > 0 %}
          <div class="table-responsive">
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
				  <td>{{ loginLog.getId() }}</td>
				  <td>{{ loginLog.getUserName() }} (id: {{ loginLog.getUserId() }})</td>
				  <td>{{ loginLog.getDate() }}</td>
				  <td>{{ loginLog.getUserIp() }}</td>
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
</body>
</html>