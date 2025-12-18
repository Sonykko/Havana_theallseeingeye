{% include "housekeeping/base/header.tpl" %}
<body>
    {% set adminToolsActive = "active" %}
    {% set ruffleActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">CFH topics tool</h2>				  		
		  {% include "housekeeping/base/alert.tpl" %}
		  <br />		  
		  <p><b>Create topic</b></p>
		  <p>This tool allows you to add a topic to CFH.</p>
		  <div class="alert__tool">		  
			  <form class="alert__tool__form" method="post">	
					<div class="alert__tool__recipient">
						<label for="user">The id key</label>
						<input type="text" name="sanctionReasonId" class="" id="sanctionReasonId" />
					</div>
					<div class="alert__tool__recipient">
						<label for="user">The value</label>
						<input type="text" name="sanctionReasonValue" class="" id="sanctionReasonValue" />
					</div>
					<div class="alert__tool__recipient">
						<label for="user">The description</label>
						<input type="text" name="sanctionReasonDesc" class="" id="sanctionReasonDesc" />
					</div>					
				<div class="alert__tool__submit">
					<button type="submit" name="action" value="createTopic">Add topic</button>
				</div>
			  </form>
		  </div>
		  <hr />		  
		  <p><b>Find topic</b></p>
		  <p>This tool allows you to search a CFH topics with topic starting or by id.</p>
		<form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
			<div class="">
				<label>Search</label>
				{% autoescape 'html' %}
				<input type="text" name="searchStr" id="searchStr" />
				{% endautoescape %}
			</div>
			<button type="submit" name="action" value="searchTopic">Submit</button>
		</form>		  
		  {% if searchTopicsDetails|length > 0 %}
		  <br />
		  <p>Topics starting with '{{ query }}' or with id '{{ query }}'</p>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>Reason ID</th>							
							<th>Reason value</th>
							<th>Reason desc</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for topics in searchTopicsDetails %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="topicId" value="{{ topics.getId() }}" />
								<td>{{ topics.getId() }}</td>
								<td><input type="text" name="sanctionReasonId" value="{{ topics.getSanctionReasonId() }}" style="width:100%" /></td>
								<td><input type="text" name="sanctionReasonValue" value="{{ topics.getSanctionReasonValue() }}" style="width:100%" /></td>
								<td><input type="text" name="sanctionReasonDesc" value="{{ topics.getSanctionReasonDesc() }}" style="width:100%" /></td>	
								<td><button type="submit" name="action" value="topicSave">Save</button><button type="submit" name="action" value="topicDelete">Delete</button></td>
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
		  <p><b>List of current topics</b></p>
		  <p>This tool allow you to see the complete list of all current topics in the Hotel.</p>
		  <div class="pagination-buttons-box">
		  {% if nextTopics|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_topics?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousTopics|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_topics?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
		  </div>			  
			  {% if CFHTopics|length > 0 %}
			  <div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>Reason ID</th>							
							<th>Reason value</th>
							<th>Reason desc</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for CFHTopics in CFHTopics %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="topicId" value="{{ CFHTopics.getId() }}" />
								<td>{{ CFHTopics.getId() }}</td>
								<td><input type="text" name="sanctionReasonId" value="{{ CFHTopics.getSanctionReasonId() }}" style="width:100%" /></td>
								<td><input type="text" name="sanctionReasonValue" value="{{ CFHTopics.getSanctionReasonValue() }}" style="width:100%" /></td>
								<td><input type="text" name="sanctionReasonDesc" value="{{ CFHTopics.getSanctionReasonDesc() }}" style="width:100%" /></td>								
								<td><button type="submit" name="action" value="topicSave">Save</button><button type="submit" name="action" value="topicDelete">Delete</button></td>
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