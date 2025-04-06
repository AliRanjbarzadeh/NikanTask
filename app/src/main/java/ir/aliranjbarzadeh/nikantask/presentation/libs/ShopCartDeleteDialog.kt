package ir.aliranjbarzadeh.nikantask.presentation.libs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.aliranjbarzadeh.nikantask.databinding.CartDeleteTemplateBinding

class ShopCartDeleteDialog(
	context: Context,
	private val onDeleteCallBack: () -> Unit,
) : BottomSheetDialog(context) {

	private lateinit var binding: CartDeleteTemplateBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = CartDeleteTemplateBinding.inflate(LayoutInflater.from(context), null, false)
		setContentView(binding.root)

		setOnShowListener {
			val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
			bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
		}

		binding.root.post {
			binding.btnDelete.setOnClickListener {
				onDeleteCallBack()
				dismiss()
			}

			binding.btnClose.setOnClickListener { dismiss() }
		}
	}
}