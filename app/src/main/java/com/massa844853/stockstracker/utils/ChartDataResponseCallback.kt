package com.massa844853.stockstracker.utils

import com.massa844853.stockstracker.models.StockPrice

interface ChartDataResponseCallback {
    fun onResponsePrices(pricesList: MutableList<StockPrice>?)
    fun onFailure(errorMessage: String?)
}