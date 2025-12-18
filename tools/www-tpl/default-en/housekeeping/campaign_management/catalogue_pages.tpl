{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set campaignManagementActive = "active" %}
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
				<input type="text" name="editId" class="form-control" id="editId" placeholder="Enter here a ID for the catalogue page..." value="{{ cataloguePages.getId() }}" />
			</div>
			<div class="form-group">
				<label>Parent ID</label>
				<select name="editParentId" id="editParentId" class="form-control">
					<option value="-1">Inicio (-1)</option>
					{% set num = 1 %}
					{% for parentNames in ParentNames %}
					<option value="{{ parentNames.getId() }}" {% if cataloguePages.getParentId() == parentNames.getId() %}selected{% endif %}>{{ parentNames.getName() }} ({{ parentNames.getId() }})</option>
					{% set num = num + 1 %}
					{% endfor %}
				</select>				
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="editOrderId" class="form-control" id="editOrderId" placeholder="Enter here a order ID for the catalogue page..." value="{{ cataloguePages.getOrderId() }}" />
			</div>
			<div class="form-group">
				<label>Min. rank</label>
				<select name="editMinRank" id="editMinRank" class="form-control">
					{% set num = 1 %}
					{% for ranks in allRanks %}
					<option value="{{ ranks.getId() }}" {% if cataloguePages.getMinRoleId() == ranks.getId() %}selected{% endif %}>{{ ranks.getName() }}</option>
					{% set num = num + 1 %}
					{% endfor %}
				</select>				
			</div>			
			<div class="form-group">
				<label>Is navigatable?</label>
				<select name="editIsNavigatable" id="editIsNavigatable" class="form-control">
					<option value="1" {% if cataloguePages.getIsNavigatable() == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if cataloguePages.getIsNavigatable() == 0 %}selected{% endif %}>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is HC only?</label>
				<select name="editIsHCOnly" id="editIsHCOnly" class="form-control">
					<option value="1" {% if cataloguePages.getIsClubOnly() == 1 %}selected{% endif %}>Yes</option>
					<option value="0" {% if cataloguePages.getIsClubOnly() == 0 %}selected{% endif %}>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Page name</label>
				<input type="text" name="editName" class="form-control" id="editName" placeholder="Enter here a name for the catalogue page..." value="{{ cataloguePages.getName() }}" />
			</div>
			<div class="form-group">
				<label>Icon</label>
				<input type="text" name="editIcon" class="form-control" id="editIcon" placeholder="Enter here a icon for the catalogue page..." value="{{ cataloguePages.getIcon() }}" />
			</div>
			<div class="form-group">
				<label>Colour</label>
				<input type="text" name="editColour" class="form-control" id="editColour" placeholder="Enter here a colour for the catalogue page..." value="{{ cataloguePages.getColour() }}" />
			</div>
			<div class="form-group">
				<label>Layout</label>
				<input type="text" name="editLayout" class="form-control" id="editLayout" placeholder="Enter here a layout for the catalogue page..." value="{{ cataloguePages.getLayout() }}" />
			</div>
			<div class="form-group">
				<label>Images</label>
				<textarea name="editImages" class="form-control" id="editImages" placeholder="Enter here the images for the catalogue page...">{{ cataloguePages.getImages() }}</textarea>
			</div>
			<div class="form-group">
				<label>Texts</label>
				<textarea name="editTexts" class="form-control" id="editTexts" placeholder="Enter here the texts for the catalogue page...">{{ cataloguePages.getTexts() }}</textarea>
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
					<option value="name">Name</option>										
					<option value="layout">Layout</option>										
					<option value="images">Images</option>										
					<option value="texts">Texts (like description)</option>										
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
		{% if searchPages|length > 0 %}
		<hr/>
		<p style="font-size:16px;"><b>Search results</b></p>
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
				  <td>{{ CataloguePagesSearch.getId() }}</td>                               
				  <td>{{ CataloguePagesSearch.getParentId() }}</td>                 
				  <td>{{ CataloguePagesSearch.getOrderId() }}</td>                 
				  <td>{{ CataloguePagesSearch.getMinRole() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getIsNavigatable() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getIsClubOnly() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getName() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getIcon() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getColour() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getLayout() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getImages() }}</td>                 			 
				  <td>{{ CataloguePagesSearch.getTexts() }}</td>                    			 
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?edit={{ CataloguePagesSearch.getId() }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?delete={{ CataloguePagesSearch.getId() }}" style="color:black;"><button type="button">Delete</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?copy={{ CataloguePagesSearch.getId() }}" style="color:black;"><button type="button">Copy</button></a>
				  </td>
                </tr>
			   {% set num = num + 1 %}
			   {% endfor %}
              </tbody>
            </table>
          </div>
		{% endif %}
		{% if noResults %}
		<hr/>
		<p style="font-size:16px;"><b>Search results</b></p>
		<p><i>No results found.</i></p>
		{% endif %} 		
          <h3 class="mt-4">Edit catalogue pages</h3>
		  <p>The catalogue pages list is seen below.</p>
		  <div class="pagination-buttons-box">
			{% if nextCatalogPages|length > 0 %}
				{% set ourNextPage = page + 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?page={{ ourNextPage }}"><button type="button">Next Page</button></a>
			{% endif %}
			{% if previousCatalogPages|length > 0 %}
				{% set ourNextPage = page - 1 %}
				<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?page={{ ourNextPage }}"><button type="button">Go back</button></a>
			{% endif %}
			</div>
		  {% if pages|length > 0 %}
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
				  <td>{{ cataloguePages.getId() }}</td>                               
				  <td>{{ cataloguePages.getParentId() }}</td>                 
				  <td>{{ cataloguePages.getOrderId() }}</td>                 
				  <td>{{ cataloguePages.getMinRole() }}</td>                 			 
				  <td>{{ cataloguePages.getIsNavigatable() }}</td>                 			 
				  <td>{{ cataloguePages.getIsClubOnly() }}</td>                 			 
				  <td>{{ cataloguePages.getName() }}</td>                 			 
				  <td>{{ cataloguePages.getIcon() }}</td>                 			 
				  <td>{{ cataloguePages.getColour() }}</td>                 			 
				  <td>{{ cataloguePages.getLayout() }}</td>                 			 
				  <td>{{ cataloguePages.getImages() }}</td>                 			 
				  <td>{{ cataloguePages.getTexts() }}</td>                 			 
				  <td>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?edit={{ cataloguePages.getId() }}" style="color:black;"><button type="button">Edit</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?delete={{ cataloguePages.getId() }}" style="color:black;"><button type="button">Delete</button></a>
					<a href="{{ site.sitePath }}/{{ site.housekeepingPath }}/campaign_management/catalogue/pages?copy={{ cataloguePages.getId() }}" style="color:black;"><button type="button">Copy</button></a>
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