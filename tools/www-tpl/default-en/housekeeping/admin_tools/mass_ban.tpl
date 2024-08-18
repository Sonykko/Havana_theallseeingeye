{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Mass banning and kicking</h2>		
		<p style="padding-bottom:50px;">This tool allows you to kick or ban Habbos if you are not in the same room (you don't even need to be in the hotel). You must include a message to explain to the Habbos why they being removed from the hotel.</p>
		{% include "housekeeping/base/alert.tpl" %}
		<div style="width: 100%;">
			<form class="massban__tool__form" method="post">
				<div class="massban__tool__badguys">
					<label for="pwd">The bad guys</label>
					<textarea class="" id="userNames" name="userNames" placeholder="Type every single name with a line break..."></textarea>
				</div>
				<div class="massban__tool__commonmessage">
					<select name="commonMessage" id="commonMessage" class="">
						<option value="">Choose a common message</option>
						{% for CFHTopics in CFHTopics %}
						<option value="{{ CFHTopics.sanctionReasonValue }}. {{ CFHTopics.sanctionReasonDesc }}">{{ CFHTopics.sanctionReasonValue }}</option>
						{% endfor %}
					</select>
				</div>
				<div class="massban__tool__custommessage">
					<label for="customMessage">Message</label>
					<input type="text" class="" id="customMessage" name="customMessage"  value="" />
				</div>
				<div class="massban__tool__time">
					<label>Ban time</label>
					<select name="banSeconds" id="banSeconds">
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
				<!--<div class="" style="display: flex; align-items: center; padding-bottom: 10px; justify-content: space-between; width: 100%;">
					<label for="pwd" style="padding-right: 20px;">Extra info</label>
					<textarea class="" name="extraNotes" placeholder="Enter here additional notes..." style="overflow-y: scroll; height: 100px; width: 350px;"></textarea>
				</div>-->
				<div class="" style="padding-bottom: 0px; width: 100%; display: flex; justify-content: space-between; align-items: flex-start;">
					<label style="display: flex; align-items: center; user-select: none; font-weight: normal;">
						Also ban computer for more than one hour
					</label>
					<input type="checkbox" id="doBanMachine" name="doBanMachine" value="true">
				</div>
				<div class="" style="padding-bottom: 10px; width: 100%; display: flex; justify-content: space-between; align-items: flex-start;">
					<label style="display: flex; align-items: center; user-select: none; font-weight: normal;">
						Also ban IP address for more than one hour
					</label>
					<input type="checkbox" id="doBanIP" name="doBanIP" value="true">
				</div>
				<div style="margin-bottom: 5px;">
					<button type="submit" name="action" value="massBan">Ban</button>
					<button type="submit" name="action" value="massKick">Kick</button>
				</div>
			</form>
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
</body>
</html>