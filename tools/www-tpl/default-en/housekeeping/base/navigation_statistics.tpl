<div class="d-flex1 " id="wrapper1">
<div class="subnav-all">
	<div class="subnav-box">
 <div class="subnav-header">General statistics</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			<text><a href="{{ site.sitePath }}/ase/housekeeping/es" class="subnav-link">Hotel statistics</a></text>				
      </div>
    </div>
	
	<div class="subnav-box">
 <div class="subnav-header">Users statistics</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        			
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/newest_players" class="subnav-link">Newest players</a></text>
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/newest_players?zerocoins" class="subnav-link">Newest players with zero coins</a></text>
      </div>
    </div>
	{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
    <div class="subnav-box">
 <div class="subnav-header">Staff logs tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/permissions" class="subnav-link">Suspicius Staff logs</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.action" class="subnav-link">Housekeeping logs</a></text>
			{% endif %}	  
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics/hklogs.login" class="subnav-link">Housekeeping login logs</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}	
	{% include "housekeeping/base/navigation_time.tpl" %}
</div>

    <div id="page-content-wrapper" class="page-content">

      

      <div class="container-fluid">
