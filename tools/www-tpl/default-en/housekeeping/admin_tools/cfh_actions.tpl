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
				{% if cfhlog.getAction() != "PICK UP" %}
				<div class="alert alert-danger">
					<text>The CFH is already picked up or it's not picked up by any Moderator yet</text>
				</div>
				{% else %}
                <tr>
                  <td style="display: flex;flex-direction: column;">					  
					  <text>- <b>Caller:</b> <a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/chatlog.action?chatId={{ cfhlog.getUserId() }}"><u>{{ cfhlog.getUsername() }}</u> (id: {{ cfhlog.getUserId() }})</a></text>
					  <text>- <b>Time:</b> {{ cfhlog.getCreatedTime() }}</text>
					  <text>- <b>Room name:</b> {{ cfhlog.getRoomName() }} (id: {{ cfhlog.getRoomId() }})</text>
					  <text>- <b>Reason for action:</b> {{ cfhlog.getReason() }}</text>					  
					  <text>- <b>Picked by:</b> {% if cfhlog.getModerator() == null %}-{% else %}{{ cfhlog.getModerator() }}{% endif %}</text>
					  <text>- <b>Time pick up:</b> {% if cfhlog.getPickedTime() == null %}-{% else %}{{ cfhlog.getPickedTime() }}{% endif %}</text>	
					  <text>- <b>Status:</b> {% if cfhlog.isDeleted() %}Picked Up{% else %}{{ cfhlog.getAction() }}{% endif %}</text>
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