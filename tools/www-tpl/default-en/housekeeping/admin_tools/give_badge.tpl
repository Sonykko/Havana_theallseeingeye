{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Remote give badge</h2>
		  <p>This tool allows you to send an badge to a Habbo if you are not in the same room (you don't even need to be in the hotel). So that you don't have to go to the room.</p>
		  <form class="table-responsive col-md-4" method="post" style="padding-left: 0;">	
		    <div class="form-group">
				<label for="user"><b>The recipient</b></label>
				<input type="text" name="user" class="form-control" id="user" placeholder="Enter here the username..." value="" />
			</div>
			<div class="form-group">
				<label for="badge"><b>Badge</b></label>
				<input type="text" name="badge" class="form-control" id="user" placeholder="Enter here the badge code..." value="" />									
			</div>		
			<button type="submit" name="action" value="giveBadge">Give badge</button>
			<button type="submit" name="action" value="removeBadge">Remove badge</button>
		  </form>
		  <br />
		  {% include "housekeeping/base/alert.tpl" %}
		 <hr /> 
		<p><b>View Remote badges log</b></p>
		  <p>Here can see the most recent logs of Remote badges created via RCON.</p>
		  <div class="pagination-buttons-box">
			{% if nextremoteGiveBadgesLogs|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/give_badge?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousremoteGiveBadgesLogs|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/give_badge?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
			{%if remoteGiveBadgesLogs|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>ID</th>
				  <th>User</th>
				  <th>Badge</th>	
                  <th>Extra notes</th>            				  				  				  
				  <th>Type</th>	
				  <th>Date</th>	
				  		  		  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for GiveBadgesList in remoteGiveBadgesLogs %}
                <tr>
				  <td>{{ GiveBadgesList.id }}</td>				  
				  <td>{{ GiveBadgesList.user }}</td>		
				  <td>{{ GiveBadgesList.argument1 }}</td>		
				  <td>{{ GiveBadgesList.argument2 }}</td>				  
				  <td>{{ GiveBadgesList.type }}</td>				  
				  <td>{{ GiveBadgesList.timestamp }}</td>				  
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