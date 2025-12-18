{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Group Description Reports</h2>		
		{% include "housekeeping/base/alert.tpl" %}
		
		<br/>
		<p>[] <b>{{ totalReports }}</b> reports in database.</p>
		<p style="font-size:16px;"><b>Query</b></p>
		
		<form id="getAllReports" action="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/groupdesc_reports_list" method="post">
			<input type="hidden" name="latest" id="latest" />
			<a href="#" onclick="submitForm()"><text style="font-weight:bold;font-size:16px;">Get latest reports</text></a>			
		</form>
		<br/>
		
		<form class="table-responsive col-md-4 stickie__search__form" method="post" name="searchQuery">
			<div class="stickie__search__query">
				<div style="display:flex;">
					<div class="stickie__search__names">
						<label style="margin-bottom: 2rem;"></label>
						<label>User name:</label>
						<label>Show max:</label>
					</div>
					
					<div class="stickie__search__inputs">
						<label>				
							<select name="criteria" id="criteria">
								<option value="1">Reported {{ site.siteName }}</option>
								<option value="0">New</option>
							</select>
						</label>
						<label>				
							<input type="text" id="text" name="reportedUser" />
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
								<option value="15" selected>15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
							</select>	
						</label>	
					</div>
				</div>
				<button type="submit" name="searchQuery" value="searchQuery" style="">Send request</button>
			</div>
		</form>
		{% if showResults %}
		<br/>
		<p style="font-size:16px;"><b>Results</b></p>
		<p>Search criteria: {{ searchCriteria }}</p>
		{% if totalReportsSearch > 0 %}
		<p>One item found.<b>{{ totalReportsSearch }}</b></p>
		<div class="table-responsive">
			<table class="table table-striped">
				<thead>
					<tr style="text-wrap: nowrap;">
						<th>Moderate</th>
						<th>Type</th>
						<th>Object ID</th>
						<th>Message</th>
						<th>Group desc</th>
						<th>Date</th>
					</tr>
				</thead>
				<tbody>
					{% set num = 1 %}
					{% for contentReportsList in latestReports %}
					<tr>
						<input type="hidden" value="{{ contentReportsList.getId() }}" class="reportId" />
						<td><input type="checkbox" value="{{ contentReportsList.getObjectId() }}" class="objectId" /></td>
						<td>{{ contentReportsList.getType() }}</td>
						<td>{{ contentReportsList.getObjectId() }}</td>
						<td>{{ contentReportsList.getMessage() }}</td>
						{% autoescape'html' %}
						<td>{{ contentReportsList.getValue() }}</td>
						{% endautoescape %}
						<td>{{ contentReportsList.getTimestamp() }}</td>
					</tr>
					{% set num = num + 1 %}
					{% endfor %}
				</tbody>
			</table>
		{% else  %}		
			<p>Nothing found to display.</p>
		{% endif %}			
			<div style="display: ruby;">
				<p>Remplacement text: :</p>
				{% autoescape'html' %}
				<input type="text" id="remplacementText" />
				{% endautoescape %}
			</div>
			<form id="processReportsForm" action="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/groupdesc_reports_list" method="post">
				<input type="hidden" name="objectIds" id="selectedObjectIds" />
				<input type="hidden" name="reportIds" id="selectedReportIds" />
				<input type="hidden" name="replacementText" id="replacementTextInput" />
				<input type="hidden" name="action" value="process" />
				<button type="button" name="action" id="undo">Undo</button>
				<button type="submit" id="process">Process</button>
			</form>	
			<br/>
			<p><b>Note:</b> when you click "Process", Groups whose checkbox has been checked description will be changed. Other reports are ignored and deleted from database.</p>
		</div>				
		{% endif %}	
      </div>
    </div>
  </div>
	<script>
		function submitForm() {
			document.getElementById('getAllReports').submit();
		}
		
		document.getElementById("undo").addEventListener("click", function () {
			document.getElementById("remplacementText").value = "";
		});		

		document.getElementById("process").addEventListener("click", function (e) {
			e.preventDefault();

			let selectedObjects = [];
			let selectedReports = [];

			document.querySelectorAll("input.objectId:checked").forEach((checkbox) => {
				selectedObjects.push(checkbox.value);
				selectedReports.push(checkbox.closest("tr").querySelector(".reportId").value);
			});

			let replacementText = document.getElementById("remplacementText").value;

			document.getElementById("selectedObjectIds").value = selectedObjects.join(",");
			document.getElementById("selectedReportIds").value = selectedReports.join(",");
			document.getElementById("replacementTextInput").value = replacementText;

			document.getElementById("processReportsForm").submit();
		});	
	</script>    
</body>
</html>