package br.com.gersonsilvafilho.lunchapp.data;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by GersonSilva on 12/22/16.
 */

public interface VotesServiceApi
{
    @GET("/vote.php")
    Call<Boolean> SendVote(@Query("dateTime") long unixtime,
                           @Query("userId") String userid,
                           @Query("restaurantId") String restaurantId);

    @GET("/list.php?")
    Call<VotesList> GetVotesFromDay(@Query("dateTime") long unixtime,
                                    @Query("restaurantId") String restaurantId);
}
