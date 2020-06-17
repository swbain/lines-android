package com.stephenbain.lines.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stephenbain.lines.api.TopicJson
import com.stephenbain.lines.databinding.FragmentHomeBinding
import com.stephenbain.lines.databinding.ListItemTopicBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = HomeAdapter()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.loading().observe(viewLifecycleOwner) { binding.loading.isVisible = it }
        viewModel.topics().observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private class HomeAdapter : ListAdapter<TopicJson, HomeViewHolder>(DIFF_CALLBACK) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
            val binding = ListItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HomeViewHolder(binding)
        }

        override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TopicJson>() {
                override fun areItemsTheSame(oldItem: TopicJson, newItem: TopicJson): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: TopicJson, newItem: TopicJson): Boolean {
                    return oldItem == newItem
                }

            }
        }
    }

    private class HomeViewHolder(private val binding: ListItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: TopicJson) {
            binding.title.text = topic.title
        }
    }
}
