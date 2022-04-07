package com.xloger.lawrefbook.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xloger.lawrefbook.BuildConfig
import com.xloger.lawrefbook.databinding.AboutFragmentBinding
import com.xloger.lawrefbook.util.customStyle
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : Fragment() {

    private var _binding: AboutFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AboutViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AboutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolBar()
    }

    @SuppressLint("ResourceType")
    private fun initView() {
        binding.aboutLawContent.apply {
            val spannable = SpannableString(text)
            spannable.customStyle(spannable, text.toString(), 0, "国家法律法规数据库（https://flk.npc.gov.cn）") {
                return@customStyle object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://flk.npc.gov.cn")).let {
                            startActivity(it)
                        }
                    }

                }
            }
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
        }
        binding.aboutAppContent.apply {
            val spannable = SpannableString(text)
            spannable.customStyle(spannable, text.toString(), 0, "https://github.com/LawRefBook/Laws") {
                return@customStyle object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/LawRefBook/Laws")).let {
                            startActivity(it)
                        }
                    }

                }
            }
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
        }
        binding.aboutVersion.apply {
            text = "当前版本：V${BuildConfig.VERSION_NAME}"
        }
        binding.aboutCheckUpdate.apply {
            setOnClickListener {
                Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/xloger/LawRefBookAndroid/releases")).let {
                    startActivity(it)
                }
            }
        }
        binding.aboutUpdateContent.apply {
            val spannable = SpannableString(text)
            spannable.customStyle(spannable, text.toString(), 0, "Github") {
                return@customStyle object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/xloger/LawRefBookAndroid")).let {
                            startActivity(it)
                        }
                    }

                }
            }
            spannable.customStyle(spannable, text.toString(), 0, "Google Play") {
                return@customStyle object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.xloger.lawrefbook")).let {
                            startActivity(it)
                        }
                    }

                }
            }
            text = spannable
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun initToolBar() {
        binding.aboutToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}