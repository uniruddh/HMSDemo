package `in`.uniruddh.hmssdk

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  13/October/2021
 * @Email: abhilvare@tetravx.com
 */
data class HMSRoom(val roomId: String) {

    val peers: List<HMSPeer> = TODO()

    @ExperimentalCoroutinesApi
    fun getPeerStates(): kotlinx.coroutines.flow.Flow<HMSPeerUpdate> = callbackFlow {
        try {
            // Keep broadcasting peer updates
            trySend(HMSPeerUpdate.PEER_JOINED)
        } catch (e: HMSException) {
            close(e)
        }
        awaitClose { cancel() }
    }

    @ExperimentalCoroutinesApi
    fun getRoomStates(): kotlinx.coroutines.flow.Flow<HMSRoomUpdate> = callbackFlow {
        try {
            // Keep broadcasting room updates
            trySend(HMSRoomUpdate.RTMP_STREAMING_STATE_UPDATED)
        } catch (e: HMSException) {
            close(e)
        }
        awaitClose {
            // Dispose any low level callbacks here
        }
    }

}