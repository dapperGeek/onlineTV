package com.app.item;

public class ItemSchedule {
    private String scheduleId;
    private String scheduleTitle;
    private String scheduleTime;

    public ItemSchedule(){

    }

    public void setScheduleId(String Id){
        this.scheduleId = Id;
    }

    public String getScheduleId(){
        return scheduleId;
    }

    public void setScheduleTime(String scheduleTime){
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleTime() {
        return this.scheduleTime;
    }

    public void setScheduleTitle(String scheduleTitle){
        this.scheduleTitle = scheduleTitle;
    }

    public String getScheduleTitle(){
        return this.scheduleTitle;
    }
}
