package com.example.android.elderlyapp.core;

import android.net.Uri;

/**
 * Created by Darsh_Shah on 25-06-2017.
 */

public class GoogleInfo {
    Uri uri;
    String name;
    public GoogleInfo(){

    }
    public GoogleInfo(Uri uri,String name){
        this.uri=uri;
        this.name=name;
    }

    public String getName(){
        return name;
    }
    public Uri getPhoto(){
        return uri;
    }

}
