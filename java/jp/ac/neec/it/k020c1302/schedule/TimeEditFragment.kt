package jp.ac.neec.it.k020c1302.schedule

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import jp.ac.neec.it.k020c1302.schedule.databinding.FragmentTimeEditBinding

class TimeEditFragment : Fragment() {
    //View Bindingを使うために宣言
    private var _binding: FragmentTimeEditBinding? = null
    private val binding get() = _binding!!

    //フラグメントを構築中にプロパティが初期化されてしまうため、この場所でDBヘルパーの生成ができない。そのため、DBヘルパーの生成をcreateViewまで遅延させる
    private lateinit var _helper: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //DBヘルパーの生成
        _helper = DatabaseHelper(requireContext())
        //インフレートでxmlレイアウトのviewリソースを利用できるようになる
        _binding = FragmentTimeEditBinding.inflate(inflater, container,false)

        //時間のスピナー作成
        ArrayAdapter.createFromResource(requireActivity(),R.array.sp_time,android.R.layout.simple_spinner_item).also{
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spTime.adapter = adapter}

        //オプションメニューを有効化
        setHasOptionsMenu(true)

        //bindingクラスのrootにアクセスできるようにする
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ボタンのリスナ設定
        binding.btRegist.setOnClickListener(ClickListener())
        //スピナーのリスナ設定
        binding.spTime.onItemSelectedListener = SpinnerListener()
        //送られてきた情報をもとに曜日と時間を設定
        //押された曜日と時間を取得
        val intent = activity?.intent
        when(intent?.getStringExtra("time")){
            "first"->{binding.spTime.setSelection(0)}
            "second"->{binding.spTime.setSelection(1)}
            "third"->{binding.spTime.setSelection(2)}
            "fourth"->{binding.spTime.setSelection(3)}
            "fifth"->{binding.spTime.setSelection(4)}
            "sixth"->{binding.spTime.setSelection(5)}
            "seventh"->{binding.spTime.setSelection(6)}
            else->{binding.spTime.setSelection(0)}
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //戻るが押されたらアクティビティを終了
        requireActivity().finish()
        return true
    }

    private inner class SpinnerListener: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //曜日と時間をDBように変換
            val time = timeTextTransform()
            val sqlSelect = "SELECT * FROM time WHERE time = '$time'"
            //DBオブジェクトを取得
            val db = _helper.readableDatabase
            //SQL実行
            val cursor = db.rawQuery(sqlSelect, null)
            //DBから取得した値を格納する変数を用意
            var hour = 0
            var minute = 0
            var notify = 0
            while (cursor.moveToNext()){
                //カラムのインデックスを取得
                val indexHour = cursor.getColumnIndex("hour")
                val indexMinute = cursor.getColumnIndex("minute")
                val indexNotify = cursor.getColumnIndex("notify")
                //インデックスからデータを取得
                hour = cursor.getInt(indexHour)
                minute = cursor.getInt(indexMinute)
                notify = cursor.getInt(indexNotify)
            }
            cursor.close()
            //それぞれのTimePickerとSwitchにDBで取得した値を反映
            binding.tpTime.setIs24HourView(true)
            binding.tpTime.hour = hour
            binding.tpTime.minute = minute
            if(notify == 0){
                binding.swNotify.isChecked = false
            }else if(notify == 1){
                binding.swNotify.isChecked = true
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //今回選択されなかった場合を想定しないため書かない
        }
    }

    private inner class ClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            Toast.makeText(requireActivity(), "登録しました", Toast.LENGTH_LONG).show()
            //DBに保存するデータを取得
            //曜日と時間をDB用に変換
            val time = timeTextTransform()
            //それぞれに入っている中身を取得
            val hour = binding.tpTime.hour
            val minute = binding.tpTime.minute
            var notify = 0
            if(!binding.swNotify.isChecked){
                notify = 0
            }else if(binding.swNotify.isChecked){
                notify = 1
            }

            //DBオブジェクトを取得
            val db = _helper.writableDatabase
            //DBに更新をかける
            val sqlUpdate = "UPDATE time SET hour = ?, minute = ?, notify = ? WHERE time = ?"
            //SQLを実行するオブジェクトを取得
            val stmt = db.compileStatement(sqlUpdate)
            //変数をバインド
            stmt.bindString(1, hour.toString())
            stmt.bindString(2, minute.toString())
            stmt.bindString(3, notify.toString())
            stmt.bindString(4, time)
            //実行
            stmt.executeUpdateDelete()
        }
    }

    override fun onDestroyView() {
        //メモリ開放
        _binding = null
        //ヘルパーオブジェクト解放
        _helper.close()
        super.onDestroyView()
    }

    //時間をDBに保存する文字列に変換
    private fun timeTextTransform(): String{
        var time = ""
        when(binding.spTime.selectedItem){
            "1時間目"->{time = "first"}
            "2時間目"->{time = "second"}
            "3時間目"->{time = "third"}
            "4時間目"->{time = "fourth"}
            "5時間目"->{time = "fifth"}
            "6時間目"->{time = "sixth"}
            "7時間目"->{time = "seventh"}
        }
        return time
    }
}