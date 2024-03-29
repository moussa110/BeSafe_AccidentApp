package com.example.besafe.slideview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.besafe.HomeActivity;
import com.example.besafe.R;

public class MainActivity extends AppCompatActivity {


    private ViewPager viewpager;
    private LinearLayout liner;
    private SlideAdapter myadapter;
    private TextView[] mdots;
    private Button next,back;

    private int mCureentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager=(ViewPager)findViewById(R.id.viewpager);
        liner=(LinearLayout)findViewById(R.id.dots);

        next=(Button)findViewById(R.id.nextBtn);
        back=(Button)findViewById(R.id.backBtn);
        //btn=(Button)findViewById(R.id.btnRegM);
        myadapter=new SlideAdapter(this);
        viewpager.setAdapter(myadapter);
        adddots(0);

        viewpager.addOnPageChangeListener(viewlistener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage-1);
            }
        });
    }

    public void adddots(int i){

        mdots=new TextView[3];
        liner.removeAllViews();

        for (int x=0;x<mdots.length;x++){

            mdots[x]=new TextView(this);
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(25);
            mdots[x].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            liner.addView(mdots[x]);
        }
        if (mdots.length>0){

            mdots[i].setTextColor(getResources().getColor(R.color.colorPrimary));

        }

    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            adddots(position);
            mCureentPage = position;

            if (position==0){
                next.setEnabled(true);
                back.setEnabled(false);
                back.setVisibility(View.INVISIBLE);

                next.setText("التالي");
                back.setText("");
            }
            else if(position==mdots.length-1){

                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setText("ابدأ");
                back.setText("السابق");

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                });


            }
            else {
                next.setEnabled(true);
                back.setEnabled(true );
                back.setVisibility(View.VISIBLE);

                next.setText("التالي");
                back.setText("السابق");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
