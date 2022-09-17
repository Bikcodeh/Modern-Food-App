package com.bikcodeh.modernfoodapp.presentation.screens.detail.pages.instructions

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.bikcodeh.modernfoodapp.databinding.FragmentInstructionsBinding
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.presentation.util.BaseFragmentBinding
import com.bikcodeh.modernfoodapp.util.extension.hide
import com.bikcodeh.modernfoodapp.util.extension.show

class InstructionsFragment :
    BaseFragmentBinding<FragmentInstructionsBinding>(FragmentInstructionsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipe: Recipe? = arguments?.getParcelable("recipeBundle")
        recipe?.let {
            setUpWebView(it)
        } ?: run {
            findNavController().popBackStack()
        }
    }

    private fun setUpWebView(recipe: Recipe) {
        with (binding) {
            instructionWv.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    instructionsPb.show()
                    instructionWv.hide()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    instructionsPb.hide()
                    instructionWv.show()
                }
            }
            instructionWv.loadUrl(recipe.sourceUrl)
        }
    }
}