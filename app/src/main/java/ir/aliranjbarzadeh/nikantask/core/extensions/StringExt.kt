package ir.aliranjbarzadeh.nikantask.core.extensions

fun String.fixArabic(): String {
	return this.replace("ي", "ی")
		.replace("ك", "ک")
		.replace("دِ", "د")
		.replace("بِ", "ب")
		.replace("زِ", "ز")
		.replace("زِ", "ز")
		.replace("ِشِ", "ش")
		.replace("ِسِ", "س")
		.replace("ى", "ی")
}

fun String.toEnglish(): String {
	val chars = CharArray(length)
	for (i in indices) {
		var ch: Char = get(i)
		if (ch.code in 0x0660..0x0669) {
			ch -= 0x0660.toChar() - '0'.code.toChar()
		} else if (ch.code in 0x06f0..0x06F9) {
			ch -= 0x06f0.toChar() - '0'.code.toChar()
		}
		chars[i] = ch
	}
	return String(chars)
}

fun String.toPersian(): String {
	val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
	val chars = CharArray(length)
	for (i in indices) {
		val digit: Char = get(i)
		if (Character.isDigit(digit)) {
			val digitValue = Character.getNumericValue(digit)
			chars[i] = persianDigits[digitValue]
		} else {
			chars[i] = digit
		}
	}

	return String(chars)
}