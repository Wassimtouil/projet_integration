package com.example.myapplication.Model.Entity

data class TravelPlan(
    val destination: String,
    val days: List<Day>
)
data class Day (
    val day:Int,
    val items:List<Item>
)
data class Item(
    val title:String,
    val details:String,
    val image_query: String,
    var image_url:String?=null,
    val localisation:String?=null
)
data class ItemWithDay(
    val day: Int,
    val item: Item
)