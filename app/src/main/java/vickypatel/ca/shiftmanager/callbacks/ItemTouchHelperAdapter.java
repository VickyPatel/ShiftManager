package vickypatel.ca.shiftmanager.callbacks;

/**
 * Created by VickyPatel on 2015-10-05.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}