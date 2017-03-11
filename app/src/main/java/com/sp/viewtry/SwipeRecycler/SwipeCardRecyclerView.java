package com.sp.viewtry.swipeRecycler;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/6.
 */

public class SwipeCardRecyclerView extends RecyclerView {
    private float mTopViewX;
    private float mTopViewY;
    private float mTopViewOffsetX=0;
    private float mTopViewOffsetY=0;
    private float mtouchdownX;
    private float mtouchdownY;
    private float mBorder = dip2px(120);
    private ItemRemovedListener mItemRemovedListener;
    private FrameLayout mDecorView;
    private int[] mDecorViewLoacation=new int[2];
    private Map<View,Animator> mAnimatorMap;
    public SwipeCardRecyclerView(Context context){
        super(context);
        initView();
    }
    public SwipeCardRecyclerView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        initView();
    }
    public SwipeCardRecyclerView(Context context,AttributeSet attributeSet,int defStyle){
        super(context,attributeSet,defStyle);
        initView();
    }
    private void initView(){
        mDecorView=(FrameLayout)((Activity)getContext()).getWindow().getDecorView();
        mDecorView.getLocationOnScreen(mDecorViewLoacation);
        mAnimatorMap=new HashMap<>();
    }
    public void setItemRemovedListener(ItemRemovedListener listener){
        mItemRemovedListener=listener;
    }
    private int dip2px(float dip){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getContext().getResources().getDisplayMetrics());
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (getChildCount()==0){
            return super.onTouchEvent(event);
        }
        View TopView=getChildAt(getChildCount()-1);
        float touchX=event.getX();
        float touchY=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (mAnimatorMap.containsKey(TopView)){
                    mAnimatorMap.get(TopView).cancel();
                    mAnimatorMap.remove(TopView);
                    mTopViewOffsetX=TopView.getX();
                    mTopViewOffsetY=TopView.getY();
                }else {
                    mTopViewX=TopView.getX();
                    mTopViewY=TopView.getY();
                    mTopViewOffsetX=0;
                    mTopViewOffsetY=0;
                }
                mtouchdownX=touchX;
                mtouchdownY=touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx=touchX-mtouchdownX;
                float dy=touchY-mtouchdownY;
                TopView.setX(mTopViewX+dx+mTopViewOffsetX);
                TopView.setY(mTopViewY+dy+mTopViewOffsetY);
                updateNextItem(Math.abs(TopView.getX()-mTopViewX)*0.2/mBorder+0.8);
                break;
            case MotionEvent.ACTION_UP:
                mtouchdownX=0;
                mtouchdownY=0;
                TouchUp(TopView);
                break;
        }
        return super.onTouchEvent(event);
    }
    private void updateNextItem(double factor){
        if (getChildCount()<2){
            return;
        }
        if (factor>1){
            factor=1;
        }
        View nextview=getChildAt(getChildCount()-2);
        nextview.setScaleX((float)factor);
        nextview.setScaleY((float)factor);
    }
    private void TouchUp(final View view){
        float targetX=0;
        float targetY=0;
        boolean isDelete=false;
        if (Math.abs(view.getX()-mTopViewX)<mBorder){
            targetX = mTopViewX;
            targetY = mTopViewY;
        }else if (view.getX() - mTopViewX > mBorder){
            isDelete = true;
            targetX = getScreenWidth() * 2;
            mItemRemovedListener.onRightRemoved();
        }else {
            isDelete=true;
            targetX=-getScreenWidth()-view.getWidth();
            mItemRemovedListener.onLeftRemoved();
        }
        View animView=view;
        TimeInterpolator timeInterpolator;
        if (isDelete){
            animView=getMirrorView(view);
            float offsetx=getX()-mDecorView.getX();
            float offsety = getY() - mDecorView.getY();
            targetY=caculateExitY(mTopViewX+offsetx,mTopViewY+offsety,animView.getX(),animView.getY(),targetX);
            timeInterpolator=new LinearInterpolator();
        }else {
            timeInterpolator=new OvershootInterpolator();
        }
        final boolean finalDel =isDelete;
        final View finalanim=animView;
        animView.animate()
                .setDuration(500)
                .x(targetX)
                .y(targetY)
                .setInterpolator(timeInterpolator)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (!finalDel){
                            mAnimatorMap.put(finalanim,animation);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (finalDel){
                            try {
                                mDecorView.removeView(finalanim);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            mAnimatorMap.remove(finalanim);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (!finalDel){
                            updateNextItem(Math.abs(view.getX() - mTopViewX) * 0.2 / mBorder + 0.8);
                        }
                    }
                });
    }
    private float getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }
    private float caculateExitY(float x1, float y1, float x2, float y2, float x3) {
        return (y2 - y1) * (x3 - x1) / (x2 - x1) + y1;
    }
    private ImageView getMirrorView(View view){
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView=new ImageView(getContext());
        Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(bitmap.getWidth(),bitmap.getHeight());
        int[] locations=new int[2];
        view.getLocationOnScreen(locations);
        view.setVisibility(GONE);
        ((SwipeCardAdapter)getAdapter()).deleteTopItem();
        mirrorView.setX(locations[0]-mDecorViewLoacation[0]);
        mirrorView.setY(locations[1]-mDecorViewLoacation[1]);
        mDecorView.addView(mirrorView,params);
        return mirrorView;
    }

}
