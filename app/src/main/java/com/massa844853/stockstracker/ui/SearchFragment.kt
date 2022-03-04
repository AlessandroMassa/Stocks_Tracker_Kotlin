package com.massa844853.stockstracker.ui
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.adapter.StatisticsListViewBaseAdapter
import com.massa844853.stockstracker.models.Statistic
import com.massa844853.stockstracker.models.StockPrice
import com.massa844853.stockstracker.repository.ChartDataRepository
import com.massa844853.stockstracker.repository.StatisticsRepository
import com.massa844853.stockstracker.utils.ChartDataResponseCallback
import com.massa844853.stockstracker.utils.StatisticsResponseCallback
import java.util.*
class SearchFragment : Fragment(), StatisticsResponseCallback, ChartDataResponseCallback {
    private var chartPrices: CandleStickChart? = null
    private var candleEntryArrayList: ArrayList<CandleEntry>? = null
    private var tabLayout: TabLayout? = null
    private val intervals = arrayOf("5m", "60m", "1d", "1d", "1wk", "1mo")
    private val periods = arrayOf("1d", "5d", "1mo", "3mo", "1y", "5y")
    private var searchView: SearchView? = null
    private var asset = ""
    private var editText: EditText? = null
    private var position = 0
    private var stockName: TextView? = null
    private var stockReturn: TextView? = null
    private var actualStockPrice: TextView? = null
    private var progressBarSearch: ProgressBar? = null
    private var listViewStatistics: ListView? = null
    private var mainActivity: MainActivity? = null
    private var statisticsRepository: StatisticsRepository? = null
    private var statisticsListViewAdapter: StatisticsListViewBaseAdapter? = null
    private var chartDataRepository: ChartDataRepository? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        view.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.backgroundblue, null))
        stockName = view.findViewById<View>(R.id.stockName) as TextView
        stockReturn = view.findViewById<View>(R.id.stockReturn) as TextView
        actualStockPrice = view.findViewById<View>(R.id.actualStockPrice) as TextView
        progressBarSearch = view.findViewById<View>(R.id.progressBarSearch) as ProgressBar
        progressBarSearch!!.visibility = View.INVISIBLE
        tabLayout = view.findViewById<View>(R.id.tabLayout) as TabLayout
        chartPrices = view.findViewById<View>(R.id.chartPrices) as CandleStickChart
        searchView = view.findViewById<View>(R.id.searchView) as SearchView
        listViewStatistics = view.findViewById<View>(R.id.listViewStatistics) as ListView
        mainActivity = activity as MainActivity?
        statisticsRepository = StatisticsRepository(requireActivity().application, this)
        statisticsListViewAdapter = StatisticsListViewBaseAdapter(mainActivity!!.newsPricesViewModel!!.statisticList, requireActivity())
        listViewStatistics!!.adapter = statisticsListViewAdapter
        candleEntryArrayList = ArrayList()
        chartDataRepository = ChartDataRepository(requireActivity().application, this)
        setChartStyle()
        if (mainActivity!!.newsPricesViewModel!!.priceList.size != 0) populateChart()
        if (mainActivity!!.newsPricesViewModel!!.statisticList.size != 0) populateStatistics()
        tabLayout!!.getTabAt(mainActivity!!.newsPricesViewModel!!.position)!!.select()
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!query.isEmpty()) {
                    asset = query
                    searchView!!.clearFocus()
                    editText!!.setText("")
                    progressBarSearch!!.visibility = View.VISIBLE
                    candleEntryArrayList = ArrayList()
                    statisticsRepository!!.start(asset)
                    chartDataRepository!!.start(asset, intervals[position], periods[position])
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView!!.setOnCloseListener {
            searchView!!.clearFocus()
            true
        }
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                position = tab.position
                mainActivity!!.newsPricesViewModel!!.position = position
                if (!mainActivity!!.newsPricesViewModel!!.asset.isEmpty()) {
                    progressBarSearch!!.visibility = View.VISIBLE
                    candleEntryArrayList = ArrayList()
                    chartDataRepository!!.start(mainActivity!!.newsPricesViewModel!!.asset, intervals[position], periods[position])
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return view
    }
    fun setChartStyle() {
        val id = searchView!!.context
                .resources
                .getIdentifier("android:id/search_src_text", null, null)
        editText = searchView!!.findViewById<View>(id) as EditText
        editText!!.textSize = 20f
        editText!!.setHintTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
        editText!!.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
        editText!!.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val yAxisRight = chartPrices!!.axisRight
        val yAxisLeft = chartPrices!!.axisLeft
        val xAxis = chartPrices!!.xAxis
        val l = chartPrices!!.legend
        l.isEnabled = false
        chartPrices!!.setDrawBorders(false)
        chartPrices!!.description = null
        chartPrices!!.setNoDataText("")
        chartPrices!!.setNoDataTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.setDrawLabels(true)
        yAxisLeft.textColor = R.color.white
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.textColor = ResourcesCompat.getColor(resources, R.color.grey, null)
        yAxisRight.setDrawLabels(false)
        yAxisRight.setDrawGridLines(false)
        yAxisRight.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setAvoidFirstLastClipping(true)
        chartPrices!!.isDragEnabled = true
        chartPrices!!.setScaleEnabled(true)
        chartPrices!!.setNoDataTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
    }
    override fun onResponse(eleStatistic: MutableList<Statistic>?) {
        if (eleStatistic != null) {
            mainActivity!!.newsPricesViewModel!!.statisticList.clear()
            mainActivity!!.newsPricesViewModel!!.statisticList.addAll(eleStatistic)
            populateStatistics()
        }
    }
    override fun onResponsePrices(pricesList: MutableList<StockPrice>?) {
        if (pricesList != null) {
            mainActivity!!.newsPricesViewModel!!.priceList.clear()
            mainActivity!!.newsPricesViewModel!!.priceList.addAll(pricesList)
            if (!asset.isEmpty()) mainActivity!!.newsPricesViewModel!!.asset = asset
            populateChart()
        }
    }
    override fun onFailure(errorMessage: String?) {
        asset = mainActivity!!.newsPricesViewModel!!.asset
        progressBarSearch!!.visibility = View.INVISIBLE
        Toast.makeText(context, "Error. $errorMessage", Toast.LENGTH_LONG).show()
    }
    private fun calculateReturn(openPrice: Double, closePrice: Double): Double {
        return closePrice / openPrice - 1
    }
    private fun populateChart() {
        requireActivity().runOnUiThread {
            for (i in mainActivity!!.newsPricesViewModel!!.priceList.indices) {
                candleEntryArrayList!!.add(CandleEntry(i.toFloat(), mainActivity!!.newsPricesViewModel!!.priceList[i].high.toFloat(), mainActivity!!.newsPricesViewModel!!.priceList[i].low.toFloat(),
                        mainActivity!!.newsPricesViewModel!!.priceList[i].open.toFloat(), mainActivity!!.newsPricesViewModel!!.priceList[i].close.toFloat()))
            }
            val set1 = CandleDataSet(candleEntryArrayList, "DataSet 1")
            set1.shadowColor = ResourcesCompat.getColor(resources, R.color.grey, null)
            set1.shadowWidth = 0.8f
            set1.decreasingColor = ResourcesCompat.getColor(resources, R.color.red, null)
            set1.decreasingPaintStyle = Paint.Style.FILL
            set1.increasingColor = ResourcesCompat.getColor(resources, R.color.lightgreen, null)
            set1.increasingPaintStyle = Paint.Style.FILL
            set1.neutralColor = ResourcesCompat.getColor(resources, R.color.grey, null)
            set1.setDrawValues(false)
            val data = CandleData(set1)
            chartPrices!!.data = data
            chartPrices!!.invalidate()
            val returnValue = Math.round(calculateReturn(mainActivity!!.newsPricesViewModel!!.priceList[0].open, mainActivity!!.newsPricesViewModel!!.priceList[mainActivity!!.newsPricesViewModel!!.priceList.size - 1].close) * 10000) / 100f
            var ret = ""
            ret = if (returnValue > 0) {
                stockReturn!!.setTextColor(ResourcesCompat.getColor(resources, R.color.lightgreen, null))
                "+$returnValue%"
            } else if (returnValue < 0) {
                stockReturn!!.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
                "$returnValue%"
            } else {
                stockReturn!!.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
                "$returnValue%"
            }
            stockReturn!!.text = ret
            val actual = (Math.round(mainActivity!!.newsPricesViewModel!!.priceList[mainActivity!!.newsPricesViewModel!!.priceList.size - 1].close * 100) / 100f).toString() + "$"
            actualStockPrice!!.text = actual
            stockName!!.text = mainActivity!!.newsPricesViewModel!!.asset.toUpperCase()
            progressBarSearch!!.visibility = View.INVISIBLE
        }
    }
    private fun populateStatistics() {
        requireActivity().runOnUiThread { statisticsListViewAdapter!!.notifyDataSetChanged() }
    }
}

