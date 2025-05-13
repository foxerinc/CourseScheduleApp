package com.dicoding.courseschedule.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()
    val nearestCourse = _queryType.switchMap {
        repository.getNearestSchedule(it)
    }

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }


}


class HomeViewModelFactory (private val courseRepository: DataRepository?): ViewModelProvider.Factory{
    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null

        fun getInstance(context: Context): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(
                    DataRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(courseRepository!!) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

}

