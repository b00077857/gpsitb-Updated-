<?php
session_start();

require 'login.php';

echo 'Hello,'.$_SESSION['username'];


?>
<html>
<h1> TRACKING HERE!!</h1>
<div id="map" style="width:1700px;height:750px;background:yellow"></div>
<script>
    function myMap() {
        var mapOptions = {
            center: new google.maps.LatLng(51.5, -0.12),
            zoom: 10,
            mapTypeId: google.maps.MapTypeId.HYBRID
        }
        var map = new google.maps.Map(document.getElementById("map"), mapOptions);
    }
</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAfBpdeKjdhDUthRGGQXQPipANPDKyrUgI&callback=myMap"async defer></script>
</html>
<?php
//require 'logout.php';
