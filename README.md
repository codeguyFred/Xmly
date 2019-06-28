仿Xmly首页
https://blog.csdn.net/u011208377/article/details/90680636

@[toc](主要节点) 
# 效果
![5d0e4d8cbdf3221132.gif](https://i.loli.net/2019/06/22/5d0e4d8cbdf3221132.gif)
# 实现方案
- [x] ViewPager
- [x] CoordinatorLayout
- [x] Behavior
# Behavior设置在哪里？
- 设置在 你要根据这个View的位置变化而做处理的View上，比如A发生变化，能触发B的变化，所以你要设置在A上，然后在自定义的Behavior处理，这样能通过A的变化来改变其他View。（个人比较喜欢这种方式处理）
- 设置在B上，然后在自定义的Behavior通过layoutDependsOn来指定B要根据哪些View来的变化来变化，这样可以指定多个变化来改变B。 这个也是官方的，在layoutDependsOn的方法入参child就是B，而dependency是parent下的其他View，总之，你依赖的那个dependency发生了变化才会触发Behavior的方法回调。

# ViewPager样式修改
ViewPager切换Item间隔处理

ViewPageronPageScroll监听
```
@Override
public void onPageScroll(int enterPosition, int leavePosition, float percent) {
                try {
                    String picUrl = mBanners.get(enterPosition).getBackground();
                    if (noEverLoad(picUrl)) {
                        vArcTarget.setTag(R.id.banner_tag, picUrl);
                        MMCImageLoader.getInstance().loadUrlImage(getActivity(),
                                picUrl, vArcTarget, R.color.transparent);
                    }
                    vArcTarget.setAlpha(percent);
                    //当超过0.96的时候设置当前的背景图，
                    // 如果在onPageSelect或者state等于SCROLL_STATE_IDLE设置太晚了
                    if (percent >= 0.96F) {
                        MMCImageLoader.getInstance().loadUrlImage(getActivity(),
                                picUrl, vArcCurrent, R.drawable.holder);
                    }
                } catch (Exception e) {
                    //下标异常，空指针异常
                }
            }
```
# 渐变效果处理
## 一张图的渐变方案（自定义View绘制）例如
```
public class ArcView extends android.support.v7.widget.AppCompatImageView {
    /**
     * rgb渐变
     *  argbEvaluator.evaluate(int )
     */
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    Paint mPaint = new Paint();
    RectF vRectF = new RectF();

    {
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.base_color_primary));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }
    public ArcView(Context context) {
        this(context, null, 0);
    }

    public ArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

 @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getContext().getResources().getDisplayMetrics());
        vRectF.set(-v, -getHeight(), getWidth() + v, getHeight());
        canvas.drawArc(vRectF, 0F, 180, true, mPaint);
    }
    
    public void setPaintColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }
}

```
## 两张图片的渐变方案，适合图片，预缓存效果更好
vArcCurrent在下方，vArcTarget在上方，通过加载上面那张之后，根据滑动进度修改透明度，当超过96%透明度的时候将底部的vArcCurrent加载了，即达到掩饰效果，但是首次加载效果依赖网速，本地有缓存之后就完美了。如果想要完美效果需要将图片先预加载Bitmap。这里预加载可以单独判断或者先缓存。

## 三张图片的方案，提前设置好前后两张图片
在两张图的基础上多一些代码。。。

# CoordinatorLayout，Behavior流程，源码简单分析
通过获取CoordinatorLayout的子View的LayoutParms反射解析并创建Behavior实例
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530202151946.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530201825452.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530202338879.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)
然后将CoordinatorLayout.Behavior的位置变化关联给Behavior，这样CoordinatorLayout的一些变化就调用Behavior相应的方法，然后你去Behavior处理方法就行了~
具体可以查看CoordinatorLayout下NestedScrollingParent2接口方法的相关实现
https://developer.android.com/reference/android/support/v4/view/NestedScrollingParent2

它把dependency加在节点Node，而child加在了Edge上
>而这个方法来收集所有的child到mDependencySortedChildren这个List，mChildDag是DirectedAcyclicGraph的实例，这个东西叫做“有向无环图”，即DAG，这里用到的就是树

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530205000818.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)

如果没有解析到behavior就设置一个默认的behavior

通过两个for循环比较 onChildViewsChanged这个方法会回调

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530204450427.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)

# 选取两张图片方案的实操过程 
----
- 继承 CoordinatorLayout.Behavior编写 Behavior的全路径名，例如com.xxx.xxx.base.lib.view.banner.BannerBehavior或者用".类名"来指定
- 编写Behavior有两种思路
1 在CoordinatorLayout层级下设置在你想要改变的那个View上，然后通过重写Behavior的layoutDependsOn方法找到CoordinatorLayout节点下的某个你需要跟随变化的View A，然后你可以在onDependentViewChanged来根据dependency的变化来改变child；（如果发现无效果，请看方法2）
2 滚动类型的，比如我们的滚动改变透明度，可以两种方式
  * child与dependency有间距，移动后位于denpency边界处，例如上方，下方，那么我们可以用默认的@string/appbar_scrolling_view_behavior来处理
  * 相对于紧贴着控件，比如child上方贴着dependency下方的控件，继续往上滑动之后，并不会回调onDependentViewChanged方法
如果你第一次使用，建议默认创建ScrollingActivity看看代码和效果
布局会有
```java
<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fitsSystemWindows="true"
        tools:context=".ScrollingActivity">

    <android.support.design.widget.AppBarLayout
            android:id="@+id/app_bar"
            android:fitsSystemWindows="true"
            android:layout_height="@dimen/app_bar_height"
            android:layout_width="match_parent"
            android:theme="@style/AppTheme.AppBarOverlay">

        <android.support.design.widget.CollapsingToolbarLayout
                android:id="@+id/toolbar_layout"
                android:fitsSystemWindows="true"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                app:toolbarId="@+id/toolbar"
                app:layout_scrollFlags="scroll|exitUntilCollapsed"
                app:contentScrim="?attr/colorPrimary">

            <android.support.v7.widget.Toolbar
                    android:id="@+id/toolbar"
                    android:layout_height="?attr/actionBarSize"
                    android:layout_width="match_parent"
                    app:layout_collapseMode="pin"
                    app:popupTheme="@style/AppTheme.PopupOverlay"/>

        </android.support.design.widget.CollapsingToolbarLayout>
    </android.support.design.widget.AppBarLayout>


    <android.support.v4.widget.NestedScrollView
            app:layout_behavior="@string/appbar_scrolling_view_behavior"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

        <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="@dimen/text_margin"
                android:text="@string/large_text"/>

    </android.support.v4.widget.NestedScrollView>

</android.support.design.widget.CoordinatorLayout>
```
而自带的behavior有这几个
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530181236763.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)
## 定义Behavior
```
public class BannerBehavior extends CoordinatorLayout.Behavior<View> {
    private int value;
    public BannerBehavior() {}
    public BannerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onStartNestedScroll(
    @NonNull CoordinatorLayout coordinatorLayout,
    @NonNull View child,
     @NonNull View directTargetChild,
     @NonNull View target, int axes, int type) {
     //竖直滑动的才拦截
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        value += dyConsumed;
        //这里用getRootView()是因为topbar_title_layout和top_bar_bg不在当前的layout上。而为了方便找arc_bg我也这样获取了，你也可以设置成dependencyView
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
```
## 设置behavior
然后我这边的布局是这样的将base_banner_behavior设置在StatusView上，只能设置在CoordinatorLayout节点下
```
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.linghit.lingjidashi.base.lib.view.StatusView
        android:id="@+id/base_state_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/base_banner_behavior">
        <com.scwang.smartrefresh.layout.SmartRefreshLayout
            android:id="@+id/base_refresh_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:srlDisableContentWhenRefresh="true">

            <android.support.v7.widget.RecyclerView
                android:id="@+id/base_refresh_list"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />
        </com.scwang.smartrefresh.layout.SmartRefreshLayout>
    </com.linghit.lingjidashi.base.lib.view.StatusView>
</android.support.design.widget.CoordinatorLayout>
```
# onStartNestedScroll onNestedScroll的回调流程
在CoordinatorLayout的onStartNestedScroll方法是这样的
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531175648388.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)
而setNestedScrollAccepted只是设置下这两种状态下是否接收，然后通过isNestedScrollAccepted获取状态，如果accepted就回调onNestedScrollAccepted给Behavior，所以相应的其他方法在一些相应的状态会回调，而你在你想要的方法回调处理就行，比如我的需要根据滑动状态即时改变状态，所以我选择在onNestedScroll处理。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190531180306600.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEyMDgzNzc=,size_16,color_FFFFFF,t_70)
Demo：[Gayhub直连](https://github.com/codeguyFred/Xmly)
~~番外篇：[喜马拉雅FM首页源码剖析实现过程]()~~
PS:
- TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getContext().getResources().getDisplayMetrics());
- getContext().getResources().getIdentifier("login_xx_" + code,
                            "string", getActivity().getPackageName());
                            
其他收获
- 缓存池 https://www.jianshu.com/p/2563ea1cb87f
- 有序无向图 https://www.jianshu.com/p/73e6c552c62a

CoordinatorLayout分析参考
- https://www.jianshu.com/p/b987fad8fcb4
- https://www.jianshu.com/p/f267fdbda794
- https://www.jianshu.com/p/6547ec3202bd
