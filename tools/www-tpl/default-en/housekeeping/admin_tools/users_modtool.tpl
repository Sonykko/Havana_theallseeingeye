{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Ban tool</h2>		
		<p>This tool allows you to kick or ban Habbos if you are not in the same room (you don't even need to be in the hotel). being removed from the hotel.
		<br><i>Use <b>Extra info</b> field for additional details like chat log urls etc. The contents will be included into this events chat record.</i></p>
		{% include "housekeeping/base/alert.tpl" %}
		{% if isValidUser %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>The badguy</label>
				<input type="text" class="form-control" id="text" name="username"  value="{{ userBan }}" placeholder="Enter here the username..." readonly>
			</div>
			<div class="form-group">
				<label>Choose a common message</label>
				<select name="alertMessage" id="alertMessage" class="form-control">
					{% set num = 1 %}
					{% for CFHTopics in CFHTopics %}
					<option value="{{ CFHTopics.sanctionReasonValue }}. {{ CFHTopics.sanctionReasonDesc }}" {%if CFHTopics.sanctionReasonId == 'AUTO_TRIGGER' %}selected{% endif %}>{{ CFHTopics.sanctionReasonValue }}</option>
					{% set num = num + 1 %}
					{% endfor %}
				</select>
			</div>
			<div class="form-group">
				<label>Ban time</label>
				<select name="banSeconds" id="banSeconds" class="form-control">
					<option value="7200" selected>2 Hours</option>
					<option value="14400">4 Hours</option>
					<option value="43200">12 Hours</option>
					<option value="86400">24 Hours</option>
					<option value="172800">2 Days</option>
					<option value="259200">3 Days</option>
					<option value="604800">7 Days</option>
					<option value="1209600">14 Days</option>
					<option value="1814400">21 Days</option>
					<option value="2592000">30 Days</option>
					<option value="5184000">60 Days</option>
					<option value="31536000">365 Days</option>
					<option value="63072000">730 Days</option>
					<option value="360100800">4167 Days</option>
				</select>
			</div>
			<div class="form-group">
				<label for="pwd">Extra info</label>
				<textarea class="form-control" name="alertMessage" placeholder="Enter here additional notes..." placeholder="Remote ban tool ban" style="height: 100px"></textarea>
			</div>
			<div class="form-group" style="margin-bottom: 0;">
				<label style="display: flex;align-items: center;user-select: none;margin-bottom: 0;">
					<input type="checkbox" id="doBanMachine" name="doBanMachine" value="true" style="margin-right: 10px;">Also ban computer for more than one hour
				</label>
			</div>
			<div class="form-group">
				<label style="display: flex;align-items: center;user-select: none;margin-bottom: 0;">
					<input type="checkbox" id="doBanIP" name="doBanIP" value="true" style="margin-right: 10px;">Also ban IP address for more than one hour
				</label>
			</div>
			<div style="margin-bottom: 5px;">
				<button type="submit" name="action" value="ban">Ban</button>
				<button type="submit" name="action" value="kick">Kick</button>
			</div>
		</form>
		{% endif %}
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