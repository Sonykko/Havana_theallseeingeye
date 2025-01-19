{% if selfReport %}
<ul class="error">
	<li>You can't report your own messages.</li>
</ul>

<p>
<a href="#" class="new-button cancel-report"><b>Cancel</b><i></i></a>
</p>
{% else %}
<p>
Are you sure you want to report the message <b>{{ subject }}</b> to the moderators and remove <b>{{ friend }}</b> from your friend list? You cant undo this.
</p>

<p>
<a href="#" class="new-button cancel-report"><b>Cancel</b><i></i></a>
<a href="#" class="new-button send-report"><b>Send report</b><i></i></a>
</p>
{% endif %}