<?php include("includes/connection.php");
include("includes/function.php");

$file_path = 'http://'.$_SERVER['SERVER_NAME'] . dirname($_SERVER['REQUEST_URI']).'/';

function get_user_info($user_id)
{
    global $mysqli;

    $user_qry="SELECT * FROM tbl_users where id='".$user_id."'";
    $user_result=mysqli_query($mysqli,$user_qry);
    $user_row=mysqli_fetch_assoc($user_result);

    return $user_row['name'];
}

if(isset($_GET['get_home_channels']))
{

    $jsonObj_0= array();

    $query_home="SELECT * FROM tbl_channels
		LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid
		WHERE tbl_channels.status=1 AND tbl_channels.slider_channel=1 ORDER BY tbl_channels.id DESC";
    $sql_home = mysqli_query($mysqli,$query_home)or die(mysqli_error());

    while($data_home = mysqli_fetch_assoc($sql_home))
    {

        $row0['id'] = $data_home['id'];
        $row0['cat_id'] = $data_home['cat_id'];
        $row0['channel_title'] = $data_home['channel_title'];
        $row0['channel_url'] = $data_home['channel_url'];
        $row0['channel_url_ios'] = $data_home['channel_url_ios'];
        $row0['channel_thumbnail'] = $file_path.'images/'.$data_home['channel_thumbnail'];
        $row0['channel_desc'] = $data_home['channel_desc'];
        $row0['rate_avg'] = $data_home['rate_avg'];

        $row0['cid'] = $data_home['cid'];
        $row0['category_name'] = $data_home['category_name'];
        $row0['category_image'] = $file_path.'images/'.$data_home['category_image'];
        $row0['category_image_thumb'] = $file_path.'images/thumbs/'.$data_home['category_image'];

        array_push($jsonObj_0,$row0);

    }

    $row['slider_list']=$jsonObj_0;

    $jsonObj= array();

    $query="SELECT * FROM tbl_channels
		LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid
		WHERE tbl_channels.status=1 ORDER BY tbl_channels.id DESC LIMIT 3";

    $sql = mysqli_query($mysqli,$query)or die(mysqli_error());

    while($data = mysqli_fetch_assoc($sql))
    {
        $row1['id'] = $data['id'];
        $row1['cat_id'] = $data['cat_id'];
        $row1['channel_title'] = $data['channel_title'];
        $row1['channel_url'] = $data['channel_url'];
        $row1['channel_url_ios'] = $data['channel_url_ios'];
        $row1['channel_thumbnail'] = $file_path.'images/'.$data['channel_thumbnail'];
        $row1['channel_desc'] = $data['channel_desc'];
        $row1['rate_avg'] = $data['rate_avg'];

        $row1['cid'] = $data['cid'];
        $row1['category_name'] = $data['category_name'];
        $row1['category_image'] = $file_path.'images/'.$data['category_image'];
        $row1['category_image_thumb'] = $file_path.'images/thumbs/'.$data['category_image'];


        array_push($jsonObj,$row1);

    }

    $row['latest_channels']=$jsonObj;


    $jsonObj_2= array();


    $query2="SELECT cid,category_name,category_image FROM tbl_category WHERE status=1 ORDER BY cid DESC LIMIT 3";
    $sql2 = mysqli_query($mysqli,$query2)or die(mysql_error());

    while($data2 = mysqli_fetch_assoc($sql2))
    {

        $row2['cid'] = $data2['cid'];
        $row2['category_name'] = $data2['category_name'];
        $row2['category_image'] = $file_path.'images/'.$data2['category_image'];
        $row2['category_image_thumb'] = $file_path.'images/thumbs/'.$data2['category_image'];


        array_push($jsonObj_2,$row2);

    }

    $row['cat_list']=$jsonObj_2;

    $set['LIVETV'] = $row;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();

}
else if(isset($_GET['get_all_channels']))
{

    $jsonObj= array();

    $query="SELECT * FROM tbl_channels
		LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid
		WHERE tbl_channels.status=1 ORDER BY tbl_channels.id DESC";
    $sql = mysqli_query($mysqli,$query)or die(mysqli_error());

    while($data = mysqli_fetch_assoc($sql))
    {
        $row['id'] = $data['id'];
        $row['cat_id'] = $data['cat_id'];
        $row['channel_title'] = $data['channel_title'];
        $row['channel_url'] = $data['channel_url'];
        $row['channel_url_ios'] = $data['channel_url_ios'];
        $row['channel_thumbnail'] = $file_path.'images/'.$data['channel_thumbnail'];
        $row['channel_desc'] = $data['channel_desc'];

        $row['total_views'] = $data['total_views'];
        $row['total_rate'] = $data['total_rate'];
        $row['rate_avg'] = $data['rate_avg'];

        $row['cid'] = $data['cid'];
        $row['category_name'] = $data['category_name'];
        $row['category_image'] = $file_path.'images/'.$data['category_image'];
        $row['category_image_thumb'] = $file_path.'images/thumbs/'.$data['category_image'];


        array_push($jsonObj,$row);

    }

    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();

}
else if(isset($_GET['get_latest_channels']))
{
    $limit=API_LATEST_LIMIT;
    $jsonObj= array();

    $query="SELECT * FROM tbl_channels
		LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid
		WHERE tbl_channels.status=1 ORDER BY tbl_channels.id DESC LIMIT $limit";
    $sql = mysqli_query($mysqli,$query)or die(mysqli_error());

    while($data = mysqli_fetch_assoc($sql))
    {
        $row['id'] = $data['id'];
        $row['cat_id'] = $data['cat_id'];
        $row['channel_title'] = $data['channel_title'];
        $row['channel_url'] = $data['channel_url'];
        $row['channel_url_ios'] = $data['channel_url_ios'];
        $row['channel_thumbnail'] = $file_path.'images/'.$data['channel_thumbnail'];
        $row['channel_desc'] = $data['channel_desc'];

        $row['total_views'] = $data['total_views'];
        $row['total_rate'] = $data['total_rate'];
        $row['rate_avg'] = $data['rate_avg'];

        $row['cid'] = $data['cid'];
        $row['category_name'] = $data['category_name'];
        $row['category_image'] = $file_path.'images/'.$data['category_image'];
        $row['category_image_thumb'] = $file_path.'images/thumbs/'.$data['category_image'];


        array_push($jsonObj,$row);

    }

    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_featured_channels']))
{
    $jsonObj_2= array();

    $query_all="SELECT * FROM tbl_channels
		LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid
		WHERE tbl_channels.featured_channel=1 AND tbl_channels.status=1 ORDER BY tbl_channels.id DESC";

    $sql_all = mysqli_query($mysqli,$query_all)or die(mysqli_error());

    while($data_all = mysqli_fetch_assoc($sql_all))
    {
        $row2['id'] = $data_all['id'];
        $row2['cat_id'] = $data_all['cat_id'];
        $row2['channel_title'] = $data_all['channel_title'];
        $row2['channel_url'] = $data_all['channel_url'];
        $row['channel_url_ios'] = $data['channel_url_ios'];
        $row2['channel_thumbnail'] = $file_path.'images/'.$data_all['channel_thumbnail'];
        $row2['channel_desc'] = $data_all['channel_desc'];
        $row2['rate_avg'] = $data_all['rate_avg'];

        $row2['cid'] = $data_all['cid'];
        $row2['category_name'] = $data_all['category_name'];
        $row2['category_image'] = $file_path.'images/'.$data_all['category_image'];
        $row2['category_image_thumb'] = $file_path.'images/thumbs/'.$data_all['category_image'];



        array_push($jsonObj_2,$row2);

    }

    $set['LIVETV'] = $jsonObj_2;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_category']))
{
    $jsonObj= array();

    $cid=API_CAT_ORDER_BY;


    $query="SELECT cid,category_name,category_image FROM tbl_category WHERE status=1 ORDER BY tbl_category.".$cid."";
    $sql = mysqli_query($mysqli,$query)or die(mysql_error());

    while($data = mysqli_fetch_assoc($sql))
    {

        $row['cid'] = $data['cid'];
        $row['category_name'] = $data['category_name'];
        $row['category_image'] = $file_path.'images/'.$data['category_image'];
        $row['category_image_thumb'] = $file_path.'images/thumbs/'.$data['category_image'];


        array_push($jsonObj,$row);

    }

    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_channels_by_cat_id']))
{
    $post_order_by=API_CAT_POST_ORDER_BY;

    $cat_id=$_GET['get_channels_by_cat_id'];

    $jsonObj= array();

    $query="SELECT * FROM tbl_channels
		LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid
		WHERE tbl_channels.cat_id='".$cat_id."' AND tbl_channels.status=1 ORDER BY tbl_channels.".$post_order_by."";

    $sql = mysqli_query($mysqli,$query)or die(mysqli_error());

    while($data = mysqli_fetch_assoc($sql))
    {
        $row['id'] = $data['id'];
        $row['cat_id'] = $data['cat_id'];
        $row['channel_title'] = $data['channel_title'];
        $row['channel_url'] = $data['channel_url'];
        $row['channel_url_ios'] = $data['channel_url_ios'];
        $row['channel_thumbnail'] = $file_path.'images/'.$data['channel_thumbnail'];
        $row['channel_desc'] = $data['channel_desc'];

        $row['total_views'] = $data['total_views'];
        $row['total_rate'] = $data['total_rate'];
        $row['rate_avg'] = $data['rate_avg'];

        $row['cid'] = $data['cid'];
        $row['category_name'] = $data['category_name'];
        $row['category_image'] = $file_path.'images/'.$data['category_image'];
        $row['category_image_thumb'] = $file_path.'images/thumbs/'.$data['category_image'];


        array_push($jsonObj,$row);

    }

    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_channel']))
{
    $jsonObj = array();
    $SQL1="select * from tbl_channels";
    $result1 = mysqli_query($mysqli,$SQL1)or die(mysqli_error());

    $jsonObj= array();

    while ($row1 = mysqli_fetch_assoc($result1))
    {

        $catArr = array();
        $catArr['id'] = $row1['id'];
        $catArr['cat_id'] = $row1['cat_id'];
        $catArr['channel_type'] = $row1['channel_type'];
        $catArr['channel_title'] = $row1['channel_title'];
        $catArr['channel_url'] = $row1['channel_url'];
        $catArr['channel_type_ios'] = $row1['channel_type_ios'];
        $catArr['channel_url_ios'] = $row1['channel_url_ios'];
        $catArr['channel_thumbnail'] = $file_path.'images/'.$row1['channel_thumbnail'];
        $catArr['channel_desc'] = $row1['channel_desc'];
        $catArr['total_views'] = $row1['total_views'];
        $catArr['total_rate'] = $row1['total_rate'];
        $catArr['rate_avg'] = $row1['rate_avg'];
        $catArr['category_name'] = $row1['category_name'];

        $scheduleQuery = "select * from tbl_schedules";
        $getSchedule = mysqli_query($mysqli,$scheduleQuery);

        $subvidArr=array();
        while ($row2 = mysqli_fetch_assoc($getSchedule))
        {
            $temp = array('schedule_id' => $row2['id'], 'schedule_title' => $row2['title'] , 'schedule_time' =>$row2['time'],'schedule_date' => $row2['date']);
            $subvidArr[] = $temp;
        }
        $catArr['schedule']=$subvidArr;
        array_push($jsonObj,$catArr);
    }

    $view_qry=mysqli_query($mysqli,"UPDATE tbl_channels SET total_views = total_views + 1");



    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_search_channels']))
{
    $jsonObj= array();

    $query="SELECT * FROM tbl_channels
		LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid
		WHERE tbl_channels.status=1 AND channel_title like '%".$_GET['get_search_channels']."%' ";
    $sql = mysqli_query($mysqli,$query);

    if(mysqli_num_rows($sql)>0){

        while($data = mysqli_fetch_assoc($sql))
        {
            $row['id'] = $data['id'];
            $row['cat_id'] = $data['cat_id'];
            $row['channel_title'] = $data['channel_title'];
            $row['channel_url'] = $data['channel_url'];
            $row['channel_url_ios'] = $data['channel_url_ios'];
            $row['channel_thumbnail'] = $file_path.'images/'.$data['channel_thumbnail'];
            $row['channel_desc'] = $data['channel_desc'];

            $row['rate_avg'] = $data['rate_avg'];

            $row['cid'] = $data['cid'];
            $row['category_name'] = $data['category_name'];
            $row['category_image'] = $file_path.'images/'.$data['category_image'];
            $row['category_image_thumb'] = $file_path.'images/thumbs/'.$data['category_image'];


            array_push($jsonObj,$row);

        }

        $set['LIVETV'] = $jsonObj;

    }
    else
    {
        $set['LIVETV'][]=array('msg' => 'No Channel Found! Try Different Keyword','Success'=>'0');

    }

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_POST['post_channel_report']))
{
    $report = cleanVars(filter_input(INPUT_POST, 'report', FILTER_SANITIZE_STRING));

    if($report)
    {
        $qry1="INSERT INTO tbl_reports (`user_id`,`email`,`channel_id`,`report`) VALUES ('".$_POST['user_id']."','".$_POST['email']."','".$_POST['channel_id']."','".$report."')";

        $result1=mysqli_query($mysqli,$qry1);


        $set['LIVETV'][] = array('msg' => 'Report has been sent successfully...','success'=>'1');
    }
    else
    {
        $set['LIVETV'][] = array('msg' => 'Please add report text','success'=>'0');
    }

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

// Handles new user registrations
else if(isset($_POST['post_user_register']))
{
    if($_POST['name']!='' AND $_POST['email']!='' AND $_POST['password']!='')
    {

        $name = filter_input(INPUT_POST, 'name', FILTER_SANITIZE_STRING);
        $email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_STRING);
        $password = filter_input(INPUT_POST, 'password', FILTER_SANITIZE_STRING);
        $phone = filter_input(INPUT_POST, 'phone', FILTER_SANITIZE_STRING);

        $qry = "SELECT * FROM tbl_users WHERE email = '$email'";
        $result = mysqli_query($mysqli,$qry);
        $row = mysqli_fetch_assoc($result);

        if($row['email']!="")
        {
            $set['LIVETV'][]=array('msg' => "Email address already used!",'success'=>'0');
        }
        else
        {


            $qry1="INSERT INTO tbl_users (`user_type`,`name`,`email`,`password`,`phone`,`status`, `created_at`) VALUES ('Normal','$name','$email','".hashPassword($password)."','$phone','1','". currentDateTime() ."')";

            $result1=mysqli_query($mysqli,$qry1);

            // get newly registered user id
            $id = mysqli_insert_id($mysqli);

            $set['LIVETV'][]=array('msg' => "Register successfully...!",'success'=>'1', 'user_id' => $id);
        }


    }
    else
    {
        $set['LIVETV'][]=array('msg' => "Empty fields!",'success'=>'0');
    }

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

// Handle user login
else if(isset($_POST['post_user_login']))
{

    $email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_STRING);
    $password = filter_input(INPUT_POST, 'password', FILTER_SANITIZE_STRING);

    $qry = "SELECT * FROM tbl_users WHERE email = '$email'";
    $result = mysqli_query($mysqli,$qry);
    $num_rows = mysqli_num_rows($result);
    $row = mysqli_fetch_assoc($result);

    if ($num_rows > 0) // if user email exists in records
    {
        // verify user password
        if(password_verify($password, $row['password'])){ // if password is correct
            $set['LIVETV'][]=array('user_id' => $row['id'],'name'=>$row['name'],'success'=>'1');
        }
        else{// if password is wrong
            $set['LIVETV'][]=array('msg' =>'Login failed, password incorrect','success'=>'0');
        }
    }
    else
    { // if user email is not in records
        $set['LIVETV'][]=array('msg' =>'Login failed, Email not found in records ','success'=>'0');
    }

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}

// Get specific user profile
else if(isset($_GET['get_user_profile']))
{
    $qry = "SELECT * FROM tbl_users WHERE id = '".$_GET['get_user_profile']."'";
    $result = mysqli_query($mysqli,$qry);

    $row = mysqli_fetch_assoc($result);

    $set['LIVETV'][]=array('user_id' => $row['id'],'name'=>$row['name'],'email'=>$row['email'],'phone'=>$row['phone'],'success'=>'1');

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
// Handle user profile update
else if(isset($_POST['post_user_profile_update']))
{
    $name = cleanVars(filter_input(INPUT_POST, 'name', FILTER_SANITIZE_STRING));
    $email = cleanVars(filter_input(INPUT_POST, 'email', FILTER_SANITIZE_STRING));
    $phone = cleanVars(filter_input(INPUT_POST, 'phone', FILTER_SANITIZE_STRING));
    $user_id = cleanVars(filter_input(INPUT_POST, 'user_id', FILTER_SANITIZE_STRING));

    if($_POST['password']!=""){ // update with user password included
        $password = cleanVars($_POST['password']);
        $user_edit= "UPDATE tbl_users SET name='$name',email='$email',password='".hashPassword($password)."',phone='$phone',updated_at='". currentDateTime() ."' WHERE id = $user_id";
    }
    else{// update without password included
        $user_edit= "UPDATE tbl_users SET name='$name',email='$email',phone='$phone' WHERE id = $user_id";
    }

    $user_res = mysqli_query($mysqli,$user_edit);

    $set['LIVETV'][]=array('msg'=>'Updated','success'=>'1');

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_POST['user_forgot_pass_email']))
{
    $host = $_SERVER['HTTP_HOST'];
    preg_match("/[^\.\/]+\.[^\.\/]+$/", $host, $matches);
    $domain_name=$matches[0];

    $email = $_POST['user_forgot_pass_email'];


    $qry = "SELECT * FROM tbl_users WHERE email = '$email'";
    $result = mysqli_query($mysqli,$qry);
    $row = mysqli_fetch_assoc($result);

    if($row['email']!="")
    {
        // Create temporary password
        $tempPass = substr(sha1($email . time()), 4, 8);
        $hashed = hashPassword($tempPass);

        $query = "UPDATE tbl_users SET password = '$hashed' WHERE email = '$email'";
        $setTemp = mysqli_query($mysqli, $query);

        $to = $email;
        // subject
        $subject = '[IMPORTANT] '.APP_NAME.' Forgot Password Information';

        @include 'temp_password_email.php';

        $headers = 'MIME-Version: 1.0' . "\r\n";
        $headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
        //$headers .= 'From: '.APP_NAME.' <noreply@'.$domain_name.'>' . "\r\n";
        $headers .= 'From: '.APP_NAME.' <'.APP_FROM_EMAIL.'>' . "\r\n";
        // Mail it
        @mail($to, $subject, $message, $headers);


        $set['LIVETV'][]=array('msg' => "Password has been sent to your mail!",'success'=>'1');
    }
    else
    {

        $set['LIVETV'][]=array('msg' => "Email not found in our database!",'success'=>'0');

    }

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_POST['post_item_rate']))
{
    $ip = $_POST['user_id'];
    $post_id = $_POST['post_id'];
    $user_id = $_POST['user_id'];
    $therate = $_POST['rate'];


    $query1 = mysqli_query($mysqli,"select * from tbl_rating where post_id  = '$post_id' and user_id = '$user_id'");
    while($data1 = mysqli_fetch_assoc($query1)){
        $rate_db1[] = $data1;
    }
    if(@count($rate_db1) == 0 ){


        $qry1="INSERT INTO tbl_rating (`post_id`,`user_id`,`rate`,`ip`) VALUES ('".$post_id."','".$user_id."','".$therate."','".$ip."')";
        $result1=mysqli_query($mysqli,$qry1);

        //Total rate result

        $query = mysqli_query($mysqli,"select * from tbl_rating where post_id  = '$post_id' ");

        while($data = mysqli_fetch_assoc($query)){
            $rate_db[] = $data;
            $sum_rates[] = $data['rate'];

        }

        if(@count($rate_db)){
            $rate_times = count($rate_db);
            $sum_rates = array_sum($sum_rates);
            $rate_value = $sum_rates/$rate_times;
            $rate_bg = (($rate_value)/5)*100;
        }else{
            $rate_times = 0;
            $rate_value = 0;
            $rate_bg = 0;
        }

        $rate_avg=round($rate_value);

        $sql="update tbl_channels set total_rate=total_rate + 1,rate_avg='$rate_avg' where id='".$post_id."'";
        mysqli_query($mysqli,$sql);

        $total_rat_sql="SELECT * FROM tbl_channels WHERE id='".$post_id."'";
        $total_rat_res=mysqli_query($mysqli,$total_rat_sql);
        $total_rat_row=mysqli_fetch_assoc($total_rat_res);


        $set['LIVETV'][]=array('total_rate' =>$total_rat_row['total_rate'],'rate_avg' =>$total_rat_row['rate_avg'],'msg' => "You have succesfully rated",'success'=>'1');

    }else{


        $set['LIVETV'][]=array('msg' => "You have already rated",'success'=>'0');
    }

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_POST['post_item_comment']))
{
    $post_id = $_POST['post_id'];
    $user_id = $_POST['user_id'];
    $comment_text = $_POST['comment_text'];

    $qry1="INSERT INTO tbl_comments (`post_id`,`user_id`,`comment_text`) VALUES ('".$post_id."','".$user_id."','".$comment_text."')";
    $result1=mysqli_query($mysqli,$qry1);


    $set['LIVETV'][]=array('msg' => "Comment post successfully...",'success'=>'1');
    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_item_comments_id']))
{
    $SQL1="select * from tbl_comments
		 where post_id='".$_GET['get_item_comments_id']."'";
    $result = mysqli_query($mysqli,$SQL1)or die(mysqli_error());

    $jsonObj= array();

    while ($data = mysqli_fetch_assoc($result))
    {
        $row['id'] = $data['id'];
        $row['post_id'] = $data['post_id'];
        $row['user_name'] = get_user_info($data['user_id']);
        $row['comment_text'] = $data['comment_text'];
        $row['date'] = $data['dt_rate'];

        array_push($jsonObj,$row);

    }


    $set['LIVETV'] = $jsonObj;
    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_schedule']))
{
    $SQL1="select * from tbl_schedules";
    $result = mysqli_query($mysqli,$SQL1)or die(mysqli_error());

    $jsonObj= array();

    while ($data = mysqli_fetch_assoc($result))
    {
        $row['schedule_id'] = $data['id'];
        $row['schedule_title'] = $data['title'];
        $row['schedule_time'] = $data['time'];

        array_push($jsonObj,$row);
    }


    $set['LIVETV'] = $jsonObj;
    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['get_videos']))
{
    $jsonObj= array();

    $query="SELECT * FROM tbl_video
 		WHERE tbl_video.status='1' ORDER BY tbl_video.id DESC";

    $sql = mysqli_query($mysqli,$query)or die(mysqli_error());

    while($data = mysqli_fetch_assoc($sql))
    {
        $row['id'] = $data['id'];
        $row['video_type'] = $data['video_type'];
        $row['video_title'] = $data['video_title'];
        $row['video_url'] = $data['video_url'];
        $row['video_id'] = $data['video_id'];


        $row['video_thumbnail_b'] = $file_path.'images/'.$data['video_thumbnail'];
        $row['video_thumbnail_s'] = $file_path.'images/thumbs/'.$data['video_thumbnail'];

        $row['total_views'] = $data['total_views'];

        array_push($jsonObj,$row);

    }

    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else if(isset($_GET['video_id']))
{
    $jsonObj= array();

    $query="SELECT * FROM tbl_video
 		WHERE tbl_video.id='".$_GET['video_id']."'";

    $sql = mysqli_query($mysqli,$query)or die(mysqli_error());

    while($data = mysqli_fetch_assoc($sql))
    {

        $row['id'] = $data['id'];
        $row['video_type'] = $data['video_type'];
        $row['video_title'] = $data['video_title'];
        $row['video_url'] = $data['video_url'];
        $row['video_id'] = $data['video_id'];

        $row['video_thumbnail_b'] = $file_path.'images/'.$data['video_thumbnail'];
        $row['video_thumbnail_s'] = $file_path.'images/thumbs/'.$data['video_thumbnail'];
        $row['total_views'] = $data['total_views'];

        array_push($jsonObj,$row);

    }

    $view_qry=mysqli_query($mysqli,"UPDATE tbl_video SET total_views = total_views + 1 WHERE id = '".$_GET['video_id']."'");


    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}
else
{
    $jsonObj= array();

    $query="SELECT * FROM tbl_settings WHERE id='1'";
    $sql = mysqli_query($mysqli,$query)or die(mysqli_error());


    while($data = mysqli_fetch_assoc($sql))
    {

        $row['app_name'] = $data['app_name'];
        $row['app_logo'] = $data['app_logo'];
        $row['app_version'] = $data['app_version'];
        $row['app_author'] = $data['app_author'];
        $row['app_contact'] = $data['app_contact'];
        $row['app_email'] = $data['app_email'];
        $row['app_website'] = $data['app_website'];
        $row['app_description'] = $data['app_description'];
        $row['app_developed_by'] = $data['app_developed_by'];

        $row['app_privacy_policy'] = stripslashes($data['app_privacy_policy']);

        $row['publisher_id'] = $data['publisher_id'];
        $row['interstital_ad'] = $data['interstital_ad'];
        $row['interstital_ad_id'] = $data['interstital_ad_id'];
        $row['interstital_ad_click'] = $data['interstital_ad_click'];
        $row['banner_ad'] = $data['banner_ad'];
        $row['banner_ad_id'] = $data['banner_ad_id'];

        $row['publisher_id_ios'] = $data['publisher_id_ios'];
        $row['app_id_ios'] = $data['app_id_ios'];
        $row['interstital_ad_ios'] = $data['interstital_ad_ios'];
        $row['interstital_ad_id_ios'] = $data['interstital_ad_id_ios'];
        $row['interstital_ad_click_ios'] = $data['interstital_ad_click_ios'];
        $row['banner_ad_ios'] = $data['banner_ad_ios'];
        $row['banner_ad_id_ios'] = $data['banner_ad_id_ios'];

        array_push($jsonObj,$row);

    }

    $set['LIVETV'] = $jsonObj;

    header( 'Content-Type: application/json; charset=utf-8' );
    echo $val= str_replace('\\/', '/', json_encode($set,JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT));
    die();
}