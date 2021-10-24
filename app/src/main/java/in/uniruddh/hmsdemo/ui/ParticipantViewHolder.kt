package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.MainApplication
import `in`.uniruddh.hmsdemo.data.model.Participant
import `in`.uniruddh.hmsdemo.databinding.ParticipantListItemBinding
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import live.hms.video.utils.SharedEglContext
import org.webrtc.RendererCommon

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  23/October/2021
 * @Email: uniruddh@gmail.com
 */
class ParticipantViewHolder(
    private val binding: ParticipantListItemBinding,
    private val getItem: (Int) -> Participant?
) : RecyclerView.ViewHolder(binding.root) {

    private var sinkAdded = false

    init {
        binding.previewView.apply {
            setEnableHardwareScaler(true)
            setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        }
    }

    fun bind(participant: Participant) {
        binding.name.text = participant.name
        if (!sinkAdded) {
            binding.previewView.apply {
                setEnableHardwareScaler(true)
                setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
                sinkAdded = false
            }
        }
    }

    fun startSurfaceView() {
        if (!sinkAdded) {
            binding.previewView.apply {

                getItem(adapterPosition)?.hmsPeer?.videoTrack?.let {
                    init(SharedEglContext.context, null)
                    it.addSink(this)
                    sinkAdded = true
                }
            }
        }
    }

    fun stopSurfaceView() {
        binding.previewView.apply {
            if (sinkAdded && adapterPosition != -1) {
                getItem(adapterPosition)?.hmsPeer?.videoTrack?.let {
                    it.removeSink(this)
                    release()
                    sinkAdded = false
                }
            }
        }
    }
}