package br.com.gersonsilvafilho.lunchapp.Username


import br.com.gersonsilvafilho.lunchapp.restaurants.UsernameContract
import com.google.common.base.Preconditions.checkNotNull


class UsernamePresenter(usernameView: UsernameContract.View){

    private val mUsernameView: UsernameContract.View

    init {
        mUsernameView = checkNotNull<UsernameContract.View>(usernameView, "restaurantView cannot be null!")
    }
}
