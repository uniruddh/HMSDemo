package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.data.model.Participant
import `in`.uniruddh.hmsdemo.databinding.ParticipantListItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  23/October/2021
 * @Email: uniruddh@gmail.com
 */
class ParticipantAdapter() : RecyclerView.Adapter<ParticipantViewHolder>() {
    private var participantList: List<Participant> = arrayListOf()

    fun refreshList(list: List<Participant>) {
        this.participantList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return participantList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ParticipantListItemBinding.inflate(inflater, parent, false)
        return ParticipantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        participantList[position]?.let {
            holder.bind(it)
        }
    }

    override fun onViewAttachedToWindow(holder: ParticipantViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.startSurfaceView()
    }

    override fun onViewDetachedFromWindow(holder: ParticipantViewHolder) {
        holder.stopSurfaceView()
        super.onViewDetachedFromWindow(holder)
    }
}