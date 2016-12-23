package br.com.gersonsilvafilho.lunchapp.data

import com.squareup.okhttp.OkHttpClient
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import java.util.*

/**
 * Created by GersonSilva on 12/22/16.
 */

class VotesService {

    private val votesServiceApi: VotesServiceApi
    private val baseUrl = "https://prjctx.000webhostapp.com/"

    init {
        val client = OkHttpClient()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        this.votesServiceApi = retrofit.create(VotesServiceApi::class.java)
    }

    fun SendVote(vote: Vote, callback: Callback<Map<String, String>>) {
        this.votesServiceApi.SendVote(vote.date, vote.restaurantId, vote.userId).enqueue(callback)
    }

    fun GetVotesFromDay(date: Date, callback: Callback<Map<String, List<String>>>) {
        this.votesServiceApi.GetVotesFromDay(date.time / 1000).enqueue(callback)
    }
}
