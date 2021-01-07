package com.lzh.chaincode;

import com.lzh.po.Fabric;
import org.hyperledger.fabric.sdk.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public class Execute {
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
}
