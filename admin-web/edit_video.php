<?php include("includes/header.php");

	require("includes/function.php");
	require("language/language.php");

	require_once("thumbnail_images.class.php");

  //Get Category
	$cat_qry="SELECT * FROM tbl_category ORDER BY category_name";
	$cat_result=mysqli_query($mysqli,$cat_qry); 

  $qry="SELECT * FROM tbl_video where id='".$_GET['video_id']."'";
  $result=mysqli_query($mysqli,$qry);
  $row=mysqli_fetch_assoc($result);
	
	if(isset($_POST['submit']))
	{
        
        
        if ($_POST['video_type']=='youtube')
        {

              $video_url=$_POST['video_url'];

              $youtube_video_url = addslashes($_POST['video_url']);
              parse_str( parse_url( $youtube_video_url, PHP_URL_QUERY ), $array_of_vars );
              $video_id=  $array_of_vars['v'];

              if($_FILES['video_thumbnail']['name']!="")
            { 
              $video_thumbnail=rand(0,99999)."_".$_FILES['video_thumbnail']['name'];
       
              //Main Image
              $tpath1='images/'.$video_thumbnail;        
              $pic1=compress_image($_FILES["video_thumbnail"]["tmp_name"], $tpath1, 80);
         
              //Thumb Image 
              $thumbpath='images/thumbs/'.$video_thumbnail;   
              $thumb_pic1=create_thumb_image($tpath1,$thumbpath,'200','200');   
            }
            else
            {
              $video_thumbnail=$_POST['video_thumbnail_name'];
            }  

        }
 
        
        if ($_POST['video_type']=='server_url')
        {
              $video_url=$_POST['video_url'];

            if($_FILES['video_thumbnail']['name']!="")
            { 
              $video_thumbnail=rand(0,99999)."_".$_FILES['video_thumbnail']['name'];
       
              //Main Image
              $tpath1='images/'.$video_thumbnail;        
              $pic1=compress_image($_FILES["video_thumbnail"]["tmp_name"], $tpath1, 80);
         
              //Thumb Image 
              $thumbpath='images/thumbs/'.$video_thumbnail;   
              $thumb_pic1=create_thumb_image($tpath1,$thumbpath,'200','200');   
            }
            else
            {
              $video_thumbnail=$_POST['video_thumbnail_name'];
            }


              $video_id='';

        } 

        if ($_POST['video_type']=='local')
        {
            if($_POST['video_file_name']!="")
            {
                $file_path = 'http://'.$_SERVER['SERVER_NAME'] . dirname($_SERVER['REQUEST_URI']).'/uploads/';
              
                $video_url=$file_path.$_POST['video_file_name'];

            }
            else
            {
                $video_url=$_POST['video_url'];
            }
            
            if($_FILES['video_thumbnail']['name']!="")
            {  
              $video_thumbnail=rand(0,99999)."_".$_FILES['video_thumbnail']['name'];
       
              //Main Image
              $tpath1='images/'.$video_thumbnail;        
              $pic1=compress_image($_FILES["video_thumbnail"]["tmp_name"], $tpath1, 80);
         
              //Thumb Image 
              $thumbpath='images/thumbs/'.$video_thumbnail;   
              $thumb_pic1=create_thumb_image($tpath1,$thumbpath,'200','200');   
            }
            else
            {
              $video_thumbnail=$_POST['video_thumbnail_name'];
            }

              $video_id='';
        } 


          
        $data = array( 
           'video_type'  =>  $_POST['video_type'],
          'video_title'  =>  $_POST['video_title'],
          'video_url'  =>  $video_url,
          'video_id'  =>  $video_id,
          'video_thumbnail'  =>  $video_thumbnail           
          );
  		 		 
    $qry=Update('tbl_video', $data, "WHERE id = '".$_POST['video_id']."'");

         
		$_SESSION['msg']="11"; 
		header( "Location:edit_video.php?video_id=".$_POST['video_id']);
		exit;	

		 
	}
	
	  
?>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script>
            $(function () {
                $('#btn').click(function () {
                    $('.myprogress').css('width', '0');
                    $('.msg').text('');
                    var video_local = $('#video_local').val();
                    if (video_local == '') {
                        alert('Please enter file name and select file');
                        return;
                    }
                    var formData = new FormData();
                    formData.append('video_local', $('#video_local')[0].files[0]);
                    $('#btn').attr('disabled', 'disabled');
                     $('.msg').text('Uploading in progress...');
                    $.ajax({
                        url: 'uploadscript.php',
                        data: formData,
                        processData: false,
                        contentType: false,
                        type: 'POST',
                        // this part is progress bar
                        xhr: function () {
                            var xhr = new window.XMLHttpRequest();
                            xhr.upload.addEventListener("progress", function (evt) {
                                if (evt.lengthComputable) {
                                    var percentComplete = evt.loaded / evt.total;
                                    percentComplete = parseInt(percentComplete * 100);
                                    $('.myprogress').text(percentComplete + '%');
                                    $('.myprogress').css('width', percentComplete + '%');
                                }
                            }, false);
                            return xhr;
                        },
                        success: function (data) {
                         
                            $('#video_file_name').val(data);
                            $('.msg').text("File uploaded successfully!!");
                            $('#btn').removeAttr('disabled');
                        }
                    });
                });
            });
        </script>
<script type="text/javascript">
$(document).ready(function(e) {
           $("#video_type").change(function(){
          
           var type=$("#video_type").val();
              
              if(type=="youtube" || type=="server_url")
              { 
                //alert(type);
                $("#video_url_display").show();
                $("#video_local_display").hide();
               } 
              else
              {                 
                $("#video_url_display").hide();               
                $("#video_local_display").show();
 
              }    
              
         });
        });
</script>
<div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="page_title_block">
            <div class="col-md-5 col-xs-12">
              <div class="page_title">Edit Video</div>
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
            <form action="" name="edit_form" method="post" class="form form-horizontal" enctype="multipart/form-data">
              <input  type="hidden" name="video_id" value="<?php echo $_GET['video_id'];?>" />

              <div class="section">
                <div class="section-body">
                    
                  <div class="form-group">
                    <label class="col-md-3 control-label">Video Title :-</label>
                    <div class="col-md-6">
                      <input type="text" name="video_title" id="video_title" value="<?php echo $row['video_title']?>" class="form-control" required>
                    </div>
                  </div>
                    
                  <div class="form-group">
                    <label class="col-md-3 control-label">Video Type :-</label>
                    <div class="col-md-6">                       
                      <select name="video_type" id="video_type" style="width:280px; height:25px;" class="select2" required>
                            <option value="">--Select Category--</option>
                            <option value="youtube" <?php if($row['video_type']=='youtube'){?>selected<?php }?>>Youtube</option>
                             
                            <option value="server_url" <?php if($row['video_type']=='server_url'){?>selected<?php }?>>From Server</option>
                            <option value="local" <?php if($row['video_type']=='local'){?>selected<?php }?>>From Local</option>
                      </select>
                    </div>
                  </div>
                  <div id="video_url_display" class="form-group" <?php if($row['video_type']=='local'){?>style="display:none;"<?php }else{?>style="display:block;"<?php }?>>
                    <label class="col-md-3 control-label">Video URL :-</label>
                    <div class="col-md-6">
                      <input type="text" name="video_url" id="video_url" value="<?php echo $row['video_url']?>" class="form-control">
                    </div>
                  </div>
                  <div id="video_local_display" class="form-group" <?php if($row['video_type']=='local'){?>style="display:block;"<?php }else{?>style="display:none;"<?php }?>>
                    <label class="col-md-3 control-label">Video Upload :-</label>
                    <div class="col-md-6">
                    
                    <input type="hidden" name="video_file_name" id="video_file_name" value="" class="form-control">
                      <input type="file" name="video_local" id="video_local" value="" class="form-control">
                      <div><label class="control-label">Current URL :-</label><?php echo $row['video_url']?></div><br>
                      <div class="progress">
                            <div class="progress-bar progress-bar-success myprogress" role="progressbar" style="width:0%">0%</div>
                        </div>

                        <div class="msg"></div>
                        <input type="button" id="btn" class="btn-success" value="Upload" />
                    </div>
                  </div><br>
                  <div id="thumbnail" class="form-group">
                    <label class="col-md-3 control-label">Thumbnail Image:-</label>
                    <div class="col-md-6">
                      <div class="fileupload_block">
                        <input type="file" name="video_thumbnail" value="" id="fileupload">
                       <?php if(isset($_GET['video_id']) and $row['video_thumbnail']!="") {?>
                       <input type="hidden" name="video_thumbnail_name" id="video_thumbnail_name" value="<?php echo $row['video_thumbnail'];?>" class="form-control">
                            
                           <?php }?>
                            <div class="fileupload_img"><img type="image" src="assets/images/add-image.png" alt="category image" /></div>
                          
                      </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-md-3 control-label">&nbsp; </label>
                    <div class="col-md-6">
                        <?php if($row['video_thumbnail']!="") {?>
                            <div class="block_wallpaper"><img src="images/<?php echo $row['video_thumbnail'];?>" alt="category image" /></div>
                          <?php } ?>
                    </div>
                  </div><br>
                   
                  <div class="form-group">
                    <div class="col-md-9 col-md-offset-3">
                      <button type="submit" name="submit" class="btn btn-primary">Save</button>
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
