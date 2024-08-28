<nav class="header-box">
        <!--<button class="btn btn-primary" id="menu-toggle">Toggle Menu</button>-->
	<text style="padding-inline: inherit;">Logged in as: <b>{{ playerDetails.getName() }}</b> (Prevous login at {{ playerDetails.getLastLoginTimeHK() }} from {{ playerDetails.getLastLoginIPHK() }}) <a class="" href="{{ site.sitePath }}/ase/housekeeping/es/logout">Log out</a></text>	
				<a class="header-links" href="{{ site.sitePath }}">Secure Login To {{ site.siteName }}</a>

            <!-- 
			<li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Dropdown
              </a>
              <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                <a class="dropdown-item" href="#">Action</a>
                <a class="dropdown-item" href="#">Another action</a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#">Something else here</a>
              </div>
            </li>
			-->
          
        
      </nav>
	  <div>
	 <div class="nav-fix">
			{% if (housekeepingManager.hasPermission(playerDetails.getRank(), 'configuration')) or (playerDetails.isTrustedPerson()) and (gameConfig.getBoolean('hk.trusted.person.enabled')) %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/system_status" class="nav-links {{ configurationsActive }}">System status</a>
				{% endif %}
			
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'root/login') %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/statistics" class="nav-links {{ dashboardActive }}">Statistics</a>
				{% endif %}
	  
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'bans') %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools" class="nav-links {{ bansActive }}">Admin tools</a>
				{% endif %}
			
			{% if housekeepingManager.hasPermission(playerDetails.getRank(), 'articles/create') %}
		<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management" class="nav-links {{ articlesCreateActive }}">Campaign management</a>
				{% endif %}
		</div>

		
	  <div class="nav-bar"></div>
	  
</div>

