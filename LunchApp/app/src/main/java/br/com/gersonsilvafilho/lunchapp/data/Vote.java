package br.com.gersonsilvafilho.lunchapp.data;

/**
 * Created by GersonSilva on 12/22/16.
 */

public class Vote {

    String userId;
    String restaurantId;
    long date;

    public Vote(String userId, String restaurantId, long date) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
    }
}
