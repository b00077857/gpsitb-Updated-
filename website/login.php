<?php

    //session_start();
session_start();

    if (isset($_POST['user']) and isset($_POST['pass'])) {

        $user_name = $_POST["user"];
        $user_pass = $_POST["pass"];

        // echo $user_name;
        //preventing sql injections
        /*
        $user_name = stripslashes($user_name);
        $user_pass = stripslashes($user_pass);
        $user_name = mysqli_real_escape_string($user_name);
        $user_pass = mysqli_real_escape_string($user_pass);
*/
        $host = "iwillinmy.com";
        $userDname = "iwillinm_test";
        $Dpassword = "q9Q{n]uiq=*0";
        $db_name = "iwillinm_test";
        $table_name = "users";

        $conn = mysqli_connect($host, $userDname, $Dpassword, $db_name);
        // mysqli_select_db($conn,$db_name);

        /*
        if ($conn) {
            echo " DB connected!!!";
        } else {
            echo 'NOPE';
        }
        */

        $query = "SELECT * FROM $table_name WHERE username='$user_name' and password='$user_pass'";

        $result = mysqli_query($conn, $query) or die("FAILED!!!".mysqli_error($conn));
        $count = mysqli_num_rows($result);

    if($count == 1){
        $_SESSION['username'] = $user_name;
        header("Location: tracking.php");
        //echo'hello ';
        //echo $user_name;
        //echo "  Start tracking!";

    }
    else{
    //3.1.3 If the login credentials doesn't match, he will be shown with an error message.
            header("Location: welcome.php");
            echo "Invalid Login Credentials.";
        }
}

if (isset($_SESSION['username'])){
    $username = $_SESSION['username'];

    //echo $username;
  // echo "<a href='logout.php'>Logout</a>";

}
else{
    //die();
    header("Location: welcome.php");
    echo 'sorry not logged in';
}

?>
<html>
<form action="welcome.php">
    <button>Log OUT</button>
</form>
</html>

