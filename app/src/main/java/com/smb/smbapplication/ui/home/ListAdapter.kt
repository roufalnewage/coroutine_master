package com.smb.smbapplication.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smb.smbapplication.AppExecutors
import com.smb.smbapplication.R
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.ui.BaseListAdapter

class ListAdapter(appExecutors: AppExecutors, private val itemClickCallback: ((Integer) -> Unit)?) : BaseListAdapter<User, ListAdapter.DataHolder>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id && oldItem.name.equals(newItem.name)
            }


        }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)

        return DataHolder(view)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class DataHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.tv_tab)

        fun bind(item :User){
            textView.text = item.name
        }
    }
}
