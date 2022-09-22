
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class User( val id:Int? = null, val name: String, val age:Int)

object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name",200)
    val age: Column<Int> = integer("age")

    override val primaryKey = PrimaryKey(id, name="PK_user_id")

    fun toUser(row: ResultRow):User=
        User(
            id=row[Users.id],
            name=row[Users.name],
            age=row[Users.age]
        )
}
fun main(args: Array<String>) {

    Database.connect("jdbc:postgresql://localhost/test", driver = "org.postgresql.Driver",
        user = "postgres", password = "password")

    //Insertion of data into table
    transaction {
        SchemaUtils.create(Users)

        Users.insert {
            it[Users.name] = "Ayush"
            it[Users.age] = 25
        }

        Users.insert {
            it[Users.name] = "Pakhi"
            it[Users.age] = 20
        }

        Users.insert {
            it[Users.name] = "Jackie"
            it[Users.age] = 30
        }

        // update data
        Users.update ({ Users.id eq 30}) {
            it[name] = "Captain Marvels"
        }

    }


    //Retrieve Data from table
    val users = transaction {
        Users.selectAll().map { Users.toUser(it) }
    }

    embeddedServer(Netty,8080){
        routing {
            get("/") {
                call.respond("Hello World!")
         }
        }
    }.start(wait = true)
}