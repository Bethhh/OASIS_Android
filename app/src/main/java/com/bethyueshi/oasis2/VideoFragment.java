package com.bethyueshi.oasis2;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoFragment extends Fragment implements View.OnClickListener {
    private GIFView instr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.video_fragment, container, false);

        instr = (GIFView)rootView.findViewById(R.id.test_pic_large);
        instr.setOnClickListener(this);

        //Bundle args = getArguments();
        //Log.d("haha", " " + args.getInt("resId"));
        //instr.setSrc(args.getInt("resId"));

        return rootView;
    }

    public void setGIF(int resId, int start){
        // GIF pic
        instr.setSrc(resId);
        // Where to draw
        instr.setStart(start);
    }

    @Override
    public void onClick(View v) {
        Log.d("instr", "there");
        switch (v.getId()) {
            case R.id.test_pic_large:
                Intent intent = new Intent(getActivity(), TimerActivity.class);
                intent.putExtra("test_num", getActivity().getIntent().getIntExtra("test_num", 0));
                Log.d("instr", "here");
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
