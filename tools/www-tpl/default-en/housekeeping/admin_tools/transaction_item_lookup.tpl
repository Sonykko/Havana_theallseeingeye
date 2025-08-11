{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Transaction Item Lookup</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% include "housekeeping/admin_tools/transaction/search_results.tpl" %}
      </div>
    </div>
  </div>
</body>
</html>