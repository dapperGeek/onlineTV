<?php include('includes/header.php'); 

    include('includes/function.php');
	include('language/language.php');  

	
	 function get_channe_info($post_id)
	 {
	 	global $mysqli;

	 	 
	 	$query="SELECT * FROM tbl_channels		 
					WHERE tbl_channels.id='".$post_id."'";
	 	 
		$sql = mysqli_query($mysqli,$query)or die(mysqli_error());
		$row=mysqli_fetch_assoc($sql);

		return stripslashes($row['channel_title']);
	 }

	 function get_user_info($user_id)
	 {
	 	global $mysqli;

	 	 
	 	$user_qry="SELECT * FROM tbl_users where id='".$user_id."'";
		$user_result=mysqli_query($mysqli,$user_qry);
		$user_row=mysqli_fetch_assoc($user_result);

		return $user_row['name'];
	 }
	 
							$tableName="tbl_comments";		
							$targetpage = "manage_comments.php"; 	
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
							
							
						 $users_qry="SELECT * FROM tbl_comments
						 ORDER BY tbl_comments.id DESC LIMIT $start, $limit";  
							 
							$users_result=mysqli_query($mysqli,$users_qry);
							
	 
	if(isset($_GET['comment_id']))
	{
		  
		 
		Delete('tbl_comments','id='.$_GET['comment_id'].'');
		
		$_SESSION['msg']="12";
		header( "Location:manage_comments.php");
		exit;
	}
	
	function nicetime($date)
	{
	    if(empty($date)) {
	        return "No date provided";
	    }
	    
	    $periods         = array("second", "minute", "hour", "day", "week", "month", "year", "decade");
	    $lengths         = array("60","60","24","7","4.35","12","10");
	    
	    $now             = time();
	    $unix_date       = strtotime($date);
	    
	       // check validity of date
	    if(empty($unix_date)) {    
	        return "Bad date";
	    }

	    // is it future date or past date
	    if($now > $unix_date) {    
	        $difference     = $now - $unix_date;
	        $tense         = "ago";
	        
	    } else {
	        $difference     = $unix_date - $now;
	        $tense         = "from now";
	    }
	    
	    for($j = 0; $difference >= $lengths[$j] && $j < count($lengths)-1; $j++) {
	        $difference /= $lengths[$j];
	    }
	    
	    $difference = round($difference);
	    
	    if($difference != 1) {
	        $periods[$j].= "s";
	    }
	    
	    return "$difference $periods[$j] {$tense}";
	}
	
	
?>


 <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title">Manage Comments</div>
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
            <?php
					$i=0;
					while($users_row=mysqli_fetch_array($users_result))
					{
					 
			?>
            <div class="col-md-6">
			  <ul class="timeline timeline-simple">
				<li class="timeline-inverted">
				  <div class="timeline-badge danger"> 
					<img src="assets/images/photo.jpg" class="img-profile"> 
				  </div>
				  <div class="timeline-panel">
					<div class="timeline-heading"> 
						<a href="javascript:void(0)" title=""> <span class="label label-danger"><?php echo get_channe_info($users_row['post_id']);?></span> </a> 
						<span class="pull-right"> 
						<a href="manage_comments.php?comment_id=<?php echo $users_row['id'];?>" onclick="return confirm('Are you sure you want to delete this comment?');" rel="tooltip" data-placement="bottom" data-original-title="Delete"> <i class="fa fa-trash" style="color:red"></i> </a> 
						 </span> 
					</div>
					<div class="timeline-body">
					  <p><?php echo $users_row['comment_text'];?></p>
					</div>
					<hr>
					<a href="#" title=""> <small class="label label-rose"> <span><?php echo get_user_info($users_row['user_id']);?></span> </small> </a> <span class="pull-right about_time" title="about 1 hour ago"><?php echo nicetime($users_row['dt_rate']);?></span>
				</div>
				</li>
			  </ul>
			</div>
			<?php
						
						$i++;
						}
			   ?>
			 
			 
			 
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
		<div class="clearfix"></div>
      </div>
    </div>   



<?php include('includes/footer.php');?>          