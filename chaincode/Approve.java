package com.lzh.chaincode;

import com.lzh.po.Fabric;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class Approve {
    public  static CompletableFuture<BlockEvent.TransactionEvent> lifecycleApproveChaincodeDefinitionForMyOrg(HFClient client, Channel channel,
                                                                                                              Collection<Peer> peers, long sequence,
                                                                                                              String chaincodeName, String chaincodeVersion, boolean initRequired, String org1ChaincodePackageID) throws InvalidArgumentException, ProposalException {
        LifecycleApproveChaincodeDefinitionForMyOrgRequest lifecycleApproveChaincodeDefinitionForMyOrgRequest = client.newLifecycleApproveChaincodeDefinitionForMyOrgRequest();
        lifecycleApproveChaincodeDefinitionForMyOrgRequest.setSequence(sequence);
        lifecycleApproveChaincodeDefinitionForMyOrgRequest.setChaincodeName(chaincodeName);
        lifecycleApproveChaincodeDefinitionForMyOrgRequest.setChaincodeVersion(chaincodeVersion);
        lifecycleApproveChaincodeDefinitionForMyOrgRequest.setInitRequired(initRequired);
        lifecycleApproveChaincodeDefinitionForMyOrgRequest.setPackageId(org1ChaincodePackageID);

        Collection<LifecycleApproveChaincodeDefinitionForMyOrgProposalResponse> lifecycleApproveChaincodeDefinitionForMyOrgProposalResponse = channel.sendLifecycleApproveChaincodeDefinitionForMyOrgProposal(lifecycleApproveChaincodeDefinitionForMyOrgRequest,
                peers);

        if(peers.size()!=lifecycleApproveChaincodeDefinitionForMyOrgProposalResponse.size()){
            System.out.println("erro ：peer proposalResponse is not enough");
        }
        for (LifecycleApproveChaincodeDefinitionForMyOrgProposalResponse response : lifecycleApproveChaincodeDefinitionForMyOrgProposalResponse) {
            final Peer peer = response.getPeer();

            if(!ChaincodeResponse.Status.SUCCESS.equals(response.getStatus())){
                System.out.println("erro: "+peer+":"+response.getMessage()+response.isInvalid()+":"+response.isVerified());
            }else {
                System.out.println("lifecycleApproveChaincodeDefinitionForMyOrg:"+peer+":SUCCESS");
            }
        }

        return channel.sendTransaction(lifecycleApproveChaincodeDefinitionForMyOrgProposalResponse);

    }
    /**
     * 在org1上审批链码
     * @param channelName 通道名称
     * @param sequence 次序
     * @param chaincodeName 链码名称
     * @param chaincodeVersion 链码版本
     * @param initRequired 是否初始化
     * @param org1ChaincodePackageID 链码包id
     * @throws Exception
     */
    public  static void lifecycleApproveChaincodeForOrg1(String channelName,long sequence,
                                                         String chaincodeName, String chaincodeVersion, boolean initRequired, String org1ChaincodePackageID) throws Exception {
        Fabric fabric = Init.initOrg1 (channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();
        System.out.println("Org1实例化完成");

        //审批链码
        CompletableFuture<BlockEvent.TransactionEvent> transactionEventCompletableFuture= lifecycleApproveChaincodeDefinitionForMyOrg(client,channel,channel.getPeers(),sequence,chaincodeName,chaincodeVersion,initRequired,org1ChaincodePackageID);
        while (!transactionEventCompletableFuture.isDone()){
            //等待审批完成
        }
            if(transactionEventCompletableFuture.isDone()){
                System.out.println("Org1审批链码交易是否合法："+transactionEventCompletableFuture.get().isValid());
                if(!transactionEventCompletableFuture.get().isValid()){
                    System.out.println(transactionEventCompletableFuture.get().toString());
                    return;
                }
                System.out.println("Org1审批链码完成");
            }
        }
    public  static void lifecycleApproveChaincodeForOrg2(String channelName,long sequence,
                                                        String chaincodeName, String chaincodeVersion, boolean initRequired, String org1ChaincodePackageID) throws Exception {
        Fabric fabric = Init.initOrg2(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();
        System.out.println("Org2实例化完成");

        //审批链码
        CompletableFuture<BlockEvent.TransactionEvent> transactionEventCompletableFuture= lifecycleApproveChaincodeDefinitionForMyOrg(client,channel,channel.getPeers(),sequence,chaincodeName,chaincodeVersion,initRequired,org1ChaincodePackageID);
        while (!transactionEventCompletableFuture.isDone()){ //等待审批完成
             }
            if(transactionEventCompletableFuture.isDone()){
                System.out.println("Org2审批链码交易是否合法："+transactionEventCompletableFuture.get().isValid());
                if(!transactionEventCompletableFuture.get().isValid()){
                    System.out.println(transactionEventCompletableFuture.get().toString());
                    return;
                }
                System.out.println("Org2审批链码完成");
            }
        }
    public  static void lifecycleApproveChaincodeForOrg3(String channelName,long sequence,
                                                         String chaincodeName, String chaincodeVersion, boolean initRequired, String org1ChaincodePackageID) throws Exception {
        Fabric fabric = Init.initOrg3(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();
        System.out.println("Org3实例化完成");

        //审批链码
        CompletableFuture<BlockEvent.TransactionEvent> transactionEventCompletableFuture= lifecycleApproveChaincodeDefinitionForMyOrg(client,channel,channel.getPeers(),sequence,chaincodeName,chaincodeVersion,initRequired,org1ChaincodePackageID);
        while (!transactionEventCompletableFuture.isDone()){ //等待审批完成
             }
            if(transactionEventCompletableFuture.isDone()){
                System.out.println("Org3审批链码交易是否合法："+transactionEventCompletableFuture.get().isValid());
                if(!transactionEventCompletableFuture.get().isValid()){
                    System.out.println(transactionEventCompletableFuture.get().toString());
                    return;
                }
                System.out.println("Org3审批链码完成");
            }
        }
}
