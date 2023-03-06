package jp.ac.neec.it.k020c1302.schedule

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog

class ScheduleAllClearDialogFragment : DialogFragment(){

    private lateinit var callback: DataPassListener
    //フラグメントから受け取るためにリスナーをセットする
    fun setDataPassListener(listener: DataPassListener) {
        callback = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //アクティビティがnull出ないならばダイアログオブジェクトを生成
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            //ダイアログの設定
            builder.setTitle(R.string.dialog_title)
            builder.setMessage(R.string.dialog_msg)
            builder.setPositiveButton(R.string.dialog_btn_yes, DialogButtonClickListener())
            builder.setNegativeButton(R.string.dialog_btn_no, DialogButtonClickListener())
            //ダイアログオブジェクト生成
            builder.create()
        }
        return dialog ?: throw IllegalStateException("アクティビティがnull")
    }

    private inner class DialogButtonClickListener: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface, button: Int) {
            //トーストで表示するメッセージ格納
            var msg = ""
            when(button){
                DialogInterface.BUTTON_POSITIVE ->{
                    msg = "削除しました"
                    //スケジュール編集フラグメントに削除するためのtrueを送る
                    callback.onDataPass(true)
                }
                DialogInterface.BUTTON_NEGATIVE ->{
                    msg = "削除しませんでした"
                    //スケジュール編集フラグメントに削除するためのtrueを送る
                    callback.onDataPass(false)
                }
            }
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
        }
    }
}