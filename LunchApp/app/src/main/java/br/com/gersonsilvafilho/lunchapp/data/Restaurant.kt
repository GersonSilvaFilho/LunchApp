package br.com.gersonsilvafilho.lunchapp.data

import com.google.common.base.Objects
import kotlin.comparisons.compareValuesBy

class Restaurant @JvmOverloads constructor(val id: String, val title: String?, val description: String?, val imageUrl: String? = null) : Comparable<Restaurant>{

    var votes : Int = 0;

    val isEmpty: Boolean
        get() = (title == null || "" == title) && (description == null || "" == description)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val Restaurant = other as Restaurant?
        return Objects.equal(id, Restaurant!!.id) &&
                Objects.equal(title, Restaurant.title) &&
                Objects.equal(description, Restaurant.description) &&
                Objects.equal(imageUrl, Restaurant.imageUrl)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, title, description, imageUrl)
    }

    fun incrementVotes(numVotes: Int)
    {
        votes += numVotes
    }

    override fun compareTo(other: Restaurant) = compareValuesBy(this, other, { it.votes })
}