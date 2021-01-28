package com.huida.navu3d.bean

import androidx.room.*

/**
 * 作者 : lei
 * 时间 : 2021/01/21.
 * 邮箱 :416587959@qq.com
 * 描述 :WorkTask增删改查
 */
@Dao
interface WorkTaskDao {
    /**
     * 增加一条
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(audioBean: WorkTaskData)
    /**
     * 删除一条
     */
    @Delete
    fun delete(audioBean: WorkTaskData)
    /**
     * 查询一个
     */
    @Query("SELECT * FROM work_task WHERE name=:name")
    fun findById(name: String): WorkTaskData?

    /**
     * 返回所有的数据,结果为LiveData
     * desc =降序排序
     */
    @Query("SELECT * FROM work_task order by sortId desc")
    fun getAlls(): MutableList<WorkTaskData>?
}