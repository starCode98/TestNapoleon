package pe.com.starcode.testnapoleonsystem.main.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import pe.com.starcode.testnapoleonsystem.R
import pe.com.starcode.testnapoleonsystem.base.Coroutines
import pe.com.starcode.testnapoleonsystem.base.room.Post
import pe.com.starcode.testnapoleonsystem.main.repository.MainRepository
import retrofit2.http.POST
import java.lang.Exception

class MainViewModel(var context: Context, var repository: MainRepository) :ViewModel() {

    private var TAG = "MainViewModel"

    private val _toastmessage = MutableLiveData<String>()
    val toastmessage: LiveData<String>
        get() = _toastmessage

    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility: LiveData<Boolean> get() = _progressVisibility

    private val _listPost = MutableLiveData<MutableList<Post>>()
    val listPost: LiveData<MutableList<Post>> get() = _listPost

    private val _successUpdateState = MutableLiveData<Post>()
    val successUpdateState: LiveData<Post>get()= _successUpdateState

    private val _successUpdate = MutableLiveData<Post>()
    val successUpdate: LiveData<Post>get()= _successUpdate

    private val _successDelete = MutableLiveData<Post>()
    val successDelete: LiveData<Post>get()= _successDelete


    //jobs
    private lateinit var job: Job

    init {
        listPosts()
    }


    private fun listPosts() {
        try {
            if (!isNetworkAvailable(context)) {
                _toastmessage.value = " Error: ${context.getString(R.string.main_error_internet)}"
                return
            }
            _progressVisibility.value = true

            job = Coroutines.iothenMain({
                repository.getPosts()
            }, {
                if (it != null) {

                    if (it.isSuccessful) {
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d(TAG, "listPosts Response:  ${it.body()}")
                            var f = repository.savePost(it.body(), context)
                            CoroutineScope(Dispatchers.Main).launch {
                                _progressVisibility.value = false
                                if (f != null) _listPost.value = f
                            }
                        }
                    } else {
                        _progressVisibility.value = false
                        val jObjError = JSONObject(it.errorBody()?.string())
                        Log.d(TAG, "error "+ it.errorBody()?.string())
                        _toastmessage.value = jObjError.getString("message")
                    }

                }

            })

        } catch (
            e: Exception
        ) {
            _toastmessage.value = " Error: ${e.message}"
        }
    }


    fun isNetworkAvailable(context: Context): Boolean {
        var mIsConnected = false
        val mConnectivityMgr =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            // Checking internet connectivity
            var activeNetwork: NetworkInfo? = null
            if (mConnectivityMgr != null) {
                activeNetwork = mConnectivityMgr.activeNetworkInfo // Deprecated in API 29
            }
            mIsConnected = activeNetwork != null
        } else {
            val allNetworks =
                mConnectivityMgr.allNetworks // added in API 21 (Lollipop)
            for (network in allNetworks) {
                val networkCapabilities =
                    mConnectivityMgr.getNetworkCapabilities(network)
                if (networkCapabilities != null) {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    ) mIsConnected = true
                }
            }
        }
        return mIsConnected
    }

    fun updateState(post: Post) {
        _progressVisibility.value = true
        try {
            if (!isNetworkAvailable(context)) {
                _toastmessage.value = " Error: ${context.getString(R.string.main_error_internet)}"
                return
            }
            job = Coroutines.iothenMain({
                repository.getUser(post.userId)
            }, {
                _progressVisibility.value = false
                if (it != null) {
//                    _progressVisibility.value = false
                    if (it.isSuccessful) {
                        CoroutineScope(Dispatchers.IO).launch {
                            post.nameUser = it.body()?.name.toString()
                            post.emailUser = it.body()?.email.toString()
                            post.state  = false
                            repository.updatePost(post, context)
                            CoroutineScope(Dispatchers.Main).launch {
                                _successUpdateState.value = post
                            }
                        }
                    } else {
                        val jObjError = JSONObject(it.errorBody()?.string())
                        _toastmessage.value = jObjError.getString("message")
                    }

                }

            })

        } catch (
            e: Exception
        ) {
            _toastmessage.value = " Error: ${e.message}"
        }


    }

    fun addToFavorite(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            post.favorite = true
            repository.updatePost(post, context)
            CoroutineScope(Dispatchers.Main).launch {
                _successUpdate.value = post
            }
        }
    }

    fun removeItem(post: Post) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.remove(post, context)
            CoroutineScope(Dispatchers.Main).launch {
                _successDelete.value = post
            }
        }
    }
    fun resfreshAll(){
        listPosts()
    }
    fun deleteAll(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.removeAll( context)
            CoroutineScope(Dispatchers.Main).launch {
                _listPost.value = arrayListOf()
            }
        }
    }

}