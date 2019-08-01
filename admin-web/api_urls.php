<?php include("includes/header.php");

$file_path = 'http://'.$_SERVER['SERVER_NAME'] . dirname($_SERVER['REQUEST_URI']).'/';
?>
<div class="row">
      <div class="col-sm-12 col-xs-12">
     	 	<div class="card">
		        <div class="card-header">
		          Example API urls
		        </div>
       			    <div class="card-body no-padding">
         		
         			 <pre><code class="html"><b>Home</b><br><?php echo $file_path."api.php?get_home_channels"?><br><br><b>All Channels</b><br><?php echo $file_path."api.php?get_all_channels"?><br><br><b>Latest Channels</b><br><?php echo $file_path."api.php?get_latest_channels"?><br><br><b>Featured Channels</b><br><?php echo $file_path."api.php?get_featured_channels"?><br><br><b>Category List</b><br><?php echo $file_path."api.php?get_category"?><br><br><b>Channels list by Cat ID</b><br><?php echo $file_path."api.php?get_channels_by_cat_id=3"?><br><br><b>Single Channels</b><br><?php echo $file_path."api.php?get_single_channel_id=14"?><br><br><b>Search Channels</b><br><?php echo $file_path."api.php?get_search_channels=tv"?><br><br><b>Report Channels</b>(Post method: post_channel_report,user_id,email,channel_id,report)<br><?php echo $file_path."api.php"?><br><br><b>User Register</b>(Post Method: post_user_register, name,email,password,phone)<br><?php echo $file_path."api.php"?><br><br><b>User Login</b>(Post Mehtod: post_user_login, email,password)<br><?php echo $file_path."api.php"?><br><br><b>User Profile</b><br><?php echo $file_path."api.php?get_user_profile=2"?><br><br><b>User Profile Update</b>(Post method: post_user_profile_update,  user_id,name,email,password,phone)<br><?php echo $file_path."api.php"?><br><br><b>Forgot Password</b><br><?php echo $file_path."api.php?user_forgot_pass_email=john@gmail.com"?><br><br><b>Rating</b>(Post Mehtod: post_item_rate,post_id,user_id,rate)<br><?php echo $file_path."api.php"?><br><br><b>User Comment</b>(Post Mehtod: post_item_comment, post_id,user_id,comment_text)<br><?php echo $file_path."api.php"?><br><br><b>Get Comment</b><br><?php echo $file_path."api.php?get_item_comments_id=15"?><br><br><b>All Videos</b><br><?php echo $file_path."api.php?get_videos"?><br><br><b>Single Video</b><br><?php echo $file_path."api.php?video_id=1"?><br><br><b>App Details</b><br><?php echo $file_path."api.php"?></code></pre>
       		
       				</div>
          	</div>
        </div>
</div>
    <br/>
    <div class="clearfix"></div>
        
<?php include("includes/footer.php");?>       
