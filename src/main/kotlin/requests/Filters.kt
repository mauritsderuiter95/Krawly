package requests

class Filters {
    var returnBody = true
    var returnHeaders = false
    var breakOnSize = true
    var timeout = 10
    var scrapeOnError = false
    var followRedirects = true
    var blockNonTextPages = true
    var requestHeaders: Map<String, String> = mapOf()
}