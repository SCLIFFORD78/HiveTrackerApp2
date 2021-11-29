package org.wit.hivetrackerapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.hivetrackerapp.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "hives.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<HiveModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HiveJSONStore(private val context: Context) : HiveStore {

    private var hives = mutableListOf<HiveModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HiveModel> {
        logAll()
        return hives
    }

    override fun findByType(type: String): List<HiveModel> {
        val resp: MutableList<HiveModel> = mutableListOf()
        for (hive in hives) if(hive.type == type) {
           resp.add(0,hive)
           }
        return if (resp.isNotEmpty()){
            resp
        } else emptyList()
    }

    override fun findByOwner(userID: Long): List<HiveModel> {
        val resp: MutableList<HiveModel> = mutableListOf()
        for (hive in hives) if(hive.userID == userID) {
            resp.add(0,hive)
        }
        return if (resp.isNotEmpty()){
            resp
        } else emptyList()
    }

    override fun findByTag(tag: Long): HiveModel? {
        return hives.find { p -> p.tag == tag }
    }


    override fun create(hive: HiveModel) {
        hive.id = generateRandomId()
        hives.add(hive)
        serialize()
    }


    override fun update(hive: HiveModel) {
        val foundHive: HiveModel? = hives.find { p -> p.id == hive.id }
        if (foundHive != null) {
            foundHive.tag = hive.tag
            foundHive.description = hive.description
            foundHive.image = hive.image
            foundHive.lat = hive.lat
            foundHive.lng = hive.lng
            foundHive.zoom = hive.zoom
            logAll()
            serialize()
        }
    }

    override fun delete(hive: HiveModel) {
        hives.remove(hive)
        serialize()
    }

    override fun find(hive: HiveModel): HiveModel? {
        return hives.find { p -> p.id == hive.id }
    }

    override fun getTag(): Long {
        var num:Long = 1
        while (hives.find { p -> p.tag == num } != null){
            num++
        }
        return num
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hives, listType)
        write(context, JSON_FILE, jsonString)
    }


    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hives = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        hives.forEach { Timber.i("$it") }
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