package `in`.uniruddh.hmsdemo

import `in`.uniruddh.hmssdk.HMSException
import `in`.uniruddh.hmssdk.HMSPeerUpdate
import `in`.uniruddh.hmssdk.HMSRoomUpdate
import `in`.uniruddh.hmssdk.HMSSDK
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  13/October/2021
 * @Email: abhilvare@tetravx.com
 */
class MainViewModel : ViewModel() {

    @ExperimentalCoroutinesApi
    fun joinMeet() {
        viewModelScope.launch {
            try {
                val meetingRoom = HMSSDK().joinMeet("1234")
                meetingRoom.getPeerStates().collect {
                    when (it) {
                        HMSPeerUpdate.PEER_JOINED -> TODO()
                        HMSPeerUpdate.PEER_LEFT -> TODO()
                        HMSPeerUpdate.AUDIO_TOGGLED -> TODO()
                        HMSPeerUpdate.VIDEO_TOGGLED -> TODO()
                        HMSPeerUpdate.BECAME_DOMINANT_SPEAKER -> TODO()
                        HMSPeerUpdate.NO_DOMINANT_SPEAKER -> TODO()
                        HMSPeerUpdate.RESIGNED_DOMINANT_SPEAKER -> TODO()
                        HMSPeerUpdate.STARTED_SPEAKING -> TODO()
                        HMSPeerUpdate.STOPPED_SPEAKING -> TODO()
                        HMSPeerUpdate.ROLE_CHANGED -> TODO()
                    }
                }

                meetingRoom.getRoomStates().collect {
                    when (it) {
                        HMSRoomUpdate.ROOM_MUTED -> TODO()
                        HMSRoomUpdate.ROOM_UNMUTED -> TODO()
                        HMSRoomUpdate.SERVER_RECORDING_STATE_UPDATED -> TODO()
                        HMSRoomUpdate.RTMP_STREAMING_STATE_UPDATED -> TODO()
                        HMSRoomUpdate.BROWSER_RECORDING_STATE_UPDATED -> TODO()
                    }
                }

            } catch (e: HMSException) {
                // handle Exception
            }
        }
    }

}