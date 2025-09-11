{% for banner in site.adsBanners %}
{% if (banner.getAdvanced() == 1) and (banner.getStatus() == 1) %}
	{{ banner.getBanner() }}<br />
{% elseif (banner.getAdvanced() == 0) and (banner.getStatus() == 1)  %}
	<a target="_blank" href="{{ banner.getUrl() }}"><img src="{{ banner.getBanner() }}"></a><br />
	<a target="_blank" href="{{ banner.getUrl() }}">{{ banner.getText() }}</a><br />
{% endif %}
{% endfor %}