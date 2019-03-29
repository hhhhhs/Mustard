package com.hs.mustard.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.hs.mustard.*;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class MainActivity1 extends AppCompatActivity {

    @BindView(R.id.chat_list)
    EasyRecyclerView chatList;
    @BindView(R.id.emotion_voice)
    ImageView emotionVoice;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.voice_text)
    TextView voiceText;
    @BindView(R.id.emotion_button)
    ImageView emotionButton;
    @BindView(R.id.emotion_add)
    ImageView emotionAdd;
    @BindView(R.id.emotion_send)
    com.hs.mustard.widget.StateButton emotionSend;
    @BindView(R.id.viewpager)
    com.hs.mustard.widget.NoScrollViewPager viewpager;
    @BindView(R.id.emotion_layout)
    RelativeLayout emotionLayout;

    private Unbinder mUnBinder;

    private com.hs.mustard.widget.EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private com.hs.mustard.ui.fragment.ChatEmotionFragment chatEmotionFragment;
    private com.hs.mustard.ui.fragment.ChatFunctionFragment chatFunctionFragment;
    private com.hs.mustard.adapter.CommonFragmentPagerAdapter adapter;

    private com.hs.mustard.adapter.ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<com.hs.mustard.enity.MessageInfo> messageInfos;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mUnBinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initWidget();
    }

    private void initWidget() {
        fragments = new ArrayList<>();
        chatEmotionFragment = new com.hs.mustard.ui.fragment.ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new com.hs.mustard.ui.fragment.ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new com.hs.mustard.adapter.CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = com.hs.mustard.widget.EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        com.hs.mustard.util.GlobalOnItemClickManagerUtils globalOnItemClickListener = com.hs.mustard.util.GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new com.hs.mustard.adapter.ChatAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        LoadData();
    }

    /**
     * item点击事件
     */
    private com.hs.mustard.adapter.ChatAdapter.onItemClickListener itemClickListener = new com.hs.mustard.adapter.ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
            Toast.makeText(MainActivity1.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            com.hs.mustard.enity.FullImageInfo fullImageInfo = new com.hs.mustard.enity.FullImageInfo();
            fullImageInfo.setLocationX(location[0]);
            fullImageInfo.setLocationY(location[1]);
            fullImageInfo.setWidth(view.getWidth());
            fullImageInfo.setHeight(view.getHeight());
            fullImageInfo.setImageUrl(messageInfos.get(position).getImageUrl());
            EventBus.getDefault().postSticky(fullImageInfo);
            startActivity(new Intent(MainActivity1.this, com.hs.mustard.ui.activity.FullImageActivity.class));
            overridePendingTransition(0, 0);
        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfos.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            com.hs.mustard.util.MediaManager.playSound(messageInfos.get(position).getFilepath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }
    };

    /**
     * 构造聊天数据
     */
    private void LoadData() {
        messageInfos = new ArrayList<>();

        /*com.hs.mustard.enity.MessageInfo messageInfo = new com.hs.mustard.enity.MessageInfo();
        messageInfo.setContent("你好，欢迎使用Rance的聊天界面框架");
        messageInfo.setType(com.hs.mustard.util.Constants.CHAT_ITEM_TYPE_LEFT);
        messageInfo.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
        messageInfos.add(messageInfo);

        com.hs.mustard.enity.MessageInfo messageInfo1 = new com.hs.mustard.enity.MessageInfo();
        messageInfo1.setFilepath("http://www.trueme.net/bb_midi/welcome.wav");
        messageInfo1.setVoiceTime(3000);
        messageInfo1.setType(com.hs.mustard.util.Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo1.setSendState(com.hs.mustard.util.Constants.CHAT_ITEM_SEND_SUCCESS);
        messageInfo1.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfos.add(messageInfo1);

        com.hs.mustard.enity.MessageInfo messageInfo2 = new com.hs.mustard.enity.MessageInfo();
        messageInfo2.setImageUrl("http://img4.imgtn.bdimg.com/it/u=1800788429,176707229&fm=21&gp=0.jpg");
        messageInfo2.setType(com.hs.mustard.util.Constants.CHAT_ITEM_TYPE_LEFT);
        messageInfo2.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
        messageInfos.add(messageInfo2);

        com.hs.mustard.enity.MessageInfo messageInfo3 = new com.hs.mustard.enity.MessageInfo();
        messageInfo3.setContent("[微笑][色][色][色]");
        messageInfo3.setType(com.hs.mustard.util.Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo3.setSendState(com.hs.mustard.util.Constants.CHAT_ITEM_SEND_ERROR);
        messageInfo3.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfos.add(messageInfo3);*/

        chatAdapter.addAll(messageInfos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final com.hs.mustard.enity.MessageInfo messageInfo) {
        messageInfo.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfo.setType(com.hs.mustard.util.Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendState(com.hs.mustard.util.Constants.CHAT_ITEM_SENDING);
        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                messageInfo.setSendState(com.hs.mustard.util.Constants.CHAT_ITEM_SEND_SUCCESS);
                chatAdapter.notifyDataSetChanged();
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                com.hs.mustard.enity.MessageInfo message = new com.hs.mustard.enity.MessageInfo();
                message.setContent("1");
                message.setType(com.hs.mustard.util.Constants.CHAT_ITEM_TYPE_LEFT);
                message.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
                messageInfos.add(message);
                chatAdapter.add(message);
                chatList.scrollToPosition(chatAdapter.getCount() - 1);
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }
}
