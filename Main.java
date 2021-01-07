package com.lzh;

public class Main {
    public static void main(String[] args) throws Exception {
//        FabricManager.installOneStep("kk","fabcar_1","C:\\Users\\123\\Desktop\\Me\\fabcar");
        boolean isSuccess = FabricManager.createChannel("edc","C:\\Users\\123\\Desktop\\Me\\edc.tx");
        System.out.println(isSuccess);
        System.out.println(FabricManager.listPeerChannels());
    }
}
