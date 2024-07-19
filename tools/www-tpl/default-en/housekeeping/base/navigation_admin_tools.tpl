<div class="d-flex1 " id="wrapper1">
<div class="subnav-all">
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
    <div class="subnav-box">
 <div class="subnav-header">Hobba discussion board</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/groups/Habbo-Hobbas/discussions" class="subnav-link">Forums</a></text>
			{% endif %}	
      </div>
    </div>
{% endif %}
	
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
	<div class="subnav-box">
 <div class="subnav-header">Hobba tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/alert" class="subnav-link">Remote alerting</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/bans_kicks" class="subnav-link">Remote banning and kicking</a></text>
			{% endif %}			
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action" class="subnav-link">User action log</a></text>
			{% endif %}		
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_mods" class="subnav-link">Calls for help</a></text>
			{% endif %}				
      </div>
    </div>
{% endif %}
	
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
	<div class="subnav-box">
 <div class="subnav-header">Supervisor Hobba tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/create') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_ban" class="subnav-link">Mass ban</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/create') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_alert" class="subnav-link">Mass alert</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/wordfilter" class="subnav-link">Wordfilter tool</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
	
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'transaction/lookup') %}
	<div class="subnav-box">
 <div class="subnav-header">Scam detection</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/search') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/users/search" class="subnav-link">{{ site.siteName }} search & information tool</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'transaction/lookup') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/transaction/lookup" class="subnav-link">Current furniture</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
	
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
	<div class="subnav-box">
 <div class="subnav-header">Private rooms</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/rooms/search" class="subnav-link">Room admin</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/room_chatlogs" class="subnav-link">Room action log</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
	
{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/edit') %}
	<div class="subnav-box">
 <div class="subnav-header">Staff moderator tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/edit') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/mass_unban" class="subnav-link">Mass unban</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/edit') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/cfh_logs" class="subnav-link">CFH action log</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/create') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/vouchers" class="subnav-link">Voucher codes tool</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}

{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/edit') %}	
	<div class="subnav-box">
 <div class="subnav-header">Staff user moderation</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics" class="subnav-link">Dashboard</a></text>
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/edit') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/users" class="subnav-link">User edit</a></text>
			{% endif %}
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'user/create') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/users/create" class="subnav-link">User create</a></text>
			{% endif %}
			{% if (housekeepingManager.hasPermission(playerDetails.getRank(), 'user/ranks')) and (playerDetails.getId() == 1) %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/give_rank" class="subnav-link">Rank manager</a></text>
			{% endif %}			
      </div>
    </div>
{% endif %}

{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}	
	<div class="subnav-box">
 <div class="subnav-header">Hobba management tools</div>
      <div class="list-group list-group-flush" style="padding-left: 2px;">        
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
			<text><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/hobbas" class="subnav-link">Check Hobba applicant</a></text>
			{% endif %}
      </div>
    </div>
{% endif %}
	{% include "housekeeping/base/navigation_time.tpl" %}
</div>

    <div id="page-content-wrapper" class="page-content">

      

      <div class="container-fluid">
