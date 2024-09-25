<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-100vw, initial-scale=0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>{{ site.siteName }}: {{ pageName }}</title>
	<link href="{{ site.staticContentPath }}/public/hk/css/bootstrap.min.css" rel="stylesheet">
	<link href="{{ site.staticContentPath }}/public/hk/css/simple-sidebar.css" rel="stylesheet">
	{% if site.hkNewStyle %}
	<link href="{{ site.staticContentPath }}/public/hk/css/hk_scale.css" rel="stylesheet">
	{% endif %}
	{% if ruffleActive %}
	<script src="{{ site.staticContentPath }}/web-gallery/static/js/libs2.js" type="text/javascript"></script>
	<script src="https://unpkg.com/@ruffle-rs/ruffle"></script>
	{% endif %}
  </head>
  

  