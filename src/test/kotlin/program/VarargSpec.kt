package orm.test.program

import org.junit.Test

class VarargSpec {
    @Test
    fun VarargSpec() {
        val arr = listOf<Int>(1,2,3)
        test(arr)
    }

    @Test
    fun list2Map() {

    }

    fun test(vararg args: Any?) {
        args.forEachIndexed { index, any -> println(""+ index + "" +any) }
    }
}
