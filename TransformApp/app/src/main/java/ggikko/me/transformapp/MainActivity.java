package ggikko.me.transformapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ggikko.me.transformapp.indicator.ExtensiblePageIndicator;

public class MainActivity extends AppCompatActivity {

    private final GgikkoPagerAdapter adapter = new GgikkoPagerAdapter();

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.extensiblePageIndicator)
    ExtensiblePageIndicator extensiblePageIndicator;

    @OnClick(R.id.next)
    void callNext() {
        int page = getItem(+1);
        viewpager.setCurrentItem(page, true);
        if(page >3){
            startActivity(new Intent(MainActivity.this, NextActivity.class));
            finish();
        }
    }

    @OnClick(R.id.jump)
    void callJump() {
        startActivity(new Intent(MainActivity.this, NextActivity.class));
        finish();
    }

    private static Uri secondUri;
    private static Uri thirdUri;
    private static Uri fourthUri;

    private VideoView second_video;
    private VideoView third_video;
    private VideoView fourth_video;

    private FrameLayout second_placeholder;
    private FrameLayout third_placeholder;
    private FrameLayout fourth_placeholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(pageChangeListener);
        viewpager.setOffscreenPageLimit(4);

        extensiblePageIndicator.initViewPager(viewpager);

        secondUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nameone);
        thirdUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nameone);
        fourthUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nameone);

    }

    private int getItem(int i) {
        return viewpager.getCurrentItem() + i;
    }

    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

        private int currentPosition = -1;
        private int startTimeForTab;

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                default:
                    throw new IllegalArgumentException("error");

                case 0:
                    break;

                case 1:
                    setVideoStartAndStop(second_video, third_video, fourth_video);
                    setVisibilityGone(second_placeholder);
                    break;


                case 2:
                    setVideoStartAndStop(third_video, second_video, fourth_video);
                    setVisibilityGone(third_placeholder);
                    break;


                case 3:
                    setVideoStartAndStop(fourth_video,second_video,third_video);
                    setVisibilityGone(fourth_placeholder);
                    break;
            }
        }
    };

    private class GgikkoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            switch (position) {
                default:
                    ViewGroup layout0 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.onepager, container, false);
                    container.addView(layout0);
                    return layout0;
                case 0:
                    ViewGroup layout = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.onepager, container, false);
                    container.addView(layout);
                    return layout;
                case 1:
                    ViewGroup layout2 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.twopager, container, false);
                    container.addView(layout2);
                    second_video = (VideoView) findViewById(R.id.two_video);
                    second_video.setVideoURI(secondUri);
                    second_placeholder = (FrameLayout) findViewById(R.id.two_placeholder);
                    return layout2;
                case 2:
                    ViewGroup layout3 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.threepager, container, false);
                    container.addView(layout3);
                    third_video = (VideoView) findViewById(R.id.three_video);
                    third_video.setVideoURI(thirdUri);
                    third_placeholder = (FrameLayout) findViewById(R.id.three_placeholder);
                    return layout3;
                case 3:
                    ViewGroup layout4 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.fourpager, container, false);
                    container.addView(layout4);
                    fourth_video = (VideoView) findViewById(R.id.four_video);
                    fourth_video.setVideoURI(fourthUri);
                    fourth_placeholder = (FrameLayout) findViewById(R.id.four_placeholder);
                    return layout4;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void setVideoStartAndStop(VideoView start, VideoView stop1, VideoView stop2){
        playVideo(start);
        stopVideo(stop1);
        stopVideo(stop2);
    }


    public void setVisibilityVisible(FrameLayout... frameLayouts){
        for (FrameLayout frameLayout : frameLayouts){
            frameLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setVisibilityGone(final FrameLayout frameLayout){
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frameLayout.setVisibility(View.GONE);
                    }
                });
            }
        };
        thread.start();
    }

    /**
     * start video
     */
    private void playVideo(VideoView videoView) {

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //TODO 에러처리
                return false;
            }
        });

        try {
            videoView.start();
        } catch (Throwable e) {
            Log.e("ggikko", "비디오 실행 불가");
        }
    }


    /**
     * stop video
     */
    private void stopVideo(VideoView videoView) {
        if (videoView == null) {
            return;
        }
        videoView.seekTo(0);
        videoView.pause();
    }


}
