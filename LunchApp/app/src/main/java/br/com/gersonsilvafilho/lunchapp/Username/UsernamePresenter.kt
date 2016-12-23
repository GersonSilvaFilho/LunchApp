package br.com.gersonsilvafilho.lunchapp.username


import br.com.gersonsilvafilho.lunchapp.restaurants.UsernameContract
import com.google.common.base.Preconditions.checkNotNull


class UsernamePresenter(usernameView: UsernameContract.View){

    private val mUsernameView: UsernameContract.View

    init {
        mUsernameView = checkNotNull<UsernameContract.View>(usernameView, "restaurantView cannot be null!")
    }
}
