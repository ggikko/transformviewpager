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

    private static Uri firstUri;
    private static Uri secondUri;
    private static Uri thirdUri;

    private final GgikkoPagerAdapter adapter = new GgikkoPagerAdapter();

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.extensiblePageIndicator)
    ExtensiblePageIndicator extensiblePageIndicator;
    @BindColor(R.color.transparent)
    int transparent;

    @OnClick(R.id.next)
    void callNext() {
        viewpager.setCurrentItem(getItem(+1), true);
    }

    @OnClick(R.id.jump)
    void callJump() {
        startActivity(new Intent(MainActivity.this, NextActivity.class));
    }

    //TODO : framelayout 걷어내고 black background에 대한 다른 대안을 고안해야함
    FrameLayout one_place_holder;
    FrameLayout two_place_holder;
    FrameLayout three_place_holder;
    FrameLayout placeholder;

    VideoView one_video;
    VideoView two_video;
    VideoView three_video;
    VideoView four_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(pageChangeListener);
        viewpager.setOffscreenPageLimit(4);

        extensiblePageIndicator.initViewPager(viewpager);

//        firsttUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nameone);

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
                case 0: {
                    playVideo(two_video);
                    stopVideo(three_video);
                    stopVideo(four_video);
                    placeholder.setVisibility(View.VISIBLE);
                    Log.e("ggikko", "position :" + position);
                    break;
                }
                case 1: {
//                    stopVideo(one_video);
                    playVideo(two_video);
                    stopVideo(three_video);
                    stopVideo(four_video);
                    placeholder.setVisibility(View.VISIBLE);
                    Log.e("ggikko", "position :" + position);
                    break;
                }
                case 2: {
//                    stopVideo(one_video);
                    stopVideo(two_video);
                    playVideo(three_video);
                    stopVideo(four_video);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    placeholder.setVisibility(View.GONE);
                                }
                            });
                        }
                    };
                    thread.start(); //start the thread
                    Log.e("ggikko", "position :" + position);
                    break;
                }
                case 3: {
//                    stopVideo(one_video);
                    stopVideo(two_video);
                    stopVideo(three_video);
                    playVideo(four_video);
                    placeholder.setVisibility(View.VISIBLE);
                    Log.e("ggikko", "position :" + position);
                    break;
                }
                case 4: {
                    break;
                }
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
//                    one_video = (VideoView) findViewById(R.id.one_video);
                    return layout;
                case 1:
                    ViewGroup layout2 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.twopager, container, false);
                    container.addView(layout2);
                    two_video = (VideoView) findViewById(R.id.two_video);
                    two_video.setVideoURI(firstUri);
                    return layout2;
                case 2:
                    ViewGroup layout3 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.threepager, container, false);
                    container.addView(layout3);
                    three_video = (VideoView) findViewById(R.id.three_video);
                    three_video.setVideoURI(secondUri);
                    placeholder = (FrameLayout) findViewById(R.id.placeholder);
                    return layout3;
                case 3:
                    ViewGroup layout4 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.fourpager, container, false);
                    container.addView(layout4);
                    four_video = (VideoView) findViewById(R.id.four_video);
                    four_video.setVideoURI(thirdUri);
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
