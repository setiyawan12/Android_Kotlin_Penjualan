package yayang.setiyawan.toko.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yayang.setiyawan.toko.MainActivity
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.app.ApiConfig
import yayang.setiyawan.toko.helper.SharedPref
import yayang.setiyawan.toko.model.ResponModel

class RegisterActivity : AppCompatActivity() {
    lateinit var s:SharedPref
    lateinit var fcm:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        s = SharedPref(this)
        getFcm()
        btn_register.setOnClickListener {
            register()
        }
    }
    private fun getFcm(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("respon", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            fcm =token.toString()
            // Log and toast
            Log.d("respon fcm:", token.toString())
        })
    }
    fun register(){
        if (et_username.text.isEmpty()) {
            et_username.error = "Kolom Nama tidak boleh kosong"
            et_username.requestFocus()
            return
        } else if (et_email.text.isEmpty()) {
            et_email.error = "Kolom Email tidak boleh kosong"
            et_email.requestFocus()
            return
        }else if(et_phone.text.isEmpty()){
            et_phone.error = "Kolom  Phone tidak boleh kosong"
            return
        }
        else if (et_password.text.isEmpty()) {
            et_password.error = "Kolom Password tidak boleh kosong"
            et_password.requestFocus()
            return
        }
        pb.visibility = View.VISIBLE
        ApiConfig.instanceRetrofit.register(et_username.text.toString(),et_email.text.toString(),et_phone.text.toString(),et_password.text.toString(),fcm).enqueue(object : Callback<ResponModel>{
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                pb.visibility = View.VISIBLE
                val respon = response.body()!!
                if (respon.success ==1){
                    s.setStatusLogin(true)
                    val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "Selamat datang " + respon.user.name, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@RegisterActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}