package ir.aliranjbarzadeh.nikantask.core.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.reachEnd(crossinline onReachEnd: () -> Unit) {
	this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
		override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
			super.onScrolled(recyclerView, dx, dy)

			if (dy > 0) {
				layoutManager?.also {
					val visibleItemCount = it.childCount
					val totalItemCount = it.itemCount
					val firstVisibleItemPosition = (it as LinearLayoutManager).findFirstVisibleItemPosition()

					if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 5) {
						onReachEnd()
					}
				}
			}
		}
	})
}