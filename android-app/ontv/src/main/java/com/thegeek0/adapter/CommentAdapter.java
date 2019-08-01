package com.thegeek0.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thegeek0.ontv.R;
import com.thegeek0.item.ItemComment;

import java.util.ArrayList;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemRowHolder> {

    private ArrayList<ItemComment> dataList;
    private Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    public CommentAdapter(Context context, ArrayList<ItemComment> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment_header, parent, false);
            return new VHHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder viewHolder, int position) {

        if (viewHolder.getItemViewType() == TYPE_HEADER) {
            VHHeader VHheader = (VHHeader) viewHolder;
            VHheader.avatar.setContentDescription(mContext.getString(R.string.app_name));
        } else if (viewHolder.getItemViewType() == TYPE_ITEM) {
            final VHItem holder = (VHItem) viewHolder;
            final ItemComment singleItem = dataList.get(position);
            holder.itemTitle.setText(singleItem.getName());
            holder.itemDesc.setText(Html.fromHtml(singleItem.getDesc()));
            holder.itemDate.setText(singleItem.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public int getItemViewType(int position) {
        return dataList.get(position) != null ? TYPE_ITEM : TYPE_HEADER;
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ItemRowHolder(View itemView) {
            super(itemView);
        }
    }

    class VHHeader extends ItemRowHolder {
        ShapedImageView avatar;

        VHHeader(View itemView) {
            super(itemView);
            this.avatar = itemView.findViewById(R.id.avatar);
        }
    }

    class VHItem extends ItemRowHolder {
        TextView itemTitle, itemDesc,itemDate;
        ShapedImageView itemImage;

        VHItem(View itemView) {
            super(itemView);
            this.itemTitle = itemView.findViewById(R.id.name);
            this.itemDesc = itemView.findViewById(R.id.comment);
            this.itemDate = itemView.findViewById(R.id.date);
            this.itemImage = itemView.findViewById(R.id.avatar);
        }
    }
}
