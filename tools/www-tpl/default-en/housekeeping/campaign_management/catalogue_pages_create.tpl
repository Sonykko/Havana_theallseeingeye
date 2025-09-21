{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	     <h2 class="mt-4">Create catalogue pages</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Here you can create a catalogue page.</p>
		<form class="table-responsive col-md-4" style="padding-left:0;" method="post">
			<div class="form-group">
				<label>Parent ID</label>
				<select name="createParentId" id="createParentId" class="form-control">
					<option value="-1" selected>Inicio (-1)</option>
					{% set num = 1 %}
					{% for parentNames in ParentNames %}
					<option value="{{ parentNames.getId() }}">{{ parentNames.getName() }} ({{ parentNames.getId() }})</option>
					{% set num = num + 1 %}
					{% endfor %}
				</select>				
			</div>
			<div class="form-group">
				<label>Order ID</label>
				<input type="text" name="createOrderId" class="form-control" id="createOrderId" placeholder="Enter here a order ID for the catalogue page..." />
			</div>
			<div class="form-group">
				<label>Min. rank</label>
				<select name="createMinRank" id="createMinRank" class="form-control">
					{% set num = 1 %}
					{% for ranks in allRanks %}
					<option value="{{ ranks.getId() }}">{{ ranks.getName() }}</option>
					{% set num = num + 1 %}
					{% endfor %}
				</select>
			</div>
			<div class="form-group">
				<label>Is navigatable?</label>
				<select name="createIsNavigatable" id="createIsNavigatable" class="form-control">
					<option value="1" selected>Yes</option>
					<option value="0">No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Is HC only?</label>
				<select name="createIsHCOnly" id="createIsHCOnly" class="form-control">
					<option value="1">Yes</option>
					<option value="0" selected>No</option>
				</select>
			</div>
			<div class="form-group">
				<label>Page name</label>
				<input type="text" name="createName" class="form-control" id="createName" placeholder="Enter here a name for the catalogue page..." />
			</div>
			<div class="form-group">
				<label>Icon</label>
				<input type="text" name="createIcon" class="form-control" id="createIcon" placeholder="Enter here a icon for the catalogue page..." />
			</div>
			<div class="form-group">
				<label>Colour</label>
				<input type="text" name="createColour" class="form-control" id="createColour" placeholder="Enter here a colour for the catalogue page..." />
			</div>
			<div class="form-group">
				<label>Layout</label>
				<input type="text" name="createLayout" class="form-control" id="createLayout" placeholder="Enter here a layout for the catalogue page..." />
			</div>
			<div class="form-group">
				<label>Images</label>
				<textarea name="createImages" class="form-control" id="createImages" placeholder="Enter here the images for the catalogue page..."></textarea>
			</div>
			<div class="form-group">
				<label>Texts</label>
				<textarea name="createTexts" class="form-control" id="createTexts" placeholder="Enter here the texts for the catalogue page..."></textarea>
			</div>
			<button type="submit" style="margin-bottom: 20px;">Save Page</button>
		</form>
    </div>
  </div>
</body>
</html>