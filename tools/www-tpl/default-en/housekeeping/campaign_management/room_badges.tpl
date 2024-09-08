{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}	
     <h2 class="mt-4">Edit Room Badges</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Edit all the room badges that are given when entering the room.</p>	
          <div class="table-responsive">
		    <form method="post">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>Room ID</th>
				  <th>Badge Code</th>
				  <th>Preview</th>
				  <th>Room Name</th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
			  {% for badgeData in roomBadges.entrySet() %}
				{% for badge in badgeData.getValue() %}
				{% set id = (badgeData.getKey()) + ('_') +  (badge) %}
				
				<input type="hidden" name="roombadge-id-{{ id }}" value="{{ id }}">
                <tr>
				  <td width="100px">
						<input type="text" name="roomad-{{ id }}-roomid" class="form-control" id="searchFor" value="{{badgeData.getKey()  }}">
				  </td>
				  <td>
						<input type="text" name="roomad-{{ id }}-badge" class="form-control" id="searchFor" value="{{ badge }}">
				  </td>
				  <td>
						<img src="{{ site.staticContentPath }}/c_images/album1584/{{ badge }}.gif">
				  </td>				  
				  <td>
						<p>{{ util.getRoomName(badgeData.getKey()) }}</p>
				  </td>
				  <td>
						<a href="/{{ site.housekeepingPath }}/campaign_management/room_badges/delete?id={{ id }}"><button type="button">Delete</button></a>
				  </td>
                  </tr>
				  {% endfor %}
			   {% endfor %}
              </tbody>
            </table>
			<div class="form-group"> 
				<button type="submit">Save Badges</button>
			</div>
		</form>
      </div>
    </div>
  </div>
  <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"></script>
  <script src="https://blackrockdigital.github.io/startbootstrap-simple-sidebar/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  </script>
</body>
</html>

