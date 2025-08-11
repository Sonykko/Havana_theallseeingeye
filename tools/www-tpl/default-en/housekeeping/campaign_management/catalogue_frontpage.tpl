{% include "housekeeping/base/header.tpl" %}
  <body>
    {% set articlesCreateActive = " active " %}
	{% include "housekeeping/base/navigation.tpl" %}
	{% include "housekeeping/base/navigation_campaign_management.tpl" %}
	<script type="text/javascript">
	function previewTS(el) {
		document.getElementById('ts-preview').innerHTML = '<img src="{{ site.staticContentPath }}/c_images/Top_Story_Images/' + el + '" /><br />';
	}
	</script>
      <h2 class="mt-4">Edit Catalogue Page</h2>
		{% include "housekeeping/base/alert.tpl" %}
		<p>Edit the catalogue front page data.</p>
		<form class="table-responsive col-md-4" method="post">
			<div class="form-group">
				<label>Header</label>
				<input type="text" class="form-control" name="header" value="{{ frontpageText2 }}">
			</div>
			<div class="form-group">
				<label>Sub Text</label>
				<input type="text" class="form-control" name="subtext" value="{{ frontpageText3 }}">
			</div>
			<div class="form-group">
				<label>Web link (optional)</label>
				<input type="text" class="form-control" name="link" value="{{ frontpageText4 }}">
			</div>
			<div class="form-group">
				<label>Image</label>
				<p>
					<select onkeypress="previewTS(this.value);" onchange="previewTS(this.value);" name="image" id="image">
					{% for image in images %}<option value="{{ image }}"{% if image == frontpageText1 %} selected{% endif %}>{{ image }}</option>{% endfor %}
					</select>
				</p>
			</div>
			<div class="form-group">
				<label>Image Preview</label>
				<div id="ts-preview"><img src="{{ site.staticContentPath }}/c_images/Top_Story_Images/{{ frontpageText1 }}" /></div>
			</div>
			
			<div class="form-group"> 
				<button type="submit" class="btn btn-info">SAVE FRONTPAGE</button>
      </div>
    </div>
  </div>
</body>
</html>