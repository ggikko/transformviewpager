package ggikko.me.transformapp;

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
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static Uri firstUri;
    private static Uri secondUri;
    private static Uri thirdUri;

    private final GgikkoPagerAdapter adapter = new GgikkoPagerAdapter();

    @Bind(R.id.viewpager) ViewPager viewpager;

    //TODO : framelayout 걷어내고 black background에 대한 다른 대안을 고안해야함
    FrameLayout one_place_holder;
    FrameLayout two_place_holder;
    FrameLayout three_place_holder;
    FrameLayout four_place_holder;

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

//        firstUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.name);
//        secondUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.name1);
//        thirdUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.name2);
    }

    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){

        private int currentPosition = -1;
        private int startTimeForTab;
        
        @Override
        public void onPageSelected(int position) {
            switch (position){
                default:
                    throw new IllegalArgumentException("error");
                case 0:{
                    one_place_holder.setVisibility(View.GONE);
                    two_place_holder.setVisibility(View.VISIBLE);
                    three_place_holder.setVisibility(View.VISIBLE);
                    playVideo(one_video);
                    stopVideo(two_video);
                    stopVideo(three_video);
                    break;
                }
                case 1:{
                    one_place_holder.setVisibility(View.VISIBLE);
                    two_place_holder.setVisibility(View.GONE);
                    three_place_holder.setVisibility(View.VISIBLE);
                    stopVideo(one_video);
                    playVideo(two_video);
                    stopVideo(three_video);
//                    stopVideo(four_video);
                    break;
                }
                case 2:{
                    one_place_holder.setVisibility(View.VISIBLE);
                    two_place_holder.setVisibility(View.VISIBLE);
                    three_place_holder.setVisibility(View.GONE);
                    stopVideo(one_video);
                    stopVideo(two_video);
                    playVideo(three_video);
//                    stopVideo(four_video);
                    break;
                }
                case 3:{
                    stopVideo(one_video);
                    stopVideo(two_video);
                    playVideo(three_video);
//                    stopVideo(four_video);
                    break;
                }
                case 4:{
                    break;
                }
            }
        }
    };

    private class GgikkoPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            switch (position) {
                default:
                    ViewGroup layout0 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.onepager, container,false);
                    container.addView(layout0);
                    return layout0;
                case 0:
                    ViewGroup layout = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.onepager, container,false);
                    container.addView(layout);
                    one_video = (VideoView) findViewById(R.id.one_video);
                    one_place_holder = (FrameLayout) findViewById(R.id.one_place_holder);
                    one_video.setVideoURI(firstUri);
                    return layout;
                case 1:
                    ViewGroup layout2 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.twopager, container,false);
                    container.addView(layout2);
                    two_video = (VideoView) findViewById(R.id.two_video);
                    two_place_holder = (FrameLayout) findViewById(R.id.two_place_holder);
                    two_video.setVideoURI(secondUri);
                    return layout2;
                case 2:
                    ViewGroup layout3 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.threepager, container,false);
                    container.addView(layout3);
                    three_video = (VideoView) findViewById(R.id.three_video);
                    three_place_holder = (FrameLayout) findViewById(R.id.three_place_holder);
                    three_video.setVideoURI(thirdUri);
                    return layout3;
                case 3:
                    ViewGroup layout4 = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.onepager, container,false);
                    container.addView(layout4);
                    four_video = (VideoView) findViewById(R.id.four_video);
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

    /** start video */
    private void playVideo(VideoView videoView){
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //TODO 에러처리
                return false;
            }
        });
        try {
            videoView.start();
        }catch(Throwable e){
            Log.e("ggikko", "비디오 실행 불가");
        }
    }

    /** stop video */
    private void stopVideo(VideoView videoView){
        if(videoView == null){
            return;
        }
        videoView.seekTo(0);
        videoView.pause();
    }



}
