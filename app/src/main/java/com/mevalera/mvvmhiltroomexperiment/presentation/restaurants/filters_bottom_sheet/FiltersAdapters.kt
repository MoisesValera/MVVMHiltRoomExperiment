package com.mevalera.mvvmhiltroomexperiment.presentation.restaurants.filters_bottom_sheet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mevalera.mvvmhiltroomexperiment.R
import com.mevalera.mvvmhiltroomexperiment.data.model.MonthsFilterConfig
import com.mevalera.mvvmhiltroomexperiment.databinding.ItemMonthFilterBinding
import com.mevalera.mvvmhiltroomexperiment.databinding.ItemYearFilterBinding
import timber.log.Timber

class YearFilterAdapter(var context: Context) : RecyclerView.Adapter<YearFilterAdapter.ViewHolder>() {
    var yearFilterAction: ((year: String) -> Unit)? = null
    private var dataList = emptyList<Int>()

    fun setDataList(dataList: List<Int>) {
        this.dataList = dataList
    }

    inner class ViewHolder(private val itemViewBinding: ItemYearFilterBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(data: Int) = with(itemViewBinding) {
            yearTxt.text = data.toString()
            yearTxt.setOnClickListener {
                Timber.d("Filtro year $data")
                yearCard.setCardBackgroundColor(context.getColor(R.color.gray3))
                yearFilterAction?.invoke(data.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            ItemYearFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount() = dataList.size
}

class MonthsFilterAdapter(var context: Context) : RecyclerView.Adapter<MonthsFilterAdapter.ViewHolder>() {
    var monthFilterAction: ((month: String) -> Unit)? = null
    private var dataList = emptyList<MonthsFilterConfig>()

    internal fun setDataList(dataList: List<MonthsFilterConfig>) {
        this.dataList = dataList
    }

    inner class ViewHolder(private val itemViewBinding: ItemMonthFilterBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(data: MonthsFilterConfig) = with(itemViewBinding) {
            monthTxt.text = data.monthStr
            root.setOnClickListener {
                Timber.d("Filtro month ${data.monthInt}")
                monthCard.setCardBackgroundColor(context.getColor(R.color.gray3))
                monthFilterAction?.invoke(data.monthInt.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            ItemMonthFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount() = dataList.size
}