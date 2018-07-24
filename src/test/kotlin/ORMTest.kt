package orm.test

import org.junit.Test
import orm.test.model.Person
import quote.*



class ORMTest {
    @Test
    fun testQueryByIdNeedSQL() {
        val s = "select * from person where id = ?"
        var r = Quote.queryByIdWithSQL<Person>(s,1)
        println(r)
    }

    @Test
    fun testQueryByIdNOSQL() {
        val r = Quote.queryByIdNOSQL<Person>(1L)
        println(r)
    }

}