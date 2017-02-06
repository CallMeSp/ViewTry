package com.sp.viewtry.SwipeRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/2/6.
 */

public class SwipeCardLayoutManager extends RecyclerView.LayoutManager{
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams(){
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state){
        detachAndScrapAttachedViews(recycler);
        for (int i=0;i<getItemCount();i++){
            View child=recycler.getViewForPosition(i);
            measureChildWithMargins(child,0,0);
            addView(child);
            int width=getDecoratedMeasuredWidth(child);
            int hight=getDecoratedMeasuredHeight(child);
            layoutDecorated(child,0,0,width,hight);
            if (i<getItemCount()-1){
                child.setScaleX(0.8f);
                child.setScaleY(0.8f);
            }
        }
    }
}
