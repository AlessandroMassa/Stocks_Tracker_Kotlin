package com.massa844853.stockstracker.models

import com.google.gson.annotations.SerializedName

class KeyStatistics(var sharesOutstanding: StatisticValue, var beta: StatisticValue, @field:SerializedName("52WeekChange") private var _52WeekChange: StatisticValue, var enterpriseValue: StatisticValue, var lastDividendDate: StatisticValue) {

    fun get_52WeekChange(): StatisticValue {
        return _52WeekChange
    }

    fun set_52WeekChange(_52WeekChange: StatisticValue) {
        this._52WeekChange = _52WeekChange
    }
}