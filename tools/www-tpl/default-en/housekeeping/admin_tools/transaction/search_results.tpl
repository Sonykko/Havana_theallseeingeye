{% if transactions|length > 0 %}
		<hr/>
		<p style="font-size:16px;"><b>Search results</b></p>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th></th>
				  <th>Item ID</th>
                  <th>Description</th>
                  <th>Coins</th>
				  <th>Pixels</th>
				  <th>Amount</th>
                  <th>Created At</th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for transaction in transactions %}
                <tr>
                  <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/transaction/track_item?id={{ transaction.getItemId() }}">Track this item</a></td>
				  <td>{{ transaction.getItemId() }}</td>
				  <td>{{ transaction.description }}</td>
                  <td>{{ transaction.costCoins }}</td>
                  <td>{{ transaction.costPixels }}</td>
				  <td>{{ transaction.amount }}</td>
				  <td>{{ transaction.getFormattedDate() }}</td>
                </tr>
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
          </div>
{% endif %}
{% if noResults %}
		<hr/>
		<p style="font-size:16px;"><b>Search results</b></p>
		<p><i>No results found.</i></p>
{% endif %}	
