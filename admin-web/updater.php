<?php
/**
 * Created by PhpStorm.
 * User: devstudio
 * Date: 18/10/2018
 * Time: 12:41
 */

include("includes/connection.php");
include("includes/function.php");
echo "HERE";


//if($result){
//    echo "DONE!!!";
//}
//else{
//    "NOT DONE";
//}

//
    $qry = "SELECT * from tbl_users";
    $passwords = array();
    $rslt = mysqli_query($mysqli, $qry);
    while ($row = mysqli_fetch_array($rslt)){
        array_push($passwords, hashPassword($row['password']));
        $id = $row['id'];
        $pswd = hashPassword($row['password']);
        $query = "UPDATE tbl_users set password = '$pswd' where id = $id";
        $result = mysqli_query($mysqli, $query);
        
    }
//    print_r($passwords);

//    for ($x=0; $x < sizeof($passwords); $x++){
//        $pswd = $passwords[$x];
//        $query = "UPDATE tbl_users set password = '$pswd'";
//        $result = mysqli_query($mysqli, $query);
//    }
