import org.gradle.api.Project

/**
 * 作者 : LiLei
 * 时间 : 2020/11/02.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object AndroidDomain {

    fun configAndroidDomain(pro: Project) {
        if (pro.plugins.hasPlugin("com.android.application")) {
            print("pro.toString()")
            print(pro.toString())
//            configAppAndroidDomain(pro)
        } else {
//            configLibAndroidDomain(pro)
        }
    }


}