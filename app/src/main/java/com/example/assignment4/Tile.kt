package com.example.assignment4

import android.content.res.Resources
import android.graphics.*

class Tile(var x: Int = 0, var y: Int = 0, res: Resources) {
    var isClicked = false
    private var defaultTile = BitmapFactory.decodeResource(res, R.drawable.ic_tile_default)
    private var clickedTile = BitmapFactory.decodeResource(res, R.drawable.ic_tile_clicked)

    init {
        var width = defaultTile.width
        var height = defaultTile.height

        width *= GameView.screenRatioX.toInt()
        height *= GameView.screenRatioY.toInt()

        defaultTile = Bitmap.createScaledBitmap(defaultTile, width, height, false)
        clickedTile = Bitmap.createScaledBitmap(clickedTile, width, height, false)
    }

    fun move(width: Int = 0, height: Int = 0) {
        x += 1
        y += 1
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true

        paint.setColor(Color.BLACK)
        canvas.drawRect(30.0f + x, 30.0f + y, 80.0f + x, 80.0f + y, paint)
    }

    fun getTile(): Bitmap {
        if (!isClicked) {
            return defaultTile
        }
        return clickedTile
    }
}
