package com.nullberg.modemtalk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nullberg.modemtalk.QueryTelephony
import com.nullberg.modemtalk.databinding.FragmentTelephonyBinding

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.nullberg.modemtalk.Utls

class TelephonyFragment : Fragment() {

    private var _binding: FragmentTelephonyBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTelephonyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connect the button click
        binding.buttonGetTelephony.setOnClickListener {
            onClickButtonGetTelephony(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun onClickButtonGetTelephony(view: View) {

        if (
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            val qt = QueryTelephony(requireContext())
            val tableLayout = binding.tableTelephony
            val labels = qt.labelStr.split("\n")
            val values = qt.valueStr.split("\n")
            Utls.populateTable(requireContext(), tableLayout, labels, values)

        } else {
            // ❌ Permission not granted → show dialog
            showPermissionDialog()
        }

    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("This app needs phone permission to read telephony info. Please grant it in settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

//AlertDialog.Builder(requireContext())
//.setTitle("Test Alert")
//.setMessage("Great, the permission is ok...")
//.setPositiveButton("OK", null)
//.setNegativeButton("Cancel", null)
//.show()