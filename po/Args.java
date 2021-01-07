package com.lzh.po;

public class Args {
    private String channelName;
    private String chaincodeName;
    private String funcName;
    private String args;

    public Args() {
    }

    public Args(String channelName, String chaincodeName, String funcName, String args) {
        this.channelName = channelName;
        this.chaincodeName = chaincodeName;
        this.funcName = funcName;
        this.args = args;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
