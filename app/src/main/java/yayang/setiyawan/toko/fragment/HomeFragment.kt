package yayang.setiyawan.toko.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.adapter.AdapterProduk
import yayang.setiyawan.toko.adapter.AdapterSlider
import yayang.setiyawan.toko.app.ApiConfig
import yayang.setiyawan.toko.model.Produk
import yayang.setiyawan.toko.model.ResponModel

class HomeFragment : Fragment() {
    lateinit var vpSlider:ViewPager
    lateinit var rvProduk: RecyclerView
    lateinit var rvProdukTerlasir: RecyclerView
    lateinit var rvElektronik: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home,container,false)
        init(view)
        getProduk()
        return view
    }
    private var listProduk:ArrayList<Produk> = ArrayList()
    fun getProduk(){
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponModel>{
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayProduk = ArrayList<Produk>()
                    for (p in res.produks) {
                        p.discount = 100000
                        arrayProduk.add(p)
                    }
                    listProduk = arrayProduk
                    displayProduk()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    fun displayProduk(){
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider3)
        val adapterSlider = AdapterSlider(arrSlider,activity)
        vpSlider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val layoutManager1 = LinearLayoutManager(activity)
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL


        rvProduk.adapter = AdapterProduk(requireActivity(),listProduk)
        rvProduk.layoutManager= layoutManager

        rvProdukTerlasir.adapter = AdapterProduk(requireActivity(),listProduk)
        rvProdukTerlasir.layoutManager = layoutManager1

        rvElektronik.adapter = AdapterProduk(requireActivity(),listProduk)
        rvElektronik.layoutManager = layoutManager2
    }

    fun init (view: View){
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvElektronik = view.findViewById(R.id.rvelektronik)
        rvProdukTerlasir=view.findViewById(R.id.rvprodukTerlasir)
    }
//    override fun onResume() {
//        super.onResume()
//        displayProduk()
//    }
}