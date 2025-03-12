package ir.aliranjbarzadeh.nikantask.data.interfaces

interface ResponseObject<out DomainObject : Any?> {
	fun toDomain(): DomainObject
}