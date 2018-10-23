package com.marveric.bestitude.smartsurvey;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

    public void doConnectFile(File logFileToUpload) throws IOException{
        URL serverUrl = new URL(this.url);
        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

        String boundaryString = "----SomeRandomText";
 //       String fileUrl = "/logs/20150208.log";
   //     File logFileToUpload = new File(fileUrl);

// Indicate that we want to write to the HTTP request body
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);

        OutputStream outputStreamToRequestBody = urlConnection.getOutputStream();
        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(outputStreamToRequestBody));

// Include value from the myFileDescription text area in the post data
        httpRequestBodyWriter.write("\n\n--" + boundaryString + "\n");
        httpRequestBodyWriter.write("Content-Disposition: form-data; name=\"myFileDescription\"");
        httpRequestBodyWriter.write("\n\n");
        httpRequestBodyWriter.write("Schema file");

// Include the section to describe the file
        httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
        httpRequestBodyWriter.write("Content-Disposition: form-data;"
                + "name=\"myFile\";"
                + "filename=\""+ logFileToUpload.getName() +"\""
                + "\nContent-Type: text/plain\n\n");
        httpRequestBodyWriter.flush();

// Write the actual file contents
        FileInputStream inputStreamToLogFile = new FileInputStream(logFileToUpload);

        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
            outputStreamToRequestBody.write(dataBuffer, 0, bytesRead);
        }

        outputStreamToRequestBody.flush();

// Mark the end of the multipart http request
        httpRequestBodyWriter.write("\n--" + boundaryString + "--\n");
        httpRequestBodyWriter.flush();

// Close the streams
        outputStreamToRequestBody.close();
        httpRequestBodyWriter.close();
        this.responseCode = urlConnection.getResponseCode();
        BufferedReader httpResponseReader =
                new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String result = "";
        String lineRead;
        while((lineRead = httpResponseReader.readLine()) != null) {
            result += lineRead;
        }
        this.response = result;
        this.isConnected = true;
        urlConnection.disconnect();

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
        if(BuildConfig.DEBUG) Log.i("insidecookie", "ins"+this.cookie);
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
