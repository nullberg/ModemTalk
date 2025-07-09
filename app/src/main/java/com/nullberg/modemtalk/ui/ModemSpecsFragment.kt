package com.nullberg.modemtalk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nullberg.modemtalk.QuerySysProps
import com.nullberg.modemtalk.Utls
import com.nullberg.modemtalk.databinding.FragmentModemspecsBinding

class ModemSpecsFragment : Fragment() {

    private var _binding: FragmentModemspecsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModemspecsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connect the button click
        binding.buttonGetSysProps.setOnClickListener {
            onClickButtonGetSysProps(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun onClickButtonGetSysProps(view: View) {

        val qsp = QuerySysProps()
        val tableLayout = binding.tableSysProps
        val labels = qsp.syspropLbls.split("\n")
        val values = qsp.syspropVals.split("\n")

        Utls.populateTable(requireContext(), tableLayout, labels, values)

    }
}
