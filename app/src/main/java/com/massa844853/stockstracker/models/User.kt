package com.massa844853.stockstracker.models

class User {
    var loginDateTime: Long? = null
    var nextMessage = 0
    var username: String? = null

    constructor() {}
    constructor(loginDateTime: Long?, nextMessage: Int, username: String?) {
        this.loginDateTime = loginDateTime
        this.nextMessage = nextMessage
        this.username = username
    }
}