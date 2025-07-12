package com.nullberg.modemtalk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nullberg.modemtalk.databinding.FragmentModemtalkBinding
import android.app.AlertDialog
import com.nullberg.modemtalk.Utls

import android.content.Context
import android.content.Intent
import android.widget.Toast

class ModemTalkFragment : Fragment() {

    private var _binding: FragmentModemtalkBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModemtalkBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connect the button click
        binding.button1.setOnClickListener {
            onClickButton1(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickButton1(view: View) {

//        Utls.simpleAlertDialog(requireContext())

        openEngineerMode(requireContext())

    }

    private fun openEngineerMode(context: Context) {

        Utls.simpleAlertDialog(context)

//        val intent = Intent()
//        intent.setClassName(
//            "com.mediatek.engineermode",
//            "com.mediatek.engineermode.bandmode.BandMode"
//        )
//        startActivity(intent)



//        val intent = Intent()
//        intent.setClassName(
//            "com.mediatek.engineermode",
//            "com.mediatek.engineermode.EngineerMode"
//        )
//        try {
//            startActivity(intent)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(context, "Cannot open EngineerMode", Toast.LENGTH_SHORT).show()
//        }


//        try {
//            val intent = Intent()
//            intent.setClassName(
//                "com.mediatek.engineermode",
//                "com.mediatek.engineermode.EngineerMode"
//            )
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Toast.makeText(context, "EngineerMode not found", Toast.LENGTH_SHORT).show()
//            e.printStackTrace()
//        }


    }


}
