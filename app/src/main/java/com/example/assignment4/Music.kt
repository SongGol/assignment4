package com.example.assignment4

data class Music(val title: String, val artist: String, var maxScore: Int = 0,
                 var bPurchase: Boolean = false, var bHeart: Boolean = false, val price: Int = 1000)
