package a13341.egco428.com.a13341

import a13341.egco428.com.a13341.CookiePovider.addMeesage
import a13341.egco428.com.a13341.DataSource.CookieDataSource
import a13341.egco428.com.a13341.Model.Cookie
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import java.util.*
import android.hardware.SensorEventListener
import android.text.format.DateFormat
import kotlinx.android.synthetic.main.activity_new_cookie.*


class NewCookie : AppCompatActivity(),SensorEventListener{

    private var sensorManager : SensorManager? = null
    private var lastUpdate:Long = 0
    private var lock:Boolean = false
    private var dataSource:CookieDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_cookie)


        setTitle("             Fortune Cookies")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dataSource = CookieDataSource(this)
        dataSource!!.open()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //textView.setBackgroundColor(Color.GREEN)
        lastUpdate = System.currentTimeMillis()

        saveBtn.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.newcookie_menu,menu)
        return true
    }

    //คล้ายๆonClick
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()

        if (id == R.id.settingBtn){
            val intent = Intent(this,AddDeleteMessage::class.java)
            startActivity(intent)
        }
        if(id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent?){
        val values : FloatArray = event!!.values
        var x = values[0]
        var y = values[1]
        var z = values[2]

        val accel = ((x*x)+(y*y)+(z*z))/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH)
        var actuaTime = System.currentTimeMillis()
        //Log.d("CHECK","$accel")

        var cookie:Cookie
        val random = Random().nextInt(dataSource!!.allComments.size)

        var timestamp = System.currentTimeMillis()/1000
        //var time = DateFormat.format("dd-MM-yyyy hh:mm:ss", timestamp*1000).toString()
        var time = DateFormat.format("dd-MM-yyyy hh:mm", timestamp*1000).toString()

        if(accel>=2){
            if(actuaTime-lastUpdate<200){
                cookie = dataSource!!.allComments.get(random)

                imageView.setImageResource(R.drawable.opened_cookie)

                if(cookie.type == "Positive"){
                    resultText.setTextColor(Color.BLUE)
                }else if(cookie.type == "Negative"){
                    resultText.setTextColor(Color.RED)
                }
                resultText.text = cookie.message.toString()
                centernewText.text = cookie.message.toString()
                dateText.text = time

                var messageM = cookie.message.toString()
                var typeM = cookie.type.toString()

                saveBtn.isEnabled = true
                saveBtn.setOnClickListener {
                    addMeesage(messageM,typeM,time)
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }

                return
            }
            lastUpdate = actuaTime
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)

    }
}
