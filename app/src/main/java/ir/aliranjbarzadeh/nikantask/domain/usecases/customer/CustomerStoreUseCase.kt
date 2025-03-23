package ir.aliranjbarzadeh.nikantask.domain.usecases.customer

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerStoreUseCase @Inject constructor(private val repository: CustomerRepository) {
	operator fun invoke(customer: Customer): Flow<ResponseResult<Customer>> = repository.store(customer)
}