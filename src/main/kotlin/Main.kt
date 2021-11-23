import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking

fun readCsv(k: Krawly) {
    runBlocking {
        csvReader().open("src/main/resources/tmp/input.csv") {
            readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
                launch {
                    k.addUrl(row["domain"]!!)
                }
            }
        }
    }
}

fun main() {
    runBlocking {
        val k = Krawly()

        k.start()

        readCsv(k)

        k.finish()
    }
}