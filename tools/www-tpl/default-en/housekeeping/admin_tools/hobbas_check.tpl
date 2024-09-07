{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
    <h2 class="mt-4">Check Hobba applicant</h2>
    <p>With this tool you can check if a user is qualified to become a Hobba and if not, why.</p>
    <form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
        <div class="">
            <label for="userName" style="font-weight: normal!important;margin-bottom: 0!important">{{ site.siteName }} name</label>
            <input type="text" name="userName" class="" id="userName" placeholder="" value="" />
        </div>
        <div class="">
            <text>or</text>
        </div>
        <div class="">
            <label for="userID" style="font-weight: normal!important;margin-bottom: 0!important">User ID</label>
            <input type="text" name="userID" class="" id="userID" placeholder="" value="" />
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
	{% else %}
	{% include "housekeeping/base/alert.tpl" %}
	{{ finalMessage }}
    {% endif %}		
</body>
</html>