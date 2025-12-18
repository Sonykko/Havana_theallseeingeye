{% include "housekeeping/base/header.tpl" %}
<body>
    {% set adminToolsActive = "active" %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Games ranks tool</h2>				  		
		  {% include "housekeeping/base/alert.tpl" %}
		  <br />		  
		  <p><b>Create rank</b></p>
		  <p>This tool allows you to add a rank to games.</p>
		  <div class="alert__tool">		  
			  <form class="alert__tool__form" method="post">	
					<div class="alert__tool__recipient">
						<label for="user">The title</label>
						<input type="text" name="rankTitle" class="" id="rankTitle" />
					</div>
					<div class="alert__tool__commonmessage">
						<select name="gameType" id="gameType">
							<option value="" disabled selected>Choose a game type</option>
						{% for gameType in gameTypes %}
							<option value="{{ gameType.name() }}">{{ gameType.name() }}</option>
						{% endfor %}
						</select>
					</div>
					<div class="alert__tool__recipient">
						<label for="user">The minimum points</label>
						<input type="text" name="rankMinPoints" class="" id="rankMinPoints" />
					</div>	
					<div class="alert__tool__recipient">
						<label for="user">The maximum points</label>
						<input type="text" name="rankMaxPoints" class="" id="rankMaxPoints" />
					</div>						
				<div class="alert__tool__submit">
					<button type="submit" name="action" value="createRank">Add rank</button>
				</div>
			  </form>
		  </div>
		  <hr />		  
		  <p><b>Find rank</b></p>
		  <p>This tool allows you to search a games ranks with rank starting or by game.</p>
		<form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
			<div class="">
				<label>Search</label>
				{% autoescape 'html' %}
				<input type="text" name="searchStr" id="searchStr" />
				{% endautoescape %}
			</div>
			<button type="submit" name="action" value="searchRank">Submit</button>
		</form>		  
		  {% if searchGamesRanksDetails|length > 0 %}
		  <br />
		  <p>Ranks starting with '{{ query }}' or with game '{{ query }}'</p>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>Rank title</th>							
							<th>Minimum points</th>
							<th>Maximum points</th>
							<th>Game type</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for ranks in searchGamesRanksDetails %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="rankId" value="{{ ranks.getId() }}" />
								<td>{{ ranks.getId() }}</td>
								<td><input type="text" name="rankTitle" value="{{ ranks.getTitle() }}" style="width:100%" /></td>
								<td><input type="text" name="rankMinPoints" value="{{ ranks.getMinPoints() }}" style="width:100%" /></td>
								<td><input type="text" name="rankMaxPoints" value="{{ ranks.getMaxPoints() }}" style="width:100%" /></td>	
								<td>	
									<select name="gameType" id="gameType">
									{% for gameType in gameTypes %}
										<option value="{{ gameType.name() }}" {% if gameType.name() == ranks.getType() %}selected{% endif %}>{{ gameType.name() }}</option>
									{% endfor %}
									</select>
								</td>									
								<td><button type="submit" name="action" value="saveRank">Save</button><button type="submit" name="action" value="deleteRank">Delete</button></td>
							</form>
						{% endautoescape %}
						</tr>
						{% set num = num + 1 %}
						{% endfor %}
					</tbody>
				</table>
				{% endif %}
			{% if searchEmpty %}
			<br>
			<p><i>No results found to display.</i></p>
			{% endif %}
		  <hr />
		  <p><b>List of current ranks</b></p>
		  <p>This tool allow you to see the complete list of all current games ranks in the Hotel.</p>
		  <div class="pagination-buttons-box">
		  {% if nextTopics|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/games_ranks?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousTopics|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/games_ranks?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
		  </div>			  
			  {% if ranks|length > 0 %}
			  <div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>Rank title</th>							
							<th>Minimum points</th>
							<th>Maximum points</th>
							<th>Game type</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for ranks in ranks %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="rankId" value="{{ ranks.getId() }}" />
								<td>{{ ranks.getId() }}</td>
								<td><input type="text" name="rankTitle" value="{{ ranks.getTitle() }}" style="width:100%" /></td>
								<td><input type="text" name="rankMinPoints" value="{{ ranks.getMinPoints() }}" style="width:100%" /></td>
								<td><input type="text" name="rankMaxPoints" value="{{ ranks.getMaxPoints() }}" style="width:100%" /></td>	
								<td>	
									<select name="gameType" id="gameType">
									{% for gameType in gameTypes %}
										<option value="{{ gameType.name() }}" {% if gameType.name() == ranks.getType() %}selected{% endif %}>{{ gameType.name() }}</option>
									{% endfor %}
									</select>
								</td>									
								<td><button type="submit" name="action" value="saveRank">Save</button><button type="submit" name="action" value="deleteRank">Delete</button></td>
							</form>
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