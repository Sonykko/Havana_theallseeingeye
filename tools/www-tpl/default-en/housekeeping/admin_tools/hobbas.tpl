{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
    <h2 class="mt-4">Check Hobba applicant</h2>
    <p>With this tool you can check if a user is qualified to become a Hobba and if not, why.</p>
    <form class="table-responsive col-md-4" method="post" style="padding-left: 0;">
        <div class="form-group">
            <label for="userName"><b>{{ site.siteName }} name</b></label>
            <input type="text" name="userName" class="form-control" id="userName" placeholder="Enter here the {{ site.siteName }} name..." value="" />
        </div>
        <div class="form-group">
            <text>OR</text>
        </div>
        <div class="form-group">
            <label for="userID"><b>User ID</b></label>
            <input type="text" name="userID" class="form-control" id="userID" placeholder="Enter here the user ID..." value="" />
        </div>
        <button type="submit">Search</button>
    </form>
	
    {% if hasReasons %}
	<hr>
	{% include "housekeeping/base/alert.tpl" %}
            {% for reason in reasons %}
                <p>{{ reason }}</p>
            {% endfor %}
		{{ finalMessage }}
	<hr>
	{% else %}
	{% include "housekeeping/base/alert.tpl" %}
	{{ finalMessage }}
    {% endif %}		

    <h3 class="mt-4">View Hobba applications form logs</h3>
    <p>Here you can see the most recent logs of users Hobba applications.</p>
    {% if nexthobbasFormLogs|length > 0 %}
        {% set ourNextPage = page + 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/hobbas?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
    {% endif %}
    {% if previoushobbasFormLogs|length > 0 %}
        {% set ourNextPage = page - 1 %}
        <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/hobbas?page={{ ourNextPage }}"><button type="button">Go back</button></a>
    {% endif %}
    <div class="table-responsive" style="padding-left: 0px;">
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
                    <td>{{ HobbasFormsList.id }}</td>
                    <td>{{ HobbasFormsList.habboname }}</td>
                    <td>{{ HobbasFormsList.email }}</td>
                    <td>{{ HobbasFormsList.firstname }}</td>
                    <td>{{ HobbasFormsList.lastname }}</td>
                    <td>{{ HobbasFormsList.timestamp }}</td>
                    <td>{{ HobbasFormsList.picked_up }}</td>
                </tr>
                {% set num = num + 1 %}
                {% endfor %}
            </tbody>
        </table>
    </div>
</body>
</html>