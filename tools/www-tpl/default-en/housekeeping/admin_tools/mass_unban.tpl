{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Mass unbanning</h2>		
		<p>This tool allows you to unban Habbos if you are not in the same room (you don't even need to be in the hotel).</p>
		{% include "housekeeping/base/alert.tpl" %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label for="pwd">The bad guys</label>
				<textarea class="form-control" id="userNames" name="userNames" rows="5" cols="50" placeholder="Type every single name with a line break..."></textarea>
			</div>
			<div style="margin-bottom: 5px;">
				<button type="submit" name="action" value="massUnban">Unban</button>
			</div>
		</form>
      </div>
    </div>
  </div>
</body>
</html>