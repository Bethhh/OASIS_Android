package com.bethyueshi.oasis2;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by bethyueshi on 3/1/16.
 */
public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {
    private int[] resIds;
    private int testNum;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Each data item is a GIF
        public GIFView gifView;
        public RelativeLayout rl;
        public ViewHolder(View v) {
            super(v);
            gifView = (GIFView)v.findViewById(R.id.test_pic);
            rl = (RelativeLayout)v.findViewById(R.id.test_border);
        }
    }

    public TestListAdapter(int[] resIds, int testNum) {
        this.resIds = resIds;
        this.testNum = testNum;
    }

    @Override
    public TestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // Creates a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_items, parent, false);

        // (Sets the view's size, margins, paddings and layout parameters)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replaces the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Gets element from dataset at this position
        // Replaces the contents of the view with that element

        GradientDrawable gd = (GradientDrawable) holder.rl.getBackground();
        //TODO move the selected test to the middle of the row
        if(position == testNum){
            gd.setStroke(20,  Color.parseColor("#ffff00"));
        }else{
            gd.setStroke(20,  Color.parseColor("#cccccc"));
        }


        Log.d("Get elem as position:",  " " + position);
        holder.gifView.setSrc(resIds[position]);
    }

    // Returns the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return resIds.length;
    }

}
