package com.xloger.lawrefbook.ui.lawreader.weight.lawmenu

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xloger.lawrefbook.databinding.DialogMenuBookBinding
import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.entity.LawMenuNode

/**
 * Created on 2022/3/20 16:11.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawMenuDialog(
    context: Context
) : BottomSheetDialog(context) {
    var listener: EventListener? = null

    private lateinit var binding: DialogMenuBookBinding
    val menuAdapter by lazy { LawMenuAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogMenuBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.menuRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
        }
        menuAdapter.setOnItemClickListener { _, _, position ->
            val lawMenuNode = menuAdapter.getItem(position) as LawMenuNode
            listener?.onMenuClick(lawMenuNode.group)
        }
    }

    interface EventListener {
        fun onMenuClick(group: Law.Group)
    }
}