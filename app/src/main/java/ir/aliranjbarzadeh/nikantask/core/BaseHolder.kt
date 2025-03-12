package ir.aliranjbarzadeh.nikantask.core

import android.os.CountDownTimer
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder<T>(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
	var timer: CountDownTimer? = null
	abstract fun onBindUI(item: T, position: Int)
}