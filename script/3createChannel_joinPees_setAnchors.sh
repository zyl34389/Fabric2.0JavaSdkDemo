CHANNEL_NAME=${1}

`./afterDocker/createChannel.sh ${CHANNEL_NAME}`
`./afterDocker/joinPeers.sh ${CHANNEL_NAME}`
`./afterDocker/setAnchors.sh ${CHANNEL_NAME}`
