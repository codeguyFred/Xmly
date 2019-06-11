package cn.codeguy.xmly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 */
public class Behavior extends CoordinatorLayout.Behavior<View> {
    private int value;

    public Behavior() {
    }

    public Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        value += dyConsumed;

        View titleBar = coordinatorLayout.getRootView().findViewById(R.id.topbar_title_layout);
        View topBarBg = coordinatorLayout.getRootView().findViewById(R.id.top_bar_bg);

        View arcBg = coordinatorLayout.getRootView().findViewById(R.id.arc_bg);
        int height = titleBar.getHeight();

//        titleBar高度的时候全部显示
//        float abs =Math.min(height, Math.max(value, 0F)) / height;
//        titleBar高度一半的时候全部显示，但是最大透明度值不能超过1
        float abs = Math.min(1, Math.min(height, Math.max(value, 0F)) / height * 2);
        arcBg.setAlpha(abs);
        topBarBg.setAlpha(abs);
    }

}

