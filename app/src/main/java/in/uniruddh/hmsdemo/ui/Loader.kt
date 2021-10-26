package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.R
import `in`.uniruddh.hmsdemo.databinding.DialogLoaderBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  26/October/2021
 * @Email: uniruddh@tetravx.com
 */
class Loader(context: Context) : AppCompatDialog(context) {

    private lateinit var binding: DialogLoaderBinding

    init {
        window?.setBackgroundDrawableResource(R.color.transparent)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.dialog_loader, null, false
        )
        setContentView(binding.root)

        setCancelable(false)
    }

    fun show(text: String) {
        binding.title.text = text
        binding.title.visibility = View.VISIBLE
        show()
    }

    override fun cancel() {
        binding.title.visibility = View.GONE
        super.cancel()
    }
}