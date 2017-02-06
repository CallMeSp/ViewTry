package com.sp.viewtry.CircleImageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.sp.viewtry.R;

/**
 * Created by Administrator on 2017/2/5.
 */

public class MyCirCleView extends ImageView {
    private static final String TAG = "MyCirCleView";
    private int type;
    private static final int TYPE_CIRCLE=0;
    private static final int TYPE_ROUND=1;
    private static final int BORDER_RADIUS_DEFAULT=10;
    private int mBorderRadius;
    private Paint mBitmapPaint;
    private int mRadius;
    private Matrix mMatrix;
    private BitmapShader mBitmapShader;
    private int mWidth;
    private RectF mRoundRect;
    public MyCirCleView(Context context){
        super(context);
    }
    public MyCirCleView(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
        Log.e(TAG, "MyCirCleView: oncreate" );

    }
    public MyCirCleView(Context context,AttributeSet attributeSet,int defstyle){
        super(context,attributeSet,defstyle);
        mMatrix=new Matrix();
        mBitmapPaint=new Paint();
        mBitmapPaint.setAntiAlias(true);
        TypedArray array=context.obtainStyledAttributes(attributeSet, R.styleable.MyCirCleView);
        mBorderRadius=array.getDimensionPixelSize(R.styleable.MyCirCleView_borderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,BORDER_RADIUS_DEFAULT,
                        getResources().getDisplayMetrics()));
        type=array.getInt(R.styleable.MyCirCleView_type,TYPE_CIRCLE);
        array.recycle();
    }
    @Override
    protected void onMeasure(int WidthmeasureSpec,int HightmeasureSpec){
        super.onMeasure(WidthmeasureSpec, HightmeasureSpec);
        if (type == TYPE_CIRCLE)
        {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }
    @Override
    protected void onDraw(Canvas canvas){
        if (getDrawable()==null){
            return;
        }
        setUpShader();
        if (type == TYPE_ROUND)
        {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        } else
        {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        }
    }
    private void setUpShader(){
        Drawable drawable=getDrawable();
        if (drawable==null){
            return;
        }
        Bitmap bmp = drawableToBitamp(drawable);
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE)
        {
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;

        } else if (type == TYPE_ROUND)
        {
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight()
                    * 1.0f / bmp.getHeight());
        }
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);
    }
    private Bitmap drawableToBitamp(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范围
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
    }
}
