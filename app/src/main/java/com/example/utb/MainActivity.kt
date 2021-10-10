package com.example.utb

import android.R.attr.bitmap
import android.R.attr.visible
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.*
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainScene: Scene
    private lateinit var mainSceneVideo: Scene
    private lateinit var sceneMission: Scene
    private lateinit var transitionSet: TransitionSet
    private lateinit var sceneRootFrameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowView()

        sceneRootFrameLayout = findViewById(R.id.sceneRootFrameLayout)

        mainScene = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.main_scene, this)
        mainSceneVideo = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.scene_video, this)
        sceneMission = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.scene_mission, this)

        initAnimation()

        mainScene.enter()

        getRandomBinary()

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
        cbTransition.addTarget(R.id.mire_d_no_gray)
        cbTransition.addTarget(R.id.mire_g_no_gray)

        transitionSet = TransitionSet()
        transitionSet.ordering = TransitionSet.ORDERING_TOGETHER

        transitionSet.addTransition(cbTransition)
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

    private fun getRandomString(): String {
        return (1..250)
            .map { (0..10).random() }
            .joinToString("")
    }

    private fun getRandomBinary() {
        val binary: TextView = findViewById(R.id.binary)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                // use runOnUiThread(Runnable action)
                runOnUiThread {
                    binary.text = getRandomString()
                }
            }
        }, 500, 600)
    }

    fun launchVideo(view: View) {
        TransitionManager.go(mainSceneVideo, transitionSet)
        getVideoView("android.resource://" + packageName + "/" + R.raw.intro).start()
    }

    fun closeVideo(view: View) {
        TransitionManager.go(mainScene, transitionSet)
    }

    fun openMissionsView(view: View) {
        TransitionManager.go(sceneMission, transitionSet)

        val constraintLayout: ConstraintLayout =
            sceneMission.sceneRoot.findViewById(R.id.content_map)

        val backgroundContainer: View =
            sceneMission.sceneRoot.findViewById(R.id.background_container)

        val mireG: ImageView =
            sceneMission.sceneRoot.findViewById(R.id.mire_g)

        val mireD: ImageView = sceneMission.sceneRoot.findViewById(R.id.mire_d)

        val bottomVideo: ImageView = sceneMission.sceneRoot.findViewById(R.id.bottom_video)

        val close: ImageView = sceneMission.sceneRoot.findViewById(R.id.close)

        val videoBackground: View = sceneMission.sceneRoot.findViewById(R.id.video_background)

        val videoPlayer: VideoView = sceneMission.sceneRoot.findViewById(R.id.video_player)

        val topVideo: ImageView = sceneMission.sceneRoot.findViewById(R.id.top_video)

        val bottomVideoWidget: ImageView =
            sceneMission.sceneRoot.findViewById(R.id.bottom_video_widget)

        Handler(Looper.getMainLooper()).postDelayed({
            constraintLayout.visibility = View.VISIBLE
            backgroundContainer.visibility = View.VISIBLE
            mireG.visibility = View.VISIBLE
            mireD.visibility = View.VISIBLE
            bottomVideo.visibility = View.VISIBLE
            close.visibility = View.VISIBLE
            videoBackground.visibility = View.VISIBLE
            videoPlayer.visibility = View.VISIBLE
            topVideo.visibility = View.VISIBLE
            bottomVideoWidget.visibility = View.VISIBLE

        }, 1500)
    }

    fun loadMission(view: View) {
        val missionDetailImage: ImageView =
            sceneMission.sceneRoot.findViewById(R.id.map_mission_detail)
        val button = view as Button
        Picasso.get().load(
            "android.resource://" + packageName + "/" + this.resources.getIdentifier(
                view.text.toString(),
                "drawable",
                this.packageName
            )
        )
            .resize(600, 800).into(missionDetailImage)
    }
}