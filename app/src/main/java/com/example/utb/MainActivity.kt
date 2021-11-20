package com.example.utb

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.transition.*
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.utb.html_decoder.HtmlDecoder
import com.squareup.picasso.Picasso
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mainScene: Scene
    private lateinit var mainSceneVideo: Scene
    private lateinit var sceneMission: Scene
    private lateinit var sceneNotice: Scene
    private lateinit var transitionSet: TransitionSet
    private lateinit var sceneRootFrameLayout: FrameLayout
    private lateinit var noticeImage: ImageView
    private lateinit var folderImage: ImageView
    private lateinit var mapImage: ImageView
    private lateinit var graphOne: ImageView
    private lateinit var graphTwo: ImageView

    private var isStarted: Boolean = false
    private var timeStart: Date? = null
    private var timeEnd: Date? = null
    private var countVideo: Int = 1
    private var countEnding: Int = 1
    private var video: Int = R.raw.intro

    private val UI_OPTIONS = (View.SYSTEM_UI_FLAG_LOW_PROFILE
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowView()

        sceneRootFrameLayout = findViewById(R.id.sceneRootFrameLayout)

        mainScene = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.main_scene, this)
        mainSceneVideo = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.scene_video, this)
        sceneMission = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.scene_mission, this)
        sceneNotice = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.scene_notice, this)

        initAnimation()

        mainScene.enter()

        noticeImage = mainScene.sceneRoot.findViewById(R.id.notice)
        folderImage = mainScene.sceneRoot.findViewById(R.id.folders)
        mapImage = mainScene.sceneRoot.findViewById(R.id.worldmap)
        graphOne = mainScene.sceneRoot.findViewById(R.id.graph)
        graphTwo = mainScene.sceneRoot.findViewById(R.id.graph_2)

        mapImage.setOnClickListener(this)
        graphOne.setOnClickListener(this)
        graphTwo.setOnClickListener(this)

        noticeImage.visibility = View.INVISIBLE
        folderImage.visibility = View.INVISIBLE

        getRandomBinary()

    }

    private fun initWindowView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val layout = window.attributes
        layout.screenBrightness = 1f
        window.attributes = layout

        window.decorView.systemUiVisibility = UI_OPTIONS

    }

    private fun initAnimation() {

        val cbTransition = ChangeBounds()
        cbTransition.duration = 500
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

        if (isStarted == false) {
            timeStart = Calendar.getInstance().time
        }

        TransitionManager.go(mainSceneVideo, transitionSet)

        val backgroundContainer: View =
            mainSceneVideo.sceneRoot.findViewById(R.id.background_container)

        val mireG: ImageView =
            mainSceneVideo.sceneRoot.findViewById(R.id.mire_g)

        val mireD: ImageView = mainSceneVideo.sceneRoot.findViewById(R.id.mire_d)

        val bottomVideo: ImageView = mainSceneVideo.sceneRoot.findViewById(R.id.bottom_video)

        val close: ImageView = mainSceneVideo.sceneRoot.findViewById(R.id.close)

        val videoBackground: View = mainSceneVideo.sceneRoot.findViewById(R.id.video_background)

        val videoPlayer: VideoView = mainSceneVideo.sceneRoot.findViewById(R.id.video_player)

        val topVideo: ImageView = mainSceneVideo.sceneRoot.findViewById(R.id.top_video)

        val bottomVideoWidget: ImageView =
            mainSceneVideo.sceneRoot.findViewById(R.id.bottom_video_widget)

        Handler(Looper.getMainLooper()).postDelayed({
            backgroundContainer.visibility = View.VISIBLE
            mireG.visibility = View.VISIBLE
            mireD.visibility = View.VISIBLE
            bottomVideo.visibility = View.VISIBLE
            close.visibility = View.VISIBLE
            videoBackground.visibility = View.VISIBLE
            videoPlayer.visibility = View.VISIBLE
            topVideo.visibility = View.VISIBLE
            bottomVideoWidget.visibility = View.VISIBLE
            getVideoView("android.resource://$packageName/$video").start()

        }, 800)

        if (!noticeImage.isVisible && !folderImage.isVisible) {
            noticeImage.visibility = View.VISIBLE
            folderImage.visibility = View.VISIBLE
        }

    }

    fun closeVideo(view: View) {

        // Return Video Intro
        video = R.raw.intro

        TransitionManager.go(mainScene, transitionSet)

        mapImage = mainScene.sceneRoot.findViewById(R.id.worldmap)
        graphOne = mainScene.sceneRoot.findViewById(R.id.graph)
        graphTwo = mainScene.sceneRoot.findViewById(R.id.graph_2)

        mapImage.setOnClickListener(this)
        graphOne.setOnClickListener(this)
        graphTwo.setOnClickListener(this)
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

        }, 800)
    }

    fun openNotice(view: View) {
        TransitionManager.go(sceneNotice, transitionSet)

        val constraintLayout: ConstraintLayout =
            sceneNotice.sceneRoot.findViewById(R.id.content_notice)

        val backgroundContainer: View =
            sceneNotice.sceneRoot.findViewById(R.id.background_container)

        val mireG: ImageView =
            sceneNotice.sceneRoot.findViewById(R.id.mire_g)

        val mireD: ImageView = sceneNotice.sceneRoot.findViewById(R.id.mire_d)

        val bottomVideo: ImageView = sceneNotice.sceneRoot.findViewById(R.id.bottom_video)

        val close: ImageView = sceneNotice.sceneRoot.findViewById(R.id.close)

        val videoBackground: View = sceneNotice.sceneRoot.findViewById(R.id.video_background)

        val videoPlayer: VideoView = sceneNotice.sceneRoot.findViewById(R.id.video_player)

        val topVideo: ImageView = sceneNotice.sceneRoot.findViewById(R.id.top_video)

        val bottomVideoWidget: ImageView =
            sceneNotice.sceneRoot.findViewById(R.id.bottom_video_widget)

        //encode text html and justify
        val textHeaderNotice: WebView =  sceneNotice.sceneRoot.findViewById(R.id.description_light_button)
        val textOneNotice: WebView = sceneNotice.sceneRoot.findViewById(R.id.description_arc)
        val textTwoNotice: WebView = sceneNotice.sceneRoot.findViewById(R.id.description_cube)

        textHeaderNotice.loadDataWithBaseURL(null,getString(R.string.text_header_notice),"text/html", "UTF-8", null)
        textHeaderNotice.setBackgroundColor(Color.TRANSPARENT)

        textOneNotice.loadDataWithBaseURL(null,getString(R.string.text_1_notice),"text/html", "UTF-8", null)
        textOneNotice.setBackgroundColor(Color.TRANSPARENT)

        textTwoNotice.loadDataWithBaseURL(null,getString(R.string.text_2_notice),"text/html", "UTF-8", null)
        textTwoNotice.setBackgroundColor(Color.TRANSPARENT)

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

        }, 800)
    }

    fun loadMission(view: View) {
        val missionDetailImage: ImageView =
            sceneMission.sceneRoot.findViewById(R.id.map_mission_detail)
        val button = view as Button
        Picasso.get().load(
            "android.resource://$packageName/" + this.resources.getIdentifier(
                view.text.toString(),
                "drawable",
                this.packageName
            )
        )
            .resize(600, 800).into(missionDetailImage)
    }

    override fun onClick(v: View?) {
        if (mapImage.isPressed && graphOne.isPressed) {
            if (countVideo == 3) {
                videoAlert(v!!)
                countVideo = 1
            } else {
                countVideo += 1
            }
        } else if (timeStart != null && mapImage.isPressed && graphTwo.isPressed) {
            if (countEnding == 3) {

                countEnding = 1

                timeEnd = Calendar.getInstance().time
                val time: Long? = timeEnd?.time?.minus(timeStart?.time!!)

                val seconds = time?.div(1000)
                val minutes = seconds?.div(60)
                val hours = minutes?.div(60)

                val timeDisplayed = hours.toString() + "H " + minutes.toString() + "MN"
                this.timeAlert(timeDisplayed)

            } else {
                countEnding += 1
            }
        }
    }

    fun videoAlert(view: View) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Vidéo")
            .setMessage("Choisissez votre vidéo")
            .setPositiveButton(
                "Gagnée"
            ) { dialog, which ->
                video = R.raw.victoire
                launchVideo(view)
            }
            .setNegativeButton(
                "Perdue"
            ) { dialog, which ->
                video = R.raw.defaite
                launchVideo(view)
            }
            .setCancelable(false)
            .create()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        } else {
            alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }

        alertDialog.show()
    }

    fun timeAlert(time: String) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Temps")
            .setMessage(time)
            .setPositiveButton(
                "Redémarrer partie"
            ) { dialog, which ->
                timeStart = null
                noticeImage = mainScene.sceneRoot.findViewById(R.id.notice)
                folderImage = mainScene.sceneRoot.findViewById(R.id.folders)
                noticeImage.visibility = View.INVISIBLE
                folderImage.visibility = View.INVISIBLE
            }
            .setNegativeButton(
                "Ok"
            ) { dialog, which ->

            }
            .setCancelable(false)
            .create()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        } else {
            alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }

        alertDialog.show()
    }

}