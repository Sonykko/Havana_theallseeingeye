{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set configurationsActive = " active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_system_status.tpl" %}
     <!--<h2 class="mt-4">{% if categoryName == "loader" %}Loader{% endif %}{% if categoryName == "maintenance" %}Maintenance{% endif %} settings</h2>		
		<p>These are the all configurations of the {% if categoryName == "loader" %}loader{% endif %}{% if categoryName == "maintenance" %}maintenance{% endif %}.</p>-->
	<h2 class="mt-4">{% if CategoryExists %}<text style="text-transform: capitalize;">{{ categoryName }}</text>{% else %}Configuration{% endif %} settings</h2>		
		<p>{% if CategoryExists %}These are the all configurations of the {{ categoryName }}.{% else %}<text style="color:red;">Please select a setting in order to edit them.</text>{% endif %}</p>
		 {% if CategoryExists %}
		 {% include "housekeeping/base/alert.tpl" %}
          <div class="table-responsive">
		    <form method="post">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th style="width: 50%;">Name</th>
				  <th>Value</th>
                </tr>
              </thead>
              <tbody>
				{% for settings in configs %}
                <tr>
				  <td>{{ settings.description }}</td>
				  <td>
						<input type="text" name="{{ settings.setting }}" class="form-control" id="searchFor" value="{{ settings.value }}">
				  </td>
                  </tr>
			   {% endfor %}
              </tbody>
            </table>
			<div class="form-group"> 
				<button type="submit">Save Configuration</button>
			</div>
		</form>
      </div>
	  {% endif %}
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