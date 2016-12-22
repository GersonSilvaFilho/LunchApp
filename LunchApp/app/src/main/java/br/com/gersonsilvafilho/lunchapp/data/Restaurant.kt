package br.com.gersonsilvafilho.lunchapp.data

import com.google.common.base.Objects
import java.util.*

class Restaurant @JvmOverloads constructor(val title: String?, val description: String?, val imageUrl: String? = null) {

    val id: String

    init {
        id = UUID.randomUUID().toString()
    }

    val isEmpty: Boolean
        get() = (title == null || "" == title) && (description == null || "" == description)

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val Restaurant = o as Restaurant?
        return Objects.equal(id, Restaurant!!.id) &&
                Objects.equal(title, Restaurant.title) &&
                Objects.equal(description, Restaurant.description) &&
                Objects.equal(imageUrl, Restaurant.imageUrl)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, title, description, imageUrl)
    }
}