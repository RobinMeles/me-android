package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.presentation.R


class VouchersApplySuccessDialog(private val context: Context,
                                 private val positiveCallback: () -> Unit){

    private val dialog: MaterialDialog = MaterialDialog.Builder(context)
            .title(context.resources.getString(R.string.success))
            .content(context.resources.getString(R.string.vouchers_apply_success))
            .positiveText(context.resources.getString(R.string.me_ok))
            .onPositive { dialog, which -> positiveCallback.invoke() }
            .build()

    fun show(){
        dialog.show()
    }
}