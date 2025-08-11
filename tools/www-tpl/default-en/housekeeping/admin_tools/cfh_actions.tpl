{% include "housekeeping/base/header.tpl" %}
<body>
    {% set bansActive = " active " %}
    {% include "housekeeping/base/navigation.tpl" %}
    {% include "housekeeping/base/navigation_admin_tools.tpl" %}
          <h2 class="mt-4">Calls for help actions</h2>
		  <p>Here can admin the Call for help what you picked up.</p>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Information</th>
				  <th>Reply to user</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for cfhlog in cfhlogsAction %}
				{% if cfhlog.action != "PICK UP" %}
				<div class="alert alert-danger">
					<text>The CFH is already picked up or it's not picked up by any Moderator yet</text>
				</div>
				{% else %}
                <tr>
                  <td style="display: flex;flex-direction: column;">					  
					  <text>- <b>Caller:</b> <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ cfhlog.userId }}"><u>{{ cfhlog.username }}</u> (id: {{ cfhlog.userId }})</a></text>
					  <text>- <b>Time:</b> {{ cfhlog.createdTime }}</text>
					  <text>- <b>Room name:</b> {{ cfhlog.roomName }} (id: {{ cfhlog.roomId }})</text>
					  <text>- <b>Reason for action:</b> {{ cfhlog.reason }}</text>					  
					  <text>- <b>Picked by:</b> {% if cfhlog.status != "1" %}-{% else %}{{ cfhlog.moderator }}{% endif %}</text>
					  <text>- <b>Time pick up:</b> {% if cfhlog.status != "1" %}-{% else %}{{ cfhlog.pickedTime }}{% endif %}</text>	
					  <text>- <b>Status:</b> {% if cfhlog.status == "1" %}Picked Up{% else %}{{ cfhlog.action }}{% endif %}</text>
				  </td>
				  <td>
					<form method="post">
						<textarea style="width: 100%;height: 110px;" id="messageReply" name="messageReply">{{ defaultReply }}</textarea>
						<button type="submit">Reply</button>
					</form>	
				</td>				
				  <td>
					  <form name ="block" id="block" method="POST">
						<button type="submit" name="block">Block</button>
					  </form>
					  <form name ="follow" id="follow" method="POST">
					   <button type="submit" name="follow">Follow</button>
					  </form>
				  </td>
                </tr>
				{% endif %}
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
      </div>
    </div>
  </div>
</body>
</html>