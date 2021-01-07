package com.lzh.chaincode;

import com.lzh.po.Fabric;
import com.lzh.po.LocalUser;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Init {
    /**
     * 初始化org1peer1
     * @param name
     * @return
     * @throws Exception
     */
    public static Fabric initOrg1(String name) throws Exception{
        //创建client
        HFClient client = createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\priv_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\cert.pem",
                "Admin",
                "org1MSP");

        //创建通道实例
        Properties peerProp = createPeerProp("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\tls-0-0-0-0-7052.pem");
        Channel channel = client.newChannel(name);
        Peer peer1 = client.newPeer("p1o1","grpcs://peer1-org1.txhy.com:7501",peerProp);
        channel.addPeer(peer1);

        Orderer orderer1 = client.newOrderer("or1o0","grpcs://orderer1-org0.txhy.com:8050",peerProp);
        channel.addOrderer(orderer1);
        channel.initialize();

        return new Fabric(client,channel);
    }

    /**
     * 初始化org2peer1
     * @param name
     * @return
     * @throws Exception
     */
    public static Fabric initOrg2(String name) throws Exception{
        //创建client实例
        HFClient client = createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org2_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org2_cert.pem",
                "Admin",
                "org2MSP");

        //创建通道实例
        Properties peerProp = createPeerProp("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\tls-0-0-0-0-7052.pem");
        Channel channel = client.newChannel(name);
        Peer peer2 = client.newPeer("p1o2","grpcs://peer1-org2.txhy.com:7551",peerProp);
        channel.addPeer(peer2);
        Orderer orderer1 = client.newOrderer("or1o0","grpcs://orderer1-org0.txhy.com:8050",peerProp);
        channel.addOrderer(orderer1);

        channel.initialize();
        return new Fabric(client,channel);
    }

    /**
     * 初始化org3peer1
     * @param name
     * @return
     * @throws Exception
     */
    public static Fabric initOrg3(String name) throws Exception{
        //创建client实例
        HFClient client = createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org3_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org3_cert.pem",
                "Admin",
                "org3MSP");

        Properties peerProp = createPeerProp("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\tls-0-0-0-0-7052.pem");
        Channel channel = client.newChannel(name);
        Peer peer3 = client.newPeer("p1o3","grpcs://peer1-org3.txhy.com:7601",peerProp);
        channel.addPeer(peer3);
        Orderer orderer1 = client.newOrderer("or1o0","grpcs://orderer1-org0.txhy.com:8050",peerProp);
        channel.addOrderer(orderer1);
        channel.initialize();
        return new Fabric(client,channel);
    }

    /**
     * 初始化org1peer1，org2peer1，org3peer1
     * @param name
     * @return
     * @throws Exception
     */
    public static Fabric initOrgs(String name) throws Exception{
        //创建client实例
        HFClient client = createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\priv_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\cert.pem",
                "Admin",
                "org1MSP");

        //创建通道实例
        Properties peerProp = createPeerProp("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\tls-0-0-0-0-7052.pem");
        Channel channel = client.newChannel(name);

        Peer peer1 = client.newPeer("p1o1","grpcs://peer1-org1.txhy.com:7501",peerProp);
        Peer peer2 = client.newPeer("p1o2","grpcs://peer1-org2.txhy.com:7551",peerProp);
        Peer peer3 = client.newPeer("p1o3","grpcs://peer1-org3.txhy.com:7601",peerProp);
        channel.addPeer(peer1);
        channel.addPeer(peer2);
        channel.addPeer(peer3);

        Orderer orderer1 = client.newOrderer("or1o0","grpcs://orderer1-org0.txhy.com:8050",peerProp);
        channel.addOrderer(orderer1);

        channel.initialize();
        return new Fabric(client,channel);
    }

    /**
     * 创建client
     * @param keyFile 节点的私钥
     * @param certFile 节点证书
     * @param userName 用户名称 e.g. Admin
     * @param mspId 节点所属组织的id e.g. org1MSP (看自己的配置文件怎么写的一致就好)
     * @return
     * @throws Exception
     */
    public static HFClient createClient(String keyFile,String certFile,String userName,String mspId) throws Exception {
        LocalUser user = new LocalUser(userName,mspId,keyFile,certFile);
        HFClient client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        client.setUserContext(user);
        return client;
    }
    public static Properties createPeerProp(String pemPath) throws IOException {
        Properties peerProp = new Properties();
        peerProp.put("pemBytes", Files.readAllBytes(Paths.get(pemPath)));
        peerProp.setProperty("sslProvider", "openSSL");
        peerProp.setProperty("negotiationType", "TLS");
        return peerProp;
    }
}
