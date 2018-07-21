<?php

    $host = "iwillinmy.com";
    $userDname = "iwillinm_gpsuser";
    $Dpassword = "c7BcKQF,X-!X";
    $db_name = "iwillinm_gps";
    $table_name = "coordinates";

    $conn3 = mysqli_connect($host, $userDname, $Dpassword, $db_name);
    // mysqli_select_db($conn,$db_name);

  //  $query3 = "SELECT * FROM $table_name";
 //   $result3= mysqli_query($conn3, $query3) or die("FAILED!!!".mysqli_error($conn3));

/*    while($row = mysqli_fetch_array($result3)){

        $lat = $row['latitude'];
        $long = $row['longitude'];
        //echo $lat . ': ' . $long . '<br/>';
    }
   */
    $latN = $_POST["la1"];
    $longN = $_POST["lo1"];
    $n = 4;

    $query3 = "INSERT INTO $table_name(latitude,longitude) VALUES ('$latN','$longN')";

    $result3= mysqli_query($conn3, $query3) or die("FAILED!!!".mysqli_error($conn3));

    if(!$result3){
        echo "Successfully added";
    }else{
        echo "Not Registered".mysqli_error($conn3);
    }

    while($row = mysqli_fetch_array($result3)){

        $lat = $row['latitude'];
        $long = $row['longitude'];
        echo $lat . ': ' . $long . '<br/>';
    }





/*
$get_result = $conn3->query("INSERT INTO user(latitude,longitude) VALUES ('$lat','$long')");
    if($get_result === true){
        echo "Successfully added";
    }else{
        echo $username."Not Registered".mysqli_error($);
    }

*/