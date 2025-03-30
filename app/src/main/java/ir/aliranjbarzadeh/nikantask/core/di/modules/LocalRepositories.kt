package ir.aliranjbarzadeh.nikantask.core.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.aliranjbarzadeh.nikantask.data.repositories.CustomerRepositoryImpl
import ir.aliranjbarzadeh.nikantask.data.repositories.OrderRepositoryImpl
import ir.aliranjbarzadeh.nikantask.data.repositories.ProductRepositoryImpl
import ir.aliranjbarzadeh.nikantask.data.repositories.SeedRepositoryImpl
import ir.aliranjbarzadeh.nikantask.data.repositories.local.CustomerDataSource
import ir.aliranjbarzadeh.nikantask.data.repositories.local.OrderDataSource
import ir.aliranjbarzadeh.nikantask.data.repositories.local.ProductDataSource
import ir.aliranjbarzadeh.nikantask.data.repositories.local.SeedDataSource
import ir.aliranjbarzadeh.nikantask.domain.repositories.CustomerRepository
import ir.aliranjbarzadeh.nikantask.domain.repositories.OrderRepository
import ir.aliranjbarzadeh.nikantask.domain.repositories.ProductRepository
import ir.aliranjbarzadeh.nikantask.domain.repositories.SeedRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalRepositories {
	@Provides
	@Singleton
	fun providesCustomerRepository(dataSource: CustomerDataSource): CustomerRepository = CustomerRepositoryImpl(dataSource)

	@Provides
	@Singleton
	fun providesProductRepository(dataSource: ProductDataSource): ProductRepository = ProductRepositoryImpl(dataSource)

	@Provides
	@Singleton
	fun providesOrderRepository(dataSource: OrderDataSource): OrderRepository = OrderRepositoryImpl(dataSource)

	@Provides
	@Singleton
	fun providesSeedRepository(dataSource: SeedDataSource): SeedRepository = SeedRepositoryImpl(dataSource)
}