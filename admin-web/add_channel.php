<?php include("includes/header.php");

require("includes/function.php");
require("language/language.php");

require_once("thumbnail_images.class.php");


//Get all Category
$qry="SELECT * FROM tbl_category";
$result=mysqli_query($mysqli,$qry);

if(isset($_POST['submit']) and isset($_GET['add']))
{

    $thumbnail=rand(0,99999)."_".$_FILES['thumbnail']['name'];
    $pic1=$_FILES['thumbnail']['tmp_name'];


    $tpath1='images/'.$thumbnail;
    copy($pic1,$tpath1);

    $thumbpath='images/thumbs/'.$thumbnail;

    $obj_img = new thumbnail_images();
    $obj_img->PathImgOld = $tpath1;
    $obj_img->PathImgNew =$thumbpath;
    $obj_img->NewWidth = 300;
    $obj_img->NewHeight = 300;
    if (!$obj_img->create_thumbnail_images())
    {
        echo $_SESSION['msg']="Thumbnail not created... please upload image again";
        exit;
    }

    $data = array(
        'cat_id'  =>  $_POST['category_id'],
        'channel_type'  =>  $_POST['channel_type'],
        'channel_title'  =>  $_POST['channel_title'],
        'premium'  =>  $_POST['premium'],
        'channel_url'  =>  $_POST['channel_url'],
        'channel_type_ios'  =>  $_POST['channel_type_ios'],
        'channel_url_ios'  =>  $_POST['channel_url_ios'],
        'channel_desc'  =>  addslashes($_POST['channel_desc']),
        'channel_thumbnail'  =>  $thumbnail
    );

    $qry = Insert('tbl_channels',$data);

    $_SESSION['msg']="10";
    header( "Location:manage_channels.php");
    exit;

}

?>
<script src="assets/js/add_channel.js" type="text/javascript"></script>


<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="page_title_block">
                <div class="col-md-5 col-xs-12">
                    <div class="page_title">Add Channel</div>
                </div>
            </div>
            <div class="clearfix"></div>
            <div class="card-body mrg_bottom">
                <form class="form form-horizontal" action="" method="post"  enctype="multipart/form-data" onsubmit="return checkValidation(this);">
                    <div class="section">
                        <div class="section-body">
                            <div class="form-group">
                                <label class="col-md-3 control-label">Select Category :-</label>
                                <div class="col-md-6">
                                    <select name="category_id" id="category_id" class="select2">
                                        <option value="">--Select Category--</option>
                                        <?php
                                        while($row=mysqli_fetch_array($result))
                                        {
                                            ?>

                                            <option value="<?php echo $row['cid'];?>"><?php echo $row['category_name'];?></option>

                                            <?php
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">Channel Title :-</label>
                                <div class="col-md-6">
                                    <input type="text" name="channel_title" id="channel_title" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">Premium Channel :-</label>
                                <div class="col-md-6">
                                    <label><input type="checkbox"  value="1" name="premium" id="premium" class="form-control">Tick for premium channels</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">Android Type :-</label>
                                <div class="col-md-6">
                                    <select name="channel_type" id="channel_type" class="select2">
                                        <option value="live_url">Live URL</option>
                                        <option value="youtube">YouTube</option>

                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">Android Channel Url :-</label>
                                <div class="col-md-6">
                                    <input type="text" name="channel_url" id="channel_url" class="form-control">
                                </div>
                            </div>
                            <div class="or_link_item">
                                <h2>OR</h2>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">iOS Type :-</label>
                                <div class="col-md-6">
                                    <select name="channel_type_ios" id="channel_type_ios" class="select2">
                                        <option value="live_url">Live URL</option>
                                        <option value="youtube">YouTube</option>

                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">iOS Channel Url :-
                                    <p class="control-label-help">(M3u8,MP4)</p>
                                </label>
                                <div class="col-md-6">
                                    <input type="text" name="channel_url_ios" id="channel_url_ios" class="form-control">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-3 control-label">Select Channel Thumbnail :-</label>
                                <div class="col-md-6">
                                    <div class="fileupload_block">
                                        <input type="file" name="thumbnail" id="fileupload">
                                        <div class="fileupload_img"> <img type="image" src="assets/images/add-image.png" alt="add image" /> </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-3">
                                    <label class="control-label">Channel Description :-</label>
                                </div>
                                <div class="col-md-6">
                                    <textarea name="channel_desc" id="channel_desc" rows="5" class="form-control"></textarea>
                                    <script>

                                        CKEDITOR.replace( 'channel_desc' );

                                    </script>
                                </div>
                            </div>
                            <div class="form-group">&nbsp;</div>
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