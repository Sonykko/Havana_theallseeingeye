{% if session.loggedIn %}
{% if hasDeletePermission == false %}
	{% set hasDeletePermission = entry.getUser().getId() == playerDetails.id %}
{% endif %}
{% endif %}
	<li id="guestbook-entry-{{ entry.getId() }}" class="guestbook-entry">
	
		<div class="guestbook-author">
			<img src="{{ site.habboImagingPath }}/habbo-imaging/avatarimage?figure={{ entry.getUser().getFigure() }}&size=s&direction=4&head_direction=4&crr=0&gesture=&frame=1" alt="{{ entry.getUser().getName() }}" title="{{ entry.getUser().getName() }}"/>
		</div>
			<div class="guestbook-actions">
			{% if hasDeletePermission %}
					<img src="{{ site.staticContentPath }}/web-gallery/images/myhabbo/buttons/delete_entry_button.gif" id="gbentry-delete-{{ entry.getId() }}" class="gbentry-delete" style="cursor:pointer" alt=""/>
					<br/>
			{% endif %}
			{% if entry.getUser().getId() != playerDetails.id %}
					<img src="{{ site.staticContentPath }}/web-gallery/images/myhabbo/buttons/report_button.gif" id="gbentry-report-{{ entry.getId() }}" class="gbentry-report" style="cursor:pointer" alt=""/>
			{% endif %}
			</div>		
		<div class="guestbook-message">
			{% if entry.getUser().isOnline() and entry.getUser().isProfileVisible() %}
			<div class="online">
			{% else %}
			<div class="offline">
			{% endif %}
				<a href="{{ site.sitePath }}/home/{{ entry.getUser().getName() }}">{{ entry.getUser().getName() }}</a>
			</div>
			<p>{{ entry.getMessage() }}</p>
		</div>
		<div class="guestbook-cleaner">&nbsp;</div>
		<div class="guestbook-entry-footer metadata">{{ entry.getCreationDate() }}</div>
	</li>