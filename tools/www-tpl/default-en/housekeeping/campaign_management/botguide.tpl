{% include "housekeeping/base/header.tpl" %}
<body>
    {% set campaignManagementActive = "active" %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_campaign_management.tpl" %}
		<h2 class="mt-4">Bot Guide tool</h2>				  		
		  {% include "housekeeping/base/alert.tpl" %}
		  <br />		  
		  <p><b>Create speech</b></p>
		  <p>This tool allows you to add a speech to Bot Guide.</p>
		  <div class="alert__tool">		  
			  <form class="alert__tool__form" method="post">	
					<div class="alert__tool__recipient">
						<label for="user">The key</label>
						<input type="text" name="speechKey" class="" id="speechKey" />
					</div>
					<div class="alert__tool__recipient">
						<label for="user">The response</label>
						<input type="text" name="response" class="" id="response" />
					</div>
					<div class="alert__tool__recipient">
						<label for="user">The trigger</label>
						<input type="text" name="speechTrigger" class="" id="speechTrigger" />
					</div>					
				<div class="alert__tool__submit">
					<button type="submit" name="action" value="createSpeech">Add speech</button>
				</div>
			  </form>
		  </div>
		  <hr />		  
		  <p><b>Find speech</b></p>
		  <p>This tool allows you to search a Bot Guide speech with response starting or by key.</p>
		<form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
			<div class="">
				<label>Search</label>
				{% autoescape 'html' %}
				<input type="text" name="searchStr" id="searchStr" />
				{% endautoescape %}
			</div>
			<button type="submit" name="action" value="searchSpeech">Submit</button>
		</form>		  
		  {% if searchBotGuideDetails|length > 0 %}
		  <br />
		  <p>Speeches starting with '{{ query }}' or with key '{{ query }}'</p>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Speech key</th>							
							<th>Response</th>
							<th>Speech trigger</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for speeches in searchBotGuideDetails %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="speechKeyOriginal" value="{{ speeches.getSpeechKey() }}" />
								<td><input type="text" name="speechKey" value="{{ speeches.getSpeechKey() }}" style="width:100%" /></td>
								<td><input type="text" name="response" value="{{ speeches.getResponse() }}" style="width:100%" /></td>
								<td><input type="text" name="speechTrigger" value="{{ speeches.getSpeechTrigger() }}" style="width:100%" /></td>	
								<td><button type="submit" name="action" value="saveSpeech">Save</button><button type="submit" name="action" value="deleteSpeech">Delete</button></td>
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
		  <p><b>List of current speeches</b></p>
		  <p>This tool allow you to see the complete list of all current Bot Guide speeches in the Hotel.</p>
		  <div class="pagination-buttons-box">
		  {% if nextSpeeches|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/botguide?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousSpeeches|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/botguide?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
		  </div>			  
			  {% if botguideSpeech|length > 0 %}
			  <div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Speech key</th>							
							<th>Response</th>
							<th>Speech trigger</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for speeches in botguideSpeech %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="speechKeyOriginal" value="{{ speeches.getSpeechKey() }}" />
								<td><input type="text" name="speechKey" value="{{ speeches.getSpeechKey() }}" style="width:100%" /></td>
								<td><input type="text" name="response" value="{{ speeches.getResponse() }}" style="width:100%" /></td>
								<td><input type="text" name="speechTrigger" value="{{ speeches.getSpeechTrigger() }}" style="width:100%" /></td>	
								<td><button type="submit" name="action" value="saveSpeech">Save</button><button type="submit" name="action" value="deleteSpeech">Delete</button></td>
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