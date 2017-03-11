package com.sp.viewtry.swipeRecycler;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public abstract class SwipeCardAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List mlist;
    public SwipeCardAdapter(List list){
        mlist=list;
    }
    public void deleteTopItem(){
        int position=getItemCount()-1;
        mlist.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount(){
        return mlist.size();
    }
}
