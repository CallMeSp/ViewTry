package com.sp.viewtry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.sp.viewtry.circleImageView.MyCirCleView;
import com.sp.viewtry.swipeRecycler.ItemRemovedListener;
import com.sp.viewtry.swipeRecycler.MyAdapter;
import com.sp.viewtry.swipeRecycler.SwipeCardLayoutManager;
import com.sp.viewtry.swipeRecycler.SwipeCardRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private MyCirCleView cirCleView;
    private MyAdapter adapter;
    @BindView(R.id.recyclerView)SwipeCardRecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        cirCleView=(MyCirCleView)findViewById(R.id.circleview);
        Glide.with(this)
                .load(R.mipmap.test)
                .into(cirCleView);

        recyclerView.setLayoutManager(new SwipeCardLayoutManager());
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        adapter=new MyAdapter(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemRemovedListener(new ItemRemovedListener() {
            @Override
            public void onRightRemoved() {

            }

            @Override
            public void onLeftRemoved() {

            }
        });
    }
}
