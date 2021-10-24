package `in`.uniruddh.hmsdemo.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  22/October/2021
 * @Email: uniruddh@gmail.com
 */
@JsonClass(generateAdapter = true)
data class TokenRequest(
    @Json(name = "room_id") val roomId: String = "",
    @Json(name = "user_id") val userId: String = "",
    @Json(name = "role") val role: String = "",
    @Json(name = "access_key") val accessKey: String = ""
)
