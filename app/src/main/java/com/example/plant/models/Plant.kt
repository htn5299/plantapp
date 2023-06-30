package com.example.plant.models

import java.io.Serializable

data class Plant (
    val name: String? = null,
    val owner: String? = null,
    val type: String? = null,
    val description: String? = null,
    val image : String? = null,
    val date : String? = null,
    var id: String?=null
)