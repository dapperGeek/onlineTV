package com.app.adapter;

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

import com.app.setv.ChannelDetailsActivity;
import com.app.setv.R;
import com.app.item.ItemChannel;
import com.app.util.PopUpAds;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ItemRowHolder> {

    private ArrayList<ItemChannel> dataList;
    private Context mContext;
    private int rowLayout;

    public FavouriteAdapter(Context context, ArrayList<ItemChannel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, final int position) {
        final ItemChannel singleItem = dataList.get(position);
        holder.text.setText(singleItem.getChannelName());
        holder.textCategory.setText(singleItem.getChannelCategory());
        Picasso.get().load(singleItem.getImage()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpAds.ShowInterstitialAds(mContext);
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
        TextView text, textCategory,textMenu;
        LinearLayout lyt_parent;
        RatingView ratingView;

        ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textCategory = itemView.findViewById(R.id.textCategory);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ratingView = itemView.findViewById(R.id.ratingView);
            textMenu = itemView.findViewById(R.id.textViewOptions);
            ratingView.setVisibility(View.GONE);
            textMenu.setVisibility(View.GONE);
        }
    }
}
