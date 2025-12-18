{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set adminToolsActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
	{% include "housekeeping/base/alert.tpl" %}
     <h2 class="mt-4">{{ site.siteName }} Restoring Tool</h2>		
		<br />
		<p>With this tool you can:</p>
		<p>1. Create & send new password to a user
		<br>2. Change users email to a given email address and send a new password to that address</p>
		<p>You can no longer return habbos original email address with this tool. To do that, use Habbo Search & Information tool's Email Log.</p>
		<br />		
		<div class="">
			<form method="post">
				<div class="restore__mass">
					<label>
						<input type="radio" id="massRestore" name="event" value="mass" checked> <b>List of usernames to restore:</b>
					</label>
					<textarea name="massRestore"></textarea>
				</div>
				<div>
					<label>
						<input type="radio" id="userRestore" name="event" value="one"> <b>One user only:</b>
					</label>
					<div class="restore__one">
						<div class="restore__one__username">
							<text>{{ site.siteName }} name:</text>
							<input type="text" id="userName" name="userName" />
						</div>
						<div class="restore__one__useremail">
							<text>New email address:</text>
							<input type="text" id="newUserEmail" name="newUserEmail" />
						</div>
					</div>
				</div>
				<br />
				<div class="restore__mail">
					<label><b>Email to be sent (do not insert user's name for security reasons):</b></label>
					<div class="restore__mail__box">
						<div class="restore__mail__subject">
							<text style="margin-bottom: 5px">Subject:</text>
							<text>Message:</text>
						</div>
						<div class="resotre__mail__message">
							<input type="text" id="subject" name="subject" value="{{ site.siteName }} Hotel Anti-Fraud Team: Your new password" />
							<textarea id="message" name="message">Hey Habbo

Take a moment to read all about fraud on our fraud tips page. And make sure your friends read them too!

{{ site.sitePath }}/help/73

Your new password is %password%

Change this password when you return to the hotel.</textarea>
						</div>	
					</div>
				</div>
				<button type="submit">Restore</button>
			</form>
		</div>
      </div>
    </div>
  </div>
</body>
</html>