
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="content-type" content="text/html" />
	<title data-translate="HEAD_TITLE"></title>

<script type="text/javascript">
var andSoItBegins = (new Date()).getTime();
</script>
    <link rel="shortcut icon" href="{{ site.staticContentPath }}/web-gallery/v2/favicon.ico" type="image/vnd.microsoft.icon" />
    <link rel="alternate" type="application/rss+xml" title="{{ site.siteName }}: RSS" href="{{ site.sitePath }}/rss" />
	
<script src="{{ site.staticContentPath }}/web-gallery/static/js/libs2.js" type="text/javascript"></script>
<script src="{{ site.staticContentPath }}/web-gallery/static/js/visual.js" type="text/javascript"></script>
<script src="{{ site.staticContentPath }}/web-gallery/static/js/libs.js" type="text/javascript"></script>
<script src="{{ site.staticContentPath }}/web-gallery/static/js/common.js" type="text/javascript"></script>
<script src="{{ site.staticContentPath }}/web-gallery/static/js/fullcontent.js" type="text/javascript"></script>
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/style.css" type="text/css" />
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/buttons.css" type="text/css" />
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/boxes.css" type="text/css" />
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/tooltips.css" type="text/css" />
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/process.css" type="text/css" />
<link rel="prefetch" href="{{ site.staticContentPath }}/web-gallery/staff/lang/hobba-form/{{ formLang }}.json" />

<script type="text/javascript">
document.habboLoggedIn = false;
var habboName = null;
var ad_keywords = "";
var habboReqPath = "{{ site.sitePath }}";
var habboStaticFilePath = "{{ site.staticContentPath }}/web-gallery";
var habboImagerUrl = "{{ site.staticContentPath }}/habbo-imaging/";
var habboPartner = "";
window.name = "habboMain";
if (typeof HabboClient != "undefined") { HabboClient.windowName = "client"; }


</script>

<script>
    async function loadTranslations() {
        try {
            const response = await fetch('{{ site.staticContentPath }}/web-gallery/staff/lang/hobba-form/{{ formLang }}.json');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const translations = await response.json();
            applyTranslations(translations);
        } catch (error) {
            console.error('Error loading translations:', error);
            applyTranslations({});
        }
    }

    function applyTranslations(translations) {
        document.querySelectorAll('[data-translate]').forEach(element => {
            const key = element.getAttribute('data-translate');
            const translation = translations[key] || key;
            
            if (element.tagName === 'TITLE') {
                document.title = translation;
            } else if (element.tagName === 'INPUT' && element.getAttribute('type') === 'submit') {
                element.setAttribute('value', translation);
            } else {
                element.innerHTML = translation;
            }
        });
    }

    document.addEventListener('DOMContentLoaded', loadTranslations);
</script>

<meta name="description" content="{{ site.siteName }} es un mundo virtual para quedar y hacer amigos" />
<meta name="keywords" content="{{ site.siteName }}, chat, amigos, personaje, virtual, mundo virtual, red social, jugar, juego, juegos, música, famosos" />

<!--[if IE 8]>
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/ie8.css" type="text/css" />
<![endif]-->
<!--[if lt IE 8]>
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/ie.css" type="text/css" />
<![endif]-->
<!--[if lt IE 7]>
<link rel="stylesheet" href="{{ site.staticContentPath }}/web-gallery/v2/styles/ie6.css" type="text/css" />
<script src="{{ site.staticContentPath }}/web-gallery/static/js/pngfix.js" type="text/javascript"></script>
<script type="text/javascript">
try { document.execCommand('BackgroundImageCache', false, true); } catch(e) {}
</script>

<style type="text/css">
body { behavior: url({{ site.staticContentPath }}/web-gallery/js/csshover.htc); }
</style>
<![endif]-->
<meta name="build" content="HavanaWeb" />
</head>
<body id="register" class="process-template secure-page">

<div id="overlay"></div>

<div id="container">
	<div class="cbb process-template-box clearfix">
		<div id="content">

			{% include "base/frontpage_header.tpl" %}
			<div id="process-content">	        	
			<div id="column1" class="column">


<table border="0" width="0" cellspacing="0" cellpadding="0">
<tr>
	<td class="bgContentLeft"><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="3" height="7" border="0"></td>
	<td rowspan="4" bgcolor=""><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="20" height="1" border="0"></td>
	<td bgcolor=""><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="455" height="1" border="0"></td>
	<td rowspan="4" bgcolor=""><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="21" height="1" border="0"></td>
	<td rowspan="4" class="bgContentRight"><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="4" height="1" border="0"></td>
</tr>

<tr>
	<td class="bgContentLeft"><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="3" height="1" border="0"></td>
	<td bgcolor="" class="tos-header" style="text-transform: uppercase;" data-translate="BE_A_HOBBA"></td>
</tr>

						{% if alert.hasAlert %}
								<div class="action-error flash-message">
				 <div class="rounded-container"><div style="background-color:black;"><div style="margin: 0px 4px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(238, 107, 122);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(231, 40, 62);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(227, 8, 33);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div></div></div><div style="margin: 0px 2px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(238, 105, 121);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 1, 27);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div></div><div style="margin: 0px 1px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(233, 64, 83);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div><div style="margin: 0px 1px; height: 1px; overflow: hidden; background-color: rgb(238, 105, 121);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div><div style="margin: 0px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 1, 27);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div><div style="margin: 0px; height: 1px; overflow: hidden; background-color: rgb(238, 107, 122);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div><div style="margin: 0px; height: 1px; overflow: hidden; background-color: rgb(231, 40, 62);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div><div style="margin: 0px; height: 1px; overflow: hidden; background-color: rgb(227, 8, 33);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div><div class="rounded-done">
				  <ul>
				   <li>{{ alert.message }}</li>
				  </ul>
				 </div><div style="background-color:black;"><div style="margin: 0px; height: 1px; overflow: hidden; background-color: rgb(227, 8, 33);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div><div style="margin: 0px; height: 1px; overflow: hidden; background-color: rgb(231, 40, 62);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div><div style="margin: 0px; height: 1px; overflow: hidden; background-color: rgb(238, 107, 122);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div><div style="margin: 0px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 1, 27);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div><div style="margin: 0px 1px; height: 1px; overflow: hidden; background-color: rgb(238, 105, 121);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div><div style="margin: 0px 1px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(233, 64, 83);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div><div style="margin: 0px 2px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(238, 105, 121);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 1, 27);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div></div><div style="margin: 0px 4px; height: 1px; overflow: hidden; background-color:black;"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(238, 107, 122);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(231, 40, 62);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(227, 8, 33);"><div style="height: 1px; overflow: hidden; margin: 0px 1px; background-color: rgb(226, 0, 26);"></div></div></div></div></div></div></div>
				</div>
				{% endif %}	

<tr>
	<td class="bgContentLeft"><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="3" height="1" border="0"></td>
	<td bgcolor=""><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="1" height="18" border="0"></td>
</tr>

<tr>
	<td class="bgContentLeft"><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="3" height="230" border="0"></td>
	<td bgcolor="" valign="top">


<img src="{{ site.staticContentPath }}/web-gallery/staff/hobba.gif" align="right" alt="" width="79" height="105" border="0">

<p data-translate="WANNA_BE_A_HOBBA"></p>

<p data-translate="QUESTIONS_REQUIREMENTS_HOBBA"></p>

<p data-translate="ANSWER_YES_DO_FORM"></p><br>

<p data-translate="IMPORTANT_INFO"></p>




<form method="post">
<input type="hidden" name="step" value="1">

<table border="0" cellspacing="0" cellpadding="0">
<tr>
	<td data-translate="HABBO_NAME"></td>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="7" height="1" border="0"></td>
	<td><input type="text" name="habboname" size="40" value=""></td>

</tr>
<tr>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="1" height="4" border="0"></td>
</tr>
<tr>
	<td data-translate="HABBO_EMAIL"></td>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="7" height="1" border="0"></td>
	<td><input type="text" name="email" size="40" value=""></td>

</tr>
<tr>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="1" height="4" border="0"></td>
</tr>
<tr>
	<td data-translate="REAL_FIRSTNAME"></td>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="7" height="1" border="0"></td>
	<td><input type="text" name="firstname" size="40" value=""></td>

</tr>
<tr>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="1" height="4" border="0"></td>
</tr>
<tr>
	<td data-translate="REAL_LASTNAME"></td>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="7" height="1" border="0"></td>
	<td><input type="text" name="lastname" size="40" value=""></td>

</tr>
<tr>
	<td><img src="{{ site.staticContentPath }}/web-gallery/staff/blank.gif" alt="" width="1" height="4" border="0"></td>
</tr>
<tr>
	<td colspan="3" align="right"><input type="submit" value="" data-translate="SUBMIT_BUTTON"></td>
</tr>
</table>

</form>

<p data-translate="REMINDER_FILL_ALL"></p>



	</td>
</tr>
</table>		
{% include "base/footer.tpl" %}				
		
				<script type="text/javascript">if (!$(document.body).hasClassName('process-template')) { Rounder.init(); }</script>

		</div>	 
</div>
</div>
</div>

</div>

<!--[if lt IE 7]>
<script type="text/javascript">
Pngfix.doPngImageFix();
</script>
<![endif]-->


<script type="text/javascript">
HabboView.run();
</script>


</body>
</html>