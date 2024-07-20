package com.example.openinapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openinapp.R
import com.example.openinapp.adapters.RecentLinksAdapter
import com.example.openinapp.adapters.TopLinksAdapter
import com.example.openinapp.apis.OpenInAppAPI
import com.example.openinapp.apis.RetrofitClient
import com.example.openinapp.data.DataResponse
import com.example.openinapp.data.OverallUrlChart
import com.example.openinapp.data.RecentLink
import com.example.openinapp.data.TopLink
import com.example.openinapp.databinding.FragmentLinkFragmentBinding
import com.example.openinapp.utils.constants
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.util.Calendar

class LinkFragment : Fragment() {
    private var _binding: FragmentLinkFragmentBinding? = null
    private var MainRecentList: MutableList<RecentLink> = ArrayList()
    private val DummyRecentList: MutableList<RecentLink> = ArrayList()

    private var MainTopLinkList: MutableList<TopLink> = ArrayList()
    private val DummyTopLinkList: MutableList<TopLink> = ArrayList()

    private var topLinksAdapter: TopLinksAdapter? = null
    private var recentLinksAdapter: RecentLinksAdapter? = null

    private var phonenumber: String? = null
    private var isSearchViewOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLinkFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpApiCall()

        _binding?.viewAllLinkTv?.setOnClickListener {
            if (_binding?.rvTopLinks?.visibility == View.VISIBLE) {
                setTopLinkRecyclerView(MainTopLinkList)
            } else if (_binding?.rvRecentLinks?.visibility == View.VISIBLE) {
                setRecentLinkRecyclerView(MainRecentList)
            }
        }

        _binding?.whatsappTv?.setOnClickListener {
            openWhatsapp()
        }

        _binding?.faqTv?.setOnClickListener {
            openFaqLink()
        }

        setUpSearchView()
    }

    private fun setUpSearchView() {
        _binding?.searchIv?.setOnClickListener {
            if (isSearchViewOpen) {
                _binding?.searchIv?.setQuery("", false)
            }
            isSearchViewOpen = !isSearchViewOpen
        }

        _binding?.searchIv?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterLists(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterLists(newText)
                return true
            }
        })
    }

    private fun setUpApiCall() {
        val apiService = RetrofitClient.retrofit.create(OpenInAppAPI::class.java)
        val call = apiService.getData(constants.token)
        call.enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    _binding?.todayClick?.totalClicksTv1?.text = data?.today_clicks.toString()
                    _binding?.source?.sourceTv1?.text = data?.top_source.toString()
                    _binding?.location?.locationTv1?.text = data?.top_location.toString()
                    MainRecentList.clear()
                    MainTopLinkList.clear()
                    DummyRecentList.clear()
                    DummyTopLinkList.clear()

                    data?.data?.recent_links?.let {
                        MainRecentList.addAll(it)
                        DummyRecentList.addAll(MainRecentList.take(4))
                    }

                    data?.data?.top_links?.let {
                        MainTopLinkList.addAll(it)
                        DummyTopLinkList.addAll(MainTopLinkList.take(4))
                    }

                    _binding?.tvGreetings?.text = greetingMessage()
                    BindToogleGroup()
                    setTopLinkRecyclerView(DummyTopLinkList)
                    setRecentLinkRecyclerView(DummyRecentList)

                    phonenumber = data?.support_whatsapp_number?.let {
                        if (it.startsWith("+")) it else "+$it"
                    }
                    data?.data?.overall_url_chart?.let {
                        processChartData(it)
                    } ?: Log.e("Chart Data Error", "overall_url_chart is null or empty")

                } else {
                    Log.e("API Error", "Error response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
            }
        })
    }

    private fun processChartData(overallUrlChart: OverallUrlChart) {
        val entries = convertOverallUrlChartToEntries(overallUrlChart)
        setupChart(entries)
    }

    private fun convertOverallUrlChartToEntries(chart: OverallUrlChart): List<Entry> {
        return listOf(
            Entry(0f, chart.hour0.toFloat()),
            Entry(1f, chart.hour1.toFloat()),
            Entry(2f, chart.hour2.toFloat()),
            Entry(3f, chart.hour3.toFloat()),
            Entry(4f, chart.hour4.toFloat()),
            Entry(5f, chart.hour5.toFloat()),
            Entry(6f, chart.hour6.toFloat()),
            Entry(7f, chart.hour7.toFloat()),
            Entry(8f, chart.hour8.toFloat()),
            Entry(9f, chart.hour9.toFloat()),
            Entry(10f, chart.hour10.toFloat()),
            Entry(11f, chart.hour11.toFloat()),
            Entry(12f, chart.hour12.toFloat()),
            Entry(13f, chart.hour13.toFloat()),
            Entry(14f, chart.hour14.toFloat()),
            Entry(15f, chart.hour15.toFloat()),
            Entry(16f, chart.hour16.toFloat()),
            Entry(17f, chart.hour17.toFloat()),
            Entry(18f, chart.hour18.toFloat()),
            Entry(19f, chart.hour19.toFloat()),
            Entry(20f, chart.hour20.toFloat()),
            Entry(21f, chart.hour21.toFloat()),
            Entry(22f, chart.hour22.toFloat()),
            Entry(23f, chart.hour23.toFloat())
        )
    }

    @SuppressLint("NewApi")
    private fun setupChart(entries: List<Entry>) {
        val dataSet = LineDataSet(entries, "Overall URL Chart").apply {
            color = ContextCompat.getColor(requireContext(), R.color.blue)
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)
            setDrawValues(false)
            setDrawCircles(true)
            setDrawFilled(true)
        }

        val gradientDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_fill)
        dataSet.fillDrawable = gradientDrawable

        val lineData = LineData(dataSet)
        _binding?.chartView?.data = lineData

        val xAxis = _binding?.chartView?.xAxis?.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = IndexAxisValueFormatter((0..23).map { String.format("%02d:00", it) })
        }

        _binding?.chartView?.axisLeft?.apply {
            setLabelCount(5, true)
            setDrawAxisLine(true)
            setDrawGridLines(true)
        }

        _binding?.chartView?.axisRight?.isEnabled = false // Disable right-side labels

        _binding?.chartView?.description?.isEnabled = false
        _binding?.chartView?.legend?.isEnabled = false
        _binding?.chartView?.invalidate()
    }

    private fun openWhatsapp() {
        phonenumber?.let {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$it&text=${
                URLEncoder.encode("Hey, I need a Help!", "UTF-8")
            }")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } ?: run {
            Log.e("Whatsapp Error", "Phone number is not initialized.")
        }
    }

    private fun openFaqLink() {
        val uri = Uri.parse("https://openinapp.com/faq")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun greetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greetings = when {
            hour in 0..11 -> "Good Morning"
            hour in 12..17 -> "Good Afternoon"
            hour in 18..23 -> "Good Evening"
            else -> "Hello"
        }
        return greetings
    }

    private fun BindToogleGroup() {
        _binding?.togglebtns?.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.toplink_btn -> {
                        _binding?.rvRecentLinks?.visibility = View.GONE
                        _binding?.rvTopLinks?.visibility = View.VISIBLE
                        setTopLinkRecyclerView(DummyTopLinkList)
                    }
                    R.id.recentlink_btn -> {
                        _binding?.rvTopLinks?.visibility = View.GONE
                        _binding?.rvRecentLinks?.visibility = View.VISIBLE
                        setRecentLinkRecyclerView(DummyRecentList)
                    }
                }
            }
        }
    }

    private fun setTopLinkRecyclerView(list: List<TopLink>) {
        _binding?.rvTopLinks?.layoutManager = LinearLayoutManager(context)
        topLinksAdapter = TopLinksAdapter(requireContext(), list)
        _binding?.rvTopLinks?.adapter = topLinksAdapter
        _binding?.rvTopLinks?.visibility = View.VISIBLE
    }

    private fun setRecentLinkRecyclerView(list: List<RecentLink>) {
        _binding?.rvRecentLinks?.layoutManager = LinearLayoutManager(context)
        recentLinksAdapter = RecentLinksAdapter(requireContext(), list)
        _binding?.rvRecentLinks?.adapter = recentLinksAdapter
        _binding?.rvRecentLinks?.visibility = View.VISIBLE
    }

    private fun filterLists(query: String?) {
        val filteredRecentList = MainRecentList.filter {
            it.title.contains(query ?: "", ignoreCase = true)
        }
        val filteredTopLinkList = MainTopLinkList.filter {
            it.title.contains(query ?: "", ignoreCase = true)
        }

        setRecentLinkRecyclerView(filteredRecentList)
        setTopLinkRecyclerView(filteredTopLinkList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
