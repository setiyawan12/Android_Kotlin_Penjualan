package yayang.setiyawan.toko.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_riwayat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.adapter.AdapterRiwayat
import yayang.setiyawan.toko.app.ApiConfig
import yayang.setiyawan.toko.helper.Helper
import yayang.setiyawan.toko.helper.SharedPref
import yayang.setiyawan.toko.model.ResponModel
import yayang.setiyawan.toko.model.Transaksi

class RiwayatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)
        Helper().setToolbar(this, toolbar, "Riwayat Belanja")

    }

    fun getRiwayat(){
        val id = SharedPref(this).getUser()!!.id
        ApiConfig.instanceRetrofit.getRiwayat(id).enqueue(object : Callback<ResponModel>{
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1){
                    displayRiwayat(res.transaksis)
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun displayRiwayat(transaksis: ArrayList<Transaksi>){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat.adapter = AdapterRiwayat(transaksis,object :AdapterRiwayat.Listeners{
            override fun onClicked(data: Transaksi) {
                TODO("Not yet implemented")
            }

        })
        rv_riwayat.layoutManager = layoutManager
    }

    override fun onResume() {
        getRiwayat()
        super.onResume()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}