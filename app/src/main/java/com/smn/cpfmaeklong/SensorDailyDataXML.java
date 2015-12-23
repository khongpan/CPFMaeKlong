package com.smn.cpfmaeklong;

/**
 * Created by Mink on 12/10/2015.
 */

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SensorDailyDataXML {
    private String strUrl=null;
    private String[] strDateTime =new String[288];
    private String[] strValue = new String[288];
    private String mIoName;
    private int iRecordCount;

    private XmlPullParserFactory xmlFactoryObject;
    private volatile boolean parsingComplete = false;


    public SensorDailyDataXML(String url){
        this.strUrl = url;
    }

    public boolean isFetchComplete(){
        return parsingComplete;
    }

    public String[] getDatas() {
        return strValue;
    }

    public int getCountRecord (){
        return iRecordCount;
    }

    public String getIoName() {
        return mIoName;
    }

    public float getDataValue(int index) {

        String str=strValue[index];

        return Float.parseFloat(str);

    }

    public void parseXML(XmlPullParser myParser) {
        int event,i = 0;
        String text=null;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("IO")) {
                            mIoName = myParser.getAttributeValue(null, "Name");
                        }
                            break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "Value":
                                strValue[i] = text;
                                i++;
                                iRecordCount++;
                                break;
                            case "IODateTime":
                                strDateTime[i] = text;
                                break;
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void fetchXML(String reqUrl){

        strUrl = reqUrl;

        parsingComplete = false;
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(strUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);
                    parseXML(myparser);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
