package com.lzh.chaincode;

import com.lzh.po.Fabric;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class Query {
    public static class Result{
        Boolean isHas;
        String channelName;
        String chaincodeName;
        long sequence;
        String version;

        public Result(Boolean isHas, String chaincodeName, long sequence, String version) {
            this.isHas = isHas;
            this.chaincodeName = chaincodeName;
            this.sequence = sequence;
            this.version = version;
        }

        public Result(String channelName, String chaincodeName, long sequence, String version) {
            this.channelName = channelName;
            this.chaincodeName = chaincodeName;
            this.sequence = sequence;
            this.version = version;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public Boolean getHas() {
            return isHas;
        }

        public String getChaincodeName() {
            return chaincodeName;
        }

        public long getSequence() {
            return sequence;
        }

        public String getVersion() {
            return version;
        }

        public void setHas(Boolean has) {
            isHas = has;
        }

        public void setChaincodeName(String chaincodeName) {
            this.chaincodeName = chaincodeName;
        }

        public void setSequence(long sequence) {
            this.sequence = sequence;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"channelName\":\"" + channelName +"\""+
                    ",\"chaincodeName\":\"" + chaincodeName +"\""+
                    ", \"sequence\":\"" + sequence +"\""+
                    ", \"version\":\"" + version + '\"' +
                    '}';
        }
    }
    public static ArrayList<Result> queryChaincodesInstalled(HFClient client, Channel channel, Collection<Peer> peers) throws InvalidArgumentException, ProposalException {

        final LifecycleQueryChaincodeDefinitionsRequest request = client.newLifecycleQueryChaincodeDefinitionsRequest();

        Collection<LifecycleQueryChaincodeDefinitionsProposalResponse> proposalResponses = channel.lifecycleQueryChaincodeDefinitions(request, peers);
        ArrayList<Result> arr = new ArrayList<>();
        for (LifecycleQueryChaincodeDefinitionsProposalResponse proposalResponse : proposalResponses) {

            if(!ChaincodeResponse.Status.SUCCESS.equals(proposalResponse.getStatus())){
                System.out.println("查询失败");
                return null;
            }
            Collection<LifecycleQueryChaincodeDefinitionsResult> chaincodeDefinitions = proposalResponse.getLifecycleQueryChaincodeDefinitionsResult();

            Iterator<LifecycleQueryChaincodeDefinitionsResult> it = chaincodeDefinitions.iterator();
            while (it.hasNext()){
                LifecycleQueryChaincodeDefinitionsResult result = it.next();
                arr.add(new Result(channel.getName(),result.getName(),result.getSequence(),result.getVersion()));
            }
        }
        return arr;
    }


    public static Result isHasChaincode(String channelName,String chaincodeName) throws Exception {
        Fabric fabric = Init.initOrgs(channelName);
        HFClient client = fabric.getClient();
        Channel channel = fabric.getChannel();

        final LifecycleQueryChaincodeDefinitionsRequest request = client.newLifecycleQueryChaincodeDefinitionsRequest();

        Collection<LifecycleQueryChaincodeDefinitionsProposalResponse> proposalResponses = channel.lifecycleQueryChaincodeDefinitions(request, channel.getPeers());
        int count =0;
        String version="";
        long sequence=0;
        for (LifecycleQueryChaincodeDefinitionsProposalResponse proposalResponse : proposalResponses) {
            Peer peer = proposalResponse.getPeer();

            if(!ChaincodeResponse.Status.SUCCESS.equals(proposalResponse.getStatus())){
                System.out.println("查询失败");
                return null;
            }
            Collection<LifecycleQueryChaincodeDefinitionsResult> chaincodeDefinitions = proposalResponse.getLifecycleQueryChaincodeDefinitionsResult();

            Iterator<LifecycleQueryChaincodeDefinitionsResult> it = chaincodeDefinitions.iterator();

            while (it.hasNext()){
                LifecycleQueryChaincodeDefinitionsResult result = it.next();
                if(chaincodeName.equals(result.getName())){
                    sequence = result.getSequence();
                    version = result.getVersion();
                    count++;
                }
            }

        }
        return new Result(0<count,chaincodeName,sequence,version);
    }
    public static Set<String> listPeerChannels(HFClient client,Peer peer) throws ProposalException, InvalidArgumentException {
        Set<String> set = client.queryChannels(peer);
        return set;
    }

}
