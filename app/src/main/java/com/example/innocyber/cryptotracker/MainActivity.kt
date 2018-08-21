package com.example.innocyber.cryptotracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.innocyber.cryptotracker.Adapter.CoinAdapter
import com.example.innocyber.cryptotracker.Common.Common
import com.example.innocyber.cryptotracker.Interface.LoadCoin
import com.example.innocyber.cryptotracker.Model.CoinModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(), LoadCoin {

    internal var items:MutableList<CoinModel> = ArrayList()
    internal lateinit var adapter: CoinAdapter
    internal lateinit var client: OkHttpClient
    internal lateinit var request: Request

    override fun onLoadCoin() {
        if(items.size <= Common.MAX_COIN_LOAD)
            loadNextTenCoin(items.size)
        else
            Toast.makeText(this@MainActivity,"Max coin should be " + Common.MAX_COIN_LOAD,Toast.LENGTH_SHORT).show()
    }

    private fun loadNextTenCoin(index: Int) {

        client = OkHttpClient()
        request = Request.Builder().url(String.format("https://api.coinmarketcap.com/v1/ticker/?start=%d&limit=10",index)).build()

        swipe_refresh_layout.isRefreshing=true
        client.newCall(request)
                .enqueue(object : Callback
                {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.d("ERROR",e.toString())
                    }

                    override fun onResponse(call: Call?, response: Response) {
                        val body = response.body()!!.string()
                        val gson= Gson()
                        val newItems = gson.fromJson<List<CoinModel>>(body,object: TypeToken<List<CoinModel>>(){}.type)
                        runOnUiThread {
                            items.addAll(newItems)
                            adapter.setLoaded()
                            adapter.updateData(items)

                            swipe_refresh_layout.isRefreshing=false
                        }
                    }
                })

    }

    private fun loadFirstTenCoin() {
        client = OkHttpClient()
        request = Request.Builder()
                .url(String.format("https://api.coinmarketcap.com/v1/ticker/?start=0&limit=10"))
                .build()


        client.newCall(request)
                .enqueue(object :Callback
                {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.d("ERROR",e.toString())
                    }

                    override fun onResponse(call: Call?, response: Response) {
                        val body = response.body()!!.string()
                        val gson=Gson()
                        items = gson.fromJson(body,object:TypeToken<List<CoinModel>>(){}.type)
                        runOnUiThread {
                            adapter.updateData(items)


                        }
                    }
                })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_refresh_layout.post { loadFirstTenCoin() }

        swipe_refresh_layout.setOnRefreshListener {
            items.clear()
            loadFirstTenCoin()
            setUpAdapter()
        }

        coin_recycler_view.layoutManager = LinearLayoutManager(this)
        setUpAdapter()
    }

private fun setUpAdapter() {
    adapter = CoinAdapter(coin_recycler_view,this@MainActivity,items)
    coin_recycler_view.adapter = adapter
    adapter.setLoadCoin(this)

    }
}
