package com.example.assignment4

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background(screenX: Int, screenY: Int, res: Resources, dr: Int) {
    var x: Int = 0
    var y: Int = 0
    var background: Bitmap = BitmapFactory.decodeResource(res, dr)
    init {
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }


}