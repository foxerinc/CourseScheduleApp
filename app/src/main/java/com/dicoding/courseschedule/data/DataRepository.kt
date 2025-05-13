package com.dicoding.courseschedule.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.QueryUtil
import com.dicoding.courseschedule.util.SortType
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

//TODO 4 : Implement repository with appropriate dao
class DataRepository(private val dao: CourseDao) {

    fun getNearestSchedule(queryType: QueryType) : LiveData<Course?> {
        val query = QueryUtil.nearestQuery(queryType)
        return dao.getNearestSchedule(query)
    }

    fun getAllCourse(sortType: SortType): LiveData<PagingData<Course>> {
        val query = QueryUtil.sortedQuery(sortType)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { dao.getAll(query) }
        ).liveData
    }

    fun getCourse(id: Int) : LiveData<Course> {
        return dao.getCourse(id)
    }

    fun getTodaySchedule() : List<Course> {
        val calendarDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val dbDay = if (calendarDayOfWeek == 1) 1 else calendarDayOfWeek
        return dao.getTodaySchedule(dbDay)
    }

    fun insert(course: Course) = executeThread {
        dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        dao.delete(course)
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        private const val PAGE_SIZE = 10

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = CourseDatabase.getInstance(context)
                    instance = DataRepository(database.courseDao())
                }
                return instance
            }
        }
    }
}