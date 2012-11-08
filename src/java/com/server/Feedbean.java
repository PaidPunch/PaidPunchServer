package com.server;

/**
 * @author qube26
 */
public class Feedbean {
    String fbid = "", action = "", name = "", price = "", buss_name = "", saveprice = "", date, time = "",
            mystery_punchid = "", punchcard_id = "", app_user_id = "", timestamp = "", ismysterypunch = "";
    String no_of_punches_per_card = "", value_of_each_punch = "", selling_price_of_punch_card = "",
            effective_discount = "", offer = "", isfbaccount = "", disc_value_of_each_punch = "";

    public String getDisc_value_of_each_punch() {
        return disc_value_of_each_punch;
    }

    public void setDisc_value_of_each_punch(String disc_value_of_each_punch) {
        this.disc_value_of_each_punch = disc_value_of_each_punch;
    }

    public String getIsfbaccount() {
        return isfbaccount;
    }

    public void setIsfbaccount(String isfbaccount) {
        this.isfbaccount = isfbaccount;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    boolean ismyfrend;

    public boolean isIsmyfrend() {
        return ismyfrend;
    }

    public void setIsmyfrend(boolean ismyfrend) {
        this.ismyfrend = ismyfrend;
    }

    public String getEffective_discount() {
        return effective_discount;
    }

    public void setEffective_discount(String effective_discount) {
        this.effective_discount = effective_discount;
    }

    public String getNo_of_punches_per_card() {
        return no_of_punches_per_card;
    }

    public void setNo_of_punches_per_card(String no_of_punches_per_card) {
        this.no_of_punches_per_card = no_of_punches_per_card;
    }

    public String getSelling_price_of_punch_card() {
        return selling_price_of_punch_card;
    }

    public void setSelling_price_of_punch_card(String selling_price_of_punch_card) {
        this.selling_price_of_punch_card = selling_price_of_punch_card;
    }

    public String getValue_of_each_punch() {
        return value_of_each_punch;
    }

    public void setValue_of_each_punch(String value_of_each_punch) {
        this.value_of_each_punch = value_of_each_punch;
    }

    public String getIsmysterypunch() {
        return ismysterypunch;
    }

    public void setIsmysterypunch(String ismysterypunch) {
        this.ismysterypunch = ismysterypunch;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getPunchcard_id() {
        return punchcard_id;
    }

    public void setPunchcard_id(String punchcard_id) {
        this.punchcard_id = punchcard_id;
    }

    public String getMystery_punchid() {
        return mystery_punchid;
    }

    public void setMystery_punchid(String mystery_punchid) {
        this.mystery_punchid = mystery_punchid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBuss_name() {
        return buss_name;
    }

    public void setBuss_name(String buss_name) {
        this.buss_name = buss_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSaveprice() {
        return saveprice;
    }

    public void setSaveprice(String saveprice) {
        this.saveprice = saveprice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
