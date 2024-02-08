package com.example.openinapp.data

import com.example.openinapp.data.model.RecentLink
import com.example.openinapp.data.model.TopLink

data class Data(
    val overall_url_chart: Map<String, Int>,
    val recent_links: MutableList<RecentLink>,
    val top_links: MutableList<TopLink>
)