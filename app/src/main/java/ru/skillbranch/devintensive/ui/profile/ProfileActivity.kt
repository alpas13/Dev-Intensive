package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.databinding.ActivityProfileBinding
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.TextBitmapBuilder
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>
    private var userInitials: String? = null
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews(savedInstanceState)
        initViewModel()
        Log.d("M_ProfileActivity", "onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
        viewModel.getRepositoryError().observe(this, Observer { updateRepoError(it) })
        viewModel.getIsRepoError().observe(this, Observer { updateRepository(it) })
    }

    private fun updateRepository(isError: Boolean) {
        if (isError) binding.etRepository.text.clear()
    }

    private fun updateRepoError(isError: Boolean) {
        binding.wrRepository.isErrorEnabled = isError
        binding.wrRepository.error = if (isError) "Невалидный адрес репозитория" else null
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "updateTheme")
        delegate.localNightMode = mode
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }

        updateAvatar(profile)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to binding.tvNickName,
            "rank" to binding.tvRank,
            "firstName" to binding.etFirstName,
            "lastName" to binding.etLastName,
            "about" to binding.etAbout,
            "repository" to binding.etRepository,
            "rating" to binding.tvRating,
            "respect" to binding.tvRespect,
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        binding.btnEdit.setOnClickListener {
            viewModel.onRepoEditCompleted(binding.wrRepository.isErrorEnabled)

            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        binding.etRepository.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                viewModel.onRepositoryChanged(s.toString())
            }
        })

        binding.btnSwitchTheme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter {
            setOf(
                "firstName",
                "lastName",
                "about",
                "repository"
            ).contains(it.key)
        }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
        }

        binding.icEye.visibility = if (isEdit) View.GONE else View.VISIBLE
        binding.wrAbout.isCounterEnabled = isEdit

        with(binding.btnEdit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_baseline_save_24,
                    context.theme
                )
            } else {
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_baseline_edit_24,
                    context.theme
                )
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile(
            firstName = binding.etFirstName.text.toString(),
            lastName = binding.etLastName.text.toString(),
            about = binding.etAbout.text.toString(),
            repository = binding.etRepository.text.toString(),
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    private fun updateAvatar(profile: Profile){
        Utils.toInitials(profile.firstName, profile.lastName)?.let {
            if (it != userInitials) {
                val avatar = getAvatarBitmap(it)
                binding.ivAvatar.setImageBitmap(avatar)
            }
        } ?: binding.ivAvatar.setImageResource(R.drawable.avatar_default)
    }

    private fun getAvatarBitmap(text: String): Bitmap {
        val color = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, color, true)

        return TextBitmapBuilder(binding.ivAvatar.layoutParams.width, binding.ivAvatar.layoutParams.height)
            .setBackgroundColor(color.data)
            .setText(text)
            .setTextSize(Utils.convertSpToPx(this, 48))
            .setTextColor(Color.WHITE)
            .build()
    }
}
