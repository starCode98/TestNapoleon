package pe.com.starcode.testnapoleonsystem.main.presentation

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import pe.com.starcode.testnapoleonsystem.R
import pe.com.starcode.testnapoleonsystem.base.retrofit.ApiHelper
import pe.com.starcode.testnapoleonsystem.base.retrofit.RetrofitBuilder
import pe.com.starcode.testnapoleonsystem.base.room.Post
import pe.com.starcode.testnapoleonsystem.databinding.ActivityMainBinding
import pe.com.starcode.testnapoleonsystem.main.adapter.PostAdapter
import pe.com.starcode.testnapoleonsystem.main.adapter.PostListener
import pe.com.starcode.testnapoleonsystem.main.repository.MainRepository

class MainActivity : AppCompatActivity(), PostListener {

    private lateinit var b: ActivityMainBinding
    private lateinit var vm: MainViewModel
    private var progressDialog: ProgressDialog? = null
    private lateinit var adapter:PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        var f = MainViewModelFactory(
            applicationContext,
            MainRepository(ApiHelper(RetrofitBuilder.apiService))
        )
        vm = ViewModelProvider(this, f).get()
        b.viewModelMain = vm
        b.lifecycleOwner = this

        vm.toastmessage.observe(this, Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        })
        vm.progressVisibility.observe(this, Observer {
            if (it) {
                showProgress()
            } else hideProgress()
        })
        vm.listPost.observe(this, Observer { list ->
            adapter = PostAdapter(list, this)
            recyclerPost.also {
                it.layoutManager = LinearLayoutManager(applicationContext)
                it.setHasFixedSize(true)
                it.adapter = adapter
            }
        })
        vm.successUpdateState.observe(this, Observer {
            showDetails(it)
            adapter.updateItem(it)
        })
        vm.successFavorite.observe(this, Observer {
            if(it)showText(getString(R.string._main_detail_add_favorite_success))
        })
    }
    private fun showText(m:String){
       Toast.makeText(applicationContext, m, Toast.LENGTH_SHORT).show()
    }

    private fun showDetails(post: Post) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_details_post)
        dialog.window!!.setGravity(Gravity.CENTER_VERTICAL)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        var close = dialog.findViewById<Button>(R.id.close)
        var add = dialog.findViewById<TextView>(R.id.addToFavorite)
        var title = dialog.findViewById<TextView>(R.id.txTitle)
        var body = dialog.findViewById<TextView>(R.id.txBody)
        var user = dialog.findViewById<TextView>(R.id.txUser)
        title.text = post.title
        body.text= post.body
        user.text= "By: User ${post.userId}"
        close.setOnClickListener {
            dialog.dismiss()
        }
        add.setOnClickListener {
            dialog.dismiss()
            vm.addToFavorite(post)
        }
    }

    fun showProgress() {
        progressDialog = ProgressDialog(this)
        progressDialog?.show()
        progressDialog?.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        progressDialog?.setContentView(R.layout.progress_custom)
    }

    fun hideProgress() {
        if (progressDialog != null) {
            Thread { runOnUiThread { progressDialog?.dismiss() } }
                .start()
        }
    }

    override fun onclick(post: Post) {
        vm.updateState(post)

    }


}