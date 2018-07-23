package quote

import java.sql.Connection
import java.sql.DriverManager
import kotlin.reflect.KClass
import kotlin.reflect.KParameter


object Quote{

    inline  fun <reified R> queryOne(sql: String, vararg args: Any?): R? {
        val pstmt = getConnection().prepareStatement(sql)
        args.forEachIndexed { index, item -> pstmt.setObject(index+1,item) }
        val rs = pstmt.executeQuery()
        val constructor = R :: class.constructors.first()
        val cParam = constructor.parameters
        var paramMap = HashMap<KParameter, Any?>()
        if (rs.next()) {
             cParam.forEach { paramMap.put(it, rs.getObject(it.name)) }
            val r = constructor.callBy(paramMap)
            return r as R
        } else {
            return null
        }
    }

    public fun getConnection(): Connection {
        Class.forName("org.h2.Driver")
        val db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/person.sql'"
        return DriverManager.getConnection(db)
    }
}