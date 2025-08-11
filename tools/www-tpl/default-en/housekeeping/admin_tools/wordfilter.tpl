{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
	     <h2 class="mt-4">Wordfilter tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if isWordEdit %}
		<p>Here you can edit word from Wordfilter.</p>
		{% for WordEdit in wordEdit %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Word</label>
				<input type="text" name="saveWord" class="form-control" id="saveWord" placeholder="Enter here the word..." value="{{ WordEdit.wordFilter }}" />
			</div>
			<div class="form-group">
				<label>Is Bannable?</label>
				<select name="isBannable" id="isBannable" class="form-control">
					<option value="1" {% if WordEdit.isBannable == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if WordEdit.isBannable != 1 %}selected{% endif %}>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is Filterable?</label>
				<select name="isFilterable" id="isFilterable" class="form-control">
					<option value="1" {% if WordEdit.isFilterable == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if WordEdit.isFilterable != 1 %}selected{% endif %}>No</option>
				</select>
			</div>
			<button type="submit">Save Word</button>
		</form>
		{% endfor %}
		{% else %}
		<p>Here you can add a word to Wordfilter.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Word</label>
				<input type="text" name="addword" class="form-control" id="addword" placeholder="Enter here the word..." />
			</div>
			<div class="form-group">
				<label>Is Bannable?</label>
				<select name="isBannable" id="isBannable" class="form-control">
					<option value="1">Yes</option>
					<option value="0" selected>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is Filterable?</label>
				<select name="isFilterable" id="isFilterable" class="form-control">
					<option value="1" selected>Yes</option>
					<option value="0">No</option>
				</select>
			</div>
			<button type="submit">Add Word</button>
		</form>
          <h2 class="mt-4">Edit words</h2>
		  <p>The Wordfilter list is seen below.</p>
		  <div class="pagination-buttons-box">
		  {% if nextWords|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/wordfilter?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousWords|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/wordfilter?page={{ ourNextPage }}&sort={{ sortBy }}"><button type="button">Go back</button></a>
			{% endif %}
		</div>
		  {% if Words|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/wordfilter?page={{ page }}&sort=id">ID</a></th>
                  <th><a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/wordfilter?page={{ page }}&sort=word">Word</a></th>
                  <th>Is Bannable?</th>
                  <th>Is Filterable?</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for word in Words %}
                <tr>
				  <td>{{ word.id }}</td>                               
				  <td>{{ word.wordFilter }}</td>                                			 
				  <td>{% if word.isBannable == 1 %}Yes{% else %}No{% endif %}</td>                 			 
				  <td>{% if word.isFilterable == 1 %}Yes{% else %}No{% endif %}</td>                 			                  			 
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/wordfilter?edit={{ word.id }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/admin_tools/wordfilter?delete={{ word.id }}" style="color:black;"><button type="button">Delete</button></a>
				</td>
                </tr>
			   {% set num = num + 1 %}
			   
			   {% endfor %}
              </tbody>
            </table>
		  </div>
		  {% else %}
		  <p><i>Nothing found to display.</i></p>
		  {% endif %} 
	  {% endif %}
    </div>
  </div>
</body>
</html>