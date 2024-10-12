<div id="staffpicks-rooms-habblet-list-container" class="habblet-list-container groups-list">
    <ul class="habblet-list">
	{% set num = 0 %}
	{% for room in StaffPickRooms %}
	
		{% set occupancyLevel = 1 %}
		{% if room.getData().getVisitorsNow() > 0 %}
		
		{% set percentage = ((room.getData().getVisitorsNow() * 100) / room.getData().getVisitorsMax()) %}
		
		{% if (percentage >= 99) %}
			{% set occupancyLevel = 5 %}
		{% elseif (percentage > 65) %}
			{% set occupancyLevel = 4 %}
		{% elseif (percentage > 32) %}
			{% set occupancyLevel = 3 %}
		{% elseif (percentage > 0) %}
			{% set occupancyLevel = 2 %}
		{% endif %}
		
		{% endif %}
	
		{% if num % 2 == 0 %}
		<li class="even room-occupancy-{{ occupancyLevel }}" roomid="{{ room.getData().getId() }}">
		{% else %}
		<li class="odd room-occupancy-{{ occupancyLevel }}" roomid="{{ room.getData().getId() }}">
		{% endif %}	
            <div>
                <span class="room-name"><a href="{{ site.sitePath }}/client?forwardId=2&amp;roomId={{ room.getData().getId() }}" onclick="HabboClient.roomForward(this, '1', 'private'); return false;" target="client">{{ room.getData().getName() }}</a></span>
				<span class="room-owner"><a href="{{ site.sitePath }}/home/{{ room.getData().getOwnerName() }}">{{ room.getData().getOwnerName() }}</a></span>   				
				<p>{% if room.isPublicRoom() %}{{ room.getData().getDescriptionPublicRoom() }}{% else %}{{ room.getData().getDescription() }}{% endif %}</p>				
            </div>
        </li>
		{% set num = num + 1 %}
		{% endfor %}
    </ul>
</div>
<input type="hidden" id="hiddenlayers" value="59,65,73,2c,20,49,27,6d,20,74,68,65,20,63,72,65,61,74,6f,72,20,6f,66,20,48,61,76,61,6e,61,5f,74,68,65,61,6c,6c,73,65,65,69,6e,67,65,79,65,20,61,6e,64,20,49,20,70,75,62,6c,69,73,68,65,64,20,69,74,20,75,6e,64,65,72,20,53,6f,6e,79,6b,6b,6f,20,6e,61,6d,65,2c,20,63,68,65,65,72,73,21" />