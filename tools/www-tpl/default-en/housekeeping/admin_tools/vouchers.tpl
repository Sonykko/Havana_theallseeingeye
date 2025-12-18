{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Voucher codes</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<br />
		<p><b>Create code</b></p>
		<p>This tool allows you to add a code to Vouchers.</p>
		<div class="alert__tool">
			<form class="alert__tool__form" method="post">	
				<div class="alert__tool__recipient">
					<label for="code">The code</label>
					<input type="text" name="voucherCode" class="" id="voucherCode" value="{{ voucherRandom }}" />
				</div>
				<div class="alert__tool__recipient">
					<label for="code">The sale code</label>
					<input type="text" name="item" class="" id="item" value="" />
				</div>	
				<div class="alert__tool__recipient">
					<label for="code">The credits</label>
					<input type="text" name="credits" class="" id="credits" value="" />
				</div>						
				<div class="alert__tool__commonmessage">
					<select name="isSingleUse" id="isSingleUse">
						<option value="" disabled selected>Is single use?</option>
						<option value="true">Yes</option>
						<option value="false">No</option>
					</select>
				</div>								
				<div class="alert__tool__commonmessage">
					<select name="allowNewUsers" id="allowNewUsers">
						<option value="" disabled selected>Allow new users?</option>
						<option value="true">Yes</option>
						<option value="false">No</option>
					</select>				
				</div>	
				<div class="alert__tool__recipient">
					<label for="code">The expiry date</label>
					<input type="datetime-local" name="expiryDate" id="expiryDate" value="" />
				</div>				
				<div class="alert__tool__submit">
					<button type="submit" name="action" value="createVoucher">Add</button>
				</div>
			 </form>
		</div>
		<hr />
		  <p><b>Find code</b></p>
		  <p>This tool allows you to search a code with letter starting or by item.</p>
		<form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
			<div class="">
				<label>Search</label>
				{% autoescape 'html' %}
				<input type="text" name="searchStr" id="searchStr" />
				{% endautoescape %}
			</div>
			<button type="submit" name="action" value="searchVoucher">Submit</button>
		</form>		  
		  {% if searchVouchersDetails|length > 0 %}
		  <br />
		  <p>Codes starting with '{{ query }}' or with by item '{{ query }}'</p>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Voucher code</th>
                  <th>Sale code</th>
                  <th>Credits amount</th>
                  <th>Single use</th>
                  <th>Allow new users</th>
                  <th>Expiry date</th>				  
				  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for voucher in searchVouchersDetails %}
                <tr>
				  <td>{{ voucher.voucherCode() }}</td>                               
				  <td>{{ voucher.saleCode() }}</td>                               
				  <td>{{ voucher.credits() }}</td>                                              			 
				  <td>{% if voucher.isSingleUse() %}Yes{% else %}No{% endif %}</td>                 			 
				  <td>{% if voucher.allowNewUsers() %}Yes{% else %}No{% endif %}</td> 
				  <td>{% if voucher.expiryDate() != null %}{{ voucher.expiryDate() }}{% else %}No limit{% endif %}</td>   				  
				  <td>
					<form method="post">
						<input type="hidden" name="voucherCode" id="voucherCode" value="{{ voucher.voucherCode() }}" />
						<button type="submit" name="action" value="deleteVoucher">Delete</button>
					</form>
				  </td>
                </tr>
			   {% set num = num + 1 %}
			   
			   {% endfor %}
              </tbody>
            </table>
		  </div>
				{% endif %}
			{% if searchEmpty %}
			<br>
			<p><i>No results found to display.</i></p>
			{% endif %}
		  <hr />
          <p><b>List of current codes</p></b>
		  <p>This tool allow you to see the complete list of all current codes in the Vouchers.</p>
		  <div class="pagination-buttons-box">
		  {% if nextVouchers|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/vouchers?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousVouchers|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/vouchers?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
		</div>		  
		  {% if Vouchers|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Voucher code</th>
                  <th>Sale code</th>
                  <th>Credits amount</th>
                  <th>Single use</th>
                  <th>Allow new users</th>
                  <th>Expiry date</th>				  
				  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for voucher in Vouchers %}
                <tr>
				  <td>{{ voucher.voucherCode() }}</td>                               
				  <td>{{ voucher.saleCode() }}</td>                               
				  <td>{{ voucher.credits() }}</td>                                              			 
				  <td>{% if voucher.isSingleUse() %}Yes{% else %}No{% endif %}</td>                 			 
				  <td>{% if voucher.allowNewUsers() %}Yes{% else %}No{% endif %}</td> 
				  <td>{% if voucher.expiryDate() != null %}{{ voucher.expiryDate() }}{% else %}No limit{% endif %}</td>   				  
				  <td>
					<form method="post">
						<input type="hidden" name="voucherCode" id="voucherCode" value="{{ voucher.voucherCode() }}" />
						<button type="submit" name="action" value="deleteVoucher">Delete</button>
					</form>
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
    </div>
  </div>
</body>
</html>