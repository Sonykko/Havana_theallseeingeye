{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set campaignManagementActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}	
     <h2 class="mt-4">Create Room Badge</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Create a room entry badge that will be given to the user as soon as they enter the room.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Room ID</label>
				<input type="text" class="form-control" name="roomid">
			</div>
			<div class="form-group">
				<label>Badge Code</label>
				<input type="text" class="form-control" name="badgecode">
			</div>
			<div class="form-group"> 
				<button type="submit">Create Entry Badge</button>
			</div>
		</form>
      </div>
    </div>
  </div>
</body>
</html>