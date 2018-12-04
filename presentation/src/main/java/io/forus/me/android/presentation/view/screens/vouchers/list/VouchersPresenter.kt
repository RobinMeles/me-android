package io.forus.me.android.presentation.view.screens.vouchers.list

import io.forus.me.android.presentation.view.base.lr.LRPresenter
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.PartialChange
import io.forus.me.android.presentation.models.vouchers.Voucher
import io.forus.me.android.domain.repository.vouchers.VouchersRepository
import io.forus.me.android.presentation.R.id.amount
import io.forus.me.android.presentation.R.id.description
import io.forus.me.android.presentation.R.id.name
import io.forus.me.android.presentation.models.currency.Currency
import io.forus.me.android.presentation.models.vouchers.Organization
import io.forus.me.android.presentation.models.vouchers.Product
import io.forus.me.android.presentation.models.vouchers.ProductCategory
import io.forus.me.android.presentation.models.vouchers.Transaction
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers


class VouchersPresenter constructor(val vouchersRepository: VouchersRepository) : LRPresenter<List<Voucher>, VouchersModel, VouchersView>() {


    override fun initialModelSingle(): Single<List<Voucher>> = Single.fromObservable(vouchersRepository.getVouchers().map { domainVouchers ->
        domainVouchers.map { domainVoucher ->
            with(domainVoucher) {

                val transactionsMapped = transactions.map {
                    val organization = Organization(it.organization.id,
                            it.organization.name, it.organization.logo, it.organization.lat, it.organization.lon, it.organization.address, it.organization.phone, it.organization.email)

                    Transaction(it.id, organization,
                            Currency(it.currency.name, it.currency.logoUrl),
                            it.amount, createdAt, Transaction.Type.valueOf(it.type.name))
                }

                val domainProduct = domainVoucher.product
                val product = when (domainProduct) {
                    null -> null
                    else -> Product(domainProduct.id, domainProduct.organizationId, domainProduct.productCategoryId, domainProduct.name, domainProduct.description, domainProduct.price, domainProduct.oldPrice, domainProduct.totalAmount, domainProduct.soldAmount, ProductCategory(domainProduct.productCategory.id, domainProduct.productCategory.key, domainProduct.productCategory.name), Organization(domainProduct.organization.id,
                            domainProduct.organization.name, domainProduct.organization.logo, domainProduct.organization.lat, domainProduct.organization.lon, domainProduct.organization.address, domainProduct.organization.phone, domainProduct.organization.email))
                }

                Voucher(isProduct, isUsed, address, name, organizationName,
                        fundName, description, createdAt, Currency(currency.name, currency.logoUrl), amount, logo,
                        transactionsMapped, product
                        )
            }
        }
    })

    override fun VouchersModel.changeInitialModel(i: List<Voucher>): VouchersModel {
        val vouchers: MutableList<Voucher> = mutableListOf()
        vouchers.addAll(i.filter { voucher -> !(voucher.isProduct && voucher.isUsed) })
        //vouchers.addAll(i.filter { voucher ->  (voucher.isProduct && voucher.isUsed)})
        return copy(items = vouchers)
    }


    override fun bindIntents() {
        val observable = loadRefreshPartialChanges()


        val initialViewState = LRViewState(
                false,
                null,
                false,
                false,
                null,
                false,
                VouchersModel())

        subscribeViewState(
                observable.scan(initialViewState, this::stateReducer)
                        .observeOn(AndroidSchedulers.mainThread()),
                VouchersView::render)

    }

    override fun stateReducer(viewState: LRViewState<VouchersModel>, change: PartialChange): LRViewState<VouchersModel> {

        if (change !is VouchersPartialChanges) return super.stateReducer(viewState, change)

        return when (change) {

            else -> {
                super.stateReducer(viewState, change)
            }
        }

    }


}