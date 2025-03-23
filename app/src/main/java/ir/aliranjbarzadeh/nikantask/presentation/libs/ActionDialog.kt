package ir.aliranjbarzadeh.nikantask.presentation.libs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.aliranjbarzadeh.nikantask.databinding.ActionsTemplateBinding
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnActionClick

class ActionDialog<T>(context: Context, private val item: T, private val position: Int, private val onActionClick: OnActionClick<T>) : BottomSheetDialog(context) {

	private lateinit var binding: ActionsTemplateBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActionsTemplateBinding.inflate(LayoutInflater.from(context), null, false)
		setContentView(binding.root)

		setOnShowListener {
			val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
			bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
		}

		binding.root.post {
			binding.btnEdit.setOnClickListener {
				onActionClick.onEdit(item, position)
				dismiss()
			}

			binding.btnDelete.setOnClickListener {
				onActionClick.onDelete(item, position)
				dismiss()
			}
		}
	}
}