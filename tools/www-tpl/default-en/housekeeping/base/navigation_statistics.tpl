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
	{% include "housekeeping/base/navigation_time.tpl" %}
</div>

    <div id="page-content-wrapper" class="page-content">

      

      <div class="container-fluid">
