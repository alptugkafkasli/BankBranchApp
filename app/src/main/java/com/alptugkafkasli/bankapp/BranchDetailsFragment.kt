package com.alptugkafkasli.bankapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alptugkafkasli.bankapp.databinding.FragmentBranchDetailsBinding
import com.google.firebase.analytics.FirebaseAnalytics


class BranchDetailsFragment : Fragment() {

    private var _binding: FragmentBranchDetailsBinding? = null
    private val binding get() = _binding!!
    private var selectedBranch: BranchItem? = null
    private var firebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedBranch = it.getParcelable("selectedBranch")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBranchDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize FirebaseAnalytics instance
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        // Log event when branch details screen is viewed
        logBranchDetailsScreenView()

        selectedBranch?.let { branch ->
            binding.SEHIRB.text=branch.dc_SEHIR
            binding.BANKASUBEB.text = branch.dc_BANKA_SUBE
            binding.BANKATIPIB.text = branch.dc_BANKA_TIPI
            binding.BANKKODUB.text = branch.dc_BANK_KODU
            binding.ADRESADIB.text = branch.dc_ADRES_ADI
            binding.ADRESB.text = branch.dc_ADRES
            binding.POSTAKODUB.text = branch.dc_POSTA_KODU
            binding.ONOFFLINEB.text = branch.dc_ON_OFF_LINE
            binding.ONOFFSITEB.text = branch.dc_ON_OFF_SITE
            binding.BOLGEKOORDINATORLUGUB.text = branch.dc_BOLGE_KOORDINATORLUGU
            binding.ENYAKIMATMB.text = branch.dc_EN_YAKIM_ATM
        }
        binding.btnShowInMap.setOnClickListener {
            selectedBranch?.let { branch ->
                val address = branch.dc_ADRES
                val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
    }
}
    private fun logBranchDetailsScreenView() {
        firebaseAnalytics?.logEvent("branch_details_viewed", null)
    }

}
