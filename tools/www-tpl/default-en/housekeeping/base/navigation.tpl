<nav class="header-box">
	<text style="padding-inline: inherit;">Logged in as: <b>{{ playerDetails.getName() }}</b> (Previous login at {{ playerDetails.getLastLoginTimeHK() }} from {{ playerDetails.getLastLoginIPHK() }}) <a class="" href="{{ site.sitePath }}/ase/housekeeping/es/logout">Log out</a></text>
				<a class="header-links" href="{{ site.sitePath }}">Secure Login To {{ site.siteName }}</a>
      </nav>
	  <div>
	 <div class="nav-fix">
			{% if (housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration')) or (playerDetails.isTrustedPerson()) and (gameConfig.getBoolean('hk.trusted.person.enabled')) %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status" class="nav-links {{ systemStatusActive }}">System status</a>
				{% endif %}

			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'root/login') %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics" class="nav-links {{ statisticsActive }}">Statistics</a>
				{% endif %}

			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools" class="nav-links {{ adminToolsActive }}">Admin tools</a>
				{% endif %}

			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'articles/create') %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management" class="nav-links {{ campaignManagementActive }}">Campaign management</a>
				{% endif %}
		</div>

		
	  <div class="nav-bar"></div>
	  
</div>

