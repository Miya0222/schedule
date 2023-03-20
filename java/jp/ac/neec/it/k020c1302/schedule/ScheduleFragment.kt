package jp.ac.neec.it.k020c1302.schedule

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import jp.ac.neec.it.k020c1302.schedule.databinding.FragmentScheduleBinding


class ScheduleFragment : Fragment() {
    //View Bindingを使うために宣言
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    //フラグメントを構築中にプロパティが初期化されてしまうため、この場所でDBヘルパーの生成ができない。そのため、DBヘルパーの生成をcreateViewまで遅延させる
    private lateinit var _helper: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //DBヘルパーの生成
        _helper = DatabaseHelper(requireContext())
        //インフレートでxmlレイアウトのviewリソースを利用できるようになる
        _binding = FragmentScheduleBinding.inflate(inflater, container,false)

        //オプションメニューを有効化
        setHasOptionsMenu(true)

        val serviceIntent = Intent(requireActivity(), NotificationService::class.java)
        requireActivity().startService(serviceIntent)

        //bindingクラスのrootにアクセスできるようにする
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //各時間のオブジェクトを取得しクリックリスナーを設定
        listenerSet()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //メニューを表示する
        inflater.inflate(R.menu.timepick, menu)
        menu.findItem(R.id.menuTimePick).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //インテントオブジェクトを生成
        val intent2TimeEditActivity = Intent(activity, TimeEditActivity::class.java)
        startActivity(intent2TimeEditActivity)
        return true
    }

    private inner class ClickListener : View.OnClickListener{
        override fun onClick(view: View) {
            //インテントオブジェクトを生成
            val intent2ScheduleEditActivity = Intent (activity, ScheduleEditActivity::class.java)
            positionJudge(intent2ScheduleEditActivity,view)
        }
    }

    override fun onStart() {
        super.onStart()
        //DBヘルパーオブジェクトを取得
        val db = _helper.readableDatabase
        val sql = "SELECT * FROM schedule"
        //SQL実行
        val cursor = db.rawQuery(sql,null)
        while (cursor.moveToNext()){
            //カラムのインデックスを取得
            val indexWeekday = cursor.getColumnIndex("weekday")
            val indexTime = cursor.getColumnIndex("time")
            val indexSubject = cursor.getColumnIndex("subject")
            val indexRoom = cursor.getColumnIndex("room")
            val indexThings = cursor.getColumnIndex("things")
            //インデックスからデータを取得
            val weekday = cursor.getString(indexWeekday)
            val time = cursor.getString(indexTime)
            val subject = cursor.getString(indexSubject)
            val room = cursor.getString(indexRoom)
            val things = cursor.getString(indexThings)
            //判定に使うための配列を作成
            val weekdays = arrayOf("monday", "tuesday", "wednesday", "thursday", "friday")
            val times = arrayOf("first", "second", "third", "fourth", "fifth", "sixth", "seventh")
            val judgeArray = arrayOf(
                arrayOf(binding.mondayFirst, binding.mondaySecond, binding.mondayThird, binding.mondayFourth, binding.mondayFifth, binding.mondaySixth, binding.mondaySeventh),
                arrayOf(binding.tuesdayFirst, binding.tuesdaySecond, binding.tuesdayThird, binding.tuesdayFourth, binding.tuesdayFifth, binding.tuesdaySixth, binding.tuesdaySeventh),
                arrayOf(binding.wednesdayFirst, binding.wednesdaySecond, binding.wednesdayThird, binding.wednesdayFourth, binding.wednesdayFifth, binding.wednesdaySixth, binding.wednesdaySeventh),
                arrayOf(binding.thursdayFirst, binding.thursdaySecond, binding.thursdayThird, binding.thursdayFourth, binding.thursdayFifth, binding.thursdaySixth, binding.thursdaySeventh),
                arrayOf(binding.fridayFirst, binding.fridaySecond, binding.fridayThird, binding.fridayFourth, binding.fridayFifth, binding.fridaySixth, binding.fridaySeventh)
            )
            //どのTextViewを使うか
            val text = judgeArray[weekdays.indexOf(weekday)][times.indexOf(time)]
            //TextViewに入れるデータ作成
            val str = "$subject\n$room\n$things"
            //選ばれたTextViewにデータを設定
            text.text = str
        }
        cursor.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //メモリ開放
        _binding = null
        //ヘルパーオブジェクト解放
        _helper.close()
        super.onDestroyView()
    }

    //可読性が下がるため関数化
    private fun listenerSet(){
        //月曜日
        binding.mondayFirst.setOnClickListener(ClickListener())
        binding.mondaySecond.setOnClickListener(ClickListener())
        binding.mondayThird.setOnClickListener(ClickListener())
        binding.mondayFourth.setOnClickListener(ClickListener())
        binding.mondayFifth.setOnClickListener(ClickListener())
        binding.mondaySixth.setOnClickListener(ClickListener())
        binding.mondaySeventh.setOnClickListener(ClickListener())
        //火曜日
        binding.tuesdayFirst.setOnClickListener(ClickListener())
        binding.tuesdaySecond.setOnClickListener(ClickListener())
        binding.tuesdayThird.setOnClickListener(ClickListener())
        binding.tuesdayFourth.setOnClickListener(ClickListener())
        binding.tuesdayFifth.setOnClickListener(ClickListener())
        binding.tuesdaySixth.setOnClickListener(ClickListener())
        binding.tuesdaySeventh.setOnClickListener(ClickListener())
        //水曜日
        binding.wednesdayFirst.setOnClickListener(ClickListener())
        binding.wednesdaySecond.setOnClickListener(ClickListener())
        binding.wednesdayThird.setOnClickListener(ClickListener())
        binding.wednesdayFourth.setOnClickListener(ClickListener())
        binding.wednesdayFifth.setOnClickListener(ClickListener())
        binding.wednesdaySixth.setOnClickListener(ClickListener())
        binding.wednesdaySeventh.setOnClickListener(ClickListener())
        //木曜日
        binding.thursdayFirst.setOnClickListener(ClickListener())
        binding.thursdaySecond.setOnClickListener(ClickListener())
        binding.thursdayThird.setOnClickListener(ClickListener())
        binding.thursdayFourth.setOnClickListener(ClickListener())
        binding.thursdayFifth.setOnClickListener(ClickListener())
        binding.thursdaySixth.setOnClickListener(ClickListener())
        binding.thursdaySeventh.setOnClickListener(ClickListener())
        //金曜日
        binding.fridayFirst.setOnClickListener(ClickListener())
        binding.fridaySecond.setOnClickListener(ClickListener())
        binding.fridayThird.setOnClickListener(ClickListener())
        binding.fridayFourth.setOnClickListener(ClickListener())
        binding.fridayFifth.setOnClickListener(ClickListener())
        binding.fridaySixth.setOnClickListener(ClickListener())
        binding.fridaySeventh.setOnClickListener(ClickListener())
    }
    //可読性が下がるため関数化
    private fun positionJudge(intent: Intent, view: View){
        when(view.id){
            R.id.mondayFirst->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","monday")
                intent.putExtra("time","first")
                //画面起動
                startActivity(intent)

            }
            R.id.mondaySecond->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","monday")
                intent.putExtra("time","second")
                //画面起動
                startActivity(intent)
            }
            R.id.mondayThird->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","monday")
                intent.putExtra("time","third")
                //画面起動
                startActivity(intent)
            }
            R.id.mondayFourth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","monday")
                intent.putExtra("time","fourth")
                //画面起動
                startActivity(intent)
            }
            R.id.mondayFifth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","monday")
                intent.putExtra("time","fifth")
                //画面起動
                startActivity(intent)
            }
            R.id.mondaySixth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","monday")
                intent.putExtra("time","sixth")
                //画面起動
                startActivity(intent)
            }
            R.id.mondaySeventh->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","monday")
                intent.putExtra("time","seventh")
                //画面起動
                startActivity(intent)
            }
            R.id.tuesdayFirst->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","tuesday")
                intent.putExtra("time","first")
                //画面起動
                startActivity(intent)

            }
            R.id.tuesdaySecond->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","tuesday")
                intent.putExtra("time","second")
                //画面起動
                startActivity(intent)
            }
            R.id.tuesdayThird->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","tuesday")
                intent.putExtra("time","third")
                //画面起動
                startActivity(intent)
            }
            R.id.tuesdayFourth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","tuesday")
                intent.putExtra("time","fourth")
                //画面起動
                startActivity(intent)
            }
            R.id.tuesdayFifth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","tuesday")
                intent.putExtra("time","fifth")
                //画面起動
                startActivity(intent)
            }
            R.id.tuesdaySixth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","tuesday")
                intent.putExtra("time","sixth")
                //画面起動
                startActivity(intent)
            }
            R.id.tuesdaySeventh->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","tuesday")
                intent.putExtra("time","seventh")
                //画面起動
                startActivity(intent)
            }
            R.id.wednesdayFirst->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","wednesday")
                intent.putExtra("time","first")
                //画面起動
                startActivity(intent)

            }
            R.id.wednesdaySecond->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","wednesday")
                intent.putExtra("time","second")
                //画面起動
                startActivity(intent)
            }
            R.id.wednesdayThird->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","wednesday")
                intent.putExtra("time","third")
                //画面起動
                startActivity(intent)
            }
            R.id.wednesdayFourth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","wednesday")
                intent.putExtra("time","fourth")
                //画面起動
                startActivity(intent)
            }
            R.id.wednesdayFifth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","wednesday")
                intent.putExtra("time","fifth")
                //画面起動
                startActivity(intent)
            }
            R.id.wednesdaySixth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","wednesday")
                intent.putExtra("time","sixth")
                //画面起動
                startActivity(intent)
            }
            R.id.wednesdaySeventh->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","wednesday")
                intent.putExtra("time","seventh")
                //画面起動
                startActivity(intent)
            }
            R.id.thursdayFirst->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","thursday")
                intent.putExtra("time","first")
                //画面起動
                startActivity(intent)

            }
            R.id.thursdaySecond->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","thursday")
                intent.putExtra("time","second")
                //画面起動
                startActivity(intent)
            }
            R.id.thursdayThird->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","thursday")
                intent.putExtra("time","third")
                //画面起動
                startActivity(intent)
            }
            R.id.thursdayFourth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","thursday")
                intent.putExtra("time","fourth")
                //画面起動
                startActivity(intent)
            }
            R.id.thursdayFifth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","thursday")
                intent.putExtra("time","fifth")
                //画面起動
                startActivity(intent)
            }
            R.id.thursdaySixth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","thursday")
                intent.putExtra("time","sixth")
                //画面起動
                startActivity(intent)
            }
            R.id.thursdaySeventh->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","thursday")
                intent.putExtra("time","seventh")
                //画面起動
                startActivity(intent)
            }
            R.id.fridayFirst->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","friday")
                intent.putExtra("time","first")
                //画面起動
                startActivity(intent)

            }
            R.id.fridaySecond->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","friday")
                intent.putExtra("time","second")
                //画面起動
                startActivity(intent)
            }
            R.id.fridayThird->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","friday")
                intent.putExtra("time","third")
                //画面起動
                startActivity(intent)
            }
            R.id.fridayFourth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","friday")
                intent.putExtra("time","fourth")
                //画面起動
                startActivity(intent)
            }
            R.id.fridayFifth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","friday")
                intent.putExtra("time","fifth")
                //画面起動
                startActivity(intent)
            }
            R.id.fridaySixth->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","friday")
                intent.putExtra("time","sixth")
                //画面起動
                startActivity(intent)
            }
            R.id.fridaySeventh->{
                //データを格納（押されたTVに入っている情報と識別情報を格納）
                intent.putExtra("weekday","friday")
                intent.putExtra("time","seventh")
                //画面起動
                startActivity(intent)
            }
        }
    }
}

