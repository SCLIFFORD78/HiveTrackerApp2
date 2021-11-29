package org.wit.hivetrackerapp.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun findByUsername(userName: String) :UserModel?
    fun findByEmail(email: String): UserModel?
}