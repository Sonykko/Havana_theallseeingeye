{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set bansActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_admin_tools.tpl" %}
	     <h2 class="mt-4">Wordfilter tool</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<br />
		<p><b>Create word</b></p>
		<p>This tool allows you to add a word to Wordfilter.</p>
		<div class="alert__tool">
			<form class="alert__tool__form" method="post">
				<div class="alert__tool__recipient">
					<label for="word">The word</label>
					<input type="text" name="addword" class="" id="addword" value="" />
				</div>
				<div class="alert__tool__commonmessage">
					<select name="isBannable" id="isBannable">
						<option value="" disabled selected>Is Bannable?</option>
						<option value="true">Yes</option>
						<option value="false">No</option>
					</select>
				</div>
				<div class="alert__tool__commonmessage">
					<select name="isFilterable" id="isFilterable">
						<option value="" disabled selected>Is Filterable?</option>
						<option value="true">Yes</option>
						<option value="false">No</option>
					</select>
				</div>
				<div class="alert__tool__submit">
					<button type="submit" name="action" value="addWord">Add</button>
				</div>
			 </form>
		</div>
		<hr />
		  <p><b>Find word</b></p>
		  <p>This tool allows you to search a word with letter starting or by id.</p>
		<form class="" method="post" style="display: flex;gap: 10px;align-items: center;">
			<div class="">
				<label>Search</label>
				{% autoescape 'html' %}
				<input type="text" name="searchStr" id="searchStr" />
				{% endautoescape %}
			</div>
			<button type="submit" name="action" value="searchWord">Submit</button>
		</form>
		  {% if searchWordsDetails|length > 0 %}
		  <br />
		  <p>Words starting with '{{ query }}' or with id '{{ query }}'</p>
				<table class="table table-striped">
					<thead>
						<tr>
						  <th>ID</a></th>
						  <th>Word</a></th>
						  <th>Is Bannable?</th>
						  <th>Is Filterable?</th>
						  <th></th>
						</tr>
					</thead>
					<tbody>
						{% set num = 1 %}
						{% for word in searchWordsDetails %}
						<tr>
						{% autoescape 'html' %}
						<form method="post">
						  <input type="hidden" name="wordId" value="{{ word.getId() }}" />
						  <td>{{ word.getId() }}</td>
						  <td>
							<input type="text" name="saveWord" id="saveWord" value="{{ word.getWord() }}" style="width:100%;" />
						  </td>
						  <td>
							<select name="isBannable" id="isBannable">
								<option value="true" {% if word.isBannable() %}selected{% endif %}>Yes</option>
								<option value="false" {% if word.isBannable() != true %}selected{% endif %}>No</option>
							</select>
						  </td>
						  <td>
							<select name="isFilterable" id="isFilterable">
								<option value="true" {% if word.isFilterable() %}selected{% endif %}>Yes</option>
								<option value="false" {% if word.isFilterable() != true %}selected{% endif %}>No</option>
							</select>
						  </td>
						  <td>
							<button type="submit" name="action" value="saveWord">Save</button>
							<button type="submit" name="action" value="deleteWord">Delete</button>
						</td>
						</form>
						{% endautoescape %}
						</tr>
						{% set num = num + 1 %}
						{% endfor %}
					</tbody>
				</table>
				{% endif %}
			{% if searchEmpty %}
			<br>
			<p><i>No results found to display.</i></p>
			{% endif %}
		  <hr />
          <p><b>List of current words</p></b>
		  <p>This tool allow you to see the complete list of all current words in the Wordfilter.</p>
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
				<form method="post">
				  <input type="hidden" name="wordId" value="{{ word.getId() }}" />
				  <td>{{ word.getId() }}</td>
				  <td>
					<input type="text" name="saveWord" id="saveWord" value="{{ word.getWord() }}" style="width:100%;" />
				  </td>
				  <td>
					<select name="isBannable" id="isBannable">
						<option value="true" {% if word.isBannable() %}selected{% endif %}>Yes</option>
						<option value="false" {% if word.isBannable() != true %}selected{% endif %}>No</option>
					</select>
				  </td>
				  <td>
					<select name="isFilterable" id="isFilterable">
						<option value="true" {% if word.isFilterable() %}selected{% endif %}>Yes</option>
						<option value="false" {% if word.isFilterable() != true %}selected{% endif %}>No</option>
					</select>
				  </td>
				  <td>
					<button type="submit" name="action" value="saveWord">Save</button>
					<button type="submit" name="action" value="deleteWord">Delete</button>
				</td>
				</form>
                </tr>
			   {% set num = num + 1 %}

			   {% endfor %}
              </tbody>
            </table>
		  </div>
		  {% else %}
		  <p><i>Nothing found to display.</i></p>
		  {% endif %}
    </div>
  </div>
</body>
</html>