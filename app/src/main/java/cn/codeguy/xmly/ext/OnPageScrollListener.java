package cn.codeguy.xmly.ext;

/**
 * <b>Package:</b> com.linghit.lingjidashi.base.lib.view.banner.ext <br>
 * <b>Create Date:</b> 2019/5/20  01:45 <br>
 * <b>@author:</b> leida <br>
 * <b>Description:</b> ViewPage的页面滚动监听器 <br>
 */
public interface OnPageScrollListener {
    /**
     * 页面滚动时调用
     *
     * @param enterPosition 进入页面的位置
     * @param leavePosition 离开的页面的位置
     * @param percent       滑动百分比
     */
    void onPageScroll(int enterPosition, int leavePosition, float percent);

    /**
     * 页面选中时调用
     *
     * @param position 选中页面的位置
     */
    void onPageSelected(int position);

    /**
     * 页面滚动状态变化时调用
     *
     * @param state 页面的滚动状态
     */
    void onPageScrollStateChanged(int state);
}