package yayang.setiyawan.toko.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_alamat.*
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.adapter.AdapterAlamat
import yayang.setiyawan.toko.model.Alamat
import yayang.setiyawan.toko.room.MyDatabase

class ListAlamatActivity : AppCompatActivity() {
    lateinit var myDb :MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat)
        mainButton()
        myDb = MyDatabase.getInstance(this)!!
    }
    private fun mainButton(){
        btn_tambahAlamat.setOnClickListener {
            startActivity(Intent(this,TambahAlamatActivity::class.java))
        }
    }

    private fun displayAlamat(){
        val arrayList = myDb.daoAlamat().getAll() as ArrayList
        if (arrayList.isEmpty())div_kosong.visibility = View.VISIBLE
        else div_kosong.visibility = View.GONE

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_alamat.adapter = AdapterAlamat(arrayList, object : AdapterAlamat.Listeners {
            override fun onClicked(data: Alamat) {
                if (myDb.daoAlamat().getByStatus(true) != null){
                    val alamatActive = myDb.daoAlamat().getByStatus(true)!!
                    alamatActive.isSelected = false
                    updateActive(alamatActive, data)
                }
            }
        })
        rv_alamat.layoutManager = layoutManager
    }
    private fun updateActive(dataActive: Alamat, dataNonActive: Alamat) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(dataActive) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateNonActive(dataNonActive)
                })
    }

    private fun updateNonActive(data: Alamat) {
        data.isSelected = true
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onBackPressed()
                })
    }
    override fun onResume() {
        displayAlamat()
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}