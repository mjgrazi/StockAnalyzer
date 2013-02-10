package stockanalyzer

//import groovyx.net.http.HTTPBuilder

class ExchangeList {

    static scaffold = true
    List exchanges = new ArrayList()
    static hasMany = [exchanges: Exchange]

    String name

    static constraints = {
    }
}
