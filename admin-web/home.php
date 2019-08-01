<?php include("includes/header.php");

$qry_cat="SELECT COUNT(*) as num FROM tbl_category";
$total_category= mysqli_fetch_array(mysqli_query($mysqli,$qry_cat));
$total_category = $total_category['num'];

$qry_channels="SELECT COUNT(*) as num FROM tbl_channels";
$total_channels = mysqli_fetch_array(mysqli_query($mysqli,$qry_channels));
$total_channels = $total_channels['num'];

$qry_comments="SELECT COUNT(*) as num FROM tbl_comments";
$total_comments = mysqli_fetch_array(mysqli_query($mysqli,$qry_comments));
$total_comments = $total_comments['num'];

$qry_reports="SELECT COUNT(*) as num FROM tbl_reports";
$total_reports = mysqli_fetch_array(mysqli_query($mysqli,$qry_reports));
$total_reports = $total_reports['num'];


$qry_video="SELECT COUNT(*) as num FROM tbl_video";
$total_videos = mysqli_fetch_array(mysqli_query($mysqli,$qry_video));
$total_videos = $total_videos['num'];

$qry_users="SELECT COUNT(*) as num FROM tbl_users";
$total_users = mysqli_fetch_array(mysqli_query($mysqli,$qry_users));
$total_users = $total_users['num'];

?>       


        <div class="btn-floating" id="help-actions">
      <div class="btn-bg"></div>
      <button type="button" class="btn btn-default btn-toggle" data-toggle="toggle" data-target="#help-actions"> <i class="icon fa fa-plus"></i> <span class="help-text">Shortcut</span> </button>
      <div class="toggle-content">
        <ul class="actions">
          <li><a href="http://www.viaviweb.com" target="_blank">Website</a></li>
           <li><a href="https://codecanyon.net/user/viaviwebtech?ref=viaviwebtech" target="_blank">About</a></li>
        </ul>
      </div>
    </div>
    <div class="row">
      <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12"> <a href="manage_category.php" class="card card-banner card-green-light">
        <div class="card-body"> <i class="icon fa fa-sitemap fa-4x"></i>
          <div class="content">
            <div class="title">Categories</div>
            <div class="value"><span class="sign"></span><?php echo $total_category;?></div>
          </div>
        </div>
        </a> </div>
      <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12"> <a href="manage_channels.php" class="card card-banner card-blue-light">
        <div class="card-body"> <i class="icon fa fa-tv fa-4x"></i>
          <div class="content">
            <div class="title">Channels</div>
            <div class="value"><span class="sign"></span><?php echo $total_channels;?></div>
          </div>
        </div>
        </a> </div>

       <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12"> <a href="manage_videos.php" class="card card-banner card-yellow-light">
        <div class="card-body"> <i class="icon fa fa-film fa-4x"></i>
          <div class="content">
            <div class="title">Videos</div>
            <div class="value"><span class="sign"></span><?php echo $total_videos;?></div>
          </div>
        </div>
        </a> 
      </div> 
      <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12"> <a href="manage_comments.php" class="card card-banner card-alicerose-light">
        <div class="card-body"> <i class="icon fa fa-comments fa-4x"></i>
          <div class="content">
            <div class="title">Comments</div>
            <div class="value"><span class="sign"></span><?php echo $total_comments;?></div>
          </div>
        </div>
        </a> </div>
      <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12"> <a href="manage_reports.php" class="card card-banner card-pink-light">
        <div class="card-body"> <i class="icon fa fa-bug fa-4x"></i>
          <div class="content">
            <div class="title">Reports</div>
            <div class="value"><span class="sign"></span><?php echo $total_reports;?></div>
          </div>
        </div>
        </a> </div>  
      <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12"> <a href="manage_users.php" class="card card-banner card-orange-light">
        <div class="card-body"> <i class="icon fa fa-users fa-4x"></i>
          <div class="content">
            <div class="title">Users</div>
            <div class="value"><span class="sign"></span><?php echo $total_users;?></div>
          </div>
        </div>
        </a> </div>
    </div>

        
<?php include("includes/footer.php");?>       
