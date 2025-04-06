package ir.aliranjbarzadeh.nikantask.core.helpers

import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

object DateTimeHelper {

	fun currentDate(): Date {
		return Calendar.getInstance().time
	}

	fun currentDateUTC(days: Int = 0, type: String = "dec"): Date {
		val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
		utcCalendar.time = currentDate()

		val dayType = if (type == "dec") {
			-1
		} else {
			1
		}
		utcCalendar.add(Calendar.DAY_OF_YEAR, days * dayType)

		return utcCalendar.time
	}

	fun formatDateTime(date: Date, persianFormat: String = "j F Y H:i", englishFormat: String = "MMM F, yyyy HH:mm"): String {
		if (LanguageHelper.getLanguage() == "fa") {
			val pDate = PersianDate(date)
			pDate.setLocal(LocaleHelper.getLocale())
			val pDateFormatter = PersianDateFormat(persianFormat)

			return pDateFormatter.format(pDate)
		}

		val dateFormat = SimpleDateFormat(englishFormat, LocaleHelper.getLocale())
		return dateFormat.format(date)
	}
}