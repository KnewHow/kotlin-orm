package orm.test.program

import org.junit.Test

class VarargSpec {
    @Test
    fun VarargSpec() {
        test(1,3,4)
    }

    fun test(vararg args: Any?) {
        args.forEachIndexed { index, any -> println(""+ index + "" +any) }
    }
}
