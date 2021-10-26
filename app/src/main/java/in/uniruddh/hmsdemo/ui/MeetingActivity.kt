package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.databinding.ActivityMainBinding
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Author: Aniruddh Bhilvare
 * @Date:  22/October/2021
 * @Email: uniruddh@gmail.com
 */
@AndroidEntryPoint
class MeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MeetingViewModel by viewModels()
    private lateinit var loader: Loader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        loader = Loader(this)

        binding.startMeetButton.setOnClickListener {
            viewModel.getAuthToken()
        }

        binding.endMeetButton.setOnClickListener {
            viewModel.endMeeting()

            binding.startMeetButton.visibility = View.VISIBLE
            binding.endMeetButton.visibility = View.GONE
        }

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        viewModel.userId.value = sharedPreferences.getString("HOST_ID", "")
        viewModel.role.value = sharedPreferences.getString("ROLE", "")
        viewModel.roomId.value = sharedPreferences.getString("ROOM_ID", "")
        viewModel.accessKey.value = sharedPreferences.getString("ACCESS_KEY", "")

        viewModel.onTokenRequested.observe(this, {
            when (it) {
                "SHOW" -> {
                    loader.show()
                }
                "HIDE" -> {
                    loader.dismiss()
                    binding.startMeetButton.visibility = View.GONE
                    binding.endMeetButton.visibility = View.VISIBLE

                    with(sharedPreferences.edit()) {
                        putString("HOST_ID", viewModel.userId.value)
                        putString("ROLE", viewModel.role.value)
                        putString("ROOM_ID", viewModel.roomId.value)
                        putString("ACCESS_KEY", viewModel.accessKey.value)
                        apply()
                    }
                }
                "ERROR" -> {
                    Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
                }
            }
        })
        setupAdapter()
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        } else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                0
            )
        }
    }

    private fun setupAdapter() {
        val participantAdapter = ParticipantAdapter()
        binding.participantList.layoutManager = LinearLayoutManager(this)
        binding.participantList.adapter = participantAdapter
        binding.participantList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.onPeerUpdated.observe(this, {
            participantAdapter.refreshList(viewModel.participantList)
        })
    }

}