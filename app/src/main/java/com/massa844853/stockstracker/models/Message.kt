package com.massa844853.stockstracker.models

import com.google.firebase.database.Exclude

class Message {
    var username: String? = null
    var sendDate: Long? = null
    var message: String? = null

    @Exclude
    var typeMessage = 0

    constructor() {}
    constructor(username: String?, sendDate: Long?, message: String?) {
        this.username = username
        this.sendDate = sendDate
        this.message = message
        typeMessage = -1
    }
}