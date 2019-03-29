package com.hs.mustard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.hs.lib.CardConfig;
import com.hs.lib.CardItemTouchHelperCallback;
import com.hs.lib.CardLayoutManager;
import com.hs.lib.OnSwipeListener;
import com.hs.mustard.*;


public class slipActivity extends AppCompatActivity {

    private List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        //退出时使用
        getWindow().setExitTransition(explode);
//第一次进入时使用
        getWindow().setEnterTransition(explode);
//再次进入时使用
        getWindow().setReenterTransition(explode);

        setContentView(R.layout.activity_slip);
        initView();
        initData();
    }

    private void initView() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MyAdapter());
        CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(recyclerView.getAdapter(), list);
        cardCallback.setOnSwipedListener(new OnSwipeListener<Integer>() {

            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (direction == CardConfig.SWIPING_LEFT) {
                    myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
                } else if (direction == CardConfig.SWIPING_RIGHT) {
                    myHolder.likeImageView.setAlpha(Math.abs(ratio));
                } else {
                    myHolder.dislikeImageView.setAlpha(0f);
                    myHolder.likeImageView.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, Integer o, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                myHolder.dislikeImageView.setAlpha(0f);
                myHolder.likeImageView.setAlpha(0f);
                Toast.makeText(slipActivity.this, direction == CardConfig.SWIPED_LEFT ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipedClear() {
                Toast.makeText(slipActivity.this, "data clear", Toast.LENGTH_SHORT).show();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }

        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(recyclerView, touchHelper);
        recyclerView.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void initData() {
        list.add(R.drawable.img_avatar_01);
        list.add(R.drawable.img_avatar_02);
        list.add(R.drawable.img_avatar_03);
        list.add(R.drawable.img_avatar_04);
        list.add(R.drawable.img_avatar_05);
        list.add(R.drawable.img_avatar_06);
        list.add(R.drawable.img_avatar_07);
    }

    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView avatarImageView = ((MyViewHolder) holder).avatarImageView;
            avatarImageView.setImageResource(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView avatarImageView;
            ImageView likeImageView;
            ImageView dislikeImageView;

            MyViewHolder(View itemView) {
                super(itemView);
                avatarImageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
                likeImageView = (ImageView) itemView.findViewById(R.id.iv_like);
                dislikeImageView = (ImageView) itemView.findViewById(R.id.iv_dislike);
            }

        }
    }

}
