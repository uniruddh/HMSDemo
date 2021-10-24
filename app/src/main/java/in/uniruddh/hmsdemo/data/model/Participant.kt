package `in`.uniruddh.hmsdemo.data.model

import live.hms.video.sdk.models.HMSPeer

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  23/October/2021
 * @Email: uniruddh@gmail.com
 */
data class Participant(
    val name: String,
    val hmsPeer: HMSPeer,
    var audioLevel: Int
)
