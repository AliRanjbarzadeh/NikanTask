package ir.aliranjbarzadeh.nikantask.core

import android.os.CountDownTimer
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder<T>(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
	abstract fun onBindUI(item: T)
}