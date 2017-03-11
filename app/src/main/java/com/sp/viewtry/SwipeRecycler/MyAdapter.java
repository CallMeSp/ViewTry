package com.sp.viewtry.swipeRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.viewtry.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public class MyAdapter extends SwipeCardAdapter<MyAdapter.MyHolder> {
    private static final String TAG = "MyAdapter";
    private Context mContext;

    public MyAdapter(Context context, List<String> list) {
        super(list);
        mContext = context;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: " );
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item, parent, false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: "+position );
        holder.setData((String)mlist.get(position));
    }
    class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        public MyHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }
        public void setData(String text) {
            mTextView.setText(text);
        }
    }
}
