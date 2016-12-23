package br.com.gersonsilvafilho.lunchapp.data;

import com.squareup.okhttp.OkHttpClient;

import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by GersonSilva on 12/22/16.
 */

public class VotesService {

    private final VotesServiceApi votesServiceApi;
    private String baseUrl = "https://prjctx.000webhostapp.com/";


    public VotesService()
    {
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.votesServiceApi = retrofit.create(VotesServiceApi.class);
    }

    public void SendVote(Vote vote, Callback<Map<String,String>> callback)
    {
        this.votesServiceApi.SendVote(vote.date, vote.restaurantId, vote.userId).enqueue(callback);
    }

    public void GetVotesFromDay(Date date, Callback<Map<String,List<String>>> callback)
    {
        this.votesServiceApi.GetVotesFromDay(date.getTime()/1000).enqueue(callback);
    }
}
