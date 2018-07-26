package quote

import annotations.Column
import annotations.Id
import annotations.Table
import com.sun.corba.se.impl.ior.ObjectAdapterIdNumber
import java.sql.Connection
import java.sql.DriverManager
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties


object Quote{

    inline  fun <reified R> queryByIdWithSQL(sql: String, vararg args: Any?): R? {
        val pstmt = getConnection().prepareStatement(sql)
        args.forEachIndexed { index, item -> pstmt.setObject(index+1,item) }
        val rs = pstmt.executeQuery()
        val constructor = R :: class.constructors.first()
        val cParams = constructor.parameters
        return  if (rs.next()) {
            val params = cParams.map { it to rs.getObject(it.name) }.toMap()
            constructor.callBy(params) as R
        } else {
            null
        }
    }

    inline  fun<reified  R> queryByIdNOSQL(vararg args: Any): R? {
        val tbName = getTableName(R::class)
        val idPair = getIdName(R::class)
        val prop2ColumnMap = getProp2Column(R::class).toMutableMap()
        prop2ColumnMap.put(idPair.first,idPair.second)
        return if(tbName==null) {
            null
        } else {
            val sql = createSQL(R::class,tbName,idPair.second,prop2ColumnMap)
            queryORM<R>(sql,prop2ColumnMap,args.toList())
        }
    }

    inline fun<reified R> queryORM(sql: String,prop2Column: Map<String, String>,args:List<Any?>):R? {
        val pstmt = getConnection().prepareStatement(sql)
        args.forEachIndexed { index, item -> pstmt.setObject(index+1,item) }
        val rs = pstmt.executeQuery()
        val constructor = R :: class.constructors.first()
        val cParams = constructor.parameters
        return  if (rs.next()) {
            val params = cParams.map { it to rs.getObject(prop2Column.get(it.name)) }.toMap()
            constructor.callBy(params) as R
        } else {
            null
        }
    }

    public fun getTableName(clazz: KClass<*>): String? {
        val tableAnno = clazz.annotations.find { it is Table } as? Table
        return  tableAnno?.name ?: clazz.simpleName
    }

    public fun getIdName(clazz: KClass<*>): Pair<String, String> {
        val idProp = clazz.memberProperties.filter { (it.annotations.filter { a -> a is Id }).isNotEmpty()  }.firstOrNull()
        val idAnno =  idProp?.annotations?.find { it is Id } as? Id
        val name = idAnno?.name
        return  if(name == null || name == "") {
            (idProp!!.name to idProp!!.name)
        } else {
            (idProp!!.name to name)
        }
    }

    public  fun getProp2Column(clazz: KClass<*>): Map<String,String> {
        return clazz.memberProperties.map { it.name to getColumnName(it) }.toMap()
    }

    public fun getColumnName(prop: KProperty<*>): String  {
        val columnAnno = prop.annotations.find { it is Column } as? Column
        val name = columnAnno?.name
        return if(name == null || name == "") {
            prop.name
        } else {
            name
        }
    }

    public fun createSQL(clazz: KClass<*>, tbName: String, idName: String, prop2Column: Map<String, String>): String {
        val sql = StringBuffer("select ")
        clazz.memberProperties.forEach{ sql.append(prop2Column.get(it.name) + ",")}
        sql.deleteCharAt(sql.length-1)
        sql.append(" from $tbName where $idName = ?")
        return  sql.toString()
    }


    public fun getConnection(): Connection {
        Class.forName("org.h2.Driver")
        val db = "jdbc:h2:mem:;INIT=runscript from 'classpath:/person.sql'"
        return DriverManager.getConnection(db)
    }
}