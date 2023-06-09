package com.worthybitbuilders.squadsense.models;

import com.worthybitbuilders.squadsense.utils.ConvertUtils;

import java.sql.Timestamp;
import java.util.Date;

public class Notification {

    private String _id;
    private String senderId;
    private String receiverId;

    private String notificationType;
    private String title;
    private String content;
    private boolean isRead;
    private String link;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public Notification(){}

    public String getId(){ return _id; }

    public void setId(String _id){ this._id = _id; }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getTimeCreated()
    {
        return createdAt;
    }
    public String getCustomTimeCreated()
    {
        //if the notification is sent today -> show time else show date
        String dateNow = ConvertUtils.DateToString(new Date(), ConvertUtils.Pattern.DAY_MONTH_YEAR);
        String dateCreatedDMY = ConvertUtils.TimestampsToString(createdAt, ConvertUtils.Pattern.DAY_MONTH_YEAR);

        if(dateNow.equals(dateCreatedDMY))
            return getTimeCreatedHm();
        else
            return getTimeCreatedDMY();
    }

    public String getTimeCreatedDMY() {
        return ConvertUtils.TimestampsToString(createdAt, ConvertUtils.Pattern.DAY_MONTH_YEAR);
    }

    public String getTimeCreatedHm() {
        return ConvertUtils.TimestampsToString(createdAt, ConvertUtils.Pattern.HOUR_MINUTE);
    }

    public String getTimeUpdate() {
        //if the notification is sent today -> show time else show date

        String dateNow = ConvertUtils.DateToString(new Date(), ConvertUtils.Pattern.DAY_MONTH_YEAR);
        String dateCreatedDMY = ConvertUtils.TimestampsToString(updatedAt, ConvertUtils.Pattern.DAY_MONTH_YEAR);
        String dateCreatedHm = ConvertUtils.TimestampsToString(updatedAt, ConvertUtils.Pattern.HOUR_MINUTE);

        if(dateNow.equals(dateCreatedDMY))
            return dateCreatedHm;
        else
            return dateCreatedDMY;
    }
}
