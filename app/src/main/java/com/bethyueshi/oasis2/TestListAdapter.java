package com.bethyueshi.oasis2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.nfc.Tag;
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
        // each data item is just a string in this case
        public GIFView gifView;
        public RelativeLayout rl;
        public ViewHolder(View v) {
            super(v);
            gifView = (GIFView)v.findViewById(R.id.test_pic);
            rl = (RelativeLayout)v.findViewById(R.id.test_border);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TestListAdapter(int[] resIds, int testNum) {
        this.resIds = resIds;
        this.testNum = testNum;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_items, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        GradientDrawable gd = (GradientDrawable) holder.rl.getBackground();

        if(position == testNum){
            //To change the stroke color
            //int width_px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, youStrokeWidth, getResources().getDisplayMetrics());
            gd.setStroke(20,  Color.parseColor("#ffff00"));
        }else{
            gd.setStroke(20,  Color.parseColor("#cccccc"));
        }


        Log.d("res", " " + position);
        holder.gifView.setSrc(resIds[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return resIds.length;
    }

}
