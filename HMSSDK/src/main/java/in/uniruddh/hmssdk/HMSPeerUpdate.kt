package `in`.uniruddh.hmssdk

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  13/October/2021
 * @Email: abhilvare@tetravx.com
 */
public enum class HMSPeerUpdate {
    PEER_JOINED,
    PEER_LEFT,
    AUDIO_TOGGLED,
    VIDEO_TOGGLED,
    BECAME_DOMINANT_SPEAKER,
    NO_DOMINANT_SPEAKER,
    RESIGNED_DOMINANT_SPEAKER,
    STARTED_SPEAKING,
    STOPPED_SPEAKING,
    ROLE_CHANGED
}