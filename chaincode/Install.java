package com.lzh.chaincode;

import com.lzh.po.Fabric;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;

public class Install {
    public  static String lifecycleInstallChaincode(HFClient client, Collection<Peer> peers, LifecycleChaincodePackage lifecycleChaincodePackage) throws InvalidArgumentException, ProposalException {
        long DEPLOYWAITTIME=180000;
        int numInstallProposal = 0;

        numInstallProposal = numInstallProposal + peers.size();

        LifecycleInstallChaincodeRequest installProposalRequest = client.newLifecycleInstallChaincodeRequest();
        installProposalRequest.setLifecycleChaincodePackage(lifecycleChaincodePackage);
        installProposalRequest.setProposalWaitTime(DEPLOYWAITTIME);
        Collection<LifecycleInstallChaincodeProposalResponse> responses = client.sendLifecycleInstallChaincodeRequest(installProposalRequest, peers);
        if(null==responses){
            System.out.println("responses is null");
        }

        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();
        String packageID = null;
        for (LifecycleInstallChaincodeProposalResponse response : responses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                System.out.printf("Successful install proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
                successful.add(response);
                if (packageID == null) {
                    packageID = response.getPackageId();
                    System.out.println("Hashcode came back as null from peer"+response.getPeer().getUrl()+packageID);
                } else {
                    if(!packageID.equals(response.getPackageId())){
                        System.out.println("Miss match on what the peers returned back as the packageID--"+packageID+"**"+response.getPackageId());
                    }
                }
            } else {
                System.out.println("fail install"+response.getPeer().getName());
                failed.add(response);
            }
        }

        System.out.printf("Received %d install proposal responses. Successful+verified: %d . Failed: %d", numInstallProposal, successful.size(), failed.size());
        System.out.println();
        if (failed.size() > 0) {
            ProposalResponse first = failed.iterator().next();
            System.out.println("Not enough endorsers for install :" + successful.size() + ".  " + first.getMessage());
        }

        return packageID;
    }

    /**
     * 给org1Pee1节点安装链码
     * @param channelName 通道名称
     * @param label 链码的标签，eg. chaincodeName_version
     * @param chaincodeSource 本地链码的目录，目录下的有个src，链码放在src下
     * @param chaincodeType 链码的语言 eg. TransactionRequest.Type.GO_LANG
     * @param chaincodePath 链码相对src目录的地址
     * @return 安装后的包id
     * @throws Exception
     */
    public static String installChaincodeForOrg1(String channelName, String label, Path chaincodeSource, TransactionRequest.Type chaincodeType, String chaincodePath) throws Exception {
        Fabric fabric = Init.initOrg1(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();

        //打包链码
        System.out.println("-----------------------链码打包-----------------------------");
        LifecycleChaincodePackage lifecycleChaincodePackage = LifecycleChaincodePackage.fromSource(label, chaincodeSource, chaincodeType,chaincodePath,null);

        //安装链码
        System.out.println("-----------------------链码安装-----------------------------");
        String packageID = lifecycleInstallChaincode(client,channel.getPeers(),lifecycleChaincodePackage);
        System.out.println(channel.getPeers()+"安装链码完成。"+"packageID="+packageID);
        return packageID;
    }
    /**
     * 给org2Pee1节点安装链码
     * @param channelName 通道名称
     * @param label 链码的标签，eg. chaincodeName_version
     * @param chaincodeSource 本地链码的目录，目录下的有个src，链码放在src下
     * @param chaincodeType 链码的语言 eg. TransactionRequest.Type.GO_LANG
     * @param chaincodePath 链码相对src目录的地址
     * @return 安装后的包id
     * @throws Exception
     */
    public static String installChaincodeForOrg2(String channelName, String label, Path chaincodeSource, TransactionRequest.Type chaincodeType, String chaincodePath) throws Exception {
        Fabric fabric = Init.initOrg2(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();

        //打包链码
        System.out.println("-----------------------链码打包-----------------------------");
        LifecycleChaincodePackage lifecycleChaincodePackage = LifecycleChaincodePackage.fromSource(label, chaincodeSource, chaincodeType,chaincodePath,null);

        //安装链码
        System.out.println("-----------------------链码安装-----------------------------");
        String packageID = lifecycleInstallChaincode(client,channel.getPeers(),lifecycleChaincodePackage);
        System.out.println(channel.getPeers()+"安装链码完成。"+"packageID="+packageID);
        return packageID;
    }
    /**
     * 给org3Pee1节点安装链码
     * @param channelName 通道名称
     * @param label 链码的标签，eg. chaincodeName_version
     * @param chaincodeSource 本地链码的目录，目录下的有个src，链码放在src下
     * @param chaincodeType 链码的语言 eg. TransactionRequest.Type.GO_LANG
     * @param chaincodePath 链码相对src目录的地址
     * @return 安装后的包id
     * @throws Exception
     */
    public static String installChaincodeForOrg3(String channelName, String label, Path chaincodeSource, TransactionRequest.Type chaincodeType, String chaincodePath) throws Exception {
        Fabric fabric = Init.initOrg3(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();

        //打包链码
        System.out.println("-----------------------链码打包-----------------------------");
        LifecycleChaincodePackage lifecycleChaincodePackage = LifecycleChaincodePackage.fromSource(label, chaincodeSource, chaincodeType,chaincodePath,null);

        //安装链码
        System.out.println("-----------------------链码安装-----------------------------");
        String packageID = lifecycleInstallChaincode(client,channel.getPeers(),lifecycleChaincodePackage);
        System.out.println(channel.getPeers()+"安装链码完成。"+"packageID="+packageID);
        return packageID;
    }
}
