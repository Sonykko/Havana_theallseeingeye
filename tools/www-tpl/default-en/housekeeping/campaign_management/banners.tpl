{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	     <h2 class="mt-4">Ads banners tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if isBannerEdit %}
		<p>Here you can edit a Banner.</p>
		{% for bannerEdit in BannerEdit %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Text (If are not Advanced)</label>
				<input type="text" name="textBanner" class="form-control" id="textBanner" placeholder="Enter here a text for the banner..." value="{{ bannerEdit.text }}" />
			</div>
			<div class="form-group">
				<label>Banner</label>
				<textarea name="saveBanner" class="form-control" id="saveBanner" placeholder="Enter here the URL of the banner image, if it are a advanced one put here the HTML of it...">{{ bannerEdit.banner }}</textarea>
			</div>
			<div class="form-group">
				<label>URL (If are not Advanced)</label>
				<input type="text" name="urlBanner" class="form-control" id="urlBanner" placeholder="Enter here a URL for the banner..." value="{{ bannerEdit.url }}" />
			</div>
			<div class="form-group">
				<label>Status</label>
				<select name="statusBanner" id="statusBanner" class="form-control">
					<option value="1" {% if bannerEdit.status == 1 %}selected{% endif %}>Active</option>
					<option value="0" {% if bannerEdit.status != 1 %}selected{% endif %}>Hidden</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is Advanced?</label>
				<select name="advancedBanner" id="advancedBanner" class="form-control">
					<option value="1" {% if bannerEdit.advanced == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if bannerEdit.advanced != 1 %}selected{% endif %}>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="orderIdBanner" class="form-control" id="orderIdBanner" placeholder="Enter here a order number for display the banner..." value="{{ bannerEdit.orderId }}" />
			</div>
			<button type="submit">Save Banner</button>
		</form>
		{% endfor %}
		{% else %}
		<p>Here you can add a Banner.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Text (If are not Advanced)</label>
				<input type="text" name="textBanner" class="form-control" id="textBanner" placeholder="Enter here a text for the banner..." value="" />
			</div>
			<div class="form-group">
				<label>Banner</label>
				<textarea name="createBanner" class="form-control" id="createBanner" placeholder="Enter here the URL of the banner image, if it are a advanced one put here the HTML of it..."></textarea>
			</div>
			<div class="form-group">
				<label>URL (If are not Advanced)</label>
				<input type="text" name="urlBanner" class="form-control" id="urlBanner" placeholder="Enter here a URL for the banner..." value="" />
			</div>
			<div class="form-group">
				<label>Status</label>
				<select name="statusBanner" id="statusBanner" class="form-control">
					<option value="1" selected>Active</option>
					<option value="0">Hidden</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is Advanced?</label>
				<select name="advancedBanner" id="advancedBanner" class="form-control">
					<option value="1" selected>Yes</option>
					<option value="0">No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="orderIdBanner" class="form-control" id="orderIdBanner" placeholder="Enter here a order number for display the banner..." value="" />
			</div>
			<button type="submit">Add Banner</button>
		</form>
          <h2 class="mt-4">Edit ads banners</h2>
		  <p>The Ads banners list is seen below.</p>
		  {% if Banners|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Text</th>
                  <th>Banner</th>
                  <th>URL</th>
                  <th>Status</th>
				  <th>Is Advanced</th>
				  <th>Order ID</th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for banner in Banners %}
                <tr>
				  <td>{{ banner.id }}</td>                               
				  <td>{{ banner.text }}</td>                 
				  <td><textarea style="width:100%;">{{ banner.banner }}</textarea></td>                 
				  <td>{{ banner.url }}</td>                 			 
				  <td>{% if banner.status == 1 %}Active{% else %}Hidden{% endif %}</td>                 			 
				  <td>{% if banner.advanced == 1 %}Yes{% else %}No{% endif %}</td>                 			 
				  <td>{{ banner.orderId }}</td>                 			 
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/banners?edit={{ banner.id }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/banners?delete={{ banner.id }}" style="color:black;"><button type="button">Delete</button></a>
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
	  {% endif %}
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