import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import reponses.Response
import requests.Request

class Krawly {
    var routineCount = 24
    set(value) {
        if (isRunning) {
            throw Exception("Can't adjust threads if scraper is already running")
        }
        field = value
    }

    var dnsServer = "1.1.1.1"
    var inputStream = Channel<Request>(100)
    var outputStream: Channel<Response> = Channel(100)
    var errorStream: Channel<Response>? = Channel(100)
    var visitedUrls: Map<String, Boolean>? = null

    private var isRunning = false
    private var runningRoutines: MutableList<Job> = mutableListOf()

    suspend fun addUrl(url: String) {
        val req = Request(url)
        inputStream.send(req)
    }

    suspend fun start() {
        for (i in 0..routineCount) {
            val routine = GlobalScope.launch (Dispatchers.IO) {
                val client = HttpClient(CIO)
                for (msg in inputStream) {
                    val response: HttpResponse = client.get("https://${msg.url}")
                    val body = response.readBytes()
                    val res = Response(msg.url, response.status.value, body)
                    outputStream.send(res)
                }
            }
            runningRoutines.add(routine)
        }
    }

    suspend fun onResponse(callback: (Response) -> Unit) {
        for (i in 0..Runtime.getRuntime().availableProcessors()) {
            val routine = GlobalScope.launch (Dispatchers.Default) {
                for (msg in outputStream) {
                    callback(msg)
                }
            }
            runningRoutines.add(routine)
        }
    }

    fun finish() {
        inputStream.close()
        runBlocking {
            for (routine in runningRoutines) {
                routine.join()
            }
        }
    }
}