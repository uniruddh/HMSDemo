package `in`.uniruddh.hmsdemo.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  22/October/2021
 * @Email: uniruddh@gmail.com
 */
@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "token") var token: String,
    @Json(name = "msg") var msg: String,
    @Json(name = "api_version") var apiVersion: String
)
