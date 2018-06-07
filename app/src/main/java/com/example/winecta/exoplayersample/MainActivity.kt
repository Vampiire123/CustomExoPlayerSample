package com.example.winecta.exoplayersample

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util

class MainActivity : Activity() {

    val url: String = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"

    var btnPlay: Button? = null
    var btnPause: Button? = null
    var btnStop: Button? = null

    private var simpleExoPlayerView: SimpleExoPlayerView? = null
    private var player: SimpleExoPlayer? = null

    private var mediaDataSourceFactory: DataSource.Factory? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var bandwidthMeter: BandwidthMeter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlay = findViewById(R.id.btn_play)
        btnPause = findViewById(R.id.btn_pause)
        btnStop = findViewById(R.id.btn_stop)

        bandwidthMeter = DefaultBandwidthMeter()
        mediaDataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"),
                bandwidthMeter as TransferListener<in DataSource>)

        initializePlayer()

        btnPlay?.setOnClickListener({
            player?.playWhenReady = true

            btnPause?.visibility = VISIBLE
            btnPlay?.visibility = INVISIBLE
        })
        btnPause?.setOnClickListener({
            player?.playWhenReady = false

            btnPlay?.visibility = VISIBLE
            btnPause?.visibility = INVISIBLE
        })
        btnStop?.setOnClickListener({
            player?.release()
            player = null
            trackSelector = null
            initializePlayer()

            btnPlay?.visibility = VISIBLE
            btnPause?.visibility = INVISIBLE
        })
    }

    private fun initializePlayer() {
        simpleExoPlayerView = findViewById(R.id.player_view)

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        simpleExoPlayerView?.player = player
        simpleExoPlayerView?.useController = false

        val extractorsFactory = DefaultExtractorsFactory()

        val mediaSource = ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null)

        player?.prepare(mediaSource)
    }
}