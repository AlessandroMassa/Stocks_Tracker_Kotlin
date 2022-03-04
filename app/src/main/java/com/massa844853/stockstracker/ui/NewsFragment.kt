package com.massa844853.stockstracker.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.adapter.NewsListViewBaseAdapter
import com.massa844853.stockstracker.models.News
import com.massa844853.stockstracker.repository.NewsRepository
import com.massa844853.stockstracker.utils.NewsResponseCallback
import java.util.*
class NewsFragment : Fragment(), NewsResponseCallback {
    private var newsRepository: NewsRepository? = null
    private var progressBar: ProgressBar? = null
    private var newsListViewBaseAdapter: NewsListViewBaseAdapter? = null
    private var listView: ListView? = null
    private var newsList: MutableList<News>? = null
    private var mainActivity: MainActivity? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        view.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.backgroundblue, null))
        progressBar = view.findViewById(R.id.progress_bar)
        listView = view.findViewById(R.id.listview_news)
        mainActivity = activity as MainActivity?
        newsList = ArrayList()
        newsListViewBaseAdapter = NewsListViewBaseAdapter(newsList, requireActivity())
        listView!!.adapter = newsListViewBaseAdapter
        listView!!.onItemClickListener = OnItemClickListener { parent, view, position, id -> }
        progressBar!!.visibility = View.VISIBLE
        newsRepository = NewsRepository(requireActivity().application, this)
        newsRepository!!.start(mainActivity!!.newsPricesViewModel!!.lastUpdate)
        return view
    }
    override fun onResponse(newsList: MutableList<News>?, lastupdate: Long) {
        this.newsList!!.addAll(newsList!!)
        if (this.newsList != null) {
            requireActivity().runOnUiThread {
                mainActivity!!.newsPricesViewModel!!.lastUpdate = lastupdate
                newsListViewBaseAdapter!!.notifyDataSetChanged()
                progressBar!!.visibility = View.GONE
            }
        }
    }
    override fun onFailure(errorMessage: String?) {
    }
}
