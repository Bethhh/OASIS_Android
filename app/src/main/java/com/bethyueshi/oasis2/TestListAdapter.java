package com.bethyueshi.oasis2;

import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bethyueshi on 3/1/16.
 */
public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ViewHolder> {
    private int[] resIds;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public GIFView gifView;
        public ViewHolder(View v) {
            super(v);
            gifView = (GIFView)v.findViewById(R.id.test_pic);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TestListAdapter(int[] resIds) {
        this.resIds = resIds;
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
        Log.d("res", " " +  position);
        holder.gifView.setSrc(resIds[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return resIds.length;
    }

}
