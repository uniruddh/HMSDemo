package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.data.model.Participant
import `in`.uniruddh.hmsdemo.data.repository.TokenRepository
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

    var participantList = arrayListOf<Participant>()
    val onPeerUpdated = MutableLiveData<Boolean>()

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
            }

            override fun onSuccess() {
                hmsSdk.removeAudioObserver()
                participantList.clear()
                onPeerUpdated.postValue(true)
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
        for (peer in room.peerList) {
            participantList.add(Participant(peer.name, peer, 0))
        }
        onPeerUpdated.postValue(true)
    }

    override fun onMessageReceived(message: HMSMessage) {
    }

    override fun onPeerUpdate(type: HMSPeerUpdate, peer: HMSPeer) {
        when (type) {
            HMSPeerUpdate.PEER_JOINED -> {
                addParticipant(peer)
                onPeerUpdated.postValue(true)
            }
            HMSPeerUpdate.PEER_LEFT -> {
                removeParticipant(peer)
                onPeerUpdated.postValue(true)
            }
        }
    }

    override fun onRoleChangeRequest(request: HMSRoleChangeRequest) {
    }

    override fun onRoomUpdate(type: HMSRoomUpdate, hmsRoom: HMSRoom) {
    }

    override fun onTrackUpdate(type: HMSTrackUpdate, track: HMSTrack, peer: HMSPeer) {
    }

    override fun onAudioLevelUpdate(speakers: Array<HMSSpeaker>) {
        if (participantList.size < 2) {
            return
        }

        if (speakers.isNotEmpty()) {
            if (speakers.size > 1) {
                speakers.sortBy { it.level }
            }
            val firstParticipant = participantList.firstOrNull()?.name ?: ""
            val topSpeaker = speakers.firstOrNull()?.peer?.name ?: ""

            if (firstParticipant != topSpeaker) {
                participantList.find { it.name == topSpeaker }?.let {
                    participantList.remove(it)
                    participantList.add(0, it)
                }
                onPeerUpdated.postValue(true)
            }
        }
    }

}