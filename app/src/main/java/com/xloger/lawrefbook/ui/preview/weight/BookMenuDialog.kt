package com.xloger.lawrefbook.ui.preview.weight

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xloger.lawrefbook.databinding.DialogMenuBookBinding
import com.xloger.lawrefbook.repository.entity.Doc
import com.xloger.lawrefbook.repository.entity.LawRefContainer
import com.xloger.lawrefbook.ui.preview.weight.entity.GroupNode
import com.xloger.lawrefbook.ui.preview.weight.entity.ItemNode
import com.xloger.lawrefbook.util.XLog

/**
 * Created on 2022/3/20 16:11.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class BookMenuDialog(
    context: Context
) : BottomSheetDialog(context) {
    var listener: EventListener? = null

    private lateinit var binding: DialogMenuBookBinding
    private val menuAdapter by lazy { BookMenuAdapter({ item -> listener?.onItemClick(item.doc) }) }

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
    }

    fun syncContainer(lawRefContainer: LawRefContainer) {
        menuAdapter.setList(tranContainer(lawRefContainer))
    }

    private fun tranContainer(lawRefContainer: LawRefContainer) : List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        lawRefContainer.groupList.forEach { group ->
            list.add(GroupNode(group.tag, group.docList.map { ItemNode(it) }))
        }
        return list
    }

    interface EventListener {
        fun onItemClick(doc: Doc)
    }
}