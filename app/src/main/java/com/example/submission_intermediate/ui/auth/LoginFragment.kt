package com.example.submission_intermediate.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.submission_intermediate.R
import com.example.submission_intermediate.databinding.FragmentLoginBinding
import com.example.submission_intermediate.service.response.LoginData
import com.example.submission_intermediate.ui.user.UserViewModel
import com.example.submission_intermediate.ui.user.ViewModelFactory
import com.example.submission_intermediate.uitls.CustomViewHelper
import com.example.submission_intermediate.uitls.UserPreferences

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupCustomView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        viewModel.loginData.observe(this) { loginData ->
            loginData?.let {
                binding.emailEd.setText(it.email)
                binding.passEd.setText(it.password)
            }
        }
    }


    private fun setupAction() {
        binding.loginBtn.setOnClickListener {
            val loginData = LoginData(
                email = binding.emailEd.text.toString().trim(),
                password = binding.passEd.text.toString().trim()
            )
            viewModel.getLoginResponse(loginData)
        }

        binding.registTv2.setOnClickListener{
            parentFragmentManager.commit {
                replace(R.id.container, RegisterFragment())
                addToBackStack(null)
            }
        }
    }

    private fun setupViewModel() {

        val preferences = UserPreferences.getInstance(requireContext().dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        viewModel.messageLogin.observe(this){message ->
            loginRes(
                viewModel.isLogin,
                message,
                userViewModel
            )
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun loginRes(login: Boolean, message: String, userViewModel: UserViewModel) {
        if (login){
            val user = viewModel.loginResult.value?.loginResult
            userViewModel.saveLoginSession(true)
            userViewModel.saveToken(user!!.token)
            userViewModel.saveName(user.name)

        } else {
            showToast("Gagal Login")
            showToast(message)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_LONG).show()
    }

    private fun setupCustomView() {
        CustomViewHelper.loginHelper(binding.emailEd, binding.passEd, binding.loginBtn)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val welcomeTitle = ObjectAnimator.ofFloat(binding.welcomeText, View.ALPHA, 1f).setDuration(200)
        val welcomeDesc = ObjectAnimator.ofFloat(binding.welcomeText2, View.ALPHA, 1f).setDuration(200)
        val loginLabel = ObjectAnimator.ofFloat(binding.loginLabel, View.ALPHA, 1f).setDuration(200)
        val emailEd = ObjectAnimator.ofFloat(binding.emailEd, View.ALPHA, 1f).setDuration(200)
        val passEd = ObjectAnimator.ofFloat(binding.passEd, View.ALPHA, 1f).setDuration(200)
        val loginBtn = ObjectAnimator.ofFloat(binding.loginBtn, View.ALPHA, 1f).setDuration(200)
        val regisText = ObjectAnimator.ofFloat(binding.registTv1, View.ALPHA, 1f).setDuration(200)
        val regisText2 = ObjectAnimator.ofFloat(binding.registTv2, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                welcomeTitle,
                welcomeDesc,
                loginLabel,
                emailEd,
                passEd,
                loginBtn,
                regisText,
                regisText2
            )
            start()
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}