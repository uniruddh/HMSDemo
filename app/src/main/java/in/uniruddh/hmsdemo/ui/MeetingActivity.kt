package `in`.uniruddh.hmsdemo.ui

import `in`.uniruddh.hmsdemo.databinding.ActivityMainBinding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.startMeetButton.setOnClickListener {
            viewModel.getAuthToken()

            binding.startMeetButton.visibility = View.GONE
            binding.endMeetButton.visibility = View.VISIBLE
        }

        binding.endMeetButton.setOnClickListener {
            viewModel.endMeeting()

            binding.startMeetButton.visibility = View.VISIBLE
            binding.endMeetButton.visibility = View.GONE
        }

        requestCameraPermission()
        requestMicPermission()
        setupAdapter()
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

        viewModel.peerUpdate.observe(this, {
            participantAdapter.refreshList(viewModel.participantList)
        })
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {

            }

            else -> {

                registerForActivityResult(ActivityResultContracts.RequestPermission()) {}.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun requestMicPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            ) -> {

            }

            else -> {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {}.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            }
        }
    }

}