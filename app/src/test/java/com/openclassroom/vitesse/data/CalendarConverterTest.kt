package com.openclassroom.vitesse.data

import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class CalendarConverterTest {

    private val converter = CalendarConverter()

    @Test
    fun fromTimestamp_withNull_returnsNull() {
        val result = converter.fromTimestamp(null)
        assertNull(result)
    }

    @Test
    fun fromTimestamp_withValidTimestamp_returnsCalendar() {
        val timestamp = 1696032000000L
        val calendar = converter.fromTimestamp(timestamp)
        assertNotNull(calendar)
        assertEquals(timestamp, calendar?.timeInMillis)
    }

    @Test
    fun calendarToTimestamp_withNull_returnsNull() {
        val result = converter.calendarToTimestamp(null)
        assertNull(result)
    }

    @Test
    fun calendarToTimestamp_withValidCalendar_returnsTimestamp() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = 1696032000000L
        }
        val timestamp = converter.calendarToTimestamp(calendar)
        assertNotNull(timestamp)
        assertEquals(calendar.timeInMillis, timestamp)
    }
}