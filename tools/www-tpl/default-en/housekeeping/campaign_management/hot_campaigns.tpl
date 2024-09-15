{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	<script type="text/javascript">
	function previewTS(el) {
		document.getElementById('ts-preview').innerHTML = '<img src="{{ site.staticContentPath }}/c_images/hot_campaign_images_all/' + el + '" class="hot__campaign__image" /><br />';
	}
	</script>
	     <h2 class="mt-4">Hot campaigns tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if isHotCampaignEdit %}
		<p>Here you can edit a Hot Campaign.</p>
		{% for hotCampaignEdit in HotCampaignEdit %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Title</label>
				<input type="text" name="title" class="form-control" id="title" placeholder="Enter here a title for the Hot Campaign..." value="{{ hotCampaignEdit.title }}" />
			</div>
			<div class="form-group">
				<label>Description</label>
				<textarea name="description" class="form-control" id="description" placeholder="Enter here the description for the Hot Campaign...">{{ hotCampaignEdit.description }}</textarea>
			</div>			
			<div class="form-group">
				<label>Image</label>
				<p>
					<select onkeypress="previewTS(this.value);" onchange="previewTS(this.value);" name="saveHotCampaign" id="saveHotCampaign">
					{% for image in images %}<option value="{{ image }}"{% if image == hotCampaignEdit.image %} selected{% endif %}>{{ image }}</option>{% endfor %}
					</select>
				</p>
			</div>
			<div class="form-group">
				<label>Image Preview</label>
				<div id="ts-preview"><img src="{{ site.staticContentPath }}/c_images/hot_campaign_images_all/{{ hotCampaignEdit.image }}" class="hot__campaign__image" /></div>
			</div>
			<div class="form-group">
				<label>URL</label>
				<input type="text" name="url" class="form-control" id="url" placeholder="Enter here a URL for the Hot Campaign..." value="{{ hotCampaignEdit.url }}" />
			</div>
			<div class="form-group">
				<label>URL text</label>
				<input type="text" name="urlText" class="form-control" id="urlText" placeholder="Enter here a URL text for the Hot Campaign..." value="{{ hotCampaignEdit.urlText }}" />
			</div>
			<div class="form-group">
				<label>Status</label>
				<select name="status" id="status" class="form-control">
					<option value="1" {% if hotCampaignEdit.status == 1 %}selected{% endif %}>Active</option>
					<option value="0" {% if hotCampaignEdit.status == 0 %}selected{% endif %}>Hidden</option>
				</select>
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="orderId" class="form-control" id="orderId" placeholder="Enter here a order number for display the Hot Campaign..." value="{{ hotCampaignEdit.orderId }}" />
			</div>
			<button type="submit">Save Hot Campaign</button>
		</form>
		{% endfor %}
		{% else %}
		<p>Here you can add a Hot Campaign.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Title</label>
				<input type="text" name="title" class="form-control" id="title" placeholder="Enter here a title for the Hot Campaign..." value="" />
			</div>
			<div class="form-group">
				<label>Description</label>
				<textarea name="description" class="form-control" id="description" placeholder="Enter here the description for the Hot Campaign..."></textarea>
			</div>
			<div class="form-group">
				<label>Image</label>
				<p>
					<select onkeypress="previewTS(this.value);" onchange="previewTS(this.value);" name="createHotCampaign" id="createHotCampaign">
					{% for image in images %}<option value="{{ image }}">{{ image }}</option>{% endfor %}
					</select>
				</p>
			</div>
			<div class="form-group">
				<label>Image Preview</label>
				<div id="ts-preview"><img src="{{ site.staticContentPath }}/c_images/hot_campaign_images_all/33_campaignButton.gif" class="hot__campaign__image" /></div>
			</div>
			<div class="form-group">
				<label>URL</label>
				<input type="text" name="url" class="form-control" id="url" placeholder="Enter here a URL for the Hot Campaign..." value="" />
			</div>
			<div class="form-group">
				<label>URL text</label>
				<input type="text" name="urlText" class="form-control" id="urlText" placeholder="Enter here a URL text for the Hot Campaign..." value="" />
			</div>
			<div class="form-group">
				<label>Status</label>
				<select name="status" id="status" class="form-control">
					<option value="1" selected>Active</option>
					<option value="0">Hidden</option>
				</select>
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="orderId" class="form-control" id="orderId" placeholder="Enter here a order number for display the Hot Campaign..." value="" />
			</div>
			<button type="submit">Add Hot Campaign</button>
		</form>
		<br /><br />
          <h2 class="mt-4">Edit hot campaigns</h2>
		  <p>The Hot Campaigns list is seen below.</p>
		  {% if HotCampaigns|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Title</th>
                  <th>Description</th>
                  <th>Image</th>
                  <th>URL</th>
				  <th>URL Text</th>
				  <th>Status</th>
				  <th>Order ID</th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for hotCampaign in HotCampaigns %}
                <tr>
				  <td>{{ hotCampaign.id }}</td>                               
				  <td>{{ hotCampaign.title }}</td>                 
				  <td>{{ hotCampaign.description }}</td>                 
				  <td><img src="{{ site.staticContentPath }}/c_images/hot_campaign_images_all/{{ hotCampaign.image }}" class="hot__campaign__image" /></td>                 			 
				  <td>{{ hotCampaign.url }}</td>                 			 
				  <td>{{ hotCampaign.urlText }}</td>                 			 
				  <td>{% if hotCampaign.status == 1 %}Active{% else %}Hidden{% endif %}</td>                 			             			 
				  <td>{{ hotCampaign.orderId }}</td>                 			 
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/hot_campaigns?edit={{ hotCampaign.id }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/hot_campaigns?delete={{ hotCampaign.id }}" style="color:black;"><button type="button">Delete</button></a>
				</td>
                </tr>
			   {% set num = num + 1 %}
			   
			   {% endfor %}
              </tbody>
            </table>
      </div>
	  {% else %}
	  <p>No Recommended groups found to display.</p>
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