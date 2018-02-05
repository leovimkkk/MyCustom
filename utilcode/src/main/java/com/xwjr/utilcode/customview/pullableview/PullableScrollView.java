package com.xwjr.utilcode.customview.pullableview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {
    private boolean isCanRefresh = true;
    private boolean isCanLoad = true;

    public boolean isCanLoad() {
        return isCanLoad;
    }

    public void setCanLoad(boolean canLoad) {
        isCanLoad = canLoad;
    }

    public boolean isCanRefresh() {
        return isCanRefresh;
    }

    public void setCanRefresh(boolean canRefresh) {
        isCanRefresh = canRefresh;
    }

    public PullableScrollView(Context context) {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return isCanRefresh && getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return isCanLoad && getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight());
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }


    private ScrollViewListener scrollViewListener = null;
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
    public interface ScrollViewListener {

        void onScrollChanged(PullableScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener){
        this.scrollViewListener = scrollViewListener;
    }
}
