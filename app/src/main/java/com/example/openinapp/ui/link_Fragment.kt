package com.example.openinapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openinapp.R
import com.example.openinapp.adapters.RecentLinksAdapter
import com.example.openinapp.adapters.TopLinksAdapter
import com.example.openinapp.apis.OpenInAppAPI
import com.example.openinapp.apis.RetrofitClient
import com.example.openinapp.data.DataResponse
import com.example.openinapp.data.model.RecentLink
import com.example.openinapp.data.model.TopLink
import com.example.openinapp.databinding.FragmentLinkFragmentBinding
import com.example.openinapp.utils.constants.token
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class LinkFragment : Fragment() {
    private var _binding: FragmentLinkFragmentBinding? = null
    private var MainRecentList: MutableList<RecentLink> = ArrayList()
    private val DummyRecentList: MutableList<RecentLink> = ArrayList()

    private var MainTopLinkList : MutableList<TopLink> = ArrayList()
    private var DummyTopLinkList : MutableList<TopLink> = ArrayList()

    lateinit var lineList : java.util.ArrayList<Entry>
    lateinit var lineDataSet: LineDataSet
    lateinit var lineData : LineData

    private var phonenumber:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLinkFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService = RetrofitClient.retrofit.create(OpenInAppAPI::class.java)
        val call = apiService.getData(token)
        call.enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    _binding?.todayClick?.totalClicksTv1?.text=data?.today_clicks.toString()
                    _binding?.source?.sourceTv1?.text=data?.top_source
                    _binding?.location?.locationTv1?.text=data?.top_location
                    MainRecentList.clear()
                    MainTopLinkList.clear()
                    DummyRecentList.clear()
                    DummyTopLinkList.clear()
                    if(data?.data?.recent_links!=null)
                    {
                        MainRecentList=(data.data.recent_links)
                        for (i in 0..3) {
                            DummyRecentList.add(MainRecentList[i])
                        }
                    }
                    if(data?.data?.top_links!=null)
                    {
                        MainTopLinkList=(data.data.top_links)
                        for (i in 0..3) {
                            DummyTopLinkList.add(MainTopLinkList[i])
                        }
                    }
                    _binding?.tvGreetings?.text=greetingMessage()
                    setGraphPropertiesAndValue(data?.data?.overall_url_chart)
                    BindToogleGroup()
                    setTopLinkRecyclerView(DummyTopLinkList)
                    setRecentLinkRecyclerView(DummyRecentList)
                    phonenumber=data?.support_whatsapp_number


                } else {
                    Log.e("API Error", "Error response code: ${response.code()}")
                }
            }


            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
            }
        })
        _binding?.whatsappTv?.setOnClickListener {

            // Create an intent to dial the phone number
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phonenumber")
            }
            // Start the intent
            startActivity(intent)
        }
        _binding?.faqTv?.setOnClickListener {
            openFaqLink()
        }
    }
    private fun openFaqLink() {
        val uri = Uri.parse("https://openinapp.com/faq")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    private fun greetingMessage():String
    {
        val calendar= Calendar.getInstance()
        val hour=calendar.get(Calendar.HOUR_OF_DAY)
        val greetings=mapOf(
            0 to "Good Night",
            5 to "Good Morning",
            12 to "Good Afternoon",
            16 to "Good Evening",
            21 to "Good Night"
        )
        return greetings[greetings.keys.firstOrNull { hour<it }]?:"Good Night"

    }
    private fun BindToogleGroup()
    {
        _binding?.togglebtns?.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.toplink_btn -> {
                        _binding?.rvRecentLinks?.visibility = View.GONE
                        _binding?.rvTopLinks?.visibility = View.VISIBLE
                    }
                    R.id.recentlink_btn -> {
                        _binding?.rvTopLinks?.visibility = View.GONE
                        _binding?.rvRecentLinks?.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    private fun setGraphPropertiesAndValue(urlChartResponse:Map<String,Int>?){
        lineList = java.util.ArrayList() // Initialize the lineList

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val startDate = dateFormat.parse(urlChartResponse?.keys?.minOrNull())
        val endDate = dateFormat.parse(urlChartResponse?.keys?.maxOrNull())
        val calendar = Calendar.getInstance()

        val textFormat = SimpleDateFormat("d MMM", Locale.US)
        _binding?.datesTv?.text = textFormat.format(startDate) + "-" + textFormat.format(endDate)

        urlChartResponse?.forEach { (key, value) ->
            val date = dateFormat.parse(key)
            calendar.time = date

            if (date in startDate..endDate) {
                val daysSinceStart = TimeUnit.MILLISECONDS.toDays(date.time - startDate.time).toFloat()
                lineList.add(Entry(daysSinceStart, value.toFloat()))
            }
        }

        lineDataSet = LineDataSet(lineList, null)
        lineDataSet.color = Color.parseColor("#0E6FFF")
        lineDataSet.setDrawValues(false) // Disable value text
        lineDataSet.setDrawCircles(false) // Disable drawing circles for data points
        lineDataSet.setDrawFilled(true)

        val startColor = Color.parseColor("#0E6FFF")
        val endColor = Color.TRANSPARENT
        val gradientFill = com.example.openinapp.utils.DrawableUtils.createGradientDrawable(startColor,endColor)
        lineDataSet.fillDrawable = gradientFill

        lineData = LineData(lineDataSet)
        _binding?.chartView?.data = lineData

        val xAxis = _binding?.chartView?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM // Set X-axis label position to the bottom
        xAxis?.setDrawAxisLine(true) // Enable drawing the axis line
        xAxis?.setDrawLabels(true) // Enable drawing the X-axis labels

        // Set custom labels for X-axis
        val labelCount = TimeUnit.MILLISECONDS.toDays(endDate.time - startDate.time).toInt()
        val xAxisValueFormatter = object : ValueFormatter() {
            private val format = SimpleDateFormat("d MMM", Locale.US)
            override fun getFormattedValue(value: Float): String {
                calendar.time = startDate
                calendar.add(Calendar.DAY_OF_YEAR, value.toInt())
                val date = calendar.time
                return if (value.toInt() % 5 == 0) {
                    format.format(date)
                } else {
                    ""
                }
            }
        }
        xAxis?.valueFormatter = xAxisValueFormatter
        xAxis?.granularity = 1f
        xAxis?.labelCount = labelCount

        val yAxisRight = _binding?.chartView?.axisRight
        yAxisRight?.isEnabled = false // Disable right-side label

        _binding?.chartView?.description?.isEnabled = false // Disable graph description

        _binding?.chartView?.legend?.isEnabled = false // Disable legend

        _binding?.chartView?.invalidate() // Refresh the chart

    }
    private fun setTopLinkRecyclerView(list:List<TopLink>)
    {
        val adapter= TopLinksAdapter(list)
        _binding?.rvTopLinks?.layoutManager=LinearLayoutManager(context)
        _binding?.rvTopLinks?.setHasFixedSize(true)
        _binding?.rvTopLinks?.adapter=adapter

    }
    private fun setRecentLinkRecyclerView(list:List<RecentLink>)
    {
        val adapter=RecentLinksAdapter(list)
        _binding?.rvRecentLinks?.layoutManager=LinearLayoutManager(context)
        _binding?.rvRecentLinks?.setHasFixedSize(true)
        _binding?.rvRecentLinks?.adapter=adapter

    }
    private fun onRecentLinksItemClicked(recentLink:RecentLink) {
        val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Create a new ClipData object
        val clipData = ClipData.newPlainText("text", recentLink.web_link)

        // Set the clipboard's primary clip
        clipboardManager.setPrimaryClip(clipData)
    }
    private fun onTopLinksItemClicked(topLink:TopLink) {
        val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Create a new ClipData object
        val clipData = ClipData.newPlainText("text", topLink.web_link)

        // Set the clipboard's primary clip
        clipboardManager.setPrimaryClip(clipData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}