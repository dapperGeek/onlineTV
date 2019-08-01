package com.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.setv.ChannelDetailsActivity;
import com.app.setv.R;
import com.app.item.ItemChannel;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Activity context;
    private ArrayList<ItemChannel> mList;

    public SliderAdapter(Activity context, ArrayList<ItemChannel> itemChannels) {
        this.context = context;
        this.mList = itemChannels;
        inflater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View imageLayout = inflater.inflate(R.layout.row_slider_item, container, false);
        assert imageLayout != null;
        ImageView imageView = imageLayout.findViewById(R.id.image);
        TextView channelName = imageLayout.findViewById(R.id.text);
        TextView channelCategory = imageLayout.findViewById(R.id.textCategory);
        RatingView ratingView = imageLayout.findViewById(R.id.ratingView);
        RelativeLayout rootLayout = imageLayout.findViewById(R.id.rootLayout);

        final ItemChannel itemChannel = mList.get(position);
        Picasso.get().load(itemChannel.getImage()).placeholder(R.drawable.placeholder).into(imageView);
        channelName.setText(itemChannel.getChannelName());
        channelCategory.setText(itemChannel.getChannelCategory());
        ratingView.setRating(Float.parseFloat(itemChannel.getChannelAvgRate()));

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChannelDetailsActivity.class);
                intent.putExtra("Id", itemChannel.getId());
                context.startActivity(intent);
            }
        });

        container.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}
