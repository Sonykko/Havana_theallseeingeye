{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}	
     <h2 class="mt-4">Edit Room Ads</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Edit all the room ads that display as billboards from within the hotel.</p>	
		  {% if roomAds|length > 0 %}
          <div class="table-responsive">
		    <form method="post">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>Is Loading Ad</th>
				  <th>Room ID</th>
				  <th>URL</th>
				  <th>Image</th>
				  <th>Enabled</th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
				{% for advertisement in roomAds %}
				<input type="hidden" name="roomad-id-{{ advertisement.getId() }}" value="{{ advertisement.getId() }}">
                  <tr>
				  <td width="100px">
						<input type="checkbox" name="roomad-{{ advertisement.getId() }}-loading-ad" {% if advertisement.isLoadingAd() %}checked{% endif %}/>
				  </td>
				  <td width="100px">
						<input type="text" name="roomad-{{ advertisement.getId() }}-roomid" class="form-control" id="searchFor" value="{{ advertisement.getRoomId() }}">
				  </td>
				  <td>
						<input type="text" name="roomad-{{ advertisement.getId() }}-url" class="form-control" id="searchFor" value="{{ advertisement.getUrl() }}">
				  </td>
				  <td>
						<input type="text" name="roomad-{{ advertisement.getId() }}-image" class="form-control" id="searchFor" value="{{ advertisement.getImage() }}">
				  </td>
				  <td width="100px">
						<input type="checkbox" name="roomad-{{ advertisement.getId() }}-enabled" {% if advertisement.isEnabled() %}checked{% endif %}/>
				  </td>
				  <td width="100px">
						<a href="/{{ site.housekeepingPath }}/campaign_management/room_ads/delete?id={{ advertisement.getId() }}"><button type="button">Delete</button></a>
				  </td>
                  </tr>
			   {% endfor %}
              </tbody>
            </table>
			<div class="form-group"> 
				<button type="submit">Save Ads</button>
			</div>
			</form>
		  </div>
		  {% else %}
		  <p><i>Nothing found to display.</i></p>
		  {% endif %} 
    </div>
  </div>
</body>
</html>