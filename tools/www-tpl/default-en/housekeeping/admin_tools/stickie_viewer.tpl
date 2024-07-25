{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Stickie Notes Reports</h2>		
		{% include "housekeeping/base/alert.tpl" %}
		
		<p>[] <b>{{ totalReports }}</b> reports in database.</p>
		<p><b>Query</b></p>
		
		<form id="getAllReports" action="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/stickie_viewer" method="post">
			<input type="hidden" name="latest" id="latest" />
			<a href="#" onclick="submitForm()">Get latest reports</a>
		</form>
		
		<form class="table-responsive col-md-4 stickie__search__form" method="post" name="searchQuery">
			<div class="stickie__search__query">
				<div style="display:flex;">
					<div class="stickie__search__names">
						<label>Criteria:</label>
						<label>User name:</label>
						<label>Show max:</label>
					</div>
					
					<div class="stickie__search__inputs">
						<label>				
							<select name="criteria" id="criteria">
								<option value="1">Stickie owner</option>
								<option value="0">New</option>
							</select>
						</label>
						<label>				
							<input type="text" id="text" name="username" />
						</label>
						<label>
							<select name="showMax" id="showMax">
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
								<option value="8">8</option>
								<option value="9">9</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20" selected>20</option>
							</select>	
						</label>	
					</div>
				</div>
				<button type="submit" name="searchQuery" value="searchQuery" style="float:right;">Query</button>
			</div>
		</form>
		{% if (showResults) and (totalReportsSearch > 0) %}
		<p><b>Results</b></p>
		<p>Search criteria: {{ searchCriteria }}</p>
		<p>One item found: <b>{{ totalReportsSearch }}</b></p>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr style="text-wrap: nowrap;">
						<th>Moderate</th>
						<th>Note update date</th>
						<th>Note creation date</th>
						<th>User name</th>
						<th>Text</th>
					</tr>
				</thead>
				<tbody>
					{% set num = 1 %}
					{% for stickieNotesList in latestReports %}
					<tr>
						<input type="hidden" value="{{ stickieNotesList.id }}" id="stickieId" />
						<input type="hidden" value="{{ stickieNotesList.roomId }}" id="stickieRoomId" />
						<td><input type="checkbox" value="{{ stickieNotesList.userName }}" id="stickieOwner" /></td>
						<td>{{ stickieNotesList.updatedAt }}</td>
						<td>{{ stickieNotesList.createdAt }}</td>
						<td>{{ stickieNotesList.userName }}</td>
						<td>{{ stickieNotesList.text }}</td>
					</tr>
					{% set num = num + 1 %}
					{% endfor %}
					
					{% set num = 1 %}
					{% for stickieNotesList in searchReports %}
					<tr>
						<input type="hidden" value="{{ stickieNotesList.id }}" id="stickieId" />
						<input type="hidden" value="{{ stickieNotesList.roomId }}" id="stickieRoomId" />
						<td><input type="checkbox" value="{{ stickieNotesList.userName }}" id="stickieOwner" /></td>
						<td>{{ stickieNotesList.updatedAt }}</td>
						<td>{{ stickieNotesList.createdAt }}</td>
						<td>{{ stickieNotesList.userName }}</td>
						<td>{{ stickieNotesList.text }}</td>
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
			<div >
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
					<option value="{{ CFHTopics.sanctionReasonValue }}. {{ CFHTopics.sanctionReasonDesc }}">{{ CFHTopics.sanctionReasonValue }}</option>
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
		<p>Nothing found to display.</p>
		<p><b>Note:</b> when you click "Delete and archive" or "Ban and archive", stickie notes that have checkbox "Moderate" checked will be deleted from database.</p>
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
	<script>
		function submitForm() {
			document.getElementById('getAllReports').submit();
		}
				
		document.addEventListener('DOMContentLoaded', function() {
			var hiddenUsernames = document.getElementById('hiddenUsernames');
			var checkboxes = document.querySelectorAll('input#stickieOwner');
			
			function updateHiddenFields() {
				hiddenUsernames.innerHTML = '';
				var stickieIds = [];
				
				checkboxes.forEach(function(checkbox) {
					if (checkbox.checked) {
						var usernameInput = document.createElement('input');
						usernameInput.type = 'hidden';
						usernameInput.name = 'userNames';
						usernameInput.value = checkbox.value;
						hiddenUsernames.appendChild(usernameInput);
						
						var row = checkbox.closest('tr');
						var stickieId = row.querySelector('input#stickieId').value;
						stickieIds.push(stickieId);
					}
				});
				
				var stickieIdsInput = document.createElement('input');
				stickieIdsInput.type = 'hidden';
				stickieIdsInput.name = 'stickieIds';
				stickieIdsInput.value = stickieIds.join(',');
				hiddenUsernames.appendChild(stickieIdsInput);
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
			
			$("#menu-toggle").click(function(e) {
				e.preventDefault();
				$("#wrapper").toggleClass("toggled");
			});
		});
	</script>    
</body>
</html>