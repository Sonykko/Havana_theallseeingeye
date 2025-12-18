{% include "housekeeping/base/header.tpl" %}
<body>
    {% set adminToolsActive = "active" %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}		
          <h2 class="mt-4">User Chatlogs</h2>
		  <p>Here can see the recently chatlogs in rooms and messenger console or conversations with friends from given user.</p>
		  {% include "housekeeping/base/alert.tpl" %}
<form class="table-responsive col-md-4" method="post" onsubmit="return update();">
<label for="groupSelector">Search Type</label>
<select id="groupSelector" class="form-control">
    <option value="chatId">Room and messenger chatlogs</option>
    <option value="id1id2">Conversations with a friend</option>
</select>

<br>

<div id="chatIdGroup" class="form-group">
    <label for="chatId">User ID</label>
    <input type="text" name="chatId" class="form-control" id="chatId" placeholder="Enter here an user ID for see all chatlogs from given user...">
</div>

<div id="id1id2Group" class="form-group" style="display: none;">
	<div class="form-group">
    <label for="chatId1">User ID</label>
    <input type="text" name="id1" class="form-control" id="chatId1" placeholder="Enter here an user ID for search messenger conversations with a friend...">
	</div>	
	
	<div class="form-group">
    <label for="chatId2">Friend ID</label>
    <input type="text" name="id2" class="form-control" id="chatId2" placeholder="Enter here the friend ID for search messenger conversations with the user...">
</div>
</div>

<button type="submit">Perform Search</button>
</form>

<script>
    const groupSelector = document.getElementById("groupSelector");
    const chatIdGroup = document.getElementById("chatIdGroup");
    const id1id2Group = document.getElementById("id1id2Group");

    groupSelector.addEventListener("change", function() {
        const selectedGroup = groupSelector.value;

        chatIdGroup.style.display = "none";
        id1id2Group.style.display = "none";

        if (selectedGroup === "chatId") {
            chatIdGroup.style.display = "block";
        } else if (selectedGroup === "id1id2") {
            id1id2Group.style.display = "block";
        }
    });
</script>
{% if userChatlogs|length > 0 %}
	<hr/>
    <p style="font-size: 16px;"><b>Search Results</b></p>
    <table class="table table-striped">
        <thead>
            <tr>
				  <th>Created Date</th>
                  <th>User</th>
				  <th>Message</th>
				  <th>Room Name/Friend Name</th>
				  <th>Type</th>
                </tr>
        </thead>
        <tbody>
    {% for chatlog in userChatlogs %}
        <tr>
			<td>{{ (chatlog.getDate() * 1000)| date("HH:mm dd/MM/yyyy") }}</td>
            <td><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ chatlog.getUserId() }}" style="color:black;"><b>{{ chatlog.getUsername() }}</b> (id: {{ chatlog.getUserId() }})</a></td>
            <td>{{ chatlog.getBody() }}{{ chatlog.getMessage() }}</td>            
            <td>
                {% if chatlog.getLogType() == "Chatlog" %}
                    {{ chatlog.getRoomName() }} (id: {{ chatlog.getRoomId() }})
                {% elseif chatlog.getLogType() == "MessengerMessage" %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ chatlog.getFriendId() }}" style="color:black;text-decoration:none;">{{ chatlog.getFriendName() }}</a>
				{% elseif chatlog.getLogType() == "Conversation" %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ chatlog.getFriendId() }}" style="color:black;"><b>{{ chatlog.getFriendName() }}</b> (id: {{ chatlog.getFriendId() }})</a>
                {% endif %}
            </td>    
			<td>
                {% if chatlog.getLogType() == "Chatlog" %}                  
					<text style="color:limegreen;"><b>Room</b></text>
                {% elseif chatlog.getLogType() == "MessengerMessage" %}
                    <text style="color:orange;"><b>Messenger</b></text>
				{% elseif chatlog.getLogType() == "Conversation" %}
                    <text style="color:blue;"><b>Conversation</b></text>
                {% endif %}
            </td>			
        </tr>
    {% endfor %}
</tbody>
    </table>
{% endif %}
      </div>
    </div>
  </div>
  <script>
function update() {
    const chatId1Input = document.getElementById("chatId1");
    const chatId2Input = document.getElementById("chatId2");
    const chatId1 = chatId1Input.value.trim();
    const chatId2 = chatId2Input.value.trim();

    if (chatId1 && chatId2) {
        var url = "/{{ site.housekeepingPath }}/chatlog.action?&id1=" + encodeURIComponent(chatId1) + "&id2=" + encodeURIComponent(chatId2);
    } else {
        var chatId = document.getElementById("chatId").value.trim();
        var url = "/{{ site.housekeepingPath }}/chatlog.action?chatId=" + encodeURIComponent(chatId);
    }

    window.location.href = url;

    return false;
}
   </script>
</body>
</html>