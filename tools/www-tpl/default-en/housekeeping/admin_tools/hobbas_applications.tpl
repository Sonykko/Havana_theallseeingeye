{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
    <h2 class="mt-4">Hobba applications</h2>
    <p>Here you can see and admin the users Hobba applications form logs.</p>
	{% include "housekeeping/base/alert.tpl" %}	
	<div class="pagination-buttons-box">
    {% if nexthobbasFormLogs|length > 0 %}
        {% set ourNextPage = page + 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/hobbas?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
    {% endif %}
    {% if previoushobbasFormLogs|length > 0 %}
        {% set ourNextPage = page - 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/hobbas?page={{ ourNextPage }}"><button type="button">Go back</button></a>
    {% endif %}
	</div>
	{% if hobbasFormLogs|length > 0 %}
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>User name</th>
                    <th>Email</th>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                {% set num = 1 %}
                {% for HobbasFormsList in hobbasFormLogs %}
                <tr>
                    <td>{{ HobbasFormsList.getId() }}</td>
                    <td>{{ HobbasFormsList.getHabboname() }}</td>
                    <td>{{ HobbasFormsList.getEmail() }}</td>
                    <td>{{ HobbasFormsList.getFirstname() }}</td>
                    <td>{{ HobbasFormsList.getLastname() }}</td>
                    <td>{{ HobbasFormsList.getTimestamp() }}</td>
                    <td>
					{% if HobbasFormsList.getPickUp() == '1' %}
					Picked
					{% else %}
						<form method="post">
							<input type="hidden" id="logId" name="logId" value="{{ HobbasFormsList.getId() }}" />
							<button type="submit">Pick Up</button>
						</form>					
					{% endif%}
					</td>
                </tr>
                {% set num = num + 1 %}
                {% endfor %}
            </tbody>
        </table>
    </div>
	{% else %}
	<p><i>Nothing found to display.</i></p>
	{% endif %} 
</body>
</html>