package ir.aliranjbarzadeh.nikantask.domain.usecases.customer

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerListUseCase @Inject constructor(private val repository: CustomerRepository) {
	operator fun invoke(): Flow<ResponseResult<List<Customer>>> = repository.list()
}