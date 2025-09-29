import com.openclassroom.vitesse.utils.createCalendar

import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class CreateCalendarTest {

    @Test
    fun `createCalendar returns correct date`() {
        val year = 2023
        val month = 5
        val day = 15

        val calendar = createCalendar(year, month, day)

        assertEquals(year, calendar.get(Calendar.YEAR))
        assertEquals(month - 1, calendar.get(Calendar.MONTH))
        assertEquals(day, calendar.get(Calendar.DAY_OF_MONTH))
    }
}