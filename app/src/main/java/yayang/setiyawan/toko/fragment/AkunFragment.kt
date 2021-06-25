package yayang.setiyawan.toko.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_akun.*
import org.w3c.dom.Text
import yayang.setiyawan.toko.R
import yayang.setiyawan.toko.activity.LoginActivity
import yayang.setiyawan.toko.activity.RiwayatActivity
import yayang.setiyawan.toko.helper.SharedPref

class AkunFragment : Fragment() {
    lateinit var tvNama:TextView
    lateinit var tvPhone:TextView
    lateinit var tvEmail:TextView
    lateinit var s:SharedPref
    lateinit var btnLogout:TextView
    lateinit var btnRiwayat: RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View= inflater.inflate(R.layout.fragment_akun,container,false)
        s = SharedPref(requireActivity())
        init(view)
        setData()
        mainButton()
        return view
    }
    fun setData() {
        if (s.getUser() == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }
        val user = s.getUser()!!
        tvNama.text = user.name
        tvEmail.text = user.email
        tvPhone.text = user.phone
    }
    private fun init(view: View){
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        btnRiwayat = view.findViewById(R.id.btn_riwayat)

    }
    private fun mainButton(){
        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        btnRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatActivity::class.java))
        }
    }
}