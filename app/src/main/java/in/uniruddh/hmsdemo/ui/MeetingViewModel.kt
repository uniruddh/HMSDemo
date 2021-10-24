package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.data.model.Participant
import `in`.uniruddh.hmsdemo.data.repository.TokenRepository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import live.hms.video.error.HMSException
import live.hms.video.media.tracks.HMSTrack
import live.hms.video.sdk.HMSActionResultListener
import live.hms.video.sdk.HMSAudioListener
import live.hms.video.sdk.HMSSDK
import live.hms.video.sdk.HMSUpdateListener
import live.hms.video.sdk.models.*
import live.hms.video.sdk.models.enums.HMSPeerUpdate
import live.hms.video.sdk.models.enums.HMSRoomUpdate
import live.hms.video.sdk.models.enums.HMSTrackUpdate
import live.hms.video.sdk.models.trackchangerequest.HMSChangeTrackStateRequest
import javax.inject.Inject

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  13/October/2021
 * @Email: uniruddh@gmail.com
 */
@HiltViewModel
class MeetingViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val hmsSdk: HMSSDK
) : ViewModel(), HMSAudioListener, HMSUpdateListener {

    private val TAG = MeetingViewModel::class.java.simpleName

    var participantList = arrayListOf<Participant>()
    val peerUpdate = MutableLiveData<Boolean>()

    fun getAuthToken() {
        viewModelScope.launch {
            tokenRepository.getAuthToken()?.let {
                val hmsConfig = HMSConfig(it.userId, it.authToken)
                startMeeting(hmsConfig)
            }
        }
    }

    private fun startMeeting(hmsConfig: HMSConfig) {
        hmsSdk.join(hmsConfig, this)
        hmsSdk.addAudioObserver(this)
    }

    fun endMeeting() {
        hmsSdk.endRoom("Done", false, object : HMSActionResultListener {
            override fun onError(error: HMSException) {
                Log.e(TAG, "endRoom onError ${error.description}")
            }

            override fun onSuccess() {
                Log.e(TAG, "endRoom onSuccess")
                hmsSdk.removeAudioObserver()
                participantList.clear()
                peerUpdate.postValue(true)
            }
        })
    }

    private fun addParticipant(peer: HMSPeer) {
        val peerInList = participantList.firstOrNull {
            it.name == peer.name
        }
        if (peerInList == null) {
            participantList.add(Participant(peer.name, peer, 0))
        }
    }

    private fun removeParticipant(peer: HMSPeer) {
        participantList.removeAll {
            it.name == peer.name
        }
    }

    override fun onChangeTrackStateRequest(details: HMSChangeTrackStateRequest) {
    }

    override fun onError(error: HMSException) {
    }

    override fun onJoin(room: HMSRoom) {
        Log.e(TAG, "onJoin: $room")
        for (peer in room.peerList) {
            participantList.add(Participant(peer.name, peer, 0))
        }
        peerUpdate.postValue(true)
    }

    override fun onMessageReceived(message: HMSMessage) {
    }

    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
        Log.e(TAG, "onPeerUpdate type=$type, peer=$peer")
        when (type) {
            HMSPeerUpdate.PEER_JOINED -> {
                addParticipant(peer)
            }
            HMSPeerUpdate.PEER_LEFT -> {
                removeParticipant(peer)
            }
        }
        peerUpdate.postValue(true)
    }

    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
    }

    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
    }

    override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {
        Log.e(TAG, "onTrackUpdate type=$type, peer=$peer")
    }

    override fun onAudioLevelUpdate(speakers: Array<HMSSpeaker>) {
        if (participantList.size < 2) {
            return
        }

        if (speakers.isNotEmpty()) {
            val firstParticipant = participantList.firstOrNull()?.name ?: ""
            val topSpeaker = speakers.firstOrNull()?.peer?.name ?: ""

            if (firstParticipant == topSpeaker) {
                Log.e(TAG, "onAudioLevelUpdate: Do nothing")
            } else {
                Log.e(TAG, "onAudioLevelUpdate: Do something")
                participantList.find { it.name == topSpeaker }?.let {
                    participantList.remove(it)
                    participantList.add(0, it)
                }
                peerUpdate.postValue(true)
            }
        }
    }

}