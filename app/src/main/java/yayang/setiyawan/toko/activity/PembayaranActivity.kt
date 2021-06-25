package yayang.setiyawan.toko.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.activity_pembayran.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.adapter.AdapterBank
import yayang.setiyawan.toko.app.ApiConfig
import yayang.setiyawan.toko.helper.Helper
import yayang.setiyawan.toko.model.Bank
import yayang.setiyawan.toko.model.Chekout
import yayang.setiyawan.toko.model.ResponModel
import yayang.setiyawan.toko.model.Transaksi

class PembayaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayran)
        Helper().setToolbar(this, toolbar, "Pembayaran")
        displayBank()
    }
    fun displayBank(){
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA", "092093871237", "Tisto wahyudi", R.drawable.logo_bca))
        arrBank.add(Bank("BRI", "86721349128", "Tisto wahyudi", R.drawable.logo_bri))
        arrBank.add(Bank("Mandiri", "02394870329", "Tisto wahyudi", R.drawable.logo_madiri))
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_data.layoutManager = layoutManager
        rv_data.adapter= AdapterBank(arrBank,object :AdapterBank.Listeners{
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }
        })
    }
    fun bayar(bank:Bank){
        val json = intent.getStringExtra("extra")!!.toString()
        val chekout = Gson().fromJson(json, Chekout::class.java)
        chekout.bank = bank.nama
        val loading = SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
        loading.setTitleText("Looading").show()
        ApiConfig.instanceRetrofit.chekout(chekout).enqueue(object : Callback<ResponModel>{
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                loading.dismiss()
                if (!response.isSuccessful){
                    error(response.message())
                    return
                }
                val respon = response.body()!!
                if (respon.success == 1){
                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                    val jsChekout = Gson().toJson(chekout, Chekout::class.java)
                    val intent = Intent(this@PembayaranActivity, SuccesActivity::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksi)
                    intent.putExtra("chekout", jsChekout)
                    startActivity(intent)
                }else{
                    error(respon.message)
                    Toast.makeText(this@PembayaranActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                loading.dismiss()
            }

        })
    }
    fun error(pesan: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(pesan)
                .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
