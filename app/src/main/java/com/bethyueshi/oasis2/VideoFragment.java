package com.bethyueshi.oasis2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class VideoFragment extends Fragment implements View.OnClickListener {
    private GIFView instr;
    private boolean clickable = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_fragment, container, false);

        instr = (GIFView)rootView.findViewById(R.id.test_pic_large);
        instr.setOnClickListener(this);
        clickable = true;

        GradientDrawable gd = (GradientDrawable) ((RelativeLayout)rootView.findViewById(R.id.fragment_bg)).getBackground();
        gd.setStroke(20,  Color.parseColor("#ffff00"));

        return rootView;
    }

    public void setGIF(int resId, int start){
        // GIF pic
        instr.setSrc(resId);
        // Where to draw
        instr.setStart(start);
    }

    public void setClickable(boolean b){
        this.clickable = b;
    }

    @Override
    public void onClick(View v) {
        Log.d("instr", "there");
        if(!clickable){
            return;
        }
        switch (v.getId()) {
            case R.id.test_pic_large:
                Intent intent = new Intent(getActivity(), TestWater.class);
                intent.putExtra("test_num", getActivity().getIntent().getIntExtra("test_num", 0));
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
