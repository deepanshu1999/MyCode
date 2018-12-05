package com.adjonline.mojitolabs;

public class ListData {

    private String text1;
    private String judge;
    private String date;
    private String printlink;

    public ListData(String t1,String t2,String t3,String t4)
    {
        text1=t1;
        judge=t2;
        date=t3;
        printlink=t4;
    }

    public String getDate() {
        return date;
    }

    public String getJudge() {
        return judge;
    }

    public String getText1() {
        return text1;
    }

    public String getPrintlink() {
        return printlink;
    }
}
