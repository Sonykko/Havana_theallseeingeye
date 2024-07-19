{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
		<h2 class="mt-4">Rank manager</h2>
		  <p>This tool allows you to set the rank to a Habbo if you are not in the same room (you don't even need to be in the hotel).</p>
		  {% include "housekeeping/base/alert.tpl" %}
		  <form class="table-responsive col-md-4" method="post" style="padding-left: 0;">	
		    <div class="form-group">
				<label for="user"><b>The recipient</b></label>
				<input type="text" name="user" class="form-control" id="user" placeholder="Enter here the username..." value="" />
			</div>
			<div class="form-group">
				<label>Select rank</label>
				<select name="rankId" id="rankId" class="form-control">
					{% set num = 1 %}
					{% for ranks in allRanks %}
					<option value="{{ ranks.id }}">{{ ranks.name }} (id: {{ ranks.id }})</option>
					{% set num = num + 1 %}
					{% endfor %}
				</select>
			</div>
			<button type="submit">Set rank</button>
		  </form>
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
</body>
</html>