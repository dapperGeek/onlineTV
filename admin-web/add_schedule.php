<?php include("includes/header.php");

require("includes/function.php");
require("language/language.php");

require_once("thumbnail_images.class.php");


//Get all Category
$qry="SELECT * FROM tbl_category";
$result=mysqli_query($mysqli,$qry);

if(isset($_POST['add_schedule']) and isset($_GET['add']))
{
    $title = $_POST['title'];
    $date = $_POST['date'];
    $time = $_POST['time'];
    $num = sizeof($title);

    for ($x = 0; $x < $num; $x++){
        $dayDate = mb_split('/', $date[$x]);
        $dbDate = $dayDate[1].'/'. $dayDate[0] . '/' . $dayDate[2];

        $data = array(
            'title' => $title[$x],
            'date'  => dateToTimestamp($date[$x]),
            'time'  =>  $time[$x]
        );

        $qry = Insert('tbl_schedules',$data);
    }

    $_SESSION['msg']="10";
    header( "Location:manage_schedule.php");
    exit;

}

?>
    <!-- <script src="assets/js/add_channel.js" type="text/javascript"></script> -->


    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="page_title_block">
                    <div class="col-md-5 col-xs-12">
                        <div class="page_title">Add Schedule</div>
                    </div>

                </div>
                <div class="clearfix"></div>
                <div class="card-body mrg_bottom">
                    <form class="form form-horizontal" action="" method="post"  enctype="multipart/form-data" onsubmit="return checkValidation(this);">
                        <div class="section">
                            <div class="section-body add_section" id="add_section">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label class="col-md-3 control-label">Program Title :-</label>
                                        <div class="col-md-6">
                                            <input type="text" name="title[]" id="channel_title" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-3 control-label">Date :-</label>
                                        <div class="col-md-2">
                                            <input type="text" name="date[]" id="mydatepicker" size="10" class="form-control" placeholder="dd/mm/YYYY">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-3 control-label">Time :-</label>
                                        <div class="col-md-2">
                                            <input type="text" name="time[]" size="10" class="form-control" placeholder="HH:mm">
                                        </div>
                                    </div>
                                    <hr>
                                </div>
                            </div>

                            <div class="section-body">
                                <div class="form-group">
                                    <div class="col-md-4 col-md-offset-3">
                                        <button type="submit" name="add_schedule" class="btn btn-primary">Save</button>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="col-md-5">
                        <button class="btn btn-default" id="add_row">Add Row</button>
                    </div>
                </div>
            </div>
        </div>
    </div>


<?php include("includes/footer.php");?>