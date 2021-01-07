package com.lzh;

import com.lzh.chaincode.*;
import com.lzh.channel.ChannelConf;
import com.lzh.po.ChaincodeMessage;
import com.lzh.po.ChannelChaincode;
import com.lzh.po.Fabric;
import com.lzh.po.LocalUser;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FabricManager {
    /**
     * 安装链码依赖
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean downloadVendor(String path) throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process p = null;

        int exitVal;
        String command = "go mod vendor";
        String dir = path+"\\src";
        System.out.println(command);
        p = rt.exec(command,null, new File(dir));
        // 进程的出口值。根据惯例，0 表示正常终止。
        exitVal = p.waitFor();
        boolean isSuccess;
        if(exitVal == 0){
            isSuccess = true;
        }else{
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 删除链码依赖和链码文件
     * @param folderPath
     */
    public static void removeChaincodeFile(String folderPath){
        delFolder(folderPath+"\\src");
        File file = new File(folderPath+"\\src");
        file.mkdir();
    }

    /**
     * 删除指定目录及其下面的所有文件和目录
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定目录下的所有文件
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 一键安装链码（org1peer1，org2peer1，org3peer1）（默认为go语言的链码），自动下载依赖，填充序号和版本号。安装成功后会自动删除依赖和链码文件
     * @param channelName
     * @param chaincodeName
     * @param chaincodePath 项目目录，里面需要有src目录，具体的链码在src中 eg. 项目目录 /home/project/src/main.go ,这里chaincodePath填  /home/project
     * @return
     * @throws Exception
     */
    public static boolean installOneStep(String channelName,String chaincodeName,String chaincodePath) throws Exception {
        boolean isDownload = downloadVendor(chaincodePath);//下载依赖
        if(!isDownload){
            return false;
        }
        Query.Result res = Query.isHasChaincode(channelName,chaincodeName);//查询是否已经安装
        long sequence;
        String version;
        if (res.getHas()){
            version = res.getVersion();
            sequence = res.getSequence()+1;
        }else {
            sequence = 1;
            version = "1";
        }
        ChaincodeMessage chaincodeMessage = FabricManager.installChaincode(channelName,chaincodeName,
                chaincodePath,sequence,version);//安装
        if(chaincodeMessage.getIsSuccess()){
            removeChaincodeFile(chaincodePath);//删除链码和依赖文件
        }
        return chaincodeMessage.getIsSuccess();
    }
    /**
     * 给org1peer1-org2peer1-org3peer1安装链码（每个组织的锚节点都的安装，最后只需要提交一次就行）
     * @param channelName 通道名称
     * @param chaincodeName 链码名称
     * @param sourcePath 链码路径
     * @param sequence 次数
     * @param chaincodeVersion 版本号
     * @throws Exception
     */
    public static ChaincodeMessage installChaincode(String channelName, String chaincodeName, String sourcePath,
                                                    long sequence, String chaincodeVersion) throws Exception {
//        //安装参数
//        String channelName = "kk";
//        String chaincodeName = "zz_1";
//        String sourcePath = "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\java\\com\\lzh\\publicsafetygo";
//        TransactionRequest.Type chaincodeType = TransactionRequest.Type.GO_LANG;
//        //审批参数
//        long sequence=1;
//        String chaincodeVersion="1";
//        boolean initRequired = true;
        //安装
        String packageID1 =  Install.installChaincodeForOrg1(channelName,chaincodeName, Paths.get(sourcePath), TransactionRequest.Type.GO_LANG,"./");
        //审批
        Approve.lifecycleApproveChaincodeForOrg1(channelName,sequence,chaincodeName,chaincodeVersion,true,packageID1);

        String packageID2 =  Install.installChaincodeForOrg2(channelName,chaincodeName, Paths.get(sourcePath), TransactionRequest.Type.GO_LANG,"./");
        Approve.lifecycleApproveChaincodeForOrg2(channelName,sequence,chaincodeName,chaincodeVersion,true,packageID2);
        String packageID3 =  Install.installChaincodeForOrg3(channelName,chaincodeName, Paths.get(sourcePath), TransactionRequest.Type.GO_LANG,"./");
        Approve.lifecycleApproveChaincodeForOrg3(channelName,sequence,chaincodeName,chaincodeVersion,true,packageID3);
        //提交
        ChaincodeMessage chaincodeMessage = Commit.commitChaincodeForOrgs(channelName,sequence,chaincodeName,chaincodeVersion,true);
        return chaincodeMessage;
    }

    /**
     * 初始化链码（init）
     * @param channelName
     * @param chaincodeName
     * @param fcn
     * @param args
     * @return
     * @throws Exception
     */
    public  static  boolean initChaincode( String channelName, String chaincodeName, String fcn, String... args) throws Exception {

        Fabric fabric = Init.initOrgs(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();
        User userContext = client.getUserContext();
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();

        TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeName(chaincodeName);
        transactionProposalRequest.setChaincodeLanguage(TransactionRequest.Type.GO_LANG);
        transactionProposalRequest.setUserContext(userContext);

        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setProposalWaitTime(60000);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setInit(true);


        Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
        for (ProposalResponse response : transactionPropResp) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }
        System.out.printf("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d\n",
                transactionPropResp.size(), successful.size(), failed.size());
        if (failed.size() > 0) {
            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
            System.out.println("Not enough endorsers for executeChaincode(move a,b,100):" + failed.size() + " endorser error: " +
                    firstTransactionProposalResponse.getMessage() +
                    ". Was verified: " + firstTransactionProposalResponse.isVerified());
        }

        System.out.println("Sending chaincode transaction(move a,b,100) to orderer.");
        CompletableFuture<BlockEvent.TransactionEvent> completableFuture = channel.sendTransaction(successful);
        while (!completableFuture.isDone()){

        }

        return completableFuture.get().isValid();
    }

    /**
     * 执行链码（set）
     * @param channelName
     * @param chaincodeName
     * @param fcn
     * @param args
     * @return
     * @throws Exception
     */
    public  static  boolean executeChaincode( String channelName, String chaincodeName, String fcn, String... args) throws Exception {

        Fabric fabric = Init.initOrgs(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();
        User userContext = client.getUserContext();
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();

        TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeName(chaincodeName);
        transactionProposalRequest.setChaincodeLanguage(TransactionRequest.Type.GO_LANG);
        transactionProposalRequest.setUserContext(userContext);

        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setProposalWaitTime(60000);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setInit(false);

        Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
        for (ProposalResponse response : transactionPropResp) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }
        System.out.printf("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d\n",
                transactionPropResp.size(), successful.size(), failed.size());
        if (failed.size() > 0) {
            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
            System.out.println("Not enough endorsers for executeChaincode(move a,b,100):" + failed.size() + " endorser error: " +
                    firstTransactionProposalResponse.getMessage() +
                    ". Was verified: " + firstTransactionProposalResponse.isVerified());
        }

        System.out.println("Sending chaincode transaction(move a,b,100) to orderer.");
        CompletableFuture<BlockEvent.TransactionEvent> completableFuture = channel.sendTransaction(successful);
        while (!completableFuture.isDone()){

        }

        return completableFuture.get().isValid();
    }

    /**
     * 查询链码（get）
     * @param channelName
     * @param chaincodeName
     * @param fcn
     * @param args
     * @return
     * @throws Exception
     */
    public  static  String queryChaincode(String channelName, String chaincodeName, String fcn, String... args) throws Exception {
        Fabric fabric = Init.initOrg1(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();
        User userContext = client.getUserContext();
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        req.setChaincodeName(chaincodeName);
        req.setChaincodeLanguage(TransactionRequest.Type.GO_LANG);
        req.setUserContext(userContext);

        req.setFcn(fcn);
        req.setProposalWaitTime(60000);
        req.setArgs(args);

        ProposalResponse[] rsp = channel.queryByChaincode(req).toArray(new ProposalResponse[0]);
        String res = rsp[0].getProposalResponse().getResponse().getPayload().toStringUtf8();

        return res;
    }

    /**
     * 获取指定通道中安装的链码
     * @param channelName
     * @return
     * @throws Exception
     */
    public static ArrayList<Query.Result> listChannelChaincodesInstalled(String channelName) throws Exception {
        Fabric fabric = Init.initOrg1(channelName);
        Channel channel = fabric.getChannel();
        HFClient client = fabric.getClient();
        ArrayList<Query.Result> arr = Query.queryChaincodesInstalled(client,channel,channel.getPeers());
        return arr;
    }

    /**
     * 获得org1peer1加入的所有通道
     * @return
     * @throws Exception
     */
    public static Set<String> listPeerChannels() throws Exception {
        String keyFile = "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\priv_sk";
        String certFile = "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\cert.pem";
        LocalUser user = new LocalUser("Admin","org1MSP",keyFile,certFile);

        HFClient client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        client.setUserContext(user);

        Properties peerProp = new Properties();
        peerProp.put("pemBytes", Files.readAllBytes(Paths.get("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\tls-0-0-0-0-7052.pem")));
        peerProp.setProperty("sslProvider", "openSSL");
        peerProp.setProperty("negotiationType", "TLS");

        Peer peer = client.newPeer("p1o1","grpcs://peer1-org1.txhy.com:7501",peerProp);
        return  Query.listPeerChannels(client,peer);
    }

    /**
     * 获得org1peer上安装的所有链码
     * @return
     * @throws Exception
     */
    public static ArrayList<ChannelChaincode> listAllChaincode() throws Exception {
        ArrayList<ChannelChaincode> arr = new ArrayList<>();
        Set<String> set = listPeerChannels();
        Iterator<String> it = set.iterator();
        while (it.hasNext()){
            String channelName = it.next();
            arr.add(new ChannelChaincode(channelName,listChannelChaincodesInstalled(channelName)));
        }
        return arr;
    }

    /**
     * 创建通道，把3个组织的节点加入通道。（后期可以修改为网站生产通道配置的yaml文件，
     * 后台自动根据yaml文件生成通道配置文件，进而创建通道，加入节点，做到网站智能管理配置）
     * @param channelName 通道名称，需要和配置文件一致
     * @param channelTxPath 通道配置文件Tx的路径（由命令行工具根据通道配置生成的通道配置文件）
     * @return
     * @throws Exception
     */
    public static Boolean createChannel(String channelName,String channelTxPath) throws Exception {
        Properties peerProp = Init.createPeerProp("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\tls-0-0-0-0-7052.pem");
        HFClient org1Client = Init.createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\priv_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\cert.pem",
                "Admin",
                "org1MSP");
        HFClient org2Client = Init.createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org2_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org2_cert.pem",
                "Admin",
                "org2MSP");
        HFClient org3Client = Init.createClient("C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org3_sk",
                "C:\\Users\\123\\Desktop\\Me\\fabricTest\\src\\main\\rKey\\Org3_cert.pem",
                "Admin",
                "org3MSP");
        Orderer orderer = org1Client.newOrderer("or1o0","grpcs://orderer1-org0.txhy.com:8050",peerProp);
        boolean isSuccess = ChannelConf.createChannelBlock(channelName,channelTxPath,orderer);

        if(isSuccess){
            Peer peer1 = org1Client.newPeer("p1o1","grpcs://peer1-org1.txhy.com:7501",peerProp);
            isSuccess = ChannelConf.joinPeer(channelName,org1Client,peer1);
        }
        if(isSuccess){
            Peer peer2 = org2Client.newPeer("p1o2","grpcs://peer1-org2.txhy.com:7551",peerProp);
            isSuccess = ChannelConf.joinPeer(channelName,org2Client,peer2);
        }
        if(isSuccess){
            Peer peer3 = org3Client.newPeer("p1o3","grpcs://peer1-org3.txhy.com:7601",peerProp);
            isSuccess = ChannelConf.joinPeer(channelName,org3Client,peer3);
        }
        return isSuccess;
    }
}
