package jp.ac.neec.it.k020c1302.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TimeEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_edit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}