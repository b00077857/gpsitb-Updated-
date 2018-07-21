<?php
    session_start();

    require 'login.php';


    $lat;
    $long;

   // $latA;
   // $longB;


    $response = array();

   

    $host = "iwillinmy.com";
    $userDname = "iwillinm_gpsuser";
    $Dpassword = "c7BcKQF,X-!X";
    $db_name = "iwillinm_gps";
    $table_name = "coordinates";

    $conn2 = mysqli_connect($host, $userDname, $Dpassword, $db_name);
    // mysqli_select_db($conn,$db_name);
    
    $query2 = "SELECT * FROM $table_name";
    $result2= mysqli_query($conn2, $query2) or die("FAILED!!!".mysqli_error($conn2));

    while($row = mysqli_fetch_array($result2)){

        $lat = $row['latitude'];
        $long = $row['longitude'];
      //  echo $lat . ': ' . $long . '<br/>';
    }

    /*updating coordinates

    $host = "iwillinmy.com";
    $userDname = "iwillinm_gpsuser";
    $Dpassword = "c7BcKQF,X-!X";
    $db_name = "iwillinm_gps";
    $table_name = "coordupdate";


    $conn4 = mysqli_connect($host, $userDname, $Dpassword, $db_name);
    // mysqli_select_db($conn,$db_name);

    $query4 = "SELECT * FROM $table_name";
    $result4= mysqli_query($conn4, $query4) or die("FAILED!!!".mysqli_error($conn2));

    while($row2 = mysqli_fetch_array($result2)){

        $lat = $row2['latitude'];
        $long = $row2['longitude'];
        echo $lat . ': ' . $long . '<br/>';
    }
*/

?>
    <html>
        <div id="contents">
           <?php //echo 'Hello,'.$_SESSION['username']; ?>
        </div>
        <div id="map" style="width:1700px;height:750px;background:white"></div>
        <script>
            function myMap() {
                var coordla = '<?php echo $lat ;?>';
                var comma = ','
                var coordlo = '<?php echo $long ;?>';
                var coords = coordla.concat(comma,coordlo);

                var b=coords.split(",");
                document.write(b);
                var coordsN=new google.maps.LatLng(parseFloat(b[0]), parseFloat(b[1]));
                 document.write(coords);

                //document.write(coordsN);
                var mapOptions = {
                    center: new google.maps.LatLng(parseFloat(b[0]), parseFloat(b[1])),
                    zoom: 15,
                    mapTypeId: google.maps.MapTypeId.HYBRID
                };
                var map = new google.maps.Map(document.getElementById("map"), mapOptions);


                var marker = new google.maps.Marker({
                    position: (coordsN),
                    title: 'HERE!!!'
                });

                marker.setMap(map);

                document.write(coordsN);

            }

        </script>

        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBnRki2JB3obMHyVbkxW0Hzr74hmPGAz3I&callback=myMap"async defer></script>

        <script type="text/javascript" src="jquery.js"> </script>
        <script type="text/javascript">
            $(document).ready(function () {
                setInterval(function () {
                    $('#map').load('tracking.php')
                }, 97000);

            });

        </script>


    </html>
<?php

//require 'logout.php';
