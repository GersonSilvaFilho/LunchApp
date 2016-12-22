package br.com.gersonsilvafilho.lunchapp.data;

import com.squareup.okhttp.OkHttpClient;

import java.util.Date;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by GersonSilva on 12/22/16.
 */

public class VotesService {

    private final VotesServiceApi votesServiceApi;
    private String baseUrl = "http://prjctx.herokuapp.com/";


    public VotesService()
    {
        OkHttpClient client = new OkHttpClient();
        // client.interceptors().add(new LoggingInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        this.votesServiceApi = retrofit.create(VotesServiceApi.class);
    }

    public void SendVote(Vote vote, Callback<Boolean> callback)
    {
        this.votesServiceApi.SendVote(vote.date, vote.userId, vote.restaurantId).enqueue(callback);
    }

    public void GetVotesFromDay(Date date, Callback<VotesList> callback)
    {
        this.votesServiceApi.GetVotesFromDay(date.getTime()/1000, "UserIDTest").enqueue(callback);
    }
}
