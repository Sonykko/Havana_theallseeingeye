<div class="d-flex1 " id="wrapper1">
<div class="subnav-all">
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'articles/create') %}
	<div class="subnav-box">
 <div class="subnav-header">News articles tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">    
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'articles/create') %}	  
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/articles/create" class="subnav-link">Create news articles</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'articles/edit_own') %}  
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/articles" class="subnav-link">Edit & delete news articles</a></text>
			{% endif %}			
      </div>
    </div>
{% endif %}
	
{% if (housekeepingManager.hasPermission(playerDetails.getRank(), 'room_ads')) or (housekeepingManager.hasPermission(playerDetails.getRank(), 'room_badges')) %}
	<div class="subnav-box">
 <div class="subnav-header">Rooms tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'room_ads') %}
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/room_ads/create?id=" class="subnav-link">Create room advertisement</a></text>
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/room_ads" class="subnav-link">Edit & delete rooms advertisements</a></text>
			{% endif %}	
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'room_badges') %}
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/room_badges/create?id=" class="subnav-link">Create room badge</a></text>
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/room_badges" class="subnav-link">Edit & delete rooms badges</a></text>
			{% endif %}	
      </div>
    </div>
{% endif %}	
	
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'infobus') %}
	<div class="subnav-box">
<div class="subnav-header">Infobus</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/infobus_polls/create" class="subnav-link">Create infobus poll</a></text>
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/infobus_polls" class="subnav-link">Manage infobus polls & doors</a></text>		
      </div>
    </div>
{% endif %}
	
	<div class="subnav-box">
<div class="subnav-header">Catalogue tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			<text>- <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/edit_frontpage" class="subnav-link">Edit catalogue front data</a></text>					
      </div>
    </div>
	{% include "housekeeping/base/navigation_time.tpl" %}
</div>

    <div id="page-content-wrapper" class="page-content">

      

      <div class="container-fluid">
