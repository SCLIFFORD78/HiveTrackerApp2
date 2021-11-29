package org.wit.hivetrackerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.hivetrackerapp.databinding.CardHiveBinding
import org.wit.hivetrackerapp.models.HiveModel


class HiveTrackerAdapter(private var hives: List<HiveModel>,
                        private val listener: OnHiveClickListener
                         ) :
    RecyclerView.Adapter<HiveTrackerAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardHiveBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hive = hives[holder.adapterPosition]
        holder.bind(hive)

    }

    override fun getItemCount(): Int = hives.size

    inner class MainHolder(private val binding : CardHiveBinding) :
        RecyclerView.ViewHolder(binding.root) ,View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onHiveClick(position)
            }
        }


        fun bind(hive: HiveModel) {
            ("Tag Number :"+hive.tag.toString()).also { binding.hiveTitle.text = it }
            binding.type.text = hive.type
            binding.hiveImage.setImageURI(hive.image)
            hive.description.also { binding.description.text = it }
        }

    }
    interface OnHiveClickListener{
        fun onHiveClick(position: Int)
    }

    interface Communicator {

        fun passDataCom(hive:HiveModel)

    }


}

