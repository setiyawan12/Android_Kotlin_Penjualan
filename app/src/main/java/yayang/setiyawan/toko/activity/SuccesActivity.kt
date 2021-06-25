package yayang.setiyawan.toko.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_succes.*
import yayang.setiyawan.toko.MainActivity
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.helper.Helper
import yayang.setiyawan.toko.model.Bank
import yayang.setiyawan.toko.model.Chekout
import yayang.setiyawan.toko.model.Transaksi
import yayang.setiyawan.toko.room.MyDatabase

class SuccesActivity : AppCompatActivity() {
    var nominal = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_succes)
        setValues()
        mainButton()
    }

    private fun mainButton(){
        btn_copyNoRek.setOnClickListener {
            copyMyText(tv_nomorRekening.text.toString())
        }
        btn_copyNominal.setOnClickListener {
            copyMyText(nominal.toString())
        }
        btn_cekStatus.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }

    }

    private fun copyMyText(text:String){
        val copyManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val copyText = ClipData.newPlainText("text", text)
        copyManager.setPrimaryClip(copyText)

        Toast.makeText(this, "Text berhasil di Kopi", Toast.LENGTH_LONG).show()
    }

    private fun setValues(){
        val jsBank = intent.getStringExtra("bank")
        val jsTransaksi = intent.getStringExtra("transaksi")
        val jsCheckout = intent.getStringExtra("chekout")

        val bank = Gson().fromJson(jsBank,Bank::class.java)
        val transaksi = Gson().fromJson(jsTransaksi, Transaksi::class.java)
        val chekout = Gson().fromJson(jsCheckout, Chekout::class.java)

        val myDb = MyDatabase.getInstance(this)!!
        for (produk in chekout.produks){
            myDb.daoKeranjang().deleteById(produk.id)
        }

        tv_nomorRekening.text = bank.rekening
        tv_namaPenerima.text = bank.penerima
        image_bank.setImageResource(bank.image)
        nominal = Integer.valueOf(transaksi.total_transfer) + transaksi.kode_unik
        tv_nominal.text = Helper().gantiRupiah(nominal)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}