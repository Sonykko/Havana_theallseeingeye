{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
    <h2 class="mt-4">Trusted person tool</h2>
    <p>With this tool you can set or revoke a trusted person for a given user.</p>
	{% include "housekeeping/base/alert.tpl" %}
    <form class="table-responsive col-md-4" method="post">
       <div class="form-group">
            <label for="userName">{{ site.siteName }} name</label>
            <input type="text" name="userName" class="form-control" id="userName" placeholder="Enter here the {{ site.siteName }} name..." value="" />
        </div>
        <div class="form-group">
            <text>OR</text>
        </div>
        <div class="form-group">
            <label for="userID">User ID</label>
            <input type="text" name="userID" class="form-control" id="userID" placeholder="Enter here the user ID..." value="" />
        </div>
			<div class="form-group">
				<label>Choose action</label>
				<select name="type" id="type" class="form-control">
					<option value="1" selected>Give trust person</option>
					<option value="0">Remove trust person</option>
				</select>
			</div>		
        <button type="submit">Submit</button>
    </form>
	
    <h2 class="mt-4">Manage current Trusted Person</h2>
    <p>Here you can see all the current active trusted persons.</p>
	<div class="pagination-buttons-box">
    {% if nextTrustedPersons|length > 0 %}
        {% set ourNextPage = page + 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/trusted_person?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
    {% endif %}
    {% if previousTrustedPersons|length > 0 %}
        {% set ourNextPage = page - 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/trusted_person?page={{ ourNextPage }}"><button type="button">Go back</button></a>
    {% endif %}
	</div>
	{% if players|length > 0 %}
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>User name</th>
                    <th>User ID</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                {% set num = 1 %}
                {% for player in players %}
                <tr>          
                    <td>{{ player.name }}</td>
                    <td>{{ player.id }}</td>
                    <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/trusted_person?revoke={{ player.name }}""><button type="button">Revoke trust person</button></a></td>
                </tr>
                {% set num = num + 1 %}
                {% endfor %}
            </tbody>
        </table>
    </div>	
	{% else %}
	<p><i>Nothing found to display.</i></p>
	{% endif %} 

    <h2 class="mt-4">View Trusted Person logs</h2>
    <p>Here you can see the most recent logs of Trusted Persons tool log.</p>
	<div class="pagination-buttons-box">
    {% if nextTrustedPersons|length > 0 %}
        {% set ourNextPage = page + 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/trusted_person?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
    {% endif %}
    {% if previousTrustedPersons|length > 0 %}
        {% set ourNextPage = page - 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/trusted_person?page={{ ourNextPage }}"><button type="button">Go back</button></a>
    {% endif %}
	</div>
	{% if trustedPersons|length > 0 %}
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <!--<th>ID</th>-->				
                    <th>User name</th>
                    <th>User ID</th>
                    <th>By Staff</th>
                    <th>Date</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
                {% set num = 1 %}
                {% for TrustedPersonList in trustedPersons %}
                <tr>
                    <!--<td>{{ TrustedPersonList.id }}</td>-->                
                    <td>{% if TrustedPersonList.userName|length > 0 %}{{ TrustedPersonList.userName }}{% else %}<i>Not provided</i>{% endif %}</td>
                    <td>{% if TrustedPersonList.userId > 0 %}{{ TrustedPersonList.userId }}{% else %}<i>Not provided</i>{% endif %}</td>
                    <td>{{ TrustedPersonList.staff }}</td>
                    <td>{{ TrustedPersonList.timestamp }}</td>
                    <td>{% if TrustedPersonList.type == 1 %}Trust person{% else %}Untrust person{% endif %}</td>
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