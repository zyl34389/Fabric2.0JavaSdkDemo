package com.lzh.po;

import com.lzh.installChaincode.Query;

import java.util.ArrayList;

public class ChannelChaincode {
    private String channelName;
    private ArrayList<Query.Result> arr;

    public ChannelChaincode(String channelName, ArrayList<Query.Result> arr) {
        this.channelName = channelName;
        this.arr = arr;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public ArrayList<Query.Result> getArr() {
        return arr;
    }

    public void setArr(ArrayList<Query.Result> arr) {
        this.arr = arr;
    }

    @Override
    public String toString() {
        return "{" +
                "\"channelName\":\""+channelName +"\""+
                ",\"chaincode\":" + arr.toString() +
                '}';
    }
}
