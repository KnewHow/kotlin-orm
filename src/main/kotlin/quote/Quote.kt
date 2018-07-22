package quote

import java.sql.Connection
import java.sql.DriverManager

interface Quote<T> {
    fun queryById(sql: String, vararg args: Any?): T?

}

class QuoteImpl<T>: Quote<T> {

    override fun queryById(sql: String, vararg args: Any?): T? {
        val pstmt = getConnection().prepareStatement(sql)
        args.forEachIndexed { index, item -> pstmt.setObject(index+1,item) }
        val rs = pstmt.executeQuery()
//        val cons = this::class.typeParameters.first().
        while (rs.next()) {

        }
        return  null
    }

    private fun getConnection(): Connection {
        Class.forName("org.h2.Driver")
        val db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/person.sql'"
        return DriverManager.getConnection(db)
    }
}