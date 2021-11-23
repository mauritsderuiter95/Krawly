package reponses

class Response(val url: String, val statusCode: Int, val body: ByteArray, ) {
    val responseHeaders: MutableList<String>? = null
    val requestHeaders: MutableList<String>? = null
    val ctx: Map<String, String> = mapOf()
    val err: Error? = null
}