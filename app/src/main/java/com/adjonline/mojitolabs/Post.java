package com.adjonline.mojitolabs;

import com.google.gson.annotations.SerializedName;

/*
{
   "0":"2018(10) ADJ 634 (FB)",
   "cita1":"2018(10) ADJ 634 (FB)",
   "1":"Dilip B. Bhosale, C.J., Ramesh Sinha and Yashwant Varma, JJ.",
   "jud":"Dilip B. Bhosale, C.J., Ramesh Sinha and Yashwant Varma, JJ.",
   "2":"In Re : Provision of Section 14A of SC\/ST (Prevention Of Atrocities) Amendment Act, 2015 Vs. NIL",
   "peti":"In Re : Provision of Section 14A of SC\/ST (Prevention Of Atrocities) Amendment Act, 2015 Vs. NIL",
   "3":"Scheduled Castes\/Scheduled Tribes (Prevention of Atrocities) Amendment Act, 2015-Section 14A(3) second proviso-Criminal Procedure Code, 1973-Section 439-Constitution of India-Article",
   "hncut":"Scheduled Castes\/Scheduled Tribes (Prevention of Atrocities) Amendment Act, 2015-Section 14A(3) second proviso-Criminal Procedure Code, 1973-Section 439-Constitution of India-Article",
   "4":"2018",
   "yer":"2018",
   "5":"October",
   "mn":"October",
   "6":"FB",
   "typ":"FB",
   "link":"http:\/\/user.adjonline.com\/myadj.php?q=2018(10)%20ADJ%20634%20(FB)&r=2018(10)%20ADJ%20634%20(FB)&f=m&u=AmanMalhotra"
}

 */
public class Post {
    @SerializedName("0")
    public String ID;
    public String cita1;
    @SerializedName("1")
    public String top;
    public String jud;
    @SerializedName("2")
    public String middle;
    public String peti;
    @SerializedName("3")
    public String bottom;
    public String hncut;
    @SerializedName("4")
    public String verybottom;
    public String yer;
    @SerializedName("5")
    public String veryverybottom;
    public String mn;
    @SerializedName("6")
    public String toobottom;
    public String typ;
    public String link;
    public Post(){

    }
}
