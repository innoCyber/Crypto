package com.example.innocyber.cryptotracker.Model

 class CoinModel{
    var id:String? = null
    var name:String? = null
    var price_in_dollars:String? = null
    var symbol:String? = null
    var percentage_change_1hr:String? = null
    var percentage_change_24hr:String? = null
    var percentage_change_7d:String? = null

    constructor(){}
     constructor(id: String, name: String, price_in_dollars: String, symbol: String, percentage_change_1hr: String, percentage_change_24hr: String, percentage_change_7d: String) {
         this.id = id
         this.name = name
         this.price_in_dollars = price_in_dollars
         this.symbol = symbol
         this.percentage_change_1hr = percentage_change_1hr
         this.percentage_change_24hr = percentage_change_24hr
         this.percentage_change_7d = percentage_change_7d
     }
 }