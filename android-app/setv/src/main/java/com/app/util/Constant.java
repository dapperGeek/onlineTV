package com.app.util;

import com.app.setv.BuildConfig;

import java.io.Serializable;

public class Constant implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String ONE_CHANNEL_ID = "1";

    private static String SERVER_URL = BuildConfig.SERVER_URL;

    public static final String IMAGE_PATH = SERVER_URL + "images/";

    public static final String CATEGORY_URL = SERVER_URL + "api.php?get_category";

    public static final String CHANNELS_BY_CAT_URL = SERVER_URL + "api.php?get_channels_by_cat_id";

    public static final String GET_USER_PROFILE_URL = SERVER_URL + "api.php?get_user_profile";

    public static final String LATEST_URL = SERVER_URL + "api.php?get_latest_channels";

    public static final String FEATURED_URL = SERVER_URL + "api.php?get_featured_channels";

    public static final String ALL_CHANNEL_URL = SERVER_URL + "api.php?get_all_channels";

    public static final String ALL_VIDEO_URL = SERVER_URL + "api.php?get_videos";

    public static final String HOME_URL = SERVER_URL + "api.php?get_home_channels";

    public static final String API_URL = SERVER_URL + "api.php";

    public static final String ARRAY_NAME = "LIVETV";

    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_CID = "cid";
    public static final String CATEGORY_IMAGE = "category_image";

    public static final String CHANNEL_ID = "id";
    public static final String CHANNEL_TITLE = "channel_title";
    public static final String CHANNEL_URL = "channel_url";
    public static final String CHANNEL_IMAGE = "channel_thumbnail";
    public static final String CHANNEL_DESC = "channel_desc";
    public static final String CHANNEL_TYPE = "channel_type";
    public static final String CHANNEL_PREMIUM = "premium";
    public static final String CHANNEL_FEE = "fee";
    public static final String CHANNEL_SUBSCRIBED = "subscribed";
    public static final String CHANNEL_AVG_RATE = "rate_avg";

    public static final String SCHEDULE_NAME = "schedule";
    public static final String SCHEDULE_ID = "schedule_id";
    public static final String SCHEDULE_TITLE = "schedule_title";
    public static final String SCHEDULE_TIME = "schedule_time";
    public static final String SCHEDULE_DATE = "schedule_date";

    public static final String VIDEO_ID = "id";
    public static final String VIDEO_TITLE = "video_title";
    public static final String VIDEO_URL = "video_url";
    public static final String VIDEO_IMAGE = "video_thumbnail_b";
    public static final String VIDEO_V_ID = "video_id";
    public static final String VIDEO_TYPE = "video_type";


    public static final String APP_NAME = "app_name";
    public static final String APP_IMAGE = "app_logo";
    public static final String APP_VERSION = "app_version";
    public static final String APP_AUTHOR = "app_author";
    public static final String APP_CONTACT = "app_contact";
    public static final String APP_EMAIL = "app_email";
    public static final String APP_WEBSITE = "app_website";
    public static final String APP_DESC = "app_description";
    public static final String APP_PRIVACY_POLICY = "app_privacy_policy";

    public static final String USER_NAME = "name";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHONE = "phone";

    public static final String RELATED_ITEM_ARRAY_NAME = "related";
    public static final String RELATED_ITEM_CHANNEL_ID = "rel_id";
    public static final String RELATED_ITEM_CHANNEL_NAME = "rel_channel_title";
    public static final String RELATED_ITEM_CHANNEL_THUMB = "rel_channel_thumbnail";

    public static final String HOME_LATEST_ARRAY = "latest_channels";
    public static final String HOME_CATEGORY_ARRAY = "cat_list";
    public static final String HOME_SLIDER_ARRAY = "slider_list";

    public static final String COMMENT_ID = "id";
    public static final String COMMENT_NAME = "user_name";
    public static final String COMMENT_DESC = "comment_text";
    public static final String COMMENT_DATE = "date";

    public static int GET_SUCCESS_MSG;
    public static final String MSG = "msg";
    public static final String SUCCESS = "success";
    public static int AD_COUNT = 0;
    public static int AD_COUNT_SHOW;

    public static boolean isBanner = false, isInterstitial = false;
    public static String adMobBannerId, adMobInterstitialId, adMobPublisherId;

    //
    public static final String makeSubsTag = "makeSubscription";


}
