{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Transaction Lookup</h2>		
		<p>Lookup transaction by a specific user, either enter their username. Will display all transaction in the past month.</p>
		{% include "housekeeping/base/alert.tpl" %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label for="searchQuery">Player name</label>
				<input type="text" name="searchQuery" class="form-control" id="searchQuery" placeholder="Looking for...">
			</div>
			<button type="submit">Perform Search</button>
		</form>
		{% include "housekeeping/admin_tools/transaction/search_results.tpl" %}
      </div>
    </div>
  </div>
</body>
</html>