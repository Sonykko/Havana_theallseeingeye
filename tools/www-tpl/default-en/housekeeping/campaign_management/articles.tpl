{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
    <h2 class="mt-4">Posted Articles</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>This includes the most recent articles posted on the site, you may edit or delete them if you wish.</p>	
		  {% if articles|length > 0 %}
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>Name</th>
				  <th>Author</th>
				  <th>Short Story</th>
				  <th>Date</th>
				  <th>Views</th>
				  <th></th>
                </tr>
              </thead>
              <tbody>
				{% for article in articles %}
                <tr>
				  <td>{{ article.title }}</td>
				  <td>{{ article.author }}</td>
				  <td>{{ article.shortstory }}</td>
				  <td>{{ article.getDate() }}</td>
				  <td>{{ article.views }}</td>
				  <td>
				  	<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/articles/edit?id={{ article.id }}"><button type="button">Edit</button></a>
				  	<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/articles/delete?id={{ article.id }}"><button type="button">Delete</button></a>
				  </td>
                </tr>
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