package mobi.sevenwinds.app.author

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.budget.AuthorCreateRequest
import mobi.sevenwinds.app.budget.AuthorRecord
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.LocalDateTime

object AuthorService {
    suspend fun addAuthor(body: AuthorCreateRequest): AuthorRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = AuthorEntity.new {
                this.name = body.name
                this.createdAt = LocalDateTime.now().toDateTime()
            }
            return@transaction entity.toResponse()
        }
    }
}