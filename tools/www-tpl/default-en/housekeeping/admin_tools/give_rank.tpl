{% include "housekeeping/base/header.tpl" %}
<body>
    {% set adminToolsActive = "active" %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Rank manager</h2>				  		
		  {% include "housekeeping/base/alert.tpl" %}
		  <br />
		  <p><b>Give rank</b></p>
		  <p>This tool allows you to set the rank to a Habbo if you are not in the same room (you don't even need to be in the hotel).</p>
		  <div class="alert__tool">		  
			  <form class="alert__tool__form" method="post">	
					<div class="alert__tool__recipient">
						<label for="user">The recipient</label>
						<input type="text" name="user" class="" id="user" value="" />
					</div>
				<div class="alert__tool__commonmessage">
					<select name="rankId" id="rankId">
						<option value="" disabled selected>Choose a rank</option>
						{% set num = 1 %}
						{% for ranks in allRanks %}
						<option value="{{ ranks.getId() }}">{{ ranks.getName() }} (id: {{ ranks.getId() }})</option>
						{% set num = num + 1 %}
						{% endfor %}
					</select>
				</div>
				<div class="" style="width: 300px;gap: 10px;display: flex;padding-bottom: 10px;">					
					<input type="checkbox" name="sendAlert" value="true" checked />Notice the new rank to the recipient in the hotel
				</div>
				<div class="alert__tool__submit">
					<button type="submit" name="action" value="giveRank">Set rank</button>
				</div>
			  </form>
		  </div>
		  <hr />
		  <p><b>Staff team list</b></p>
		  <p>This list allows you to see information of Staff team.</p>
		  {% if staffDetailsList|length > 0 %}
		  <div style="display: flex;flex-direction:column;">
                {% for staff in staffDetailsList %}
                    <text>{{ staff.getName() }} - {{ staff.getRankName() }} (id: {{ staff.getRank().getRankId() }})</text>
                {% endfor %}	
		  </div>
		  {% else %}
		  <p><i>Nothing found to display.</i></p>
		  {% endif %} 		  
		  <hr />
		  <p><b>Staff texts variables</b></p>
		  <p>This tool allows you to set the texts variables information of Staff ranks.</p>
		  {% if allRanks|length > 0 %}
		  <div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Rank ID</th>
							<th>Name</th>
							<th>Badge</th>
							<th>Description</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for ranks in allRanks %}
						<tr>
							<form method="post">
								<td><input type="hidden" name="rankIdVars" value="{{ ranks.getId() }}" />{{ ranks.getId() }}</td>
								<td><input type="text" name="rankNameVars" value="{{ ranks.getName() }}" /></td>
								<td><input type="text" name="rankBadgeVars" value="{{ ranks.getBadge() }}" style="margin-right:5px;" class="badge-input" /> - Preview: <img src="{{ site.staticContentPath }}/c_images/album1584/{{ ranks.getBadge() }}.gif" class="badge-preview" /></td>
								<td><textarea name="rankDescVars" style="width:100%;">{{ ranks.getDescription() }}</textarea></td>
								<td><button type="submit" name="action" value="staffVars">Save</button></td>
							</form>
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
  <script>
document.querySelectorAll('.badge-input').forEach(function(input) {
    input.setAttribute('data-original', input.value);

    let typingTimer;
    const typingInterval = 1500;

    input.addEventListener('input', function() {
        clearTimeout(typingTimer);
        typingTimer = setTimeout(() => {
            let badgeCode = this.value.trim().toUpperCase();
            const originalCode = this.getAttribute('data-original');

            if (badgeCode === "") {
                badgeCode = originalCode;
                this.value = originalCode;
            }

            const img = this.closest('td').querySelector('.badge-preview');
            img.src = '{{ site.staticContentPath }}/c_images/album1584/' + badgeCode + '.gif';
        }, typingInterval);
    });
});
  </script>
</body>
</html>