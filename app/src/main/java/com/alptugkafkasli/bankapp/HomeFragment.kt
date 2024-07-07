package com.alptugkafkasli.bankapp

import BranchAdapter
import HomeViewModel
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alptugkafkasli.bankapp.databinding.ActivityMainBinding
import com.alptugkafkasli.bankapp.databinding.FragmentHomeBinding
import okhttp3.internal.assertThreadDoesntHoldLock

class HomeFragment : Fragment(), BranchClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: BranchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  // Menü seçeneklerinin olduğunu belirtir
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val toolbar: Toolbar = binding.toolbar
        toolbar.title = "BankApp"
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        setObserver()
        if (isInternetAvailable(requireContext())) {
            homeViewModel.getBranchs()
        } else {
            Toast.makeText(context, "Please turn on the internet", Toast.LENGTH_LONG).show()
        }

        // SearchView'ı başlat
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObserver() {
        homeViewModel.branchList.observe(viewLifecycleOwner, { branchList ->
            if (branchList != null) {
                adapter = BranchAdapter(requireContext(), branchList, this)
                binding.rvBranch.adapter = adapter
                binding.loadingLayout.visibility = View.GONE
            } else {
                // Boş branchList durumunu uygun şekilde ele al
                Toast.makeText(context, "No branches found", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClicked(item: BranchItem) {
        val bundle = Bundle().apply {
            putParcelable("selectedBranch", item)
        }
        findNavController().navigate(R.id.action_homeFragment_to_branchDetailsFragment, bundle)
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }
}
