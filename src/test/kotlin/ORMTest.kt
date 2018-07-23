package orm.test

import org.junit.Test
import orm.test.model.Person
import quote.*



class ORMTest {
    @Test
    fun testPersonQuery() {
        val s = "select * from person where id = ?"
        var r = Quote.queryOne<Person>(s,1)
        println(r)
    }

}