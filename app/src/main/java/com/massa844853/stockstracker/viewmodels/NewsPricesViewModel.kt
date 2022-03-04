package com.massa844853.stockstracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.massa844853.stockstracker.models.Statistic
import com.massa844853.stockstracker.models.StockPrice
import java.util.*

class NewsPricesViewModel(application: Application) : AndroidViewModel(application) {
    var lastUpdate: Long
    var priceList: MutableList<StockPrice>
    var statisticList: MutableList<Statistic>
    var position: Int
    var asset: String

    init {
        priceList = ArrayList()
        statisticList = ArrayList()
        lastUpdate = 0
        position = 0
        asset = ""
    }
}