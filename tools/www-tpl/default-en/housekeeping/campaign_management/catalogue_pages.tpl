{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	     <h2 class="mt-4">Manage catalogue pages</h2>
		{% include "housekeeping/base/alert.tpl" %}
		{% if isPageEdit %}
		<p>Here you can edit a catalogue page.</p>
		{% for cataloguePages in pageEdit %}
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Page ID</label>
				<input type="text" name="editId" class="form-control" id="editId" placeholder="Enter here a ID for the catalogue page..." value="{{ cataloguePages.id }}" />
			</div>
			<div class="form-group">
				<label>Parent ID</label>
				<input type="text" name="editParentId" class="form-control" id="editParentId" placeholder="Enter here a parent ID for the catalogue page..." value="{{ cataloguePages.parent_id }}" />
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="editOrderId" class="form-control" id="editOrderId" placeholder="Enter here a order ID for the catalogue page..." value="{{ cataloguePages.order_id }}" />
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="editMinRank" class="form-control" id="editMinRank" placeholder="Enter here a min. rank ID for the catalogue page..." value="{{ cataloguePages.order_id }}" />
			</div>
			<div class="form-group">
				<label>Is navigatable?</label>
				<select name="editIsNavigatable" id="editIsNavigatable" class="form-control">
					<option value="1" {% if cataloguePages.is_navigatable == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if cataloguePages.is_navigatable == 0 %}selected{% endif %}>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is HC only?</label>
				<select name="editIsHCOnly" id="editIsHCOnly" class="form-control">
					<option value="1" {% if cataloguePages.is_club_only == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if cataloguePages.is_club_only == 0 %}selected{% endif %}>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Page name</label>
				<input type="text" name="editName" class="form-control" id="editName" placeholder="Enter here a name for the catalogue page..." value="{{ cataloguePages.name }}" />
			</div>
			<div class="form-group">
				<label>Icon</label>
				<input type="text" name="editIcon" class="form-control" id="editIcon" placeholder="Enter here a icon for the catalogue page..." value="{{ cataloguePages.icon }}" />
			</div>
			<div class="form-group">
				<label>Colour</label>
				<input type="text" name="editColour" class="form-control" id="editColour" placeholder="Enter here a colour for the catalogue page..." value="{{ cataloguePages.colour }}" />
			</div>
			<div class="form-group">
				<label>Layout</label>
				<input type="text" name="editLayout" class="form-control" id="editLayout" placeholder="Enter here a layout for the catalogue page..." value="{{ cataloguePages.layout }}" />
			</div>
			<div class="form-group">
				<label>Images</label>
				<textarea name="editImages" class="form-control" id="editImages" placeholder="Enter here the images for the catalogue page...">{{ cataloguePages.images }}</textarea>
			</div>
			<div class="form-group">
				<label>Texts</label>
				<textarea name="editTexts" class="form-control" id="editTexts" placeholder="Enter here the texts for the catalogue page...">{{ cataloguePages.texts }}</textarea>
			</div>
			<button type="submit" style="margin-bottom: 20px;">Save Page</button>
		</form>
		{% endfor %}
		{% else %}
		<p>Here you can search a catalogue page.</p>
		<form class="table-responsive col-md-4" style="padding-left: 0;" method="post">
			<div class="form-group">
				<label for="field">Field</label>
				<select name="searchField" class="form-control" id="field">
					<option value="id">Page ID</option>
					<option value="parent_id">Parent ID</option>
					<option value="min_role">Min. Rank</option>
					<option value="name">Page Name</option>										
					<option value="texts">Page Texts (like description)</option>										
				</select>
			</div>
			<div class="form-group">
				<label for="field">Search type</label>
				<select name="searchType" class="form-control" id="field">
					<option value="contains">Contains</option>
					<option value="starts_with">Starts with</option>
					<option value="ends_with">Ends with</option>
					<option value="equals">Equals</option>
				</select>
			</div>
			<div class="form-group">
				<label for="searchFor">Search data</label>
				<input type="text" name="searchQuery" class="form-control" id="searchFor" placeholder="Looking for...">
			</div>
			<button type="submit">Perform Search</button>
		</form>
		<br>
		{% if searchPages|length > 0 %}
		<h5>Search Results</h5>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
				  <th>ID</th>
                  <th>Parent ID</th>
                  <th>Order ID</th>
                  <th>Min. rank</th>
                  <th>Is navigatable</th>
				  <th>HC only</th>
				  <th>Name</th>
				  <th>Icon</th>
				  <th>Colour</th>
				  <th>Layout</th>
				  <th>Images</th>
				  <th>Texts</th>
				  <th>Action</th>			  
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for CataloguePagesSearch in searchPages %}
                <tr>
                  <td>{{ CataloguePagesSearch.id }}</td>                               
				  <td>{{ CataloguePagesSearch.parent_id }}</td>                 
				  <td>{{ CataloguePagesSearch.order_id }}</td>                 
				  <td>{{ CataloguePagesSearch.min_role }}</td>                 			 
				  <td>{{ CataloguePagesSearch.is_navigatable }}</td>                 			 
				  <td>{{ CataloguePagesSearch.is_club_only }}</td>                 			 
				  <td>{{ CataloguePagesSearch.name }}</td>                 			 
				  <td>{{ CataloguePagesSearch.icon }}</td>                 			 
				  <td>{{ CataloguePagesSearch.colour }}</td>                 			 
				  <td>{{ CataloguePagesSearch.layout }}</td>                 			 
				  <td>{{ CataloguePagesSearch.images }}</td>                 			 
				  <td>{{ CataloguePagesSearch.texts }}</td>                 			 
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?edit={{ CataloguePagesSearch.id }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?delete={{ CataloguePagesSearch.id }}" style="color:black;"><button type="button">Delete</button></a>
				  </td>
                </tr>
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
          </div>
		{% endif %}
          <h3 class="mt-4">Edit catalogue pages</h3>
		  <p>The catalogue pages list is seen below.</p>
		  		<div style="margin:10px; margin-left: 0;">
			{% if nextCatalogPages|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousCatalogPages|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Parent ID</th>
                  <th>Order ID</th>
                  <th>Min. rank</th>
                  <th>Is navigatable</th>
				  <th>HC only</th>
				  <th>Name</th>
				  <th>Icon</th>
				  <th>Colour</th>
				  <th>Layout</th>
				  <th>Images</th>
				  <th>Texts</th>
				  <th>Action</th>
                </tr>
              </thead>
              <tbody>
			    {% set num = 1 %}
				{% for cataloguePages in pages %}
                <tr>
				  <td>{{ cataloguePages.id }}</td>                               
				  <td>{{ cataloguePages.parent_id }}</td>                 
				  <td>{{ cataloguePages.order_id }}</td>                 
				  <td>{{ cataloguePages.min_role }}</td>                 			 
				  <td>{{ cataloguePages.is_navigatable }}</td>                 			 
				  <td>{{ cataloguePages.is_club_only }}</td>                 			 
				  <td>{{ cataloguePages.name }}</td>                 			 
				  <td>{{ cataloguePages.icon }}</td>                 			 
				  <td>{{ cataloguePages.colour }}</td>                 			 
				  <td>{{ cataloguePages.layout }}</td>                 			 
				  <td>{{ cataloguePages.images }}</td>                 			 
				  <td>{{ cataloguePages.texts }}</td>                 			 
				  <!--<td>{% if hotCampaign.status == 1 %}Active{% else %}Hidden{% endif %}</td>-->              			             			                			 
				  <!--<td>{% if recommended.isPicked == 1 %}Yes{% else %}No{% endif %}</td>-->
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?edit={{ cataloguePages.id }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?delete={{ cataloguePages.id }}" style="color:black;"><button type="button">Delete</button></a>
				  </td>
                </tr>
				{% set num = num + 1 %}
				
			   {% endfor %}
              </tbody>
            </table>
      </div>
	  {% endif %}
    </div>
  </div>
  <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"></script>
  <script src="https://blackrockdigital.github.io/startbootstrap-simple-sidebar/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script>
    $("#menu-toggle").click(function(e) {
      e.preventDefault();
      $("#wrapper").toggleClass("toggled");
    });
  </script>
</body>
</html>