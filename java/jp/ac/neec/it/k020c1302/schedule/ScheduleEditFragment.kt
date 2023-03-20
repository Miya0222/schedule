package jp.ac.neec.it.k020c1302.schedule

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import jp.ac.neec.it.k020c1302.schedule.databinding.FragmentScheduleEditBinding

//ダイアログフラグメントからフラグメントに送るためのインターフェイス
interface DataPassListener {
    fun onDataPass(flg: Boolean)
}

class ScheduleEditFragment : Fragment(),DataPassListener {
    //View Bindingを使うために宣言
    private var _binding: FragmentScheduleEditBinding? = null
    private val binding get() = _binding!!

    //フラグメントを構築中にプロパティが初期化されてしまうため、この場所でDBヘルパーの生成ができない。そのため、DBヘルパーの生成をcreateViewまで遅延させる
    private lateinit var _helper: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //DBヘルパーの生成
        _helper = DatabaseHelper(requireContext())
        //インフレートでxmlレイアウトのviewリソースを利用できるようになる
        _binding = FragmentScheduleEditBinding.inflate(inflater, container, false)
        //スピナー作成
        //requireActivity()はnullを許容しないthis.activityと同じ意味
        //曜日のスピナー作成
        ArrayAdapter.createFromResource(requireActivity(),R.array.sp_weekday,android.R.layout.simple_spinner_item).also{
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spWeekday.adapter = adapter}
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
        binding.spWeekday.onItemSelectedListener = SpinnerListener()
        binding.spTime.onItemSelectedListener = SpinnerListener()
        //送られてきた情報をもとに曜日と時間を設定
        //押された曜日と時間を取得
        val intent = activity?.intent
        val weekday = intent?.getStringExtra("weekday")
        val time = intent?.getStringExtra("time")
        when(weekday){
            "monday"->{binding.spWeekday.setSelection(0)}
            "tuesday"->{binding.spWeekday.setSelection(1)}
            "wednesday"->{binding.spWeekday.setSelection(2)}
            "thursday"->{binding.spWeekday.setSelection(3)}
            "friday"->{binding.spWeekday.setSelection(4)}
            else -> binding.spWeekday.setSelection(0)
        }
        when(time){
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option, menu)
        menu.findItem(R.id.menuAllClear).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.toString() == "時間割を全て削除"){
            //ダイアログフラグメントオブジェクトを生成
            val dialogFragment = ScheduleAllClearDialogFragment()
            dialogFragment.setDataPassListener(this)
            //ダイアログを表示
            dialogFragment.show(childFragmentManager, "ScheduleAllClearDialogFragment")
        }else{
            //戻るが押されたらアクティビティを終了
            requireActivity().finish()
        }
        return true
    }
    //スケジュールオールクリアダイアログフラグメントから送られてくる
    override fun onDataPass(flg: Boolean) {
        //ダイアログフラグメントから削除するtrueが送られてきたら
        if(flg){
            //DBオブジェクトを取得
            val db = _helper.writableDatabase
            //すべての時間割のDBをリセットする
            val sqlUpdate = "UPDATE schedule SET subject = '', room = '', things = ''"
            //SQLを実行するオブジェクトを取得
            val stmt = db.compileStatement(sqlUpdate)
            //実行
            stmt.executeUpdateDelete()
            //EditTextに入っている中身を削除する
            binding.etRoom.setText("")
            binding.etSubject.setText("")
            binding.etThings.setText("")
        }else{
            //何も実装する予定はない
        }
    }

    private inner class SpinnerListener: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //曜日と時間をDBように変換
            val weekday = weekdayTextTransform()
            val time = timeTextTransform()
            val sqlSelect = "SELECT * FROM schedule WHERE weekday = '$weekday' AND time = '$time'"
            //DBオブジェクトを取得
            val db = _helper.readableDatabase
            //SQL実行
            val cursor = db.rawQuery(sqlSelect, null)
            //DBから取得した値を格納する変数を用意
            var subject = ""
            var room = ""
            var things = ""
            while (cursor.moveToNext()){
                //カラムのインデックスを取得
                val indexSubject = cursor.getColumnIndex("subject")
                val indexRoom = cursor.getColumnIndex("room")
                val indexThings = cursor.getColumnIndex("things")
                //インデックスからデータを取得
                subject = cursor.getString(indexSubject)
                room = cursor.getString(indexRoom)
                things = cursor.getString(indexThings)
            }
            cursor.close()
            //それぞれのEditTextにDBで取得した値を反映
            binding.etSubject.setText(subject)
            binding.etRoom.setText(room)
            binding.etThings.setText(things)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            //今回選択されなかった場合を想定しないため書かない
        }
    }

    private inner class ClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            if(binding.etSubject.text.length <= 5 && binding.etRoom.text.length <= 5 && binding.etThings.text.length <= 15){
                Toast.makeText(requireActivity(), "登録しました", Toast.LENGTH_LONG).show()
                //DBに保存するデータを取得
                //曜日と時間をDB用に変換
                val weekday = weekdayTextTransform()
                val time = timeTextTransform()
                //それぞれEditTextに入っている中身を取得
                val subject = binding.etSubject.text
                val room = binding.etRoom.text
                val things = binding.etThings.text
                //DBオブジェクトを取得
                val db = _helper.writableDatabase
                //DBに更新をかける
                val sqlUpdate = "UPDATE schedule SET subject = ?, room = ?, things = ? WHERE weekday = ? AND time = ?"
                //SQLを実行するオブジェクトを取得
                val stmt = db.compileStatement(sqlUpdate)
                //変数をバインド
                stmt.bindString(1, subject.toString())
                stmt.bindString(2, room.toString())
                stmt.bindString(3, things.toString())
                stmt.bindString(4, weekday)
                stmt.bindString(5, time)
                //実行
                stmt.executeUpdateDelete()
            }
            else{
                Toast.makeText(requireActivity(), "文字数が多すぎます",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        //メモリ開放
        _binding = null
        //ヘルパーオブジェクト解放
        _helper.close()
        super.onDestroyView()
    }

    //曜日をDBに保存する文字列に変換
    private fun weekdayTextTransform(): String{
        var weekday = ""
        when(binding.spWeekday.selectedItem){
            "月曜日"->{weekday = "monday"}
            "火曜日"->{weekday = "tuesday"}
            "水曜日"->{weekday = "wednesday"}
            "木曜日"->{weekday = "thursday"}
            "金曜日"->{weekday = "friday"}
        }
        return weekday
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