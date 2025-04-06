package ir.aliranjbarzadeh.nikantask.presentation.libs

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.databinding.CartQuantityTemplateBinding

class ShopCartDialog(
	context: Context,
	private val initialQuantity: Int = 0,
	private val onSaveCallBack: (quantity: Int) -> Unit,
) : BottomSheetDialog(context) {

	private lateinit var binding: CartQuantityTemplateBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = CartQuantityTemplateBinding.inflate(LayoutInflater.from(context), null, false)
		setContentView(binding.root)

		setOnShowListener {
			val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
			bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
		}

		binding.root.post {
			binding.btnIncrease.setOnClickListener { changeQuantity(true) }

			binding.btnDecrease.setOnClickListener { changeQuantity() }

			binding.btnSave.setOnClickListener {
				val quantity = binding.etQuantity.text.toString().toIntOrNull() ?: 0

				if (quantity >= 0) {
					onSaveCallBack(quantity)
					dismiss()
				} else {
					Toast.makeText(context, context.getString(R.string.quantity_error), Toast.LENGTH_SHORT).show()
				}
			}

			//Set initialized quantity (use for edit)
			binding.etQuantity.setText(initialQuantity.toString())
		}
	}

	private fun changeQuantity(isIncrease: Boolean = false) {
		var quantity = binding.etQuantity.text.toString().toIntOrNull() ?: 0

		if (isIncrease) quantity += 1
		else if (quantity > 1) quantity -= 1

		binding.etQuantity.setText(quantity.toString())
	}
}