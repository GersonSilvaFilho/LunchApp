package br.com.gersonsilvafilho.lunchapp.util

import com.chibatching.kotpref.KotprefModel

/**
 * Created by GersonSilva on 12/22/16.
 */
object UserInfo : KotprefModel() {
    var username: String by stringPrefVar()
}
