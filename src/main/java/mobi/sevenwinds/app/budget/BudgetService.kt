package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorEntity
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetCreateRequest): BudgetRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.author = body.authorId?.let { AuthorEntity.findById(it) }
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val allRecords = BudgetEntity.find { BudgetTable.year eq param.year }
                .orderBy(BudgetTable.month to SortOrder.ASC, BudgetTable.amount to SortOrder.DESC)
                .toList()

            val filteredRecords = param.authorName?.let { nameFilter ->
                allRecords.filter { entity ->
                    val authorName = entity.author?.name
                    authorName != null && authorName.contains(nameFilter, ignoreCase = true)
                }
            } ?: allRecords

            val pagedRecords = filteredRecords.drop(param.offset).take(param.limit)

            val sumByType = filteredRecords.groupBy { it.type.name }
                .mapValues { it.value.sumOf { v -> v.amount } }

            return@transaction BudgetYearStatsResponse(
                total = filteredRecords.size,
                totalByType = sumByType,
                items = pagedRecords.map { it.toResponse() }
            )
        }
    }
}