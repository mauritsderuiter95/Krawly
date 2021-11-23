package requests

class Request(val url: String) {
    var filters: Filters = Filters()
    var ctx: Map<String, String> = mapOf()

    constructor(url: String, filters: Filters) : this(url) {
        this.filters = filters
    }

    constructor(url: String, filters: Filters, ctx: Map<String, String>) : this(url, filters) {
        this.ctx = ctx
    }
}