package com.example.bff30;

import java.util.List;

public class ItemList {
    private double totalPrice;
    private String currency;
    private String title;
    private String payDate;
    private String creationDate;
    private List<String> debtors;
    private String paying;
    private String description;


    public ItemList(double totalPrice, String currency, String title, String payDate, String creationDate, String paying, List<String> debtors, String description) {
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.title = title;
        this.payDate = payDate;
        this.creationDate = creationDate;
        this.debtors = debtors;
        this.paying = paying;
        this.description = description;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getDebtors() {
        return debtors;
    }

    public void setDebtors(List<String> debtors) {
        this.debtors = debtors;
    }

    public String getPaying() {
        return paying;
    }

    public void setPaying(String paying) {
        this.paying = paying;
    }
}
