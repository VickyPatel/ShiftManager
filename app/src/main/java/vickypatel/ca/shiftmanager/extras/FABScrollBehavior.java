package vickypatel.ca.shiftmanager.extras;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by vicky on 06/11/15.
 */
public class FABScrollBehavior extends FloatingActionButton.Behavior {


    public FABScrollBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if(dyConsumed > 0 && child.getVisibility() == View.VISIBLE){
            child.animate().translationY(child.getHeight()+10).setInterpolator(new AccelerateInterpolator(2)).start();
            child.setVisibility(View.GONE);
        } else if(dyConsumed < 0 && child.getVisibility() == View.GONE){
            child.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            child.setVisibility(View.VISIBLE);
        }
    }
}