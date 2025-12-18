{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
     <h2 class="mt-4">Mass unbanning</h2>		
		<p style="padding-bottom:50px;">This tool allows you to unban Habbos if you are not in the same room (you don't even need to be in the hotel).</p>
		<form class="massban__tool__form" method="post">
			<div class="massban__tool__badguys">
				<label for="pwd">The bad guys</label>
				<textarea class="" id="userNames" name="userNames"></textarea>
			</div>
			<div style="margin-bottom: 5px;">
				<button type="submit" name="action" value="massUnban">Unban</button>
			</div>
		</form>
		{{ moderator }}
		<br/>
		{% include "housekeeping/base/alert.tpl" %}
      </div>
    </div>
  </div>
</body>
</html>