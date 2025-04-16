package com.example.bookapitest

import com.example.bookapitest.models.Recommendation

data class RecState(
    val id_text:Int=0,
    val error:String="",
    val recList:List<Recommendation> = listOf()
)
