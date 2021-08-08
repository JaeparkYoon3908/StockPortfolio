package com.yjpapp.swipelayout.implments;

import android.view.View;
import android.widget.BaseAdapter;


import androidx.recyclerview.widget.RecyclerView;

import com.yjpapp.swipelayout.SimpleSwipeListener;
import com.yjpapp.swipelayout.SwipeLayout;
import com.yjpapp.swipelayout.interfaces.SwipeAdapterInterface;
import com.yjpapp.swipelayout.interfaces.SwipeItemMangerInterface;
import com.yjpapp.swipelayout.util.Attributes.Mode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * SwipeItemMangerImpl is a helper class to help all the adapters to maintain open status.
 */
public abstract class SwipeItemMangerImpl implements SwipeItemMangerInterface {
    private Mode mode;
    public final int INVALID_POSITION;
    protected int mOpenPosition;
    protected Set<Integer> mOpenPositions;
    protected Set<SwipeLayout> mShownLayouts;
    protected BaseAdapter mBaseAdapter;
    protected RecyclerView.Adapter mRecyclerAdapter;

    public SwipeItemMangerImpl(BaseAdapter adapter) {
        this.mode = Mode.Single;
        this.INVALID_POSITION = -1;
        this.mOpenPosition = -1;
        this.mOpenPositions = new HashSet();
        this.mShownLayouts = new HashSet();
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter can not be null");
        } else if (!(adapter instanceof SwipeItemMangerInterface)) {
            throw new IllegalArgumentException("adapter should implement the SwipeAdapterInterface");
        } else {
            this.mBaseAdapter = adapter;
        }
    }

    public SwipeItemMangerImpl(RecyclerView.Adapter adapter) {
        this.mode = Mode.Single;
        this.INVALID_POSITION = -1;
        this.mOpenPosition = -1;
        this.mOpenPositions = new HashSet();
        this.mShownLayouts = new HashSet();
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter can not be null");
        } else if (!(adapter instanceof SwipeItemMangerInterface)) {
            throw new IllegalArgumentException("adapter should implement the SwipeAdapterInterface");
        } else {
            this.mRecyclerAdapter = adapter;
        }
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        this.mOpenPositions.clear();
        this.mShownLayouts.clear();
        this.mOpenPosition = -1;
    }

    public abstract void initialize(View var1, int var2);

    public abstract void updateConvertView(View var1, int var2);

    public abstract void bindView(View var1, int var2);

    public int getSwipeLayoutId(int position) {
        if (this.mBaseAdapter != null) {
            return ((SwipeAdapterInterface)((SwipeAdapterInterface)this.mBaseAdapter)).getSwipeLayoutResourceId(position);
        } else {
            return this.mRecyclerAdapter != null ? ((SwipeAdapterInterface)((SwipeAdapterInterface)this.mRecyclerAdapter)).getSwipeLayoutResourceId(position) : -1;
        }
    }

    public void openItem(int position) {
        if (this.mode == Mode.Multiple) {
            if (!this.mOpenPositions.contains(position)) {
                this.mOpenPositions.add(position);
            }
        } else {
            this.mOpenPosition = position;
        }

        if (this.mBaseAdapter != null) {
            this.mBaseAdapter.notifyDataSetChanged();
        } else if (this.mRecyclerAdapter != null) {
            this.mRecyclerAdapter.notifyDataSetChanged();
        }

    }

    public void closeItem(int position) {
        if (this.mode == Mode.Multiple) {
            this.mOpenPositions.remove(position);
        } else if (this.mOpenPosition == position) {
            this.mOpenPosition = -1;
        }

        if (this.mBaseAdapter != null) {
            this.mBaseAdapter.notifyDataSetChanged();
        } else if (this.mRecyclerAdapter != null) {
            this.mRecyclerAdapter.notifyDataSetChanged();
        }

    }

    public void closeAllExcept(SwipeLayout layout) {
        Iterator i$ = this.mShownLayouts.iterator();

        while(i$.hasNext()) {
            SwipeLayout s = (SwipeLayout)i$.next();
            if (s != layout) {
                s.close();
            }
        }

    }

    public void closeAllItems() {
        if (this.mode == Mode.Multiple) {
            this.mOpenPositions.clear();
        } else {
            this.mOpenPosition = -1;
        }

        Iterator i$ = this.mShownLayouts.iterator();

        while(i$.hasNext()) {
            SwipeLayout s = (SwipeLayout)i$.next();
            s.close();
        }

    }

    public void removeShownLayouts(SwipeLayout layout) {
        this.mShownLayouts.remove(layout);
    }

    public List<Integer> getOpenItems() {
        return (List)(this.mode == Mode.Multiple ? new ArrayList(this.mOpenPositions) : Arrays.asList(this.mOpenPosition));
    }

    public List<SwipeLayout> getOpenLayouts() {
        return new ArrayList(this.mShownLayouts);
    }

    public boolean isOpen(int position) {
        if (this.mode == Mode.Multiple) {
            return this.mOpenPositions.contains(position);
        } else {
            return this.mOpenPosition == position;
        }
    }

    class SwipeMemory extends SimpleSwipeListener {
        private int position;

        SwipeMemory(int position) {
            this.position = position;
        }

        public void onClose(SwipeLayout layout) {
            if (SwipeItemMangerImpl.this.mode == Mode.Multiple) {
                SwipeItemMangerImpl.this.mOpenPositions.remove(this.position);
            } else {
                SwipeItemMangerImpl.this.mOpenPosition = -1;
            }

        }

        public void onStartOpen(SwipeLayout layout) {
            if (SwipeItemMangerImpl.this.mode == Mode.Single) {
                SwipeItemMangerImpl.this.closeAllExcept(layout);
            }

        }

        public void onOpen(SwipeLayout layout) {
            if (SwipeItemMangerImpl.this.mode == Mode.Multiple) {
                SwipeItemMangerImpl.this.mOpenPositions.add(this.position);
            } else {
                SwipeItemMangerImpl.this.closeAllExcept(layout);
                SwipeItemMangerImpl.this.mOpenPosition = this.position;
            }

        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    class OnLayoutListener implements SwipeLayout.OnLayout {
        private int position;

        OnLayoutListener(int position) {
            this.position = position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void onLayout(SwipeLayout v) {
            if (SwipeItemMangerImpl.this.isOpen(this.position)) {
                v.open(false, false);
            } else {
                v.close(false, false);
            }

        }
    }

    class ValueBox {
        SwipeItemMangerImpl.OnLayoutListener onLayoutListener;
        SwipeItemMangerImpl.SwipeMemory swipeMemory;
        int position;

        ValueBox(int position, SwipeItemMangerImpl.SwipeMemory swipeMemory, SwipeItemMangerImpl.OnLayoutListener onLayoutListener) {
            this.swipeMemory = swipeMemory;
            this.onLayoutListener = onLayoutListener;
            this.position = position;
        }
    }
}