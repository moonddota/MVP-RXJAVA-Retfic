package app.com.skylinservice.manager.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.com.skylinservice.R;

/**
 * Created by liuxuan on 2018/1/4.
 */

public class SkyLinViewPagerInditor extends LinearLayout {

    private TextView skylinInditor_tv_imake, skylinInditor_tv_imake_bottomline;
    private TextView skylinInditor_tv_ijoin, skylinInditor_tv_ijoin_bottomline;
    private ViewPager viewPager;
    private RelativeLayout skylininditor_rl_right, skylininditor_rl_left;

    public SkyLinViewPagerInditor(Context context) {
        super(context);


    }

    public SkyLinViewPagerInditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_viewpager_inditor, this, true);
        skylinInditor_tv_imake = view.findViewById(R.id.skylin_inditor_i_make);
        skylinInditor_tv_imake_bottomline = view.findViewById(R.id.skylin_inditor_i_make_bottomline);
        skylinInditor_tv_ijoin = view.findViewById(R.id.skylin_inditor_i_join);
        skylinInditor_tv_ijoin_bottomline = view.findViewById(R.id.skylin_inditor_i_join_bottomline);
        skylininditor_rl_left = view.findViewById(R.id.skylininditor_rl_left);
        skylininditor_rl_right = view.findViewById(R.id.skylininditor_rl_right);

    }

    public void setTitle(String title1, String title2) {
        skylinInditor_tv_imake.setText(title1);
        skylinInditor_tv_ijoin.setText(title2);
    }

    public void setRelativeViewPager(ViewPager relativeViewPager, final Button teammanager_btn_create) {
        viewPager = relativeViewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setSelectOne(true);
                    setSelectTwo(false);
                    if (teammanager_btn_create != null)
                        teammanager_btn_create.setVisibility(View.VISIBLE);
                } else {
                    setSelectOne(false);
                    setSelectTwo(true);
                    if (teammanager_btn_create != null)
                        teammanager_btn_create.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        skylininditor_rl_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        skylininditor_rl_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);

            }
        });

    }

    private void setSelectOne(boolean select) {
        skylinInditor_tv_imake.setTextColor(select ? getResources().getColor(R.color.primary) : Color.GRAY);
        skylinInditor_tv_imake_bottomline.setBackgroundColor(select ? getResources().getColor(R.color.primary) : Color.GRAY);

    }

    private void setSelectTwo(boolean select) {
        skylinInditor_tv_ijoin.setTextColor(select ? getResources().getColor(R.color.primary) : Color.GRAY);
        skylinInditor_tv_ijoin_bottomline.setBackgroundColor(select ? getResources().getColor(R.color.primary) : Color.GRAY);

    }

}
