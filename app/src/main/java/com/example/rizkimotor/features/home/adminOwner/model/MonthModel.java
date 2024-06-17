package com.example.rizkimotor.features.home.adminOwner.model;

public class MonthModel {

    private long January;
    private long February;
    private long March;
    private long April;
    private long May;
    private long June;
    private long July;
    private long August;
    private long September;
    private long October;
    private long November;
    private long December;

    public MonthModel(long january, long february, long march, long april, long may, long june, long july, long august, long september, long october, long november, long december) {

    }

    public long getMonthValue(int val) {
        switch (val) {
            case 1:
                return January;
            case 2:
                return February;
            case 3:
                return March;
            case 4:
                return April;
            case 5:
                return May;
            case 6:
                return June;
            case 7:
                return July;
            case 8:
                return August;
            case 9:
                return September;
            case 10:
                return October;
            case 11:
                return November;
            case 12:
                return December;
            default:
                throw new IllegalArgumentException("Invalid month value: " + val);
        }
    }

    public long getJanuary() {
        return January;
    }

    public void setJanuary(long january) {
        January = january;
    }

    public long getFebruary() {
        return February;
    }

    public void setFebruary(long february) {
        February = february;
    }

    public long getMarch() {
        return March;
    }

    public void setMarch(long march) {
        March = march;
    }

    public long getApril() {
        return April;
    }

    public void setApril(long april) {
        April = april;
    }

    public long getMay() {
        return May;
    }

    public void setMay(long may) {
        May = may;
    }

    public long getJune() {
        return June;
    }

    public void setJune(long june) {
        June = june;
    }

    public long getJuly() {
        return July;
    }

    public void setJuly(long july) {
        July = july;
    }

    public long getAugust() {
        return August;
    }

    public void setAugust(long august) {
        August = august;
    }

    public long getSeptember() {
        return September;
    }

    public void setSeptember(long september) {
        September = september;
    }

    public long getOctober() {
        return October;
    }

    public void setOctober(long october) {
        October = october;
    }

    public long getNovember() {
        return November;
    }

    public void setNovember(long november) {
        November = november;
    }

    public long getDecember() {
        return December;
    }

    public void setDecember(long december) {
        December = december;
    }
}
