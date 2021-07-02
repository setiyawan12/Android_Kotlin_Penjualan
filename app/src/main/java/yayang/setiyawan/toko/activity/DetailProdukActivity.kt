package yayang.setiyawan.toko.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbar_custom.*
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.helper.Helper
import yayang.setiyawan.toko.model.Produk
import yayang.setiyawan.toko.room.MyDatabase
import yayang.setiyawan.toko.util.Config

class DetailProdukActivity : AppCompatActivity() {
    lateinit var myDb:MyDatabase
    lateinit var produk:Produk
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)
        myDb = MyDatabase.getInstance(this)!!
        getInfo()
        mainButton()
        checkKeranjang()
    }

    fun mainButton(){
        btn_keranjang.setOnClickListener {
            val data = myDb.daoKeranjang().getProduk(produk.id)
            if (data == null) {
                insert()
            } else {
                data.jumlah += 1
                update(data)
            }
        }
//        btn_favorit.setOnClickListener {
//            val listData = myDb.daoKeranjang().getAll() // get All data
//            for (note: Produk in listData) {
//                println("-----------------------")
//                println(note.name)
//                println(note.harga)
//            }
//        }
        btn_toKeranjang.setOnClickListener {
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }
    private fun insert(){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkKeranjang()
                    Log.d("respons", "data inserted")
                    SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("keranjang")
                            .setContentText("Di Tambah Ke Keranjang")
                            .show()
                }
        )
    }
    private fun update(data:Produk){
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkKeranjang()
                    Log.d("respons", "data inserted")
//                    Toast.makeText(this, "Berhasil menambah kekeranjang", Toast.LENGTH_SHORT).show(
                })
    }
    private fun checkKeranjang() {
        val dataKranjang = myDb.daoKeranjang().getAll()
        if (dataKranjang.isNotEmpty()) {
            div_angka.visibility = View.VISIBLE
            tv_angka.text = dataKranjang.size.toString()
        } else {
            div_angka.visibility = View.GONE
        }
    }

    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        produk = Gson().fromJson<Produk>(data, Produk::class.java)

        // set Value
        tv_nama.text = produk.name
        tv_harga.text = Helper().gantiRupiah(produk.harga)
        tv_deskripsi.text = produk.deskripsi

        val img = produk.image
        println("HASIL"+img)
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .resize(400, 400)
                .into(image)
        // setToolbar
        Helper().setToolbar(this, toolbar, produk.name)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}