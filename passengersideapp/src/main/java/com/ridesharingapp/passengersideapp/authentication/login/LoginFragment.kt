package com.ridesharingapp.passengersideapp.authentication.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.ridesharingapp.common.uicommon.handleToast
import com.zhuinden.simplestackextensions.fragmentsktx.lookup

class LoginFragment : Fragment() {
    private val viewModel by lazy { lookup<LoginViewModel>()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("LoginFragment", "onCreateView")
        viewModel.toastHandler = {
            handleToast(it)
        }

        return ComposeView(requireContext()).apply {
            // Dispose the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoginScreen(viewModel)
            }
        }
    }
}