package com.app.item;

public class ItemCategory {

    private String CategoryId;
    private String CategoryName;
    private String CategoryImageUrl;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String id) {
        this.CategoryId = id;
    }


    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String name) {
        this.CategoryName = name;
    }

    public String getCategoryImage() {
        return CategoryImageUrl;

    }

    public void setCategoryImage(String image) {
        this.CategoryImageUrl = image;
    }

}
