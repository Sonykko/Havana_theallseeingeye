{% include "housekeeping/base/header.tpl" %}
  <body>
<div class="ee" style="
    background-image: url({{ site.sitePath }}/public/hk/visual/smokejoint_mlg.gif);
    background-repeat: no-repeat;
    width: 240px;
    height: 138px;
    bottom: 0;
    position: fixed;
    right: 0;
"></div>
  <div class="reproductor" style="cursor: url(&quot;{{ site.sitePath }}/public/hk/visual/hitmarker_mlg.png&quot;), auto;">
  <div  style="pointer-events: none;">
  <!--<script>
  const playSound = function() {
  var sonido = new Audio("{{ site.sitePath }}/public/hk/visual/mynamejeff.mp3");
  sonido.play();
  document.removeEventListener('click', playSound);
}
document.addEventListener('click', playSound);
</script>-->
<script>
    let boton = document.querySelector(".reproductor")

    boton.addEventListener("click", () => {
      let etiquetaAudio = document.createElement("audio")
      etiquetaAudio.setAttribute("src", "{{ site.sitePath }}/public/hk/visual/mynamejeff.mp3")
      etiquetaAudio.play()
    })
</script>
	{% include "housekeeping/base/navigation.tpl" %}
<div style="display: flex;flex-direction: column;align-items: center;">
     <h2 class="mt-4">Forbidden</h2>
		  <text>You don't have permissions to access to this location.</text>
		  <text>This attempt is monitored in the Housekeeping log.</text>
		  <text>Has been saved with username {{ playerDetails.getName }} from <script type="application/javascript">
  function getIP(json) {
    document.write("", json.ip);
  }
</script>
<script type="application/javascript" src="https://api.ipify.org?format=jsonp&callback=getIP"></script>)</text>
<br>
<iframe width="920" height="420" src="https://www.youtube.com/embed/Nz072Y6hM68?rel=0&amp;autoplay=1" title="Snoop dogg MLG" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
 </div>
 </div>
 </div>
</body>
</html>