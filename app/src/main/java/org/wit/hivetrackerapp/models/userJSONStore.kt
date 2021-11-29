package org.wit.usertrackerapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.hivetrackerapp.helpers.*
import org.wit.hivetrackerapp.models.UserModel
import org.wit.hivetrackerapp.models.UserStore
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE_USER = "users.json"
val gsonBuilderUser: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listTypeuser: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore {

    private var users = mutableListOf<UserModel>()

    init {
        if (exists(context, JSON_FILE_USER)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<UserModel> {
        logAll()
        return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        user.dateJoined = Date ()
        users.add(user)
        serialize()
    }


    override fun update(user: UserModel) {
        val founduser: UserModel? = users.find { p -> p.id == user.id }
        if (founduser != null) {
            founduser.email = user.email
            founduser.firstName = user.firstName
            founduser.image = user.image
            founduser.secondName = user.secondName
            founduser.userName = user.userName
            founduser.password = user.password
            logAll()
            serialize()
        }
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    override fun findByUsername(userName: String): UserModel? {
        return users.find { p -> p.userName == userName }
    }

    override fun findByEmail(email: String): UserModel? {
        return users.find { p -> p.email == email }
    }

    private fun serialize() {
        val jsonString = gsonBuilderUser.toJson(users, listTypeuser)
        write(context, JSON_FILE_USER, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE_USER)
        users = gsonBuilderUser.fromJson(jsonString, listTypeuser)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}