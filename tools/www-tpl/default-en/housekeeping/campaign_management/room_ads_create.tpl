{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set campaignManagementActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
     <h2 class="mt-4">Create Ad</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Create a room ad that will display as a billboards from within the hotel.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Room ID</label>
				<input type="text" class="form-control" name="roomid">
			</div>
			<div class="form-group">
				<label>URL</label>
				<input type="text" class="form-control" name="url">
			</div>
			<div class="form-group">
				<label>Image</label>
				<input type="text" class="form-control" name="image">
			</div>
			<div class="form-group">
				<label>Enabled</label>
				<input type="checkbox" name="enabled" checked />
			</div>
			<div class="form-group">
				<label>Room loading/intermission ad</label>
				<input type="checkbox" name="loading-ad"/>
			</div>
			<div class="form-group"> 
				<button type="submit">Create Ad</button>
			</div>
		</form>
      </div>
    </div>
  </div>
</body>
</html>