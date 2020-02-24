package com.osiog.myoldmancare.Utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by OSIOG on 2018/6/30.
 */

public interface RecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int position);
}
