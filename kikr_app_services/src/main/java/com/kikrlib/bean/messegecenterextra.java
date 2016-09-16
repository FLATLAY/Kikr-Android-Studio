package com.kikrlib.bean;

/**
 * Created by Tycho on 8/25/2016.
 */

public class messegecenterextra {
    String  user_idrec;
    String user_idsend;
    String otherdata;


    public messegecenterextra(String user_idrec, String user_idsend, String otherdata){


        this.user_idrec = user_idrec;
        this.user_idsend = user_idsend;
        this.otherdata = otherdata;

    }

    public String getuser_idrec() {
        return user_idrec;
    }

    public void setuser_idrec(String created_at) {
        this.user_idrec = user_idrec;
    }

    public String getuser_idsend() {
        return user_idsend;
    }

    public void setuser_idsend(String title) {
        this.user_idsend = user_idsend;
    }

    public String getotherdata() {
        return otherdata;
    }

    public void setotherdata(String text) {
        this.otherdata = text;
    }


}
