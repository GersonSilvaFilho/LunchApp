package br.com.gersonsilvafilho.lunchapp


import android.content.Context
import br.com.gersonsilvafilho.lunchapp.data.RestaurantRepositories
import br.com.gersonsilvafilho.lunchapp.data.RestaurantRepository
import br.com.gersonsilvafilho.lunchapp.data.RestaurantServiceApiImpl

/**
 * Enables injection of mock implementations for [ImageFile] and
 * [RestaurantsRepository] at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
 object Injection {
    fun provideRestaurantsRepository(context: Context): RestaurantRepository {
        return RestaurantRepositories.getInMemoryRepoInstance(RestaurantServiceApiImpl(context))!!
    }
}