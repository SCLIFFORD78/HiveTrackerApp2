package org.wit.hivetrackerapp.models

interface HiveStore {
    fun findAll(): List<HiveModel>
    fun findByType(type: String): List<HiveModel>
    fun findByOwner(userID: Long): List<HiveModel>
    fun findByTag(tag: Long) :HiveModel?
    fun create(hive: HiveModel)
    fun update(hive: HiveModel)
    fun delete(hive: HiveModel)
    fun find(hive: HiveModel) :HiveModel?
    fun getTag():Long

}