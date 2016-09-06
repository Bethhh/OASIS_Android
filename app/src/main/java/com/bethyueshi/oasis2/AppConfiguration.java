package com.bethyueshi.oasis2;

/**
 * Created by bethyueshi on 8/20/16.
 */
public class AppConfiguration {
    public static final boolean DEBUG = true;
    public static final int NUM_OF_MAPS = 4;
    public static final int TOTAL_TEST = DEBUG? 2:2;
    public static final double PH_TEST_WAIT_MIN = DEBUG ? 0.1 : 0.25;//0.25;
    public static final double PH_RECORD_WAIT_MIN = DEBUG? 0.1 : 1;//1;
    public static final double METAL_TEST_WAIT_MIN = DEBUG? 0.1 : 0.5;//0.5;
    public static final double METAL_RECORD_WAIT_MIN = DEBUG? 0.1 : 2;//2;

    public static final String STR_SUBMIT_TO = "http://52.53.177.54/";
    public static final String STR_PHP = ".php";
    public static final String STR_PUT = "put";
    public static final String STR_GET = "get";

    public static final String STR_PH = "ph";
    public static final String STR_PHOTO = "photo";
    public static final String STR_GEO = "geo";
    public static final String STR_METAL = "metals";
    public static final String STR_LAT = "lat";
    public static final String STR_LONG = "long";

    public static final int PURPOSE_TEST = 1;
    public static final int PURPOSE_PROFILE = 2;
    public static String profile_path = "";

    //TODO use correct video
    public static int getTestVideo(int testNum){
        switch(testNum){
            case 0:
                return R.drawable.testph;
            case 1:
                return R.drawable.testmetal;
            case 2:
                return R.drawable.testph;
            case 3:
                return R.drawable.testph;
            default:
                return R.drawable.testph;
        }
    }

    //TODO use correct video
    public static int getRecordVideo(int testNum){
        switch(testNum){
            case 0:
                return R.drawable.recordph;
            case 1:
                return R.drawable.recordmetal;
            case 2:
                return R.drawable.recordph;
            case 3:
                return R.drawable.recordph;
            default:
                return R.drawable.recordph;
        }
    }


}
