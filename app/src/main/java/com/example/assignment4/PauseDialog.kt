package com.example.assignment4

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.assignment4.databinding.DialogPauseBinding

class PauseDialog : DialogFragment(){
    private lateinit var binding: DialogPauseBinding
    private var mListener: OnSelectListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPauseBinding.inflate(inflater, container, false)

        //재개버튼
        binding.pauseStartLo.setOnClickListener {
            mListener?.onSelectSet("start")
            this.dismiss()
        }
        //재시작버튼
        binding.pauseRestartLo.setOnClickListener {
            mListener?.onSelectSet("restart")
            this.dismiss()
        }
        //종료버튼
        binding.pauseReturnLo.setOnClickListener {
            mListener?.onSelectSet("return")
            this.dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    interface OnSelectListener {
        fun onSelectSet(type: String)
    }

    public fun setOnSelectListener(listener: OnSelectListener) {
        mListener = listener
    }

    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener(object: DialogInterface.OnKeyListener{
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.d("PauseDialog", "back_key")
                    return true
                }
                return false
            }
        })
    }
}