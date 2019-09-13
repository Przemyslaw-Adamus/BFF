package com.example.bff30;

import java.util.List;

public class ItemSettlementList {
    private double totalPrice;
    private String currency;
    private String title;
    private String date;
    private String [] listCosts;

    public ItemSettlementList(double totalPrice, String currency, String title ,String date, String[] listCosts) {
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.title = title;
        this.date = date;
        this.listCosts = listCosts;
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

    public String getDate() {
        return date;
    }

    public void setDate(String payDate) {
        this.date = date;
    }


    public String[] getListCosts() {
        return listCosts;
    }

    public void setListCosts(String[] listCosts) {
        this.listCosts = listCosts;
    }
}
