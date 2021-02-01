package com.huida.navu3d.bean

import androidx.room.*

/**
 * 作者 : lei
 * 时间 : 2021/01/21.
 * 邮箱 :416587959@qq.com
 * 描述 :WorkTask增删改查
 */
@Dao
interface PointXYDao {
    /**
     * 增加一条
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(audioBean: PointXYData)

    /**
     * 删除一条
     */
    @Delete
    fun delete(audioBean: PointXYData)

    /**
     * 查询AB点,,1是A点,2是B点
     */
    @Query("SELECT * FROM point_xy WHERE workTaskId=:workTaskId or type=:type ")
    fun findByType(workTaskId:Int,type: Int): PointXYData?

    /**
     * 返回所有的数据,结果为LiveData
     * desc =降序排序
     */
    @Query("SELECT * FROM point_xy order by sortId desc")
    fun getAlls(): MutableList<PointXYData>?


}