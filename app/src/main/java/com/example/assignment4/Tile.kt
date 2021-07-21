package com.example.assignment4

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

data class Tile(var x: Float, var y: Float) {
    val dx = 0.1f
    val dy = 0.1f

    fun move(width: Int = 0, height: Int = 0) {
        x += dx
        y += dy
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true

        paint.setColor(Color.BLACK)
        canvas.drawRect(30.0f + x, 30.0f + y, 80.0f + x, 80.0f + y, paint)
    }
}
