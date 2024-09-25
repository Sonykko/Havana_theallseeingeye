<div id="badge-editor-flash">
 <p>Adobe Flash player requerido.</p>
 <p><a href="http://www.adobe.com/go/getflashplayer">Clic aquí para instalar Adobe Flash player</a>.</p>
</div>
<script type="text/javascript">
document.habboLoggedIn = {{ session.loggedIn }};
var habboName = "{{ session.loggedIn ? playerDetails.getName() : "" }}";
var ad_keywords = "";
var habboReqPath = "{{ site.sitePath }}";
var habboStaticFilePath = "{{ site.staticContentPath }}/web-gallery";
var habboImagerUrl = "{{ site.staticContentPath }}/habbo-imaging/";
var habboPartner = "";
window.name = "habboMain";
if (typeof HabboClient != "undefined") { HabboClient.windowName = "client"; }

</script>
<script type="text/javascript" language="JavaScript">
var habboReqPath = "{{ site.sitePath }}";
var swfobj = new SWFObject("{{ site.staticContentPath }}/flash/BadgeEditor.swf", "badgeEditor", "280", "366", "8");
swfobj.addParam("base", "{{ site.staticContentPath }}/flash/");
swfobj.addParam("bgcolor", "#FFFFFF");
swfobj.addVariable("post_url", "{{ site.staticContentPath }}/{{ site.housekeepingPath }}/admin_tools/api/groups/update_group_badge?");
swfobj.addVariable("__app_key", "HavanaWeb");
swfobj.addVariable("groupId", "{{ group.getId() }}");
swfobj.addVariable("badge_data", "{{ group.getBadge() }}");
swfobj.addVariable("localization_url", "{{ site.staticContentPath }}/xml/badge_editor.xml");
swfobj.addVariable("xml_url", "{{ site.staticContentPath }}/xml/badge_data.xml");
swfobj.addParam("allowScriptAccess", "always");
swfobj.write("badge-editor-flash");
</script>
<script>
	window.RufflePlayer = window.RufflePlayer || {};
		window.RufflePlayer.config = {
			"autoplay": "on",
			"unmuteOverlay": "hidden",
			"splashScreen": false,
		};
</script>
<script>
function loadBadgeEditor() {
		const ruffle = window.RufflePlayer.newest();
		const player = ruffle.createPlayer();
		const container = document.getElementById("badge-editor-flash");
		container.innerHTML = '';
		container.appendChild(player);
		player.load({
			url: '{{ site.staticContentPath }}/flash/BadgeEditor.swf',
			parameters: 'base={{ site.staticContentPath }}}/flash/&'+
						'bgcolor=#FFFFFF&'+
						'post_url={{ site.staticContentPath }}/{{ site.housekeepingPath }}/admin_tools/api/groups/update_group_badge?&'+
						'__app_key=HavanaWeb&'+
						'groupId={{ group.getId() }}&'+
						'badge_data={{ group.getBadge() }}&'+
						'localization_url={{ site.staticContentPath }}/xml/badge_editor.xml&'+
						'xml_url={{ site.staticContentPath }}/xml/badge_data.xml&',
			allowScriptAccess: true,
		});
		player.style.width = "280px";
		player.style.height = "366px";
		player.style.frameRate = "8";
		}
</script>