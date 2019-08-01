package com.app.setv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.fragment.IntroFragment;
import com.app.util.IsRTL;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class IntroActivity extends AppCompatActivity {

    View circle1, circle2, circle3;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    Button btnNext, btnSkip;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        IsRTL.ifSupported(IntroActivity.this);
        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        circle3 = findViewById(R.id.circle3);

        btnNext = findViewById(R.id.button_next);
        btnSkip = findViewById(R.id.button_skip);

        pager = findViewById(R.id.view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        setIndicator(0);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setIndicator(int index) {
        switch (index) {
            case 0:
                circle1.setBackgroundResource(R.drawable.circle_fill);
                circle2.setBackgroundResource(R.drawable.circle);
                circle3.setBackgroundResource(R.drawable.circle);
                break;
            case 1:
                circle2.setBackgroundResource(R.drawable.circle_fill);
                circle1.setBackgroundResource(R.drawable.circle);
                circle3.setBackgroundResource(R.drawable.circle);
                break;
            case 2:
                circle3.setBackgroundResource(R.drawable.circle_fill);
                circle1.setBackgroundResource(R.drawable.circle);
                circle2.setBackgroundResource(R.drawable.circle);
                break;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            IntroFragment tp;
            tp = IntroFragment.newInstance(position);
            return tp;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
