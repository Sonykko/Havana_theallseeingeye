{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set dashboardActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_statistics.tpl" %}
		<h2 class="mt-4">Housekeeping Staff logs</h2>
		  <p>The recently Housekeeping Staff action logs list is seen below.</p>
		  {% include "housekeeping/base/alert.tpl" %}
			<div class="pagination-buttons-box">
			{% if nextStaffActionLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.action?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousStaffActionLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.action?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
		  {% if StaffActionLogs|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>             				  				  				  
				  <th>User</th>
				  <th>Description</th>
				  <th>Date</th>				  
				  <th>User IP</th>				  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for staffActionLog in StaffActionLogs %}
                <tr>
				  <td>{{ staffActionLog.getId() }}</td>
				  <td>{{ staffActionLog.getUserName() }} (id: {{ staffActionLog.getUserId() }})</td>
				  <td>{{ staffActionLog.getDescription() }}</td>
				  <td>{{ staffActionLog.getDate() }}</td>
				  <td>{{ staffActionLog.getUserIp() }}</td>		 
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