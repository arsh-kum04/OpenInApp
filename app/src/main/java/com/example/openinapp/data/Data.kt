package com.example.openinapp.data

data class data(
    val favourite_links: List<Any>,
    val overall_url_chart: OverallUrlChart,
    val recent_links: List<RecentLink>,
    val top_links: List<TopLink>
)