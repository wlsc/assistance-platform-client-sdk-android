package de.tudarmstadt.informatik.tk.android.kraken.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

@Deprecated
public class NonSwipeableViewPager extends ViewPager {

    private boolean enabled;

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            try {
                return super.onTouchEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            try {
                return super.onInterceptTouchEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}