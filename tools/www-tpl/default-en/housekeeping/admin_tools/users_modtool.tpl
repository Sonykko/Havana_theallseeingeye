{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Remote banning and kicking</h2>		
		<p>This tool allows you to kick or ban Habbos if you are not in the same room (you don't even need to be in the hotel). You must include a message to explain to the {{ site.siteName }} why they are being removed from the hotel.</p>
		<!--<p><i>Use <b>Extra info</b> field for additional details like chat log urls etc. The contents will be included into this events chat record.</i></p>-->
		{% include "housekeeping/base/alert.tpl" %}
		{% if isValidUser %}
		<div class="ban__tool">
			<form method="post">
				<table>
					<thead>
						<tr>
							<th>
								<label>The bad guy</label>
							</th>
							<th style="margin-bottom: 0.5rem;">
								<label></label>
							</th>
							<th>
								<label>Message</label>					
							</th>
							<th>
								<label>Ban time</label>
							</th>
							<th style="display: flex;">
								<label>Extra info</label>						
							</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<input type="text" id="text" name="username"  value="{{ userBan }}" readonly />
							</td>
							<td>
								<select name="commonMessage" id="commonMessage">
									<option value="">Choose a common message</option>
									{% for CFHTopics in CFHTopics %}
									<option value="{{ CFHTopics.getSanctionReasonValue() }}. {{ CFHTopics.getSanctionReasonDesc() }}">{{ CFHTopics.getSanctionReasonValue() }}</option>
									{% endfor %}
								</select>					
							</td>
							<td>
								<input type="text" id="customMessage" name="customMessage"  value="" />
							</td>
							<td>
								<select name="banSeconds" id="banSeconds" class="">
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
							</td>
							<td>
								<textarea id="notes" name="notes"></textarea>
							</td>
							<td class="form-group" style="margin-bottom: 0;">
								<label style="display: flex;align-items: center;user-select: none;margin-bottom: 0;">
									<input type="checkbox" id="doBanMachine" name="doBanMachine" value="true" style="margin-right: 10px;">Also ban computer for more than one hour
								</label>
							</td>
							<td class="form-group" style="margin-bottom: 0;">
								<label style="display: flex;align-items: center;user-select: none;margin-bottom: 0;">
									<input type="checkbox" id="doBanIP" name="doBanIP" value="true" style="margin-right: 10px;">Also ban IP address for more than one hour
								</label>
							</td>
							<td style="margin-bottom: 5px;">
								<button type="submit" name="action" value="ban">Ban</button>
								<button type="submit" name="action" value="kick">Kick</button>
							</td>					
						</tr>
					</tbody>
				</table>
			</form>
		</div>
		{% endif %}
      </div>
    </div>
  </div>
</body>
</html>