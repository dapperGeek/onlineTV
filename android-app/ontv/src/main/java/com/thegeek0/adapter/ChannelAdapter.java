package com.thegeek0.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thegeek0.ontv.ChannelDetailsActivity;
import com.thegeek0.ontv.R;
import com.thegeek0.ontv.ReportChannelActivity;
import com.thegeek0.db.DatabaseHelper;
import com.thegeek0.item.ItemChannel;
import com.thegeek0.util.PopUpAds;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by laxmi.
 */
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ItemRowHolder> {

    private ArrayList<ItemChannel> dataList;
    private Context mContext;
    private int rowLayout;
    private DatabaseHelper databaseHelper;

    public ChannelAdapter(Context context, ArrayList<ItemChannel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final ItemChannel singleItem = dataList.get(position);
        holder.text.setText(singleItem.getChannelName());
        holder.textCategory.setText(singleItem.getChannelCategory());
        Picasso.get().load(singleItem.getImage()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.ratingView.setRating(Float.parseFloat(singleItem.getChannelAvgRate()));
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpAds.ShowInterstitialAds(mContext);
                Intent intent = new Intent(mContext, ChannelDetailsActivity.class);
                intent.putExtra("Id", singleItem.getId());
                mContext.startActivity(intent);
            }
        });

        holder.textMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, holder.textMenu);
                popup.inflate(R.menu.options_menu);
                Menu popupMenu = popup.getMenu();
                if (databaseHelper.getFavouriteById(singleItem.getId())) {
                    popupMenu.findItem(R.id.option_add_favourite).setVisible(false);
                } else {
                    popupMenu.findItem(R.id.option_remove_favourite).setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_add_favourite:
                                ContentValues fav = new ContentValues();
                                fav.put(DatabaseHelper.KEY_ID, singleItem.getId());
                                fav.put(DatabaseHelper.KEY_TITLE, singleItem.getChannelName());
                                fav.put(DatabaseHelper.KEY_IMAGE, singleItem.getImage());
                                fav.put(DatabaseHelper.KEY_CATEGORY, singleItem.getChannelCategory());
                                databaseHelper.addFavourite(DatabaseHelper.TABLE_FAVOURITE_NAME, fav, null);
                                Toast.makeText(mContext, mContext.getString(R.string.favourite_add), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.option_remove_favourite:
                                databaseHelper.removeFavouriteById(singleItem.getId());
                                Toast.makeText(mContext, mContext.getString(R.string.favourite_remove), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.option_report:
                                Intent intent = new Intent(mContext, ReportChannelActivity.class);
                                intent.putExtra("Id", singleItem.getId());
                                intent.putExtra("cName", singleItem.getChannelName());
                                intent.putExtra("cCategory", singleItem.getChannelCategory());
                                intent.putExtra("cImage", singleItem.getImage());
                                intent.putExtra("cRate", singleItem.getChannelAvgRate());
                                mContext.startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text, textCategory, textMenu;
        LinearLayout lyt_parent;
        RatingView ratingView;

        ItemRowHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            textCategory = itemView.findViewById(R.id.textCategory);
            textMenu = itemView.findViewById(R.id.textViewOptions);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            ratingView = itemView.findViewById(R.id.ratingView);
        }
    }
}
