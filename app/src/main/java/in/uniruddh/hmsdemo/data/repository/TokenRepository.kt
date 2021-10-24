package `in`.uniruddh.hmsdemo.data.repository

import `in`.uniruddh.hmsdemo.MainApplication
import `in`.uniruddh.hmsdemo.data.api.APIConstant
import `in`.uniruddh.hmsdemo.data.api.APIUrl
import `in`.uniruddh.hmsdemo.data.model.RoomDetail
import `in`.uniruddh.hmsdemo.data.model.TokenRequest
import `in`.uniruddh.hmsdemo.data.model.TokenResponse
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  22/October/2021
 * @Email: uniruddh@gmail.com
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
class TokenRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    moshi: Moshi
) {
    private val tokenRequestAdapter: JsonAdapter<TokenRequest> =
        moshi.adapter(TokenRequest::class.java)

    private val tokenResponseAdapter = moshi.adapter(TokenResponse::class.java)

    suspend fun getAuthToken() : RoomDetail? = withContext(Dispatchers.IO) {

        val tokenRequest = TokenRequest()
        val tokenReqBody = tokenRequestAdapter.toJson(tokenRequest)
            .toRequestBody(APIConstant.CONTENT_TYPE_JSON.toMediaType())

        val request = Request.Builder()
            .url(APIUrl.getTokenUrl())
            .addHeader("Accept-Type", "application/json")
            .post(tokenReqBody)
            .build()
        val response = okHttpClient.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.source()?.let {
                tokenResponseAdapter.fromJson(it)?.let { tokenResponse ->

                    return@withContext RoomDetail(
                        tokenRequest.roomId,
                        tokenRequest.userId,
                        tokenResponse.token,
                        "https://prod-in.100ms.live/init"
                    )
                }
            }
        }
        return@withContext null
    }
}