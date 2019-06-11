package cn.codeguy.xmly;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * <b>Package:</b> cn.codeguy.xmly <br>
 * <b>Create Date:</b> 2019-06-11  15:43 <br>
 * <b>@author:</b> leida <br>
 * <b>Description:</b>  <br>
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private Activity mContext;
    private int mCount=10;

    public SimpleAdapter(Activity context) {
        mContext=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = mContext.getLayoutInflater().inflate(R.layout.item_layout, viewGroup, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public void setData(int i) {
        mCount+=i;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
