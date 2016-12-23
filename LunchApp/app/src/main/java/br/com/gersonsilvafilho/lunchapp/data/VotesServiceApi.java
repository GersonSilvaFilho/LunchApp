package br.com.gersonsilvafilho.lunchapp.data;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by GersonSilva on 12/22/16.
 */

public interface VotesServiceApi
{
    @GET("/vote.php")
    Call<Map<String,String>> SendVote(@Query("dateTime") long unixtime,
                        @Query("restaurantId") String restaurantId,
                        @Query("userId") String userid);

    @GET("/vote.php")
    Call<Map<String,String>> ChangeVote(@Query("dateTime") long unixtime,
                          @Query("restaurantId") String restaurantId,
                          @Query("userId") String userid);

    @GET("/list.php?")
    Call<Map<String,List<String>>> GetVotesFromDay(@Query("dateTime") long unixtime);
}
