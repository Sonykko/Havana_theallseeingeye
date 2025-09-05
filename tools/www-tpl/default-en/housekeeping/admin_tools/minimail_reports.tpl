{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">MiniMail Reports</h2>					
		<br/>
		<form class="table-responsive col-md-4 stickie__search__form" method="post" name="searchQuery">
			<div>
				<div style="display:flex;">
					<div class="stickie__search__names">
						<label>Sender name:</label>
						<label>Reporter name:</label>
					</div>
					
					<div class="stickie__search__inputs">
						<label>				
							<input type="text" id="text" name="sender" />
						</label>
						<label>				
							<input type="text" id="text" name="reporter" />
						</label>
				</div>				
			</div>
			<button type="submit" name="searchQuery" value="searchQuery">Filter reports</button>
		</form>
		<br/>
		<br/>
		{% include "housekeeping/base/alert.tpl" %}	
		{% if (showResults) and (totalReportsSearch > 0) %}
		<hr>
		<p style="font-size:16px;"><b>Search results</b></p>
		<p>Latest MiniMail complaints:</p>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr style="text-wrap: nowrap;">
						<th>Moderate</th>
						<th>Author</th>
						<th>Target</th>
						<th>Message Subject</th>
						<th>Message Content</th>
						<th>Date Sent</th>
					</tr>
				</thead>
				<tbody>
					{% set num = 1 %}
					{% for minimailMessage in latestReports %}
					<tr>
						<input type="hidden" value="{{ minimailMessage.getId() }}" id="messageId" />
						<td><input type="checkbox" value="{{ minimailMessage.getAuthorName() }}" id="senderName" /></td>
						<td class="senderColumn">{{ minimailMessage.getAuthorName() }}</td>
						<td class="targetColumn">{{ minimailMessage.getTargetName() }}</td>
						<td>{{ minimailMessage.getSubject() }}</td>
						<td>{{ minimailMessage.getMessage() }}</td>
						<td>{{ minimailMessage.getDate() }}</td>
					</tr>
					{% set num = num + 1 %}
					{% endfor %}
				</tbody>
			</table>
			<div style="margin-bottom:1rem;">
				<button type="submit" name="action" id="toggleCheckboxes">Toggle</button>
				<button type="submit" name="action" id="onlyDelete">Delete and archive</button>
				<button type="submit" name="action" id="onlyArchive">Archive</button>
			</div>
		</div>

		<p><b>Note:</b> when you click "Delete and archive" or "Ban and archive", stickie notes that have checkbox "Moderate" checked will be deleted from database.</p>
		<div class="stickie__ban__parent">
		<div class="stickie__ban__toggle">
			<a href="#" id="toggleBanTool"><p>Ban selected...</p></a>
		</div>

		<div style="display:none;" class="stickie__ban__tool" id="banTool">
		<form method="post">
			<div>
				<label>Ban selected accounts for:</label>
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
			<div>
				<label for="customMessage">Message:</label>
				<select name="commonMessage" id="commonMessage">
					<option value="">Choose a common message</option>
					{% for CFHTopics in CFHTopics %}
					<option value="{{ CFHTopics.getgetSanctionReasonValue()() }}. {{ CFHTopics.getgetSanctionReasonDesc()() }}">{{ CFHTopics.getgetSanctionReasonValue()() }}</option>
					{% endfor %}
				</select>
			</div>
			<div>				
				<input type="text" id="customMessage" name="customMessage"  value="" style="width: 500px;" />
			</div>
			<div>
				<label for="pwd">Extra info:</label>
			</div>
			<div>
				<textarea name="alertMessage" style="height: 100px;width: 500px;"></textarea>
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
				<button type="submit" name="action" value="massBan">Ban and archive</button>
			</div>
			<div id="hiddenUsernames"></div>
		</form>	
		</div>
		{% elseif (showResults) and (totalReportsSearch == 0)  %}
		<hr>
		<p style="font-size:16px;"><b>Search results</b></p>
		<p>Latest MiniMail complaints:</p>
		<p><i>No reports found.</i></p>
		{% endif %}	
      </div>
    </div>
  </div>
	<script>			
		document.addEventListener('DOMContentLoaded', function() {
			var hiddenUsernames = document.getElementById('hiddenUsernames');
			var checkboxes = document.querySelectorAll('input#senderName');
			
			function updateHiddenFields() {
				const hiddenUsernames = document.getElementById('hiddenUsernames');
				hiddenUsernames.innerHTML = '';
				
				const checkboxes = document.querySelectorAll('input#senderName');
				const messagesId = [];
				
				checkboxes.forEach(function (checkbox) {
					if (checkbox.checked) {
						const usernameInput = document.createElement('input');
						usernameInput.type = 'hidden';
						usernameInput.name = 'userNames';
						usernameInput.value = checkbox.value;
						hiddenUsernames.appendChild(usernameInput);

						const row = checkbox.closest('tr');
						const messageId = row.querySelector('input#messageId').value;
						messagesId.push(messageId);
					}
				});

				const messagesIdInput = document.createElement('input');
				messagesIdInput.type = 'hidden';
				messagesIdInput.name = 'messagesId';
				messagesIdInput.value = messagesId.join(',');
				hiddenUsernames.appendChild(messagesIdInput);
			}			
			
			checkboxes.forEach(function(checkbox) {
				checkbox.addEventListener('change', updateHiddenFields);
			});
			
			
			document.getElementById('toggleCheckboxes').addEventListener('click', function() {
				var checkboxes = document.querySelectorAll('table input[type="checkbox"]');
				var toggle = !checkboxes[0].checked;
				checkboxes.forEach(function(checkbox) {
					checkbox.checked = toggle;
				});
				setTimeout(updateHiddenFields, 0);
			});
			
			document.getElementById('toggleBanTool').addEventListener('click', function(event) {
				event.preventDefault();
				var form = document.getElementById('banTool');
				updateHiddenFields();
				form.style.display = form.style.display === 'none' || form.style.display === '' ? 'block' : 'none';
			});
			
			document.getElementById('onlyDelete').addEventListener('click', function(event) {
				event.preventDefault();
				updateHiddenFields();
				
				var form = document.createElement('form');
				form.method = 'post';
				form.action = window.location.href;
				
				var actionInput = document.createElement('input');
				actionInput.type = 'hidden';
				actionInput.name = 'onlyDelete';
				actionInput.value = 'true';
				form.appendChild(actionInput);
				
				var hiddenFields = document.getElementById('hiddenUsernames').cloneNode(true);
				form.appendChild(hiddenFields);
				
				document.body.appendChild(form);
				form.submit();
			});
			
			
				document.getElementById('onlyArchive').addEventListener('click', function(event) {
				event.preventDefault();
				updateHiddenFields();
				
				var form = document.createElement('form');
				form.method = 'post';
				form.action = window.location.href;
				
				var actionInput = document.createElement('input');
				actionInput.type = 'hidden';
				actionInput.name = 'onlyArchive';
				actionInput.value = 'true';
				form.appendChild(actionInput);
				
				var hiddenFields = document.getElementById('hiddenUsernames').cloneNode(true);
				form.appendChild(hiddenFields);
				
				document.body.appendChild(form);
				form.submit();
			});			
		});
	</script>    
</body>
</html>