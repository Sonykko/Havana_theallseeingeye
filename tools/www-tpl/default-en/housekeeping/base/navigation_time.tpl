
	<div class="subnav-time"><b>Local time:</b>
	<div id="current_date"></p>
			<script>
			document.getElementById("current_date").innerHTML = Date();
			</script>
	</div><br/>
	<b>Server time:</b><br>
	habbo.frenchrules.net<br>
<text id="dateAndTime"></text>
<script>

      var dateAndTime = document.getElementById('dateAndTime');

      var currentTime = new Date();

      dateAndTime.innerHTML = currentTime;
          
    </script>
	</div>
	
	{% include "housekeeping/base/footer.tpl" %}	