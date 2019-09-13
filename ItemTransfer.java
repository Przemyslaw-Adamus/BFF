package com.example.bff30;

public class ItemTransfer {
    private int id;
    private String title;
    private int senderId;
    private String senderName;
    private int recipientId;
    private String recipientName;
    private float value;
    private int settlementId;
    private String date;
    private int isPaid;
    private int isSend;

    public ItemTransfer(int id, String title, int senderId, int recipientId, float value, int settlementId) {
        this.id = id;
        this.title = title;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.value = value;
        this.settlementId = settlementId;
    }

    public ItemTransfer(int id, String title, int senderId, String senderName, int recipientId, String recipientName, float value, int settlementId) {
        this.id = id;
        this.title = title;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.value = value;
        this.settlementId = settlementId;
    }

    public ItemTransfer(int id, String title, int senderId, String senderName, int recipientId, String recipientName, float value, int settlementId, String date, int isPaid, int isSend) {
        this.id = id;
        this.title = title;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.value = value;
        this.settlementId = settlementId;
        this.date = date;
        this.isPaid = isPaid;
        this.isSend = isSend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(int settlementId) {
        this.settlementId = settlementId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int isPaid() {
        return isPaid;
    }

    public void setPaid(int paid) {
        isPaid = paid;
    }

    public int isSend() {
        return isSend;
    }

    public void setSend(int send) {
        isSend = send;
    }
}
