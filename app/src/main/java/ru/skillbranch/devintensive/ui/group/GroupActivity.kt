package ru.skillbranch.devintensive.ui.group

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.databinding.ActivityGroupBinding
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.viewmodels.GroupViewModel
import ru.skillbranch.devintensive.ui.adapters.UserAdapter

class GroupActivity : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: GroupViewModel
    private lateinit var binding: ActivityGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Enter user name"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        userAdapter = UserAdapter { viewModel.handleSelectedItem(it.id) }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        with(binding.rvUserList) {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@GroupActivity)
            addItemDecoration(divider)
        }
        binding.fab.setOnClickListener {
            viewModel.handleCreateGroup()
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
        viewModel.getUserData().observe(this, Observer { userAdapter.updateData(it) })
        viewModel.getSelectedData().observe(this, Observer {
            updateChips(it)
            toggleFab(it.size > 1)
        })
    }

    private fun toggleFab(isShow: Boolean) {
        if (isShow) binding.fab.show() else binding.fab.hide()
    }

    private fun addChipToGroup(user: UserItem) {
        val chip = Chip(this).apply {
            text = user.fullName
            chipIcon = ResourcesCompat
                .getDrawable(context.resources, R.drawable.avatar_default, context.theme)
            isCloseIconVisible = true
            tag = user.id
            isClickable = true
            closeIconTint = ColorStateList.valueOf(Color.WHITE)
            chipBackgroundColor = ColorStateList.valueOf(getColor(R.color.color_primary_light))
            setTextColor(Color.WHITE)
        }
        chip.setOnCloseIconClickListener { viewModel.handleRemoveChip(it.tag.toString()) }
        binding.chipGroup.addView(chip)
    }

    private fun updateChips(listUsers: List<UserItem>) {
        binding.chipGroup.visibility = if (listUsers.isEmpty()) View.GONE else View.VISIBLE

        val users = listUsers.associateBy { user -> user.id }
            .toMutableMap()

        val views = binding.chipGroup.children.associateBy { view -> view.tag }

        for ((k, v) in views) {
            if (!users.containsKey(k)) binding.chipGroup.removeView(v) else users.remove(k)
        }

        users.forEach { (_, v) -> addChipToGroup(v) }
    }
}
