{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Voucher codes</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Here you can add a Voucher code.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Voucher code</label>
				<input type="text" name="voucherCode" class="form-control" id="voucherCode" placeholder="Enter here the Voucher code..." value="{{ voucherRandom }}" />
			</div>
			<div class="form-group">
				<label>Credits amount</label>
				<input type="text" name="credits" class="form-control" id="credits" placeholder="Enter here the amount of credits..." />
			</div>
			<div class="form-group">
				<label>Expiry date (leave in blank for no limit)</label>
				<input type="datetime-local" name="expiryDate" class="form-control" id="expiryDate" placeholder="Enter here a expiry date, leave in blank for no limit..." value="" />
			</div>
			<div class="form-group">
				<label>Is single use?</label>
				<select name="isSingleUse" id="isSingleUse" class="form-control">
					<option value="1" selected>Yes</option>
					<option value="0">No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Allow new users?</label>
				<select name="allowNewUsers" id="allowNewUsers" class="form-control">
					<option value="1">Yes</option>
					<option value="0" selected>No</option>
				</select>
			</div>
			<button type="submit">Add Voucher</button>
		</form>
          <h2 class="mt-4">Voucher codes list</h2>
		  <p>The Voucher codes list is seen below.</p>
          <div class="table-responsive" style="padding-left: 15px;">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Voucher code</th>
                  <th>Credits amount</th>
                  <th>Expiry date</th>
                  <th>Single use</th>
                  <th>Allow new users</th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for voucher in Vouchers %}
                <tr>
				  <td>{{ voucher.voucherCode }}</td>                               
				  <td>{{ voucher.credits }}</td>                 
				  <td>{% if voucher.expiryDate != null %}{{ voucher.expiryDate }}{% else %}No limit{% endif %}</td>                                			 
				  <td>{% if voucher.isSingleUse == 1 %}Yes{% else %}No{% endif %}</td>                 			 
				  <td>{% if voucher.allowNewUsers == 1 %}Yes{% else %}No{% endif %}</td>                 			 
                </tr>
			   {% set num = num + 1 %}
			   
			   {% endfor %}
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