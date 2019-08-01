package com.thegeek0.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thegeek0.item.ItemSchedule;
import com.thegeek0.ontv.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ItemRowHolder> {

    private ArrayList<ItemSchedule> dataList;
    private Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ScheduleAdapter(Context context, ArrayList<ItemSchedule> dataList) {
        this.dataList = new ArrayList<>();
        this.dataList = dataList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_schedule, parent, false);
            return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, final int position) {
        final ItemSchedule singleItem = this.dataList.get(position);

                holder.timeView.setText(singleItem.getScheduleTime());
                holder.titleView.setText(singleItem.getScheduleTitle());
    }

    @Override
    public int getItemCount() {
        return (dataList != null ? dataList.size() : 0);
    }

    public int getItemViewType(int position) {
        return dataList.get(position) != null ? TYPE_ITEM : TYPE_HEADER;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView timeView, titleView;
        LinearLayout relativeLayout;

        ItemRowHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.time_view);
            titleView = itemView.findViewById(R.id.title_view);
            relativeLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}