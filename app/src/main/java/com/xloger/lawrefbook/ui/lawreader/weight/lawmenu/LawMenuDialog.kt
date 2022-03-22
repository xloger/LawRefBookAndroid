package com.xloger.lawrefbook.ui.lawreader.weight.lawmenu

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xloger.lawrefbook.databinding.DialogMenuBookBinding
import com.xloger.lawrefbook.repository.entity.Law
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
    private val menuAdapter by lazy { LawMenuAdapter() }

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

    fun syncContainer(law: Law) {
        menuAdapter.setList(tranContainer(law))
    }

    private fun tranContainer(law: Law) : List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        law.group.groupList.forEach { group ->
            list.add(LawMenuNode(group))
        }
        return list
    }

    interface EventListener {
        fun onMenuClick(group: Law.Group)
    }
}