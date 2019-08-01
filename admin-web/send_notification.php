<?php include("includes/header.php");

  require("includes/function.php");
  require("language/language.php");

  //'filters' => array(array('Area' => '=', 'value' => 'ALL')),

  function get_cat_name($cat_id)
  { 
    global $mysqli;

    $cat_qry="SELECT * FROM tbl_category WHERE cid='".$cat_id."'";
    $cat_result=mysqli_query($mysqli,$cat_qry); 
    $cat_row=mysqli_fetch_assoc($cat_result); 
     
    return $cat_row['category_name'];

  }
 
  
  $data_qry="SELECT * FROM tbl_channels ORDER BY channel_title";
  $data_result=mysqli_query($mysqli,$data_qry); 

  if(isset($_POST['submit']))
  {

     if($_POST['external_link']!="")
     {
        $external_link = $_POST['external_link'];
     }
     else
     {
        $external_link = false;
     } 

     if($_POST['cat_id']!=0)
     {

        $cat_name=get_cat_name($_POST['cat_id']);
         
     }
     else
     {
        $cat_name='';
     }

      

    if($_FILES['big_picture']['name']!="")
    {   

        $big_picture=rand(0,99999)."_".$_FILES['big_picture']['name'];
        $tpath2='images/'.$big_picture;
        move_uploaded_file($_FILES["big_picture"]["tmp_name"], $tpath2);

        $file_path = 'http://'.$_SERVER['SERVER_NAME'] . dirname($_SERVER['REQUEST_URI']).'/images/'.$big_picture;
          
        $content = array(
                         "en" => $_POST['notification_msg']                                                 
                         );

        $fields = array(
                        'app_id' => ONESIGNAL_APP_ID,
                        'included_segments' => array('All'),                                            
                        'data' => array("foo" => "bar","channel_id"=>$_POST['channel_id'],"external_link"=>$external_link),
                        'headings'=> array("en" => $_POST['notification_title']),
                        'contents' => $content,
                        'big_picture' =>$file_path,
                        'ios_attachments' => array(
                             'id' => $file_path,
                        ),                     
                        );

        $fields = json_encode($fields);
        print("\nJSON sent:\n");
        print($fields);

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "https://onesignal.com/api/v1/notifications");
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json; charset=utf-8',
                                                   'Authorization: Basic '.ONESIGNAL_REST_KEY));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_HEADER, FALSE);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);

        $response = curl_exec($ch);
        curl_close($ch);

        
    }
    else
    {

 
        $content = array(
                         "en" => $_POST['notification_msg']
                          );

        $fields = array(
                        'app_id' => ONESIGNAL_APP_ID,
                        'included_segments' => array('All'),                                      
                        'data' => array("foo" => "bar","channel_id"=>$_POST['channel_id'],"external_link"=>$external_link),
                        'headings'=> array("en" => $_POST['notification_title']),
                        'contents' => $content
                        );

        $fields = json_encode($fields);
        print("\nJSON sent:\n");
        print($fields);

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, "https://onesignal.com/api/v1/notifications");
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json; charset=utf-8',
                                                   'Authorization: Basic '.ONESIGNAL_REST_KEY));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
        curl_setopt($ch, CURLOPT_HEADER, FALSE);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);

        $response = curl_exec($ch);
        
        
        
        curl_close($ch);


    }
        
        $_SESSION['msg']="17";
     
        header( "Location:send_notification.php");
        exit; 
     
     
  }
  
   

?>
<div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title">Send Notification</div>
            </div>
          </div>
          <div class="clearfix"></div>
          <div class="row mrg-top">
            <div class="col-md-12">
               
              <div class="col-md-12 col-sm-12">
                <?php if(isset($_SESSION['msg'])){?> 
                 <div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                  <?php echo $client_lang[$_SESSION['msg']] ; ?></a> </div>
                <?php unset($_SESSION['msg']);}?> 
              </div>
            </div>
          </div>
          <div class="card-body mrg_bottom"> 
            <form action="" name="addeditcategory" method="post" class="form form-horizontal" enctype="multipart/form-data">
               
              <div class="section">
                <div class="section-body">

                  <div class="form-group">
                    <label class="col-md-3 control-label">Title :-</label>
                    <div class="col-md-6">
                      <input type="text" name="notification_title" id="notification_title" class="form-control" value="" placeholder="" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-md-3 control-label">Message :-</label>
                    <div class="col-md-6">
                        <textarea name="notification_msg" id="notification_msg" class="form-control" required></textarea>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-md-3 control-label">Image :-<br/>(Optional)<p class="control-label-help">(Recommended resolution: 600x293 or 650x317 or 700x342 or 750x366)</p></label>

                    <div class="col-md-6">
                      <div class="fileupload_block">
                         <input type="file" name="big_picture" value="" id="fileupload">
                         <div class="fileupload_img"><img type="image" src="assets/images/add-image.png" alt="category image" /></div>    
                      </div>
                    </div>
                  </div>
                  <div class="col-md-9 mrg_bottom link_block">
                  <div class="form-group">
                    <label class="col-md-4 control-label">Channel :-<br/>(Optional)
                    <p class="control-label-help">To directly open single channel when click on notification</p></label>
                    <div class="col-md-8">
                      <select name="channel_id" id="channel_id" class="select2" required>
                        <option value="0">--Select Channel--</option>
                        <?php
                            while($data_row=mysqli_fetch_array($data_result))
                            {
                        ?>                       
                        <option value="<?php echo $data_row['id'];?>"><?php echo $data_row['channel_title'];?></option>                           
                        <?php
                          }
                        ?>
                      </select>
                    </div>
                  </div> 
                  <div class="or_link_item">
                  <h2>OR</h2>
                  </div>
                  <div class="form-group">
                    <label class="col-md-4 control-label">External Link :-<br/>(Optional)</label>
                    <div class="col-md-8">
                      <input type="text" name="external_link" id="external_link" class="form-control" value="" placeholder="http://www.viaviweb.com">
                    </div>
                  </div>   
                </div>   
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="submit" class="btn btn-primary">Send</button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
        
<?php include("includes/footer.php");?>       
