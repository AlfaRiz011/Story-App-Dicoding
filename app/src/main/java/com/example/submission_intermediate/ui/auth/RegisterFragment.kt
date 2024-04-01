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
import com.example.submission_intermediate.R
import com.example.submission_intermediate.databinding.FragmentResgisterBinding
import com.example.submission_intermediate.service.response.LoginData
import com.example.submission_intermediate.service.response.RegisterData
import com.example.submission_intermediate.uitls.CustomViewHelper

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentResgisterBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResgisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCustomView()
        setupAction()
        setupViewModel()
        playAnimation()
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.messageRegister.observe(this){message ->
            registRes(
                viewModel.isRegistered,
                message
            )
        }
    }

    private fun registRes(registered: Boolean, message: String) {
        if(registered){
            showToast("Berhasil Register")
            goToLogin()
        } else {
            showToast("Gagal Register")
            showToast(message)
        }
    }

    private fun goToLogin() {
        val email = binding.emailEd.text.toString().trim()
        val password = binding.passEd.text.toString().trim()

        viewModel.setLoginData(email, password)

        parentFragmentManager.commit {
            replace(R.id.container, LoginFragment())
            addToBackStack(null)
        }
    }

    private fun setupAction() {
        binding.registerBtn.setOnClickListener {
            val registerData = RegisterData(
                name = binding.nameEd.text.toString().trim(),
                email = binding.emailEd.text.toString().trim(),
                password = binding.passEd.text.toString().trim()
            )

            viewModel.getRegisterResponse(registerData)
        }

        binding.registTv2.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container, LoginFragment())
                addToBackStack(null)
            }
        }
    }


    private fun showToast(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_LONG).show()
    }

    private fun setupCustomView() {
        CustomViewHelper.registHelper(binding.nameEd,binding.emailEd, binding.passEd, binding.registerBtn)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val welcomeTitle = ObjectAnimator.ofFloat(binding.welcomeText, View.ALPHA, 1f).setDuration(200)
        val welcomeDesc = ObjectAnimator.ofFloat(binding.welcomeText2, View.ALPHA, 1f).setDuration(200)
        val registerLabel = ObjectAnimator.ofFloat(binding.registerLabel, View.ALPHA, 1f).setDuration(200)
        val nameEd = ObjectAnimator.ofFloat(binding.nameEd, View.ALPHA, 1f).setDuration(200)
        val emailEd = ObjectAnimator.ofFloat(binding.emailEd, View.ALPHA, 1f).setDuration(200)
        val passEd = ObjectAnimator.ofFloat(binding.passEd, View.ALPHA, 1f).setDuration(200)
        val registerBtn = ObjectAnimator.ofFloat(binding.registerBtn, View.ALPHA, 1f).setDuration(200)
        val loginText = ObjectAnimator.ofFloat(binding.registTv1, View.ALPHA, 1f).setDuration(200)
        val loginText2 = ObjectAnimator.ofFloat(binding.registTv2, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                welcomeTitle,
                welcomeDesc,
                registerLabel,
                nameEd,
                emailEd,
                passEd,
                registerBtn,
                loginText,
                loginText2
            )
            start()
        }
    }
}
