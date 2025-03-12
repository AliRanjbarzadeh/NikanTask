package ir.aliranjbarzadeh.nikantask.core.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.aliranjbarzadeh.nikantask.data.repositories.CustomerRepositoryImpl
import ir.aliranjbarzadeh.nikantask.data.repositories.local.CustomerDataSource
import ir.aliranjbarzadeh.nikantask.domain.repositories.CustomerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalRepositories {
	@Provides
	@Singleton
	fun providesCustomerRepository(dataSource: CustomerDataSource): CustomerRepository = CustomerRepositoryImpl(dataSource)
}