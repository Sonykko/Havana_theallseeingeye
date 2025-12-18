{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set statisticsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_statistics.tpl" %}
          <h2 class="mt-4">Newest Players</h2>
		  {% if zeroCoinsFlag %}
		  <p>The recently joined player with zero coins list is seen below.</p>
		  {% else %}
		  <p>The recently joined player list is seen below.</p>
		  {% endif %}
			<div class="pagination-buttons-box">
			{% set zeroCoinsValue = '' %}
			{% if zeroCoinsFlag %}
				{% set zeroCoinsValue = '&zerocoins' %}
			{% endif %}
			
			{% if nextPlayers|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/newest_players?page={{ ourNextPage }}{{ zeroCoinsValue }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousPlayers|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/newest_players?page={{ ourNextPage }}{{ zeroCoinsValue }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
		  {% if players|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
				  <th>Email</th>
				  <th>Look</th>
				  <th>Motto</th>
				  <th>Credits</th>
				  <th>Pixels</th>
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/newest_players?page={{ page }}{{ zeroCoinsValue }}&sort=created_at">Date joined</a></th>
				  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/newest_players?page={{ page }}{{ zeroCoinsValue }}&sort=last_online">Last online</a></th>
				</tr>
			   </thead>
			 <tbody>
			  {% set num = 1 %}
		   	  {% for player in players %}
				<tr>
				  <td>{{ player.id }}</td>
				  <td>{{ player.name }}</td>
			 	  <td>{{ player.email }}</td>
				  <td><img src="{{ site.sitePath }}/habbo-imaging/avatarimage?figure={{ player.figure }}&size=s"></td>
				  {% autoescape 'html' %}
				  <td>{{ player.motto }}</td>
				  {% endautoescape %}
				  <td>{{ player.credits }}</td>
				  <td>{{ player.pixels }}</td>
				  <td>{{ player.formatJoinDate("dd-MM-yyyy HH:mm:ss") }}</td>
				  <td>{{ player.formatLastOnline("dd-MM-yyyy HH:mm:ss") }}</td>
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