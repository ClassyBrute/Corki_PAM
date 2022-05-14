package com.example.corki.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corki.R
import com.example.corki.adapters.PostsAdapter
import com.example.corki.databinding.FragmentSearchBinding
import com.example.corki.models.post.Post
import com.example.corki.viewmodel.PostViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var _recyclerView: RecyclerView? = null
    private val recyclerView get() = _recyclerView

    private var firstJson: String = ""
    private var secondJson: String = ""

    //POSTS
    private lateinit var postViewModel: PostViewModel
    private var postsList = emptyList<Post>()

    //FILTERS
    private var map = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _recyclerView = binding.recyclerSearch
        recyclerView?.layoutManager = LinearLayoutManager(this.context)

        binding.dateTime1.hint = DateFormat.getDateInstance().format(Date())
        binding.dateTime1.setOnClickListener { showDatePicker() }

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        postViewModel.postViewModel()
        observePostViewModel()

        binding.floatingActionButton.setOnClickListener {
            initiateSearch()
        }

        return root
    }

    private fun observePostViewModel() {
        postViewModel.getPostsWithQuery(map).observe(viewLifecycleOwner) { data ->
            data.posts.let {
                postsList = it
            }
            val adapter = PostsAdapter(postsList)
            recyclerView?.adapter = adapter
        }
    }

    private fun initiateSearch() {
        map = mutableMapOf<String, String>()

        if (!binding.menuSubjectItem.text.isNullOrEmpty()) {
            map["subjects"] = binding.menuSubjectItem.text.toString().lowercase()
        }
        if (!binding.menuLevelItem.text.isNullOrEmpty()) {
            map["level"] = binding.menuLevelItem.text.toString().lowercase()
        }
        if (!binding.menuCityItem.text.isNullOrEmpty()) {
            map["cities"] = binding.menuCityItem.text.toString()
        }
        if (!binding.maxPriceEdit.text.isNullOrEmpty()) {
            map["price"] = binding.maxPriceEdit.text.toString()
        }
        if (!binding.dateTime1.text.isNullOrEmpty()) {
            map["dateFrom"] = firstJson
            map["dateTo"] = secondJson
        }

        postViewModel.getPostsWithQuery(map)
    }

    private fun showDatePicker() {
        val dateRangePicker: MaterialDatePicker<Pair<Long, Long>>?
        var dateRange: Pair<Long, Long>?

        dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
            .build()

        dateRangePicker.show(parentFragmentManager, "MATERIAL_DATE_PICKER")

        dateRangePicker.addOnPositiveButtonClickListener {
            //TODO TIME PICKER MAYBE
            dateRange = Pair(dateRangePicker.selection?.first, dateRangePicker.selection?.second)
            val first = DateFormat.getDateInstance().format(dateRange?.first)
            firstJson = "${SimpleDateFormat("yyyy-MM-dd").format(Date(dateRange!!.first))}T00:00:00Z"
            val second = DateFormat.getDateInstance().format(dateRange?.second)
            secondJson = "${SimpleDateFormat("yyyy-MM-dd").format(Date(dateRange!!.second))}T23:59:59Z"

            val date = "$first - $second"
            binding.dateTime1.setText(date)
        }
    }

    override fun onResume() {
        super.onResume()
        val subjects = resources.getStringArray(R.array.subjects)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.search_item, subjects)
        binding.menuSubjectItem.setAdapter(arrayAdapter1)

        val levels = resources.getStringArray(R.array.level)
        val arrayAdapter2 = ArrayAdapter(requireContext(), R.layout.search_item, levels)
        binding.menuLevelItem.setAdapter(arrayAdapter2)

        val cities = resources.getStringArray(R.array.city)
        val arrayAdapter3 = ArrayAdapter(requireContext(), R.layout.search_item, cities)
        binding.menuCityItem.setAdapter(arrayAdapter3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}