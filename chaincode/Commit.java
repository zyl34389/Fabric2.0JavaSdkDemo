package com.lzh.chaincode;


import com.lzh.po.ChaincodeMessage;
import com.lzh.po.Fabric;
import org.hyperledger.fabric.sdk.*;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class Commit {
    public  static CompletableFuture<BlockEvent.TransactionEvent> commitChaincodeDefinitionRequest(HFClient client, Channel channel, long definitionSequence, String chaincodeName, String chaincodeVersion, boolean initRequired, Collection<Peer> endorsingPeers) throws Exception {
        LifecycleCommitChaincodeDefinitionRequest lifecycleCommitChaincodeDefinitionRequest = client.newLifecycleCommitChaincodeDefinitionRequest();

        lifecycleCommitChaincodeDefinitionRequest.setSequence(definitionSequence);
        lifecycleCommitChaincodeDefinitionRequest.setChaincodeName(chaincodeName);
        lifecycleCommitChaincodeDefinitionRequest.setChaincodeVersion(chaincodeVersion);

        lifecycleCommitChaincodeDefinitionRequest.setInitRequired(initRequired);

        Collection<LifecycleCommitChaincodeDefinitionProposalResponse> lifecycleCommitChaincodeDefinitionProposalResponses = channel.sendLifecycleCommitChaincodeDefinitionProposal(lifecycleCommitChaincodeDefinitionRequest, endorsingPeers);

        for (LifecycleCommitChaincodeDefinitionProposalResponse resp : lifecycleCommitChaincodeDefinitionProposalResponses) {

            final Peer peer = resp.getPeer();
            if(!ChaincodeResponse.Status.SUCCESS.equals(resp.getStatus())){
                System.out.println();
                System.out.println("error: "+peer.toString()+":"+resp.isVerified()+":"+resp.getMessage());
            }
        }

        return channel.sendTransaction(lifecycleCommitChaincodeDefinitionProposalResponses);

    }
    /**
     * 向3个组织提交链码
     * @param channelName 通道名称
     * @param definitionSequence 定义次序
     * @param chaincodeName 链码名称
     * @param chaincodeVersion 链码版本
     * @param initRequired 是否初始化
     * @throws Exception
     */
    public static ChaincodeMessage commitChaincodeForOrgs(String channelName, long definitionSequence, String chaincodeName, String chaincodeVersion, boolean initRequired) throws Exception {
        Fabric fabric = Init.initOrgs(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();

        CompletableFuture<BlockEvent.TransactionEvent> future = commitChaincodeDefinitionRequest(client,channel,definitionSequence,chaincodeName,chaincodeVersion,initRequired,channel.getPeers());
        while (!future.isDone()){
            //等待提交完成
        }
        ChaincodeMessage chaincodeMessage = null;
        if(future.isDone()){
            System.out.println("提交链码交易是否合法："+future.get().isValid());
            if (!future.get().isValid()){
                chaincodeMessage = new ChaincodeMessage(false,"链码提交失败！");
            }else {
                chaincodeMessage = new ChaincodeMessage(true,"链码提交成功！");
            }
        }
        System.out.println("提交链码完成");
        return chaincodeMessage;
    }

}
