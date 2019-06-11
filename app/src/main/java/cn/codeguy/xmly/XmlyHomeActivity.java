package cn.codeguy.xmly;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.youth.banner.Banner;

import cn.codeguy.xmly.ext.OnPageScrollListener;
import cn.codeguy.xmly.ext.ViewPagerUtil;
import cn.codeguy.xmly.ui.main.SectionsPagerAdapter;

public class XmlyHomeActivity extends AppCompatActivity {

    private View vTopBarBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmly_home);
        vTopBarBg=findViewById(R.id.top_bar_bg);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setTopBarBgAlpha(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    /**
     * 切换到其他页面的时候当前vTopBarBg的透明度
     */
    private float mCurrentAlpha = -1F;

    private void setTopBarBgAlpha(int index) {
        if (index==0) {
            if (mCurrentAlpha != -1) {
                vTopBarBg.setAlpha(mCurrentAlpha);
                mCurrentAlpha = -1;
            }
        } else {
            if (mCurrentAlpha == -1) {
                mCurrentAlpha = vTopBarBg.getAlpha();
            }
            vTopBarBg.setAlpha(1);
        }
    }
}