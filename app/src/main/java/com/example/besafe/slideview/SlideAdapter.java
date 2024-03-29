package com.example.besafe.slideview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.besafe.R;


/**
 * Created by Sumeet Jain on 25-02-2018.
 */

public class SlideAdapter extends PagerAdapter {


    Context context;
    LayoutInflater inflater;

    public SlideAdapter(Context context){
        this.context=context;
    }

    //Array
    public int[] list_images={

            R.drawable.accident,
            R.drawable.siren,
            R.drawable.mapslide

    };

    public String[] list_title={

            "Be Safe",
            "Accident Accelator",
            "Services"

    };

    public String[] list_description={


            "اذا كنت في الطريق وحدث لك شئ \n" +
                    "ستجدنا بجانبك",
            " يقوم باكتشاف التصادم تلقائي وتحديد موقعك\n" +
                    "ويقوم بارساله لارقام الطورائ الخاصة بك",
            "ستجد بجانبك متخصصي صيانة السيارات وسيارات الانقاذ وكل ماتحتاجه "

    };
    public int[] list_color={

            Color.rgb(22, 160, 133),
            Color.rgb(22, 160, 133),
            Color.rgb(22, 160, 133)
    };

    @Override
    public int getCount() {
        return list_title.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view==(LinearLayout)obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.slidelinearlayout);


        ImageView img = (ImageView)view.findViewById(R.id.slideimg);
        TextView txt1 = (TextView) view.findViewById(R.id.slidetitle);
        TextView txt2 = (TextView) view.findViewById(R.id.slidedescription);


        img.setImageResource(list_images[position]);
        txt1.setText(list_title[position]);
        txt2.setText(list_description[position]);
        linearLayout.setBackgroundColor(list_color[position]);



        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}