package vickypatel.ca.shiftmanager.callbacks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vickypatel.ca.shiftmanager.R;

/**
 * Created by VickyPatel on 2015-10-05.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public final ItemTouchHelperAdapter mAdapter;
    public Context context;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, Context context) {
        mAdapter = adapter;
        this.context = context;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }


    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        LinearLayout undo = (LinearLayout) viewHolder.itemView.findViewById(R.id.undo);
        undo.setVisibility(View.VISIBLE);
        RelativeLayout dataCard = (RelativeLayout) viewHolder.itemView.findViewById(R.id.dataCard);



        Button text = (Button) viewHolder.itemView.findViewById(R.id.undo_text);
        text.setText("changed");
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }
        });

//        if (undo != null) {
//            // optional: tapping the message dismisses immediately
//            TextView text = (TextView) viewHolder.itemView.findViewById(R.id.undo_text);
//            text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
//                    }
//            });
//
//            TextView button = (TextView) viewHolder.itemView.findViewById(R.id.undo_button);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
////                    clearView(recyclerView, viewHolder);
//                    undo.setVisibility(View.GONE);
//                }
//            });
//            undo.postDelayed(new Runnable() {
//                public void run() {
//                    if (undo.isShown())
//                        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
//                }
//            },1000);
//        }
    }

    @Override
    public void onChildDraw(Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            final LinearLayout undo = (LinearLayout) viewHolder.itemView.findViewById(R.id.undo);
            RelativeLayout dataCard = (RelativeLayout) viewHolder.itemView.findViewById(R.id.dataCard);
            Button undoButton = (Button) viewHolder.itemView.findViewById(R.id.undo_button);

            final float alpha = 1.0f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            dataCard.setAlpha(alpha);
            dataCard.setTranslationX(dX);
            System.out.println(dX + " " + dY);

            undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                    clearView(recyclerView, viewHolder);
                    undo.setVisibility(View.GONE);
                }
            });




//            undo.postDelayed(new Runnable() {
//                public void run() {
//                    if (undo.isShown())
//                        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
//                }
//            },1000);


//            Drawable d = ContextCompat.getDrawable(context, R.drawable.custom_bottom_border);
//            d.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
//            d.draw(c);


        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


}
