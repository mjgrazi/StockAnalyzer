package stockanalyzer

class Stock {

    static scaffold = true
//    static hasMany = [quotes:BasicQuote]
    String code
    String name

    static constraints = {
        code(blank: false)
        name(blank: false)
    }
}
