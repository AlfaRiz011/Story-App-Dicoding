package com.example.submission_intermediate.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission_intermediate.databinding.FragmentHomeBinding
import com.example.submission_intermediate.service.api.ApiConfig
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.ui.story.UploadActivity
import com.example.submission_intermediate.ui.user.UserViewModel
import com.example.submission_intermediate.ui.user.ViewModelFactory
import com.example.submission_intermediate.uitls.UserPreferences

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var token: String
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {

        binding.addStory.setOnClickListener{
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupView() {
        recyclerView = binding.rvStoryContainer
        homeAdapter = HomeAdapter(requireContext())
        recyclerView.adapter = homeAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            setupViewModel()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupViewModel() {
        val preferences = UserPreferences.getInstance(requireContext().dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getToken().observe(viewLifecycleOwner) {
            token = it
            val homeViewModel: HomeViewModel by viewModels {
                HomeViewModelFactory(
                    requireContext(),
                    ApiConfig.getApiService(),
                    token
                )
            }

            homeViewModel.story.observe(viewLifecycleOwner) {story ->
                homeAdapter.submitData(lifecycle, story)
            }
        }
    }


}
