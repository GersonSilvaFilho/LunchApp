package br.com.gersonsilvafilho.lunchapp.data

import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

/**
 * Created by GersonSilva on 12/22/16.
 */

interface VotesServiceApi {
    @GET("/vote.php")
    fun SendVote(@Query("dateTime") unixtime: Long,
                 @Query("restaurantId") restaurantId: String,
                 @Query("userId") userid: String): Call<Map<String, String>>

    @GET("/vote.php")
    fun ChangeVote(@Query("dateTime") unixtime: Long,
                   @Query("restaurantId") restaurantId: String,
                   @Query("userId") userid: String): Call<Map<String, String>>

    @GET("/list.php?")
    fun GetVotesFromDay(@Query("dateTime") unixtime: Long): Call<Map<String, List<String>>>
}
