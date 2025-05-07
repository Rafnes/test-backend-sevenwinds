package mobi.sevenwinds.app.author

import mobi.sevenwinds.app.budget.AuthorRecord
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object AuthorTable : IntIdTable("author") {
    val name = varchar("\"ФИО\"", 100)
    val createdAt = datetime("Дата создания")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var name by AuthorTable.name
    var createdAt by AuthorTable.createdAt

    fun toResponse(): AuthorRecord {
        return AuthorRecord(name, createdAt)
    }
}
