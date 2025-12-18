{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set campaignManagementActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
		<h2 class="mt-4">Edit Infobus Poll</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>View Infobus Poll Results</p>
	
		{% if noAnswers %}
		<p>There are no answers to this poll yet</p>
		{% else %}
		<p><img src="{{ imageData }}"></p>
		{% endif %}
      </div>
    </div>
  </div>
</body>
</html>