package com.example.submission_intermediate.ui.user

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.submission_intermediate.R
import com.example.submission_intermediate.databinding.FragmentUserBinding
import com.example.submission_intermediate.ui.auth.AuthActivity
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.uitls.UserPreferences

class UserFragment : Fragment() {

    private lateinit var binding : FragmentUserBinding
    private lateinit var userId: String
    private lateinit var username: String
    private lateinit var userViewModel: UserViewModel
    private lateinit var preferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = UserPreferences.getInstance(requireContext().dataStore)
        userViewModel = ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userId = ""
        username = ""

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        with(binding){
            btnLanguage.setOnClickListener { changeLanguage() }
            logout.setOnClickListener { logout() }
        }
    }

    private fun logout() {
        userViewModel.clearDataLogin()
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun changeLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun setupViewModel() {
        userViewModel.getUid().observe(viewLifecycleOwner, Observer {
            userId = it
            updateView()
        })

        userViewModel.getName().observe(viewLifecycleOwner, Observer {
            username = it
            updateView()
        })
    }

    private fun setupView() {
        with(binding){
            uid.text = userId
            name.text = username
        }
    }

    private fun updateView() {
        with(binding){
            uid.text = userId
            name.text = username
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.getUid().removeObservers(viewLifecycleOwner)
        userViewModel.getName().removeObservers(viewLifecycleOwner)
    }
}
