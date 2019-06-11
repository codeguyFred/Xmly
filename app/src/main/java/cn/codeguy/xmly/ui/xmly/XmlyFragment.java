package cn.codeguy.xmly.ui.xmly;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.view.BannerViewPager;

import java.util.ArrayList;

import cn.codeguy.xmly.R;
import cn.codeguy.xmly.SimpleAdapter;
import cn.codeguy.xmly.ext.OnPageScrollListener;
import cn.codeguy.xmly.ext.ViewPagerUtil;

public class XmlyFragment extends Fragment {

    private XmlyViewModel mViewModel;
    private Banner vBanner;
    private BannerViewPager vBannerViewPager;
    private  BannerListener mBannerListener;
    private RecyclerView vList;
    ArrayList<String> imageUrls = new ArrayList<>();

    {
        imageUrls.add("http://pic15.nipic.com/20110628/1369025_192645024000_2.jpg");
        imageUrls.add("http://pic37.nipic.com/20140113/8800276_184927469000_2.png");
        imageUrls.add("http://pic18.nipic.com/20120204/8339340_144203764154_2.jpg");
        imageUrls.add("http://pic1.nipic.com/2009-02-17/200921701719614_2.jpg");
    }


    public static XmlyFragment newInstance() {
        XmlyFragment xmlyFragment = new XmlyFragment();
        return xmlyFragment;
    }

    public BannerListener getBannerListener() {
        return mBannerListener;
    }

    public void setBannerListener(BannerListener mBannerListener) {
        this.mBannerListener = mBannerListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xmly_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vList=view.findViewById(R.id.list);
        vList.setLayoutManager(new LinearLayoutManager(getContext()));
        final SimpleAdapter adapter = new SimpleAdapter(getActivity());
        vList.setAdapter(adapter);
        mViewModel = ViewModelProviders.of(this).get(XmlyViewModel.class);
        mViewModel.getCurrentName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                adapter.setData(10);
            }
        });

        mViewModel.request();
        vBanner = view.findViewById(R.id.banner);
        vBannerViewPager = vBanner.findViewById(R.id.bannerViewPager);
        vBannerViewPager.setOffscreenPageLimit(2);
        vBannerViewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        15, getContext().getResources().getDisplayMetrics()));

        vBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        }).
                //设置图片集合
                        setImages(imageUrls).
                //是否自动播放
                        isAutoPlay(false).
                //banner设置方法全部调用完毕时最后调用
                        start();

        ViewPagerUtil.bind(vBannerViewPager, new OnPageScrollListener() {
            @Override
            public void onPageScroll(int enterPosition, int leavePosition, float percent) {
                int enter = vBanner.toRealPosition(enterPosition);
                int leave =vBanner.toRealPosition(leavePosition);
                if (mBannerListener != null /*&& percent != 2.3841858E-7*/) {
                    mBannerListener.onPageScroll(enter, leave, percent);
                }
//                L.i("ViewPager", "onPageScrolled————>"
//                        + "    进入页面：" + enter
//                        + "    离开页面：" + leave
//                        + "    滑动百分比：" + percent);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public interface BannerListener {
        void onPageScroll(int enterPosition, int leavePosition, float percent);
    }

}
