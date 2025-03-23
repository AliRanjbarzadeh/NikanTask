package ir.aliranjbarzadeh.nikantask.core.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.EditText
import gun0912.tedkeyboardobserver.TedKeyboardObserver

object KeyboardHelper {
	fun init(activity: Activity, inputs: List<EditText>) {
		TedKeyboardObserver(activity)
			.listen { isShow ->
				if (!isShow)
					inputs.forEach { it.clearFocus() }
			}
	}

	fun hideKeyboard(view: View) {
		val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
		imm.hideSoftInputFromWindow(view.windowToken, 0)
	}
}