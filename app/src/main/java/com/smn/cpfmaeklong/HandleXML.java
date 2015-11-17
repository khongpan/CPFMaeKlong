package com.smn.cpfmaeklong;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandleXML {
    private String LastValue = "-";
    private String LastIODateTime = "-";
    private String[] Message =new String[10000];
    private String[] NodeDateTime =new String[10000];
    int countRecord = 0,index = -1,index2 = -1;
    private String urlString = null;
    private String[] ValueY = new String[300] ;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    public HandleXML(String url){
        this.urlString = url;
    }
    public String getLastValue(){
        return LastValue;
    }
    public String getLastIODateTime(){
        return LastIODateTime;
    }
    public int getCountRecord (){
        return countRecord;
    }
    public String getValue(){
        index++;
        return ValueY[index];
    }
    public String Message(){
        index2++;
        return Message[index2];
    }
    public String NodeDateTime(){
        index++;
        return NodeDateTime[index];
    }
    public void parseXMLAndStoreIt(XmlPullParser myParser) {
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
                            case "LastValue":
                                LastValue = text;
                                break;
                            case "LastIODateTime":
                                LastIODateTime = text;
                                break;
                            case "Value":
                                ValueY[i] = text;
                                i++;
                                countRecord = countRecord + 1;
                                break;
                            case "NodeDateTime":
                                NodeDateTime[i] = text;
                                break;
                            case "Message":
                                Message[i] = text;
                                i++;
                                countRecord++;
                                break;
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
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
                    parseXMLAndStoreIt(myparser);
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