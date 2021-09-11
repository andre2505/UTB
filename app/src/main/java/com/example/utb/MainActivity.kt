package com.example.utb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.*
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var mainScence: Scene
    private lateinit var mainScenceVideo: Scene
    private lateinit var currentScene: Scene
    private lateinit var transition: Transition
    private lateinit var transitionSet: TransitionSet
    private lateinit var buttonVideo: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val sceneRootFrameLayout: FrameLayout = findViewById(R.id.sceneRootFrameLayout)
        // Step 1: Create a Scene object for both the starting and ending layout
        mainScence = Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.main_scene, this)
        mainScenceVideo =
            Scene.getSceneForLayout(sceneRootFrameLayout, R.layout.main_scene_video, this)
        mainScence.enter()

        buttonVideo = mainScence.sceneRoot.findViewById(R.id.video)
        currentScene = mainScence

        val cbTransition = ChangeBounds()
        cbTransition.duration = 1000
        cbTransition.interpolator = LinearInterpolator()
        cbTransition.addTarget(R.id.mire_d)

        /*val fadeInTransition = Fade(Fade.IN)
        fadeInTransition.duration = 250
        fadeInTransition.startDelay = 400
        fadeInTransition.addTarget(R.id.mire_d)

        val fadeOutTransition = Fade(Fade.OUT)
        fadeOutTransition.duration = 50
        fadeOutTransition.addTarget(R.id.mire_d)*/

        transitionSet = TransitionSet()
        transitionSet.ordering = TransitionSet.ORDERING_TOGETHER

        transitionSet.addTransition(cbTransition)
        //transitionSet.addTransition(fadeInTransition)
        //transitionSet.addTransition(fadeOutTransition)

        buttonVideo.setOnClickListener {
            TransitionManager.go(mainScenceVideo, transitionSet)
            currentScene = mainScenceVideo
        }
    }


}