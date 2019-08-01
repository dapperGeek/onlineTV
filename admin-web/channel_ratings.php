<?php include("includes/header.php");

	require("includes/function.php");
	require("language/language.php");

	require_once("thumbnail_images.class.php");
	
	
	  $viv_qry="SELECT * FROM  tbl_channels WHERE id='".$_GET['channel_id']."'";
    $viv_res=mysqli_query($mysqli,$viv_qry);
    $viv_row=mysqli_fetch_assoc($viv_res);
	 	
	 function rate_count($channel_id,$rate_number)
   {
      global $mysqli;

      $qry_rating="SELECT COUNT(*) as num FROM tbl_rating WHERE post_id='".$channel_id."' AND rate='".$rate_number."'";
      $total_rate = mysqli_fetch_array(mysqli_query($mysqli,$qry_rating));
      $total_rate = $total_rate['num'];

      return $total_rate;
   }
	 
?>
   
               <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title"><?php echo $viv_row['channel_title'];?> Ratings</div>
            </div>      
          </div> 
      <div class="clearfix"></div>  
          <div class="card-body">
            <div class="row">
          <div class="col-md-5">
        <div class="card card-content mr_top0">
          <div class="review_block">
             <span><?php echo $viv_row['channel_title'];?></span>
              
             <?php if($viv_row['channel_thumbnail']) {?>
                              <img src="images/<?php echo $viv_row['channel_thumbnail'];?>" class="thumbnail"/>
              <?php }else{ ?>
               <img type="image" src="assets/images/add-image.png" alt="add image" class="thumbnail" /> 
              <?php }?>
           </div>                           
           
        </div>
        </div>  
        <div class="col-md-6">
        <div class="card" style="margin-top:0px;">
          <div class="status-bar"></div>
          <div class="action-bar"> <a href="#" class="zmdi zmdi-star"></a>
          <h3 style="padding-left:20px;margin-bottom:20px;"><b>Rating</b></h3>
          </div>
          <div class="list-group lg-alt lg-even-black">
          <table width="100%">
            <tbody>
            <tr>
              <td colspan="3" style="padding:15px;">
                <div style="float:left;">
                  
                  <img src="assets/images/<?php if($viv_row['rate_avg']>=4.5){?>star.png<?php }else{?>star_e.png<?php }?>" style="height:50px;width:50px"> 
                  <img src="assets/images/<?php if($viv_row['rate_avg']>=3.5){?>star.png<?php }else{?>star_e.png<?php }?>" style="height:50px;width:50px">
                  <img src="assets/images/<?php if($viv_row['rate_avg']>=2.5){?>star.png<?php }else{?>star_e.png<?php }?>" style="height:50px;width:50px">
                  <img src="assets/images/<?php if($viv_row['rate_avg']>=1.5){?>star.png<?php }else{?>star_e.png<?php }?>" style="height:50px;width:50px">
                  <img src="assets/images/<?php if($viv_row['rate_avg']>=1){?>star.png<?php }else{?>star_e.png<?php }?>" style="height:50px;width:50px">
                </div>
                <span style="height:50px;display:inline-block;font-size:65pt;font-weight:bold;padding-left:20px;line-height:40px;"><?php echo $viv_row['rate_avg'];?></span></td>
            </tr>
            <tr>
              <td width="50%" align="right" style="padding:5px;"><img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"></td>
              <td width="30px" align="center"><?php echo rate_count($_GET['channel_id'],5);?></td>
              <td align="left" style="padding:10px"><span style="display:block;height:15px;background-color:#ea1f62;width:0%"></span></td>
            </tr>
            <tr>
              <td width="50%" align="right" style="padding:5px;"><img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"></td>
              <td width="30px" align="center"><?php echo rate_count($_GET['channel_id'],4);?></td>
              <td align="left" style="padding:10px"><span style="display:block;height:15px;background-color:#ea1f62;width:0%"></span></td>
            </tr>
            <tr>
              <td width="50%" align="right" style="padding:5px;"><img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"></td>
              <td width="30px" align="center"><?php echo rate_count($_GET['channel_id'],3);?></td>
              <td align="left" style="padding:10px"><span style="display:block;height:15px;background-color:#ea1f62;width:0%"></span></td>
            </tr>
            <tr>
              <td width="50%" align="right" style="padding:5px;"><img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"></td>
              <td width="30px" align="center"><?php echo rate_count($_GET['channel_id'],2);?></td>
              <td align="left" style="padding:10px"><span style="display:block;height:15px;background-color:#ea1f62;width:0%"></span></td>
            </tr>
            <tr>
              <td width="50%" align="right" style="padding:5px;"><img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star_e.png" style="height:30px;width:30px"> <img src="assets/images/star.png" style="height:30px;width:30px"></td>
              <td width="30px" align="center"><?php echo rate_count($_GET['channel_id'],1);?></td>
              <td align="left" style="padding:10px"><span style="display:block;height:15px;background-color:#ea1f62;width:0%"></span></td>
            </tr>
            </tbody>
          </table>
          </div>
        </div>                     
        </div>
      </div>
      <div class="clearfix"></div>
          </div>
    
        </div>
       </div>
    </div>    
        
<?php include("includes/footer.php");?>       
