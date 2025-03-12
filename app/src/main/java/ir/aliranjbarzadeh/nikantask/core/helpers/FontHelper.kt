package ir.aliranjbarzadeh.nikantask.core.helpers

import android.content.Context
import android.graphics.Typeface
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.calligraphy3.R
import io.github.inflationx.calligraphy3.TypefaceUtils
import io.github.inflationx.viewpump.ViewPump
import java.io.InputStream

object FontHelper {
	const val REGULAR = "regular.ttf"
	const val LIGHT = "light.ttf"
	const val BOLD = "bold.ttf"

	fun setFont(context: Context, fontWeight: String = REGULAR) {
		ViewPump.init(
			ViewPump.builder()
				.addInterceptor(
					CalligraphyInterceptor(
						CalligraphyConfig.Builder()
							.setDefaultFontPath(getFontPath(context, fontWeight))
							.setFontAttrId(R.attr.fontPath)
							.build()
					)
				)
				.build()
		)
	}

	fun getTypeFace(context: Context, fontWeight: String = REGULAR): Typeface? {
		return try {
			TypefaceUtils.load(context.assets, getFontPath(context, fontWeight))
		} catch (_: Exception) {
			null
		}
	}

	private fun getFontPath(context: Context, fontWeight: String): String {
		val language = LanguageHelper.getLanguage()
		var fontPath = "fonts/$fontWeight"

		if (!LanguageHelper.isDefault(language)) {
			val assetManager = context.resources.assets
			var inputStream: InputStream? = null
			try {
				val mPath = "fonts/$language/$fontWeight"
				inputStream = assetManager.open(mPath)
				fontPath = mPath
			} catch (_: Exception) {
			} finally {
				inputStream?.close()
			}
		}
		return fontPath
	}
}