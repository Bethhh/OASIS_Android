package com.bethyueshi.oasis2;

/**
 * Created by bethyueshi on 8/20/16.
 */
public class AppConfiguration {
    public static final int NUM_OF_MAPS = 4;
    public static final int TOTAL_TEST = 2;
    public static final double PH_TEST_WAIT_MIN = 0.1;//0.25;
    public static final double PH_RECORD_WAIT_MIN = 0.2;//1;
    public static final double METAL_TEST_WAIT_MIN = 0.1;//0.5;
    public static final double METAL_RECORD_WAIT_MIN = 0.2;//2;

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
