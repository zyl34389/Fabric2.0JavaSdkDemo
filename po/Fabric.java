package com.lzh.po;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

public class Fabric {
    private HFClient client;
    private Channel channel;

    public Fabric(HFClient client, Channel channel) {
        this.client = client;
        this.channel = channel;
    }

    public HFClient getClient() {
        return client;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setClient(HFClient client) {
        this.client = client;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
