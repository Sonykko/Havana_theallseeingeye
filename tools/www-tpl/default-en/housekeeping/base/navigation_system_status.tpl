<div class="d-flex1 " id="wrapper1">
<div class="subnav-all">
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
    <div class="subnav-box">
 <div class="subnav-header">Configurations tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations" class="subnav-link">General hotel settings</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
    <div class="subnav-box">
 <div class="subnav-header">Configurations tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=miscellaneous" class="subnav-link">Miscellaneous settings</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
    <div class="subnav-box">
 <div class="subnav-header">Site configurations tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=site" class="subnav-link">General site settings</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=hotel" class="subnav-link">Hotel settings</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=housekeeping" class="subnav-link">Housekeeping settings</a></text>
			{% endif %}			
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=maintenance" class="subnav-link">Maintenance settings</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
    <div class="subnav-box">
 <div class="subnav-header">Client configurations tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=client" class="subnav-link">General client settings</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=loader" class="subnav-link">Loader settings</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=games" class="subnav-link">Games settings</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=catalogue" class="subnav-link">Catalogue settings</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=coins" class="subnav-link">Coins settings</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
    <div class="subnav-box">
 <div class="subnav-header">Server configurations tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=server" class="subnav-link">General server settings</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=host" class="subnav-link">Host settings</a></text>
			{% endif %}			
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status/configurations?settings=email" class="subnav-link">Email SMTP settings</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
	{% include "housekeeping/base/navigation_time.tpl" %}
</div>

    <div id="page-content-wrapper" class="page-content">

      

      <div class="container-fluid">
