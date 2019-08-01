package com.thegeek0.item;

public class ItemChannel {

    private String id;
    private String channelUrl;
    private String channelImage;
    private String channelName;
    private String channelCategory;
    private String channelDescription;
    private String channelAvgRate;
    private String channelFee;
    private boolean isTv;
    private boolean isPremium;
    private boolean isSubscribed;

    public ItemChannel() {
        // TODO Auto-generated constructor stub
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String url) {
        this.channelUrl = url;
    }


    public String getImage() {
        return channelImage;
    }

    public void setImage(String image) {
        this.channelImage = image;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelname) {
        this.channelName = channelname;
    }

    public String getDescription() {
        return channelDescription;
    }

    public void setDescription(String desc) {
        this.channelDescription = desc;
    }

    public boolean isTv() {
        return isTv;
    }

    public void setIsTv(boolean flag) {
        this.isTv = flag;
    }

    public String getChannelCategory() {
        return channelCategory;
    }

    public void setChannelCategory(String channelCategory) {
        this.channelCategory = channelCategory;
    }

    public String getChannelAvgRate() {
        return channelAvgRate;
    }

    public void setChannelAvgRate(String channelAvgRate) {
        this.channelAvgRate = channelAvgRate;
    }
    
    public void setIsPremium(boolean premium){
        this.isPremium = premium;
    }

    public boolean isPremium(){
        return this.isPremium;
    }

    public void setIsSubscribed(boolean subscribed){
        this.isSubscribed = subscribed;
    }

    public boolean isSubscribed(){
        return this.isSubscribed;
    }

    public void setChannelFee(String channelFee){
        this.channelFee = channelFee;
    }

    public String getChannelFee(){
        return this.channelFee;
    }
}
