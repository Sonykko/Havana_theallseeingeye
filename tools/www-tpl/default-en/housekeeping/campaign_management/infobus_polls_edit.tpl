{% include "housekeeping/base/header.tpl" %}
  <body>
	{% set campaignManagementActive = "active" %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
		<script type="text/javascript">
		var lastID = {{ poll.getPollData().getAnswers().size() }};
		
		function insertAfter(newNode, referenceNode) {
			referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
		}
				
		function cloneAnswer() {
			var div = document.getElementById('answer' + lastID);
			lastID++;
			clone = div.cloneNode(true); // true means clone all childNodes and all event handlers
			clone.id = "answer" + lastID;
			clone.childNodes[1].id = 'answer' + lastID + 'title';
			insertAfter(clone, div);
			document.getElementById('answer' + lastID + 'title').innerHTML = "Answer " + lastID;
		}
		
		function removeLatestAnswer() {
			if (lastID > 1) {
				var div = document.getElementById('answer' + lastID);
				div.parentNode.removeChild(div);
				lastID--;
			}
		}
		</script>
	
		<h2 class="mt-4">Edit Infobus Poll</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Edit an infobus poll</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Question:</label>
				<input type="text" class="form-control" id="text" name="question" value="{{ poll.getPollData().getQuestion() }}"></input>
			</div>
			{% set answerCount = 1 %}
			{% for answer in poll.getPollData().getAnswers() %}
			<div class="form-group" id="answer{{ answerCount }}">
				<label id="answer{{ answerCount }}title">Answer {{ answerCount }}</label>
				<input name="answers[]" type="text" class="form-control" value="{{ answer }}">
			</div>
			{% set answerCount = answerCount + 1 %}
			{% endfor %}
			
			<p><button type="button" class="btn btn-success" onclick="cloneAnswer()">Add answer</button></p>
			<p><button type="button" class="btn btn-primary" onclick="removeLatestAnswer()">Remove answer</button></p>
			
			<div class="form-group"> 
				<input type="hidden" id="text" name="creator" value="{{ playerDetails.id }}">
				<button type="submit" class="btn btn-info">Save Poll</button>
			</div>
		</form>
      </div>
    </div>
  </div>
</body>
</html>