package com.nullberg.modemtalk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nullberg.modemtalk.databinding.FragmentModemtalkBinding
import com.nullberg.modemtalk.Utls

import android.content.Context
import android.content.Intent
import android.util.Log
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

        binding.buttonOpenSimpleUI.setOnClickListener {
            onClickButtonOpenSimpleUI(it)
        }

        binding.buttonDMESG.setOnClickListener {
            onClickButtonDMESG(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickButtonCheckEngMode(view: View) {

        val engModePath = "/system/priv-app/EngineerMode/EngineerMode.apk"

        val fileEngMode = File(engModePath)

        // I DID THIS:  $ adb shell pm list packages | grep -i engineer
        // It returned com.mediatek.engineermode !!

        if (fileEngMode.exists()) {
            Utls.custAlertDialog(requireContext(), "YES!!\n$engModePath\nexists!!")
        } else {
            Utls.custAlertDialog(requireContext(), "Did not find ENG MODE!!")
        }

    }

    private fun onClickButtonOpenEngMode(view: View) {

//        Utls.simpleAlertDialog(requireContext())

        openEngineerMode(requireContext())

    }

    private fun onClickButtonOpenSimpleUI(view: View) {

//        Utls.custAlertDialog(requireContext(), "Open simple UI...")

        try {

//          This WORKS
//          Runtime.getRuntime().exec( arrayOf(  "su", "-c", "am start -n com.nullberg.simplui01/.MainActivity" ))

//            val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(
//                "com.nullberg.simplui01")
//            if (launchIntent != null) {
//                startActivity(launchIntent)
//            } else {
//                Utls.custAlertDialog(requireContext(), "launchIntent is null!")
//            }

            val intent = Intent()
            intent.setClassName(
                "com.nullberg.simplui01",
                "com.nullberg.simplui01.MainActivity"
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)   // important if not called from Activity
            requireContext().startActivity(intent)



        } catch(e : Exception) {
            Utls.custAlertDialog(requireContext(), "${e.message}")
            Log.e("Launch SimpleUI01", "Launch failed", e)
        }

//        val launchIntent = requireContext().packageManager.getLaunchIntentForPackage("com.nullberg.simplui01")
//        if (launchIntent != null) {
//            startActivity(launchIntent)
//        } else {
//            Utls.custAlertDialog(requireContext(), "com.nullberg.simplui01 failed to launch")
//        }

    }


    private fun onClickButtonDMESG(v: View) = Thread {
        try {
            val cmd = arrayOf("su", "-c", "logcat -b radio -d | grep -i 'lte\\|band\\|rsrp\\|rssi'")
            val process = Runtime.getRuntime().exec(cmd)
            val out = process.inputStream.bufferedReader().readLines()
                .takeLast(20) // show latest 20 lines
                .joinToString("\n")

            v.post { binding.textViewDmesg.text = out.ifEmpty { "No LTE/band info found." } }
        } catch (e: Exception) {
            v.post { binding.textViewDmesg.text = "Error: ${e.message}" }
        }
    }.start()
/*

    private fun onClickButtonDMESG(v: View) = Thread {
        try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "dmesg"))
            val out = process.inputStream.bufferedReader().readLines()
                .takeLast(10) // keep only last 10 lines
                .joinToString("\n")

            v.post { binding.textViewDmesg.text = out }
        } catch (e: Exception) {
            v.post { binding.textViewDmesg.text = "Error: ${e.message}" }
        }
    }.start()


 */


/*
    private fun onClickButtonDMESG(context: View) {

//      Utls.custAlertDialog(requireContext(),"works")

//      binding.textViewDmesg.text = "test dmesg"

        Thread {
            try {
                // Run 'dmesg' as root
                val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "dmesg"))
                val reader = process.inputStream.bufferedReader()
                val outpt = reader.readText()
                reader.close()

                // Update the TextView on the main thread
                binding.textViewDmesg.text = outpt

            } catch (e: Exception) {
                e.printStackTrace()
                binding.textViewDmesg.text = "Error: ${e.message}"
            }
        }.start()

    }
*/
    private fun openEngineerMode(context: Context) {


        // THIS WORKS!!
        // Equiv to su -c "am start -n com.mediatek.engineermode/.EngineerMode"
        // -c flag is for command

        try {
            Runtime.getRuntime().exec(
                arrayOf(
                    "su", "-c",
                    "am start -n com.mediatek.engineermode/.EngineerMode"
                )
            )
        } catch (e : Exception) {
            Utls.custAlertDialog(requireContext(), "${e.message}");
            Log.e("Launch EngineerMode fail", "Launch failed", e)
        }

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
