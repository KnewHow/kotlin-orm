package orm.test.model

import annotations.Column
import annotations.Id
import annotations.Table

@Table("person")
data class Person(
        @property:Id("id")
        val id: Long,
        @property:Column("pname")
        val name: String,
        @property:Column
        val age: Int,
        @property:Column
        val address: String,
        @property:Column
        val job: String
)