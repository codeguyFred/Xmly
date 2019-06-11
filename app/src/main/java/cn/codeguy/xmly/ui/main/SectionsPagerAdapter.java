package cn.codeguy.xmly.ui.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.codeguy.xmly.R;
import cn.codeguy.xmly.ui.xmly.XmlyFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private ImageView vCurrent;
    private ImageView vTarget;
    ArrayList<String> mBgs = new ArrayList<>();

    {
        mBgs.add("http://pic15.nipic.com/20110628/1369025_192645024000_2.jpg");
        mBgs.add("http://pic37.nipic.com/20140113/8800276_184927469000_2.png");
        mBgs.add("http://pic18.nipic.com/20120204/8339340_144203764154_2.jpg");
        mBgs.add("http://pic1.nipic.com/2009-02-17/200921701719614_2.jpg");
    }
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Activity mContext;

    public SectionsPagerAdapter(Activity context, FragmentManager fm) {
        super(fm);
        mContext = context;
        vCurrent = mContext.findViewById(R.id.current);
        vTarget = mContext.findViewById(R.id.target);
        Glide.with(mContext).load(mBgs.get(0)).into(vCurrent);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        PlaceholderFragment placeholderFragment = PlaceholderFragment.newInstance(position + 1);
        if (position==0) {
            XmlyFragment xmlyFragment=XmlyFragment.newInstance();
            xmlyFragment.setBannerListener(new XmlyFragment.BannerListener() {
                @Override
                public void onPageScroll(int enterPosition, int leavePosition, float percent) {
                    try {
                        String picUrl = mBgs.get(enterPosition);
                        if (noEverLoad(picUrl)) {
                            vTarget.setTag(R.id.banner_tag, picUrl);
                            Glide.with(mContext).load(picUrl).into(vTarget);
                        }
                        vTarget.setAlpha(percent);
                        //当超过0.96的时候设置当前的背景图，
                        // 如果在onPageSelect或者state等于SCROLL_STATE_IDLE设置太晚了
                        if (percent >= 0.96F) {
                            Glide.with(mContext).load(picUrl).into(vCurrent);
                        }
                    } catch (Exception e) {
                        //下标异常，空指针异常
                    }
                }
            });
            return xmlyFragment;
        }
        else
            return PlaceholderFragment.newInstance(position + 1);
    }

    /**
     * 是否加载过图片
     */
    private boolean noEverLoad(String picUrl) {
        return vTarget.getTag(R.id.banner_tag) == null ||
                !vTarget.getTag(R.id.banner_tag).equals(picUrl);
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}