package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.data.model.Participant
import `in`.uniruddh.hmsdemo.databinding.ParticipantListItemBinding
import androidx.recyclerview.widget.RecyclerView
import live.hms.video.utils.SharedEglContext
import org.webrtc.RendererCommon

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  23/October/2021
 * @Email: uniruddh@gmail.com
 */
class ParticipantViewHolder(
    private val binding: ParticipantListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var sinkAdded = false
    private lateinit var participant: Participant

    init {
        binding.previewView.apply {
            setEnableHardwareScaler(true)
            setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        }
    }

    fun bind(participant: Participant) {
        this.participant = participant
        binding.name.text = participant.name
    }

    fun startSurfaceView() {
        if (!sinkAdded) {
            binding.previewView.apply {
                participant?.hmsPeer?.videoTrack?.let {
                    init(SharedEglContext.context, null)
                    it.addSink(this)
                    sinkAdded = true
                }
            }
        }
    }

    fun stopSurfaceView() {
        binding.previewView.apply {
            if (sinkAdded) {
                participant?.hmsPeer?.videoTrack?.let {
                    it.removeSink(this)
                    release()
                    sinkAdded = false
                }
            }
        }
    }
}