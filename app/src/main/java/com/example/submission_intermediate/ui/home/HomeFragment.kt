package com.example.submission_intermediate.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission_intermediate.databinding.FragmentHomeBinding
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.ui.story.UploadActivity
import com.example.submission_intermediate.ui.user.UserViewModel
import com.example.submission_intermediate.ui.user.ViewModelFactory
import com.example.submission_intermediate.uitls.UserPreferences

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
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

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = homeAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { homeAdapter.retry() }
            )
        }

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

        userViewModel.getToken().observe(viewLifecycleOwner) { token ->
            Log.d("HomeFragment", "Token: $token")

            val mainViewModel: HomeViewModel by viewModels {
                MainViewModelFactory(requireActivity().application, "Bearer $token")
            }

            mainViewModel.story.observe(viewLifecycleOwner) { stories ->
                Log.d("HomeFragment", "Received $stories stories")
                homeAdapter.submitData(lifecycle, stories)
            }

        }
    }
}
