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
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.io.File

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

        binding.buttonCheckEngMode.setOnClickListener {
            onClickButtonCheckEngMode(it)
        }

        // Connect the button click
        binding.buttonOpenEngMode.setOnClickListener {
            onClickButtonOpenEngMode(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickButtonCheckEngMode(view: View) {

        val engModePath = "/system/priv-app/EngineerMode/EngineerMode.apk";

        val fileEngMode = File(engModePath)

        // I DID THIS:  $ adb shell pm list packages | grep -i engineer
        // It returned com.mediatek.engineermode !!

        if (fileEngMode.exists()) {
            Utls.custAlertDialog(requireContext(), "YES!!\n$engModePath\nexists!!");
        } else {
            Utls.custAlertDialog(requireContext(), "Did not find ENG MODE!!")
        }

    }

    private fun onClickButtonOpenEngMode(view: View) {

//        Utls.simpleAlertDialog(requireContext())

        openEngineerMode(requireContext())

    }

    private fun openEngineerMode(context: Context) {


        // THIS WORKS!!
        // Equiv to su -c "am start -n com.mediatek.engineermode/.EngineerMode"
        // -c flag is for command
        Runtime.getRuntime().exec(arrayOf(
            "su", "-c",
            "am start -n com.mediatek.engineermode/.EngineerMode"
        ))

//        Runtime.getRuntime().exec(arrayOf(
//            "su", "-c",
//            "am start -n com.mediatek.engineermode/.bandselect.BandSelect"
//        ))

        // Also can do su -c am start -a android.intent.action.DIAL -d tel:*#*#3646633#*#*"
        // but

//        val intent = context.packageManager.getLaunchIntentForPackage("com.android.settings")
//        val intent = context.packageManager.getLaunchIntentForPackage("com.mediatek.engineermode")
//        Works but it doesn't paste the dialer code.
//        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:*#*#3646633#*#*"))
//        startActivity(intent)


//
//        try {
//            startActivity(intent)
//        } catch(e : Exception) {
//
//            Utls.custAlertDialog(context, "${e.message}");
//            Log.e("EngineerMode", "Launch failed", e)
//        }


//        val intent = Intent()
//        intent.setClassName(
//            "com.mediatek.engineermode",
//            "com.mediatek.engineermode.bandmode.BandMode"
//        )
//        startActivity(intent)


    }


}
