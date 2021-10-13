package `in`.uniruddh.hmssdk

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  13/October/2021
 * @Email: abhilvare@tetravx.com
 */
class HMSSDK {

    suspend fun joinMeet(meetingId: String): HMSRoom {
        return suspendCoroutine { continuation ->
            try {
                // Perform joinMeet operation
                // Requesting server to connect us
                continuation.resume(HMSRoom(meetingId))
            } catch (e: HMSException) {
                continuation.resumeWithException(e)
            }
        }
    }
}