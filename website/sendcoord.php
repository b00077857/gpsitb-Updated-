<?php

    $host = "iwillinmy.com";
    $userDname = "iwillinm_gpsuser";
    $Dpassword = "c7BcKQF,X-!X";
    $db_name = "iwillinm_gps";
    $table_name = "coordinates";

    $conn4 = mysqli_connect($host, $userDname, $Dpassword, $db_name);
    // mysqli_select_db($conn,$db_name);

    $query4 = "SELECT * FROM $table_name ORDER BY locno DESC";
    $result4= mysqli_query($conn4, $query4) or die("FAILED!!!".mysqli_error($conn4));

    $res = mysqli_fetch_array($result4);

    $resultA = array();

    array_push($resultA,array(
            "latitude"=>$res['latitude'],
            "longitude"=>$res['longitude'],
        )
    );

    echo json_encode(array("result"=>$resultA));

    while($row3 = mysqli_fetch_array($result4)){

        $lat = $row3['latitude'];
        $long = $row3['longitude'];
       // echo $lat . ': ' . $long . '<br/>';
    }