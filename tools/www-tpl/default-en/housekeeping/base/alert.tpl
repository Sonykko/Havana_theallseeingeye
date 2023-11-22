{% if alert.hasAlert %}
<div class="alert alert-{{ alert.colour }}">
				<text>{{ alert.message }}</text>
			</div>
{% endif %}