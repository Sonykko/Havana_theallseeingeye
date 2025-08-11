{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
     <h2 class="mt-4">Infobus Polls</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>This lists all infobus polls that have been created and used.</p>
		<p><a href="/{{ site.housekeepingPath }}/campaign_management/infobus_polls/create" class="btn btn-danger">New Poll</a></p>
		<p>
			<a href="/{{ site.housekeepingPath }}/campaign_management/infobus_polls/door_status?status=1" class="btn btn-info">Open Infobus Doors</a>
		</p>
		<p>
			<a href="/{{ site.housekeepingPath }}/campaign_management/infobus_polls/door_status?status=0" class="btn btn-info">Close Infobus Doors</a>
		</p>
		<p>
			<a href="/{{ site.housekeepingPath }}/campaign_management/infobus_polls/close_event" class="btn btn-warning">End Event</a>
		</p>	
          <h2 class="mt-4">Manage Infobus Polls</h2>
		  <p>The recently Infobus Polls list is seen below.</p>
		  {% if infobusPolls|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th></th>
				  <th>Question</th>
				  <th>Created By</th>
				  <th>Created At</th>
				  <th></th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
				{% for infobusPoll in infobusPolls %}
                <tr>
				  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/infobus_polls/view_results?id={{ infobusPoll.id }}" class="btn btn-success">View Results</a></td>
				  <td>{{ infobusPoll.getPollData().getQuestion() }}</td>
				  <td>{{ infobusPoll.getCreator() }}</td>
				  <td>{{ infobusPoll.getCreatedAtFormatted() }}</td>
				  <td>
				  	<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/infobus_polls/edit?id={{ infobusPoll.id }}" class="btn btn-primary">Edit</a>
				  	<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/infobus_polls/delete?id={{ infobusPoll.id }}" class="btn btn-danger">Delete</a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/infobus_polls/clear_results?id={{ infobusPoll.id }}" class="btn btn-warning">Clear Results</a>
				  </td>
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/infobus_polls/send_poll?id={{ infobusPoll.id }}" class="btn btn-info">Send Poll</a>
				  </td>
                </tr>
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