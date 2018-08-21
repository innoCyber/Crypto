package com.example.innocyber.cryptotracker.Adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.innocyber.cryptotracker.Common.Common
import com.example.innocyber.cryptotracker.Interface.LoadCoin
import com.example.innocyber.cryptotracker.Model.CoinModel
import com.example.innocyber.cryptotracker.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*

class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var coinIcon = itemView.coin_icon
    var coinSymbol = itemView.coin_symbol
    var coinName = itemView.coin_name
    var coinPrice = itemView.price_in_dollars
    var onHourValueChange = itemView.one_hour
    var tewntyFourHoursValueChange = itemView.twenty_four_hours
    var sevenDaysValueChange = itemView.seven_days
}
class CoinAdapter(recyclerView: RecyclerView, internal  var activity: Activity,var items: List<CoinModel>) : RecyclerView.Adapter<CoinViewHolder>() {

    internal var loadCoin:LoadCoin? =  null
    var isLoading:Boolean = false
    var visisbleThreshold = 5
    var lastVisibleItem:Int = 0
    var totalItemCount:Int = 0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if(!isLoading && totalItemCount <= lastVisibleItem + visisbleThreshold){

                    if(loadCoin != null){
                        loadCoin!!.onLoadCoin()
                        isLoading = true
                    }
                }
            }
        })
    }

    fun setLoadCoin(loadCoin: LoadCoin){

        this.loadCoin = loadCoin
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(activity)
                .inflate(R.layout.coin_layout,parent,false)
        return CoinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coinModel = items.get(position)
        val item = holder as CoinViewHolder

        item.coinName.text = coinModel.name
        item.coinSymbol.text = coinModel.symbol
        item.coinPrice.text = coinModel.price_in_dollars
        item.onHourValueChange.text = coinModel.percentage_change_1hr + "%"
        item.tewntyFourHoursValueChange.text = coinModel.percentage_change_24hr + "%"
        item.sevenDaysValueChange.text = coinModel.percentage_change_7d + "%"

        Picasso.with(activity.baseContext)
                .load(StringBuilder(Common.imageUrl)
                        .append(coinModel.symbol!!.toLowerCase())
                        .append(".png")
                        .toString())

        item.onHourValueChange.setTextColor(if(coinModel.percentage_change_1hr!!.contains("-"))
        Color.parseColor("#FF0000")
        else
        Color.parseColor("#32CD32"))

        item.tewntyFourHoursValueChange.setTextColor(if(coinModel.percentage_change_24hr!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32"))

        item.sevenDaysValueChange.setTextColor(if(coinModel.percentage_change_7d!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32"))

    }

    fun setLoaded(){
        isLoading = false
    }

    fun updateData(coinModels: List<CoinModel>){
        this.items = coinModels
        notifyDataSetChanged()
    }

}