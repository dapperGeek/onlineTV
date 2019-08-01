package com.thegeek0.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thegeek0.ontv.ChannelDetailsActivity;
import com.thegeek0.ontv.R;
import com.thegeek0.item.ItemChannel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ItemRowHolder> {

    private ArrayList<ItemChannel> dataList;
    private Context mContext;

    public RelatedAdapter(Context context, ArrayList<ItemChannel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_related_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, final int position) {
        final ItemChannel singleItem = dataList.get(position);
        holder.text.setText(singleItem.getChannelName());
        Picasso.get().load(singleItem.getImage()).placeholder(R.drawable.placeholder_1).into(holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChannelDetailsActivity.class);
                intent.putExtra("Id", singleItem.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        LinearLayout lyt_parent;

        ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
        }
    }
}
