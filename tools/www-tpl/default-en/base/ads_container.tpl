{% for banner in site.adsBanners %}
{% if banner.getAdvanced() == 1 %}
	{{ banner.getBanner() }}<br />
{% else %}
	<a target="_blank" href="{{ banner.getUrl() }}"><img src="{{ banner.getImage() }}"></a><br />
	<a target="_blank" href="{{ banner.getUrl() }}">{{ banner.getText() }}</a><br />"
{% endif %}
{% endfor %}