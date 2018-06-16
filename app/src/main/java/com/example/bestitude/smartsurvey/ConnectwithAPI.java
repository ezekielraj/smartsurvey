package com.example.bestitude.smartsurvey;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;


//import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class ConnectwithAPI {

    private String url;
    private String method;
    private String cookie;
    private String response;
    private Integer responseCode;
    private boolean isConnected = false;

    public ConnectwithAPI(String url, String method){
        this.url = url;
        this.method = method;
    }

    public String getCookie(){
        if(!isConnected){
            try {
                throw new IOException("error");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.cookie;
    }
    public String getResponse(){
        if(!isConnected){
            try {
                throw new IOException("some error 1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.response;
    }
    public Integer getResponseCode(){
        if(!isConnected){
            try {
                throw new IOException("some error 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.responseCode;
    }

    public void newConnection(String url, String method){
        this.url = url;
        this.method = method;
        this.cookie = null;
        this.response = null;
        this.responseCode = null;
        this.isConnected = false;
    }

    public void doConnect(Map<String, String> params, String cookie) throws IOException{
        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(this.method);
        if(method.toLowerCase().equals("get")){
            con.setDoInput(true);
            if(cookie != null){
                con.setInstanceFollowRedirects(false);
                con.setRequestProperty("Cookie", cookie);
            }
            con.connect();
        }else if(method.toLowerCase().equals("post")){
            if(params != null){
                con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            }
            if(cookie != null){
                con.setInstanceFollowRedirects(false);
                con.setRequestProperty("Cookie", cookie);
            }
            con.setDoInput(true);
            con.setDoOutput(true);

            con.connect();
            if(params != null){
                OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
                osw.write(makeParams(params));
                osw.flush();
                osw.close();
            }
        }
        if(cookie == null) {
            this.cookie = con.getHeaderField("Set-Cookie");
        }else{
            this.cookie = cookie;
        }
        Log.w("insidecookie", "ins"+this.cookie);
        this.responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String result = "";
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        this.response = result;
        this.isConnected = true;
        con.disconnect();
    }

    private String makeParams(Map<String, String> params){
        if(params.isEmpty())
            return null;
        String result = "";
        for(Entry<String, String> e: params.entrySet()){
            result += e.getKey()+"="+e.getValue()+"&";
        }
//        result = StringUtil.stripSuffix(result, "&");
        return result.substring(0, result.length()-1);
    }
}
