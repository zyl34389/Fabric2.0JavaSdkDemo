export CHANNEL_NAME=${1}
`./beforeDocker/createChannelConfigFile.sh ${CHANNEL_NAME}`
`./beforeDocker/updateAnchorPeers.sh ${CHANNEL_NAME}`
