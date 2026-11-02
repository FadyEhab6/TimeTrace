package com.example.timetrace2.ui.pages.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrace2.databinding.ItemTimeEntryBinding

private object EntryDiff : DiffUtil.ItemCallback<TimeEntry>() {
    override fun areItemsTheSame(a: TimeEntry, b: TimeEntry) = a.id == b.id
    override fun areContentsTheSame(a: TimeEntry, b: TimeEntry) = a == b
}

class EntryAdapter(
    private val onClick: (TimeEntry) -> Unit
) : ListAdapter<TimeEntry, EntryAdapter.VH>(EntryDiff) {

    class VH(val binding: ItemTimeEntryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemTimeEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = getItem(position)
        with(holder.binding) {
            entryTitle.text = e.title
            entrySubtitle.text = "${e.category.label}  •  ${e.shortRange()}"
            entryDuration.text = e.durationLabel()
            entryIcon.setImageResource(e.category.iconRes)
            entryIcon.setColorFilter(e.category.colorInt)
            iconBox.background.mutate().setTint(ColorUtils.setAlphaComponent(e.category.colorInt, 38))
            rowRoot.setOnClickListener { onClick(e) }
        }
    }
}