{% include "housekeeping/base/header.tpl" %}
<body>
    {% set adminToolsActive = "active" %}
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
		  {% if cfhlogs|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr style="text-wrap: nowrap;">
				  <th>Caller</th>                            
				  <th>Time</th>
				  <th>Room name</th>
				  <th>Message</th>	
				  <th>Picked by</th>	
				  <th>Time picked</th>				  
				  <th>Action</th>				  
				  <th>Message reply</th>				  				    	  
				  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for cfhlog in cfhlogs %}
				{% autoescape 'html' %}
                <tr>                 
				  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ cfhlog.getUserId() }}">{{ cfhlog.getUsername() }} (id: {{ cfhlog.getUserId() }})</a></td>
				  <td>{{ cfhlog.getCreatedTime() }}</td>
				  <td>{{ cfhlog.getRoomName() }} (id: {{ cfhlog.getRoomId() }})</td>
				  <td>{{ cfhlog.getReason() }}</td>					  
				  <td>{% if cfhlog.getModerator() == null %}-{% else %}{{ cfhlog.getModerator() }}{% endif %}</td>
				  <td>{% if cfhlog.getPickedTime() == null %}-{% else %}{{ cfhlog.getPickedTime() }}{% endif %}</td>
				  <td>{% if cfhlog.getAction() == null %}-{% else %}{{ cfhlog.getAction() }}{% endif %}</td>
				  <td>{% if cfhlog.getAction() == 'REPLY' %}{{ cfhlog.getMessageToUser() }}{% else %}-{% endif %}</td>				  					  
				  {% if cfhlog.isDeleted() or (now > cfhlog.getExpireTime()) %}
				  <td>-</td>				  
				  {% else %}
				  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/api/cfh.pick?cryId={{ cfhlog.getCryId() }}">Pick Up</a></td>
				  {% endif %}
                </tr>
			   {% set num = num + 1 %}
			   {% endautoescape %}
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