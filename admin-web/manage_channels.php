<?php include("includes/header.php");

	require("includes/function.php");
	require("language/language.php");
	 
if(isset($_POST["search"]))
{
	$channel_name=$_POST['search_value']; 
	
  $qry="SELECT * FROM tbl_channels,tbl_category WHERE cat_id=cid and channel_title LIKE '%".$channel_name."%' ORDER BY tbl_channels.id DESC"; 
	 
}
else
{ 

  $tableName="tbl_channels";    
  $targetpage = "manage_channels.php";  
  $limit = 12; 
  
  $query = "SELECT COUNT(*) as num FROM $tableName";
  $total_pages = mysqli_fetch_array(mysqli_query($mysqli,$query));
  $total_pages = $total_pages['num'];
  
  $stages = 3;
  $page=0;
  if(isset($_GET['page'])){
  $page = mysqli_real_escape_string($mysqli,$_GET['page']);
  }
  if($page){
    $start = ($page - 1) * $limit; 
  }else{
    $start = 0; 
    } 

  $qry="SELECT tbl_channels.*,tbl_category.category_name FROM tbl_channels
LEFT JOIN tbl_category ON tbl_channels.cat_id= tbl_category.cid ORDER BY tbl_channels.id DESC LIMIT $start, $limit";	  
}
	$result=mysqli_query($mysqli,$qry);
 
	
if(isset($_GET['channel_id']))
{
	$img_res=mysqli_query($mysqli,'SELECT * FROM tbl_channels WHERE id=\''.$_GET['channel_id'].'\'');
	$img_res_row=mysqli_fetch_assoc($img_res);


	if($img_res_row['channel_thumbnail']!="")
    {
    	unlink('images/'.$img_res_row['channel_thumbnail']);
	  	unlink('images/thumbs/'.$img_res_row['channel_thumbnail']);

	}

	Delete('tbl_channels','id='.$_GET['channel_id'].'');

	$_SESSION['msg']="12";
	header( "Location:manage_channels.php");
	exit;
	 
	
}	

//Featured Channel
if(isset($_GET['featured_deactive_id']))
{
  $data = array('featured_channel'  =>  '0');
  
  $edit_status=Update('tbl_channels', $data, "WHERE id = '".$_GET['featured_deactive_id']."'");
  
   $_SESSION['msg']="16";
   header( "Location:manage_channels.php");
   exit;
}
if(isset($_GET['featured_active_id']))
{
  $data = array('featured_channel'  =>  '1');
  
  $edit_status=Update('tbl_channels', $data, "WHERE id = '".$_GET['featured_active_id']."'");
  
  $_SESSION['msg']="15";
   header( "Location:manage_channels.php");
   exit;
}

//Active and Deactive status
if(isset($_GET['status_deactive_id']))
{
  $data = array('status'  =>  '0');
  
  $edit_status=Update('tbl_channels', $data, "WHERE id = '".$_GET['status_deactive_id']."'");
  
   $_SESSION['msg']="14";
   header( "Location:manage_channels.php");
   exit;
}
if(isset($_GET['status_active_id']))
{
  $data = array('status'  =>  '1');
  
  $edit_status=Update('tbl_channels', $data, "WHERE id = '".$_GET['status_active_id']."'");
  
  $_SESSION['msg']="13";
   header( "Location:manage_channels.php");
   exit;
}

//Featured Channel
if(isset($_GET['slider_deactive_id']))
{
  $data = array('slider_channel'  =>  '0');
  
  $edit_status=Update('tbl_channels', $data, "WHERE id = '".$_GET['slider_deactive_id']."'");
  
   $_SESSION['msg']="14";
   header( "Location:manage_channels.php");
   exit;
}
if(isset($_GET['slider_active_id']))
{
  $data = array('slider_channel'  =>  '1');
  
  $edit_status=Update('tbl_channels', $data, "WHERE id = '".$_GET['slider_active_id']."'");
  
  $_SESSION['msg']="13";
   header( "Location:manage_channels.php");
   exit;
}
	 
?>
    <div class="row">
      <div class="col-xs-12">
        <div class="card mrg_bottom">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title">Manage Channels</div>
            </div>
            <div class="col-md-7 col-xs-12">              
                  <div class="search_list">
                    <div class="search_block">
                      <form  method="post" action="">
                        <input class="form-control input-sm" placeholder="Search..." aria-controls="DataTables_Table_0" type="search" name="search_value" required>
                        <button type="submit" name="search" class="btn-search"><i class="fa fa-search"></i></button>
                      </form>  
                    </div>
<!--                    <div class="add_btn_primary"> <a href="add_channel.php?add=yes">Add Channel</a> </div>-->
                  </div>
                  
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
          <div class="col-md-12 mrg-top">
            <div class="row">
              <?php 
            $i=0;
            while($row=mysqli_fetch_array($result))
            {         
        ?>
              <div class="col-lg-3 col-sm-6 col-xs-12">
                <div class="block_wallpaper">
                  <div class="wall_category_block">
                    <h2><?php echo $row['category_name'];?></h2>  
                                
                  </div>
                  <div class="wall_image_title">
                     <p><?php echo $row['channel_title'];?></p>
                    <ul>
                      <li><a href="javascript:void(0)" data-toggle="tooltip" data-tooltip="<?php echo $row['total_views'];?> Views"><i class="fa fa-eye"></i></a></li>                      
                      
                      <li>
                        <?php if($row['slider_channel']!="0"){?>
                         <a href="manage_channels.php?slider_deactive_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="Slider"><div style="color:green;"><i class="fa fa-sliders"></i></div></a> 
                      <?php }else{?>
                         <a href="manage_channels.php?slider_active_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="Set Slider"><i class="fa fa-sliders"></i></a> 
                      <?php }?>
                      </li>

                      <?php if($row['featured_channel']!="0"){?>
                        <li><a href="manage_channels.php?featured_deactive_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="Featured"><div style="color:green;"><i class="fa fa-circle"></i></div></a></li>
                      <?php }else{?>
                        <li><a href="manage_channels.php?featured_active_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="Set Featured"><i class="fa fa-circle"></i></a></li>
                      <?php }?> 
                      
                      <li><a href="channel_ratings.php?channel_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="View Rates"><i class="fa fa-star"></i></a></li>
                    </ul>
                    <ul>
                        
                      <li><a href="edit_channel.php?channel_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="Edit"><i class="fa fa-edit"></i></a></li>
                      <li><a href="?channel_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="Delete" onclick="return confirm('Are you sure you want to delete this channel?');"><i class="fa fa-trash"></i></a></li>

                      <?php if($row['status']!="0"){?>
                      <li><div class="row toggle_btn"><a href="manage_channels.php?status_deactive_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="ENABLE"><img src="assets/images/btn_enabled.png" alt="wallpaper_1" /></a></div></li>

                      <?php }else{?>
                      
                      <li><div class="row toggle_btn"><a href="manage_channels.php?status_active_id=<?php echo $row['id'];?>" data-toggle="tooltip" data-tooltip="DISABLE"><img src="assets/images/btn_disabled.png" alt="wallpaper_1" /></a></div></li>
                  
                      <?php }?>
                    <ul>  
                  </div>
                  <span><img src="images/<?php echo $row['channel_thumbnail'];?>" /></span>
                </div>
              </div>
          <?php
            
            $i++;
              }
        ?>     
         
       
      </div>
          </div>
          <div class="col-md-12 col-xs-12">
            <div class="pagination_item_block">
              <nav>
              	<?php if(!isset($_POST["search"])){ include("pagination.php");}?>                 
              </nav>
            </div>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>
    </div>            
               
        
<?php include("includes/footer.php");?>       
