package ir.aliranjbarzadeh.nikantask.domain

enum class Loading(val value: Int) {
	Normal(0),
	LoadMore(1),
	Loaded(2);

	companion object {
		fun fromValue(value: Int?): Loading {
			return Loading.entries.find { it.value == value } ?: Normal
		}
	}
}