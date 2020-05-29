/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var starCenter: ImageView
    lateinit var rotaterButton: Button
    lateinit var translaterButton: Button
    lateinit var scalerButton: Button
    lateinit var faderButton: Button
    lateinit var colorizerButton: Button
    lateinit var showerStarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        starCenter = star
        rotaterButton = rotateButton
        translaterButton = translateButton
        scalerButton = scaleButton
        faderButton = fadeButton
        colorizerButton = colorizeButton
        showerStarButton = showerButton

        rotaterButton.setOnClickListener {
            rotater()
        }

        translaterButton.setOnClickListener {
            translater()
        }

        scalerButton.setOnClickListener {
            scaler()
        }

        faderButton.setOnClickListener {
            fader()
        }

        colorizerButton.setOnClickListener {
            colorizer()
        }

        showerStarButton.setOnClickListener {
            shower()
        }
    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(starCenter, View.ROTATION, -360f,0f)
        animator.duration = 1000
        animator.disableViewDuringAnimation(rotaterButton)
        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(starCenter,View.TRANSLATION_X, 200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(translaterButton)
        animator.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,4f)
        val animator =  ObjectAnimator.ofPropertyValuesHolder(starCenter,scaleX,scaleY)
        animator.duration = 1000
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scalerButton)
        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(starCenter,View.ALPHA, 0f)
        animator.duration = 1000
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(faderButton)
        animator.start()
    }

    private fun colorizer() {
        val animator = ObjectAnimator.ofArgb(starCenter.parent,"backgroundColor", Color.BLACK, Color.BLUE)
        animator.duration = 1000
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizerButton)
        animator.start()
    }

    private fun shower() {
        val container = starCenter.parent as ViewGroup
        val width = container.width
        val height = container.height
        var starW = starCenter.width.toFloat()
        var starH = starCenter.height.toFloat()

        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        )
        newStar.scaleX = Math.random().toFloat() * 1.5f + 0.1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY
        newStar.translationX = Math.random().toFloat() * width - starW/2

        val mover = ObjectAnimator.ofFloat(newStar,View.TRANSLATION_Y,-starH, height + starH)
        mover.interpolator = AccelerateInterpolator(1f)

        val rotator = ObjectAnimator.ofFloat(newStar,View.ROTATION,(Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        val translator = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_X,(Math.random().toFloat() * width - starW/2))
        translator.interpolator = AccelerateInterpolator(0.2f)

        container.addView(newStar)

        val set = AnimatorSet()
        set.playTogether(mover,rotator,translator)
        set.duration = (Math.random() * 1500 +500).toLong()
        set.addListener(object:AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        set.start()
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view : View){
        addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        }
        )
    }

}
