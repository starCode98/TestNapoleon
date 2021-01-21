package pe.com.starcode.testnapoleonsystem.main.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.com.starcode.testnapoleonsystem.main.repository.MainRepository

class MainViewModelFactory (private val context: Context,
    private val repository: MainRepository
) :
    ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(context,repository) as T
    }

}