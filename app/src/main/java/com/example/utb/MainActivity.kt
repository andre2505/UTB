package com.example.utb

import android.os.Bundle
import android.transition.*
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var mainScene: Scene
    private lateinit var mainSceneVideo: Scene
    private lateinit var transitionSet: TransitionSet
    private lateinit var sceneRootFrameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowView()

        sceneRootFrameLayout = findViewById(R.id.sceneRootFrameLayout)

        mainScene = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.main_scene, this)
        mainSceneVideo =
            Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.main_scene_video, this)

        initAnimation()

        mainScene.enter()
    }

    private fun initWindowView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun initAnimation() {
        val cbTransition = ChangeBounds()
        cbTransition.duration = 1000
        cbTransition.interpolator = LinearInterpolator()
        cbTransition.addTarget(R.id.mire_d)
        cbTransition.addTarget(R.id.mire_g)

        val fadeInTransition = Fade(Fade.IN)
        fadeInTransition.duration = 1000
        fadeInTransition.startDelay = 500
        fadeInTransition.addTarget(R.id.top_video)
        fadeInTransition.addTarget(R.id.bottom_video)
        fadeInTransition.addTarget(R.id.close)
        fadeInTransition.addTarget(R.id.bottom_video_widget)

        val fadeOutTransition = Fade(Fade.OUT)
        fadeOutTransition.startDelay = 500
        fadeOutTransition.duration = 1000
        fadeOutTransition.addTarget(R.id.mire_d)
        fadeInTransition.addTarget(R.id.top_video)
        fadeInTransition.addTarget(R.id.bottom_video)
        fadeInTransition.addTarget(R.id.close)
        fadeInTransition.addTarget(R.id.bottom_video_widget)

        transitionSet = TransitionSet()
        transitionSet.ordering = TransitionSet.ORDERING_TOGETHER

        transitionSet.addTransition(cbTransition)
        transitionSet.addTransition(fadeInTransition)
        transitionSet.addTransition(fadeOutTransition)
    }

    private fun getVideoView(path: String): VideoView {
        val videoView: VideoView = mainSceneVideo.sceneRoot.findViewById(R.id.video_player)

        videoView.setVideoPath(path)
        prepareVideo(videoView)

        return videoView
    }

    private fun prepareVideo(videoView: VideoView) {
        videoView.setOnPreparedListener { mp -> //Get your video's width and height
            val videoWidth = mp.videoWidth
            val videoHeight = mp.videoHeight

            //Get VideoView's current width and height
            val videoViewWidth = videoView.width
            val videoViewHeight = videoView.height
            val xScale = videoViewWidth.toFloat() / videoWidth
            val yScale = videoViewHeight.toFloat() / videoHeight
            val scale = Math.min(xScale, yScale)
            val scaledWidth = scale * videoWidth
            val scaledHeight = scale * videoHeight

            //Set the new size for the VideoView based on the dimensions of the video
            val layoutParams = videoView.layoutParams
            layoutParams.width = scaledWidth.toInt()
            layoutParams.height = scaledHeight.toInt()
            videoView.layoutParams = layoutParams
        }
    }

    fun launchVideo(view: View) {
        TransitionManager.go(mainSceneVideo, transitionSet)
        getVideoView("android.resource://" + packageName + "/" + R.raw.intro).start()
    }

    fun closeVideo(view: View) {
        TransitionManager.go(mainScene, transitionSet)
    }
}