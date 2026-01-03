{% include "housekeeping/base/header.tpl" %}
<body>
    {% set adminToolsActive = "active" %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Hobba activity log</h2>				  		
		  {% include "housekeeping/base/alert.tpl" %}
		  <br />		  		  
		  <p><b>List of current logs</b></p>
		  <p>This tool allow you to see the complete list of all current Hobba logs in the Hotel.</p>
		  <div class="pagination-buttons-box">
		  {% if nextLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/hobba_logs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/hobba_logs?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
		  </div>			  
			  {% if HobbaLogs|length > 0 %}
			  <div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Action</th>
							<th>User</th>							
							<th>Target</th>
							<th>Message</th>
							<th>Extra notes</th>
							<th>Time</th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for HobbaLogs in HobbaLogs %}
						<tr>
						{% autoescape 'html' %}
							<td>{{ HobbaLogs.getAction() }}</td>
							<td>{{ HobbaLogs.getUserId() }}</td>
							<td>{{ HobbaLogs.getTargetId() }}</td>
							<td>{% if HobbaLogs.getMessage()|length > 0 %}{{ HobbaLogs.getMessage() }}{% else %}-{% endif %}</td>
							<td>{% if HobbaLogs.getExtraNotes()|length > 0 %}{{ HobbaLogs.getExtraNotes() }}{% else %}-{% endif %}</td>
							<td>{{ HobbaLogs.formatCreatedAt("dd-MM-yyyy HH:mm:ss") }}</td>
						{% endautoescape %}	
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
  </div>
</body>
</html>