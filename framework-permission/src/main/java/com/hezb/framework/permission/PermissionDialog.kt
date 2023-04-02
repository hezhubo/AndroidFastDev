package com.hezb.framework.permission

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes

/**
 * Project Name: AndroidFastDev
 * File Name:    PermissionDialog
 *
 * Description: 权限申请提示弹窗.
 *
 * @author  hezhubo
 * @date    2023年03月29日 15:06
 */
class PermissionDialog(
    context: Context,
    private val content: String? = null,
    @LayoutRes private val layoutId: Int = R.layout.framework_permission_dialog
) : Dialog(context) {

    private var mTitleTv: TextView? = null
    private var mCancelBtn: Button? = null
    private var mConfirmBtn: Button? = null

    private var dialogCallback: DialogCallback? = null

    private var title: String? = null
    private var cancelText: String? = null
    private var confirmText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        mTitleTv = findViewById(R.id.tv_title)
        mCancelBtn = findViewById(R.id.btn_cancel)
        mConfirmBtn = findViewById(R.id.btn_confirm)
        findViewById<TextView>(R.id.tv_content)?.text = content

        mCancelBtn?.setOnClickListener {
            dialogCallback?.onCancel()
            dismiss()
        }

        mConfirmBtn?.setOnClickListener {
            dialogCallback?.onConfirm()
            dismiss()
        }

        title?.let {
            mTitleTv?.text = it
        }
        cancelText?.let {
            mCancelBtn?.text = it
        }
        confirmText?.let {
            mConfirmBtn?.text = it
        }
    }

    override fun dismiss() {
        super.dismiss()
        dialogCallback?.onDismiss()
    }

    fun setDialogCallback(callback: DialogCallback) {
        dialogCallback = callback
    }

    fun setTitle(title: String) {
        this.title = title
        mTitleTv?.text = title
    }

    fun setCancelText(cancelText: String) {
        this.cancelText = cancelText
        mCancelBtn?.text = cancelText
    }

    fun setConfirmText(confirmText: String) {
        this.confirmText = confirmText
        mConfirmBtn?.text = confirmText
    }

    interface DialogCallback {
        fun onCancel()

        fun onConfirm()

        fun onDismiss()
    }

}