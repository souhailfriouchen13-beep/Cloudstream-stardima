package com.stardima

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class StardimaPlugin: MainAPI() {
    override var mainUrl = "https://stardima.one"
    override var name = "Stardima"
    override val supportedTypes = setOf(TvType.Anime, TvType.Movie, TvType.TvSeries)

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query"
        val document = app.get(url).document
        return document.select("div.result-item").map {
            val title = it.select("h3 a").text()
            val link = it.select("h3 a").attr("href")
            val poster = it.select("img").attr("src")
            TvSeriesSearchResponse(title, link, this.name, TvType.TvSeries, poster, null, null)
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        val title = doc.select("h1").text()
        val poster = doc.select("div.poster img").attr("src")
        val episodes = doc.select("ul.episodes li a").mapIndexed { index, ep ->
            Episode(
                data = ep.attr("href"),
                name = ep.text(),
                episode = index + 1
            )
        }
        return TvSeriesLoadResponse(
            title = title,
            url = url,
            apiName = this.name,
            type = TvType.TvSeries,
            episodes = episodes,
            posterUrl = poster
        )
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        val iframe = doc.select("iframe").attr("src")
        loadExtractor(iframe, data, subtitleCallback, callback)
        return true
    }
}
