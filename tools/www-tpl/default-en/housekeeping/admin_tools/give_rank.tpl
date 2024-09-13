{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
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
						<option value="">Choose a rank</option>
						{% set num = 1 %}
						{% for ranks in allRanks %}
						<option value="{{ ranks.id }}">{{ ranks.name }} (id: {{ ranks.id }})</option>
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
		  <div style="display: flex;flex-direction:column;">
                {% for staff in staffDetailsList %}
                    <text>{{ staff.name }} - {{ staff.rankName }} (id: {{ staff.rankId }})</text>
                {% endfor %}	
		  </div>
		  <hr />
		  <p><b>Staff texts variables</b></p>
		  <p>This tool allows you to set the texts variables information of Staff ranks.</p>
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
								<td><input type="hidden" name="rankIdVars" value="{{ ranks.id }}" />{{ ranks.id }}</td>
								<td><input type="text" name="rankNameVars" value="{{ ranks.name }}" /></td>
								<td><input type="text" name="rankBadgeVars" value="{{ ranks.badge }}" style="margin-right:5px;" class="badge-input" /> - Preview: <img src="{{ site.sitePath }}/c_images/album1584/{{ ranks.badge }}.gif" class="badge-preview" /></td>
								<td><textarea name="rankDescVars" style="width:100%;">{{ ranks.description }}</textarea></td>
								<td><button type="submit" name="action" value="staffVars">Save</button></td>
							</form>
						</tr>
						{% set num = num + 1 %}
						{% endfor %}
					</tbody>
				</table>
			</div>		  
      </div>
    </div>
  </div>
  <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"></script>
  <script src="https://blackrockdigital.github.io/startbootstrap-simple-sidebar/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  </script>
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
            img.src = '{{ site.sitePath }}/c_images/album1584/' + badgeCode + '.gif';
        }, typingInterval);
    });
});
  </script>
</body>
</html>