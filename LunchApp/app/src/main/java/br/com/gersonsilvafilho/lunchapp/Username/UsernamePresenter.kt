package br.com.gersonsilvafilho.lunchapp.username


import br.com.gersonsilvafilho.lunchapp.restaurants.UsernameContract


class UsernamePresenter(usernameView: UsernameContract.View){

    private val mUsernameView: UsernameContract.View

    init {
        mUsernameView = usernameView
    }
}
