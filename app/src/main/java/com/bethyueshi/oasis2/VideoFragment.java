package com.bethyueshi.oasis2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoFragment extends Fragment {
    private GIFView instr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_fragment, container, false);

        instr = (GIFView)rootView.findViewById(R.id.test_pic_large);

        //Bundle args = getArguments();
        //Log.d("haha", " " + args.getInt("resId"));
        //instr.setSrc(args.getInt("resId"));

        return rootView;
    }

    public void setGIF(int resId){
        instr.setSrc(resId);
    }
}