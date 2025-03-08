{% include "housekeeping/base/header.tpl" %}
<body>
{% include "housekeeping/base/navigation.tpl" %}
{% include "housekeeping/base/navigation_time.tpl" %}
	<div style="display: flex;flex-direction: column;align-items: center;margin-top: -220px;">
		<h2 class="mt-4">Forbidden</h2>
		<text>You don't have permissions to access to this location on this server.</text>
		<text>This attempt is monitored in the Housekeeping log.</text>
		<text>Has been saved with username <b>{{ playerDetails.getName }}</b> from <b>{{ userIp }}</b></text>
		<br/>
		<div style="display:contents;">
			<img src="{{ site.staticContentPath }}/public/hk/visual/allseeingeye.png" draggable="false" id="permissions__eye" style="" />
		</div>
	</div>
</body>
</html>