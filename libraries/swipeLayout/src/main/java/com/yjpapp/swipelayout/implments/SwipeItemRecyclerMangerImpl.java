package com.yjpapp.swipelayout.implments;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.yjpapp.swipelayout.SwipeLayout;

public class SwipeItemRecyclerMangerImpl extends SwipeItemMangerImpl {
    protected RecyclerView.Adapter mAdapter;

    public SwipeItemRecyclerMangerImpl(RecyclerView.Adapter adapter) {
        super(adapter);
        this.mAdapter = adapter;
    }

    public void bindView(View target, int position) {
        int resId = this.getSwipeLayoutId(position);
        OnLayoutListener onLayoutListener = new OnLayoutListener(position);
        SwipeLayout swipeLayout = (SwipeLayout)target.findViewById(resId);
        if (swipeLayout == null) {
            throw new IllegalStateException("can not find SwipeLayout in target view");
        } else {
            if (swipeLayout.getTag(resId) == null) {
                SwipeMemory swipeMemory = new SwipeMemory(position);
                swipeLayout.addSwipeListener(swipeMemory);
                swipeLayout.addOnLayoutListener(onLayoutListener);
                swipeLayout.setTag(resId, new ValueBox(position, swipeMemory, onLayoutListener));
                this.mShownLayouts.add(swipeLayout);
            } else {
                ValueBox valueBox = (ValueBox)swipeLayout.getTag(resId);
                valueBox.swipeMemory.setPosition(position);
                valueBox.onLayoutListener.setPosition(position);
                valueBox.position = position;
            }

        }
    }

    public void initialize(View target, int position) {
    }

    public void updateConvertView(View target, int position) {
    }
}