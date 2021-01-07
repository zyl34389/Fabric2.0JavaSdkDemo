package com.lzh.channel;

import com.lzh.chaincode.Init;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChannelConf {
    /**
     * 创建新的通道，生成通道配置块（只能先生成通道配置块，在反序列化才能加入节点）
     * @param channelName 通道名称，需要和通道配置文件中的通道名称一致
     * @param channelTxPath 用通道配置文件生成的tx文件的路径（就是用命令行生成通道配置文件的输出，不是原来的.yaml文件）
     * @param orderer orderer节点
     * @return 是否成功
     * @throws Exception
     */
    public static Boolean createChannelBlock(String channelName, String channelTxPath, Orderer orderer) throws Exception {
        //创建客户端
        HFClient client = Init.createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\priv_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\cert.pem",
                "Admin",
                "org1MSP");

        //由通道文件生成通道配置对象
        ChannelConfiguration cc = new ChannelConfiguration(new File(channelTxPath));
        //用户对通道配置文件签名
        byte[] signData = client.getChannelConfigurationSignature(cc,client.getUserContext());
        //生成通道
        Channel channel = null;
        try {
            channel = client.newChannel(channelName,orderer,cc,signData);
        } catch (TransactionException e) {
            System.out.println("通道名称已存在，请跟换名称，重新生成通道文件！");
            e.printStackTrace();
            return false;
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            return false;
        }
        //序列化
        channel.serializeChannel(new File("./"+channelName+".block"));
//        System.out.println("Fabric 创建通道完成");
        return true;
    }

    /**
     * 将节点加入通道
     * @param channelName 通道名称
     * @param client 节点所在组织的admin用户构成的客户端对象
     * @param peer 要加入的peer节点
     * @return 是否成功
     * @throws Exception
     */
    public static boolean joinPeer(String channelName, HFClient client, Peer peer) throws Exception {
        byte [] channelByte = Files.readAllBytes(Paths.get(("./"+channelName+".block")));
        Channel channel = client.deSerializeChannel(channelByte);
        channel.initialize();
        try {
            channel.joinPeer(peer);
        } catch (ProposalException e) {
            System.out.println("加入节点失败！");
            return false;
        }
        channel.serializeChannel(new File("./"+channelName+".block"));
//        System.out.println(FabricManager.listPeerChannels());//查看peer1Org1加进去没？
        return true;
    }
}
