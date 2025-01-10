{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set dashboardActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_statistics.tpl" %}
     <h2 class="mt-4">Hotel Statistics</h2>
		  <p>Welcome to the housekeeping for {{ site.siteName }} Hotel, here you can manage a lot of things at once, such as users, news, site content and view the statistics of the hotel.</p>
		   <div class="table-responsive col-md-4">
            <table class="table table-striped">
			<thead>
				<tr>
					<td></td>
					<td></td>
				</tr>
			</thead>
			<tbody class="col-md-4">
				<tr>
					<td><strong>Havana Version</strong></td>
					<td>1.2</td>
				</tr>
				<tr>
					<td>Users</td>
					<td>{{ stats.userCount }}</td>
				</tr>
				<tr>
					<td>Room Items</td>
					<td>{{ stats.roomItemCount }}</td>
				</tr>
				<tr>
					<td>Inventory Items</td>
					<td>{{ stats.inventoryItemsCount }}</td>
				</tr>
				<tr>
					<td>Groups</td>
					<td>{{ stats.groupCount }}</td>
				</tr>
				<tr>
					<td>Pets</td>
					<td>{{ stats.petCount }}</td>
				</tr>
				<tr>
					<td>Photos</td>
					<td>{{ stats.photoCount }}</td>
				</tr>
			</tbody>
			</table>
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
</body>
</html>