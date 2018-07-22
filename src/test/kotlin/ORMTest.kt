package orm.test

import org.junit.After
import java.sql.DriverManager
import org.junit.Before
import java.sql.Connection
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.DbUtils
import org.apache.commons.dbutils.handlers.BeanHandler
import org.junit.Test
import java.sql.ResultSet




class ORMTest {
    private var connection: Connection? = null


    @Before
    @Throws(Exception::class)
    fun setupDB() {
        Class.forName("org.h2.Driver")
        val db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/person.sql'"
        connection = DriverManager.getConnection(db)
    }

    @After
    fun closeBD() {
        DbUtils.closeQuietly(connection)
    }


}