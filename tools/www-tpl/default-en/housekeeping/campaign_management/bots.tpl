{% include "housekeeping/base/header.tpl" %}
<body>
    {% set campaignManagementActive = "active" %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_campaign_management.tpl" %}
		<h2 class="mt-4">Bot admin</h2>
		  {% include "housekeeping/base/alert.tpl" %}
		  <br />
		  <p><b>Find bot</b></p>
		  <p>This tool allows you to search a bots with name starting or by id.</p>
		<form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
			<div class="">
				<label>Search</label>
				{% autoescape 'html' %}
				<input type="text" name="searchStr" id="searchStr" />
				{% endautoescape %}
			</div>
			<button type="submit" name="action" value="searchBot">Submit</button>
		</form>
		  {% if searchBotsDetails|length > 0 %}
		  <br />
		  <p>Bots starting with '{{ query }}' or with id '{{ query }}'</p>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
							<th>Mission</th>
							<th>Figure</th>
							<th>Speech</th>
							<th>Response</th>
							<th>Unrecognised response</th>
							<th>Hand items</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for bots in searchBotsDetails %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="botId" value="{{ bots.getId() }}" />
								<td>{{ bots.getId() }}</td>
								<td><input type="text" name="name" value="{{ bots.getName() }}" style="width:100%" /></td>
								<td><input type="text" name="mission" value="{{ bots.getMission() }}" style="width:100%" /></td>
								<td>
									<text style="display:flex;margin-bottom:10px;">v31: <input type="text" name="figure" value="{{ bots.getFigure() }}" style="width:100%" /></text>
									<text style="display:flex;">r39: <input type="text" name="figureFlash" value="{{ bots.getFigureFlash() }}" style="width:100%" /></text>
								</td>
								<td><textarea name="speech" style="width:100%;">{{ bots.getSpeeches() }}</textarea></td>
								<td><textarea name="response" style="width:100%;">{{ bots.getResponses() }}</textarea></td>
								<td><textarea name="unrecognisedSpeech" style="width:100%;">{{ bots.getUnrecognisedSpeech() }}</textarea></td>
								<td><textarea name="drink" style="width:100%;">{{ bots.getDrinks() }}</textarea></td>
								<td><button type="submit" name="action" value="saveBot">Save</button></td>
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
		  <p><b>List of current bots</b></p>
		  <p>This tool allow you to see the complete list of all current bots in the Hotel.</p>
			  {% if bots|length > 0 %}
			  <div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
							<th>Mission</th>
							<th>Figure</th>
							<th>Speech</th>
							<th>Response</th>
							<th>Unrecognised response</th>
							<th>Hand items</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for bots in bots %}
						<tr>
						{% autoescape 'html' %}
							<form method="post">
								<input type="hidden" name="botId" value="{{ bots.getId() }}" />
								<td>{{ bots.getId() }}</td>
								<td><input type="text" name="name" value="{{ bots.getName() }}" style="width:100%" /></td>
								<td><input type="text" name="mission" value="{{ bots.getMission() }}" style="width:100%" /></td>
								<td>
									<text style="display:flex;margin-bottom:10px;">v31: <input type="text" name="figure" value="{{ bots.getFigure() }}" style="width:100%" /></text>
									<text style="display:flex;">r39: <input type="text" name="figureFlash" value="{{ bots.getFigureFlash() }}" style="width:100%" /></text>
								</td>
								<td><textarea name="speech" style="width:100%;">{{ bots.getSpeeches() }}</textarea></td>
								<td><textarea name="response" style="width:100%;">{{ bots.getResponses() }}</textarea></td>
								<td><textarea name="unrecognisedSpeech" style="width:100%;">{{ bots.getUnrecognisedSpeech() }}</textarea></td>
								<td><textarea name="drink" style="width:100%;">{{ bots.getDrinks() }}</textarea></td>
								<td><button type="submit" name="action" value="saveBot">Save</button></td>
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