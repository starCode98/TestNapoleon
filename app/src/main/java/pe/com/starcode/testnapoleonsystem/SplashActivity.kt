package pe.com.starcode.testnapoleonsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private var CURRENT_TIME:Long= 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        CoroutineScope(Dispatchers.Main).launch {
            Handler().postDelayed({
              goMain()
            }, CURRENT_TIME)
        }
    }

    private fun goMain() {
//        var intent = Intent(this@SplashActivity, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
    }
}