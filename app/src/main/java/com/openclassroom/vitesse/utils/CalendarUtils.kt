package com.openclassroom.vitesse.utils

import java.util.Calendar

fun createCalendar(year: Int, month: Int, day: Int): Calendar {
    return Calendar.getInstance().apply {
        set(year, month - 1, day)
    }
}