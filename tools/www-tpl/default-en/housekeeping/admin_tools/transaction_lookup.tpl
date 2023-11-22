{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set bansActive = " active " %}
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
		<br>
		{% include "housekeeping/transaction/search_results.tpl" %}
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

{% include "housekeeping/base/footer.tpl" %}