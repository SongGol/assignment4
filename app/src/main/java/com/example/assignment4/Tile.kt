package com.example.assignment4

import android.graphics.Canvas
import android.graphics.Paint

data class Tile(var x: Float, var y: Float) {
    val dx = 0.1f
    val dy = 0.1f

    fun move(width: Int, height: Int) {
        x += dx
        y += dy
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true


    }
}
