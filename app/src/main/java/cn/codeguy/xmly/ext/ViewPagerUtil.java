package cn.codeguy.xmly.ext;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

/**
 * <b>Package:</b> com.linghit.lingjidashi.base.lib.view.banner.ext <br>
 * <b>Create Date:</b> 2019/5/20  01:45 <br>
 * <b>@author:</b> leida <br>
 * <b>Description:</b> ViewPager工具类 <br>
 */
public class ViewPagerUtil {
    /**
     * 给ViewPager绑定自定义的滚动监听
     */
    public static void bind(@NonNull ViewPager viewPager, @NonNull OnPageScrollListener onPageScrollListener) {
        final ViewPagerHelper helper = new ViewPagerHelper();
        // 给helper设置滚动监听
        helper.setOnPageScrollListener(onPageScrollListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                helper.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                helper.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                helper.onPageScrollStateChanged(state);
            }
        });
    }
}