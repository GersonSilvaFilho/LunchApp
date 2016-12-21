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
        val note = o as Restaurant?
        return Objects.equal(id, note!!.id) &&
                Objects.equal(title, note.title) &&
                Objects.equal(description, note.description) &&
                Objects.equal(imageUrl, note.imageUrl)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, title, description, imageUrl)
    }
}