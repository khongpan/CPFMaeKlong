package com.smn.cpfmaeklong;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mink on 12/27/2015.
 */
public class SensorInfoXML {
    private String mStrUrl=null;

    private String mStrName="-";
    private String mStrType="-";
    private String mStrUnit="-";
    private String mStrIoDateTime="-";
    private String mStrLastValue="-";
    private String mStrDetails="-";



    private XmlPullParserFactory xmlFactoryObject;
    private volatile boolean parsingComplete = false;


    public SensorInfoXML(String url){
        this.mStrUrl = url;
    }

    public boolean isFetchComplete(){
        return parsingComplete;
    }

    public String getIoDateTime() {
        return mStrIoDateTime;
    }

    public String getLastValue() {
        return mStrLastValue;
    }

    public String getIoName() {
        return mStrName;
    }

    public String getDetails() {
        return mStrDetails;
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
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (name) {
                            case "Name":
                                mStrName = text;
                                break;
                            case "Type":
                                mStrType = text;
                                break;
                            case "Unit":
                                mStrType = text;
                                break;
                            case "LastIODateTime":
                                mStrIoDateTime = text;
                                break;
                            case "LastValue":
                                mStrLastValue = text;
                                break;
                            case "Detail":
                                mStrDetails = text;
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

    public void fetchXML(){
        fetchXML(mStrUrl);
    }

    public void fetchXML(String reqUrl){

        mStrUrl = reqUrl;

        parsingComplete = false;
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(mStrUrl);
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
