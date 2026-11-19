package com.example.timetrace2.ui.pages.timeline

import android.app.AlertDialog
import android.app.Dialog
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timetrace2.databinding.TimelineDetailsBinding
import com.example.timetrace2.databinding.TimelineScreenBinding
import androidx.compose.ui.tooling.preview.Preview
@Preview(showBackground = true)
@Composable
fun TimelineScreenPreview() {
    TimelineScreen()
}
@Composable
fun TimelineScreen(viewModel: TimelineViewModel = viewModel()) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val selected by viewModel.selectedEntry.collectAsStateWithLifecycle()
    val dialogMode by viewModel.dialog.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // System back from the details page returns to the list
    BackHandler(enabled = selected != null) { viewModel.closeDetails() }

    val entry = selected
    if (entry == null) {
        val adapter = remember { EntryAdapter { viewModel.openDetails(it.id) } }
        AndroidViewBinding(TimelineScreenBinding::inflate) {
            if (timelineList.adapter == null) timelineList.adapter = adapter
            addButton.setOnClickListener { viewModel.openAddDialog() }
            adapter.submitList(entries)
        }
    } else {
        AndroidViewBinding(TimelineDetailsBinding::inflate) {
            detailTitle.text = entry.title
            detailCategory.text = entry.category.name
            detailTime.text = entry.longRange()
            detailDuration.text = entry.durationLabel()
            detailSource.text = entry.source
            detailIcon.setImageResource(entry.category.iconRes)
            detailIcon.setColorFilter(entry.category.colorInt)
            detailIconBox.background.mutate()
                .setTint(ColorUtils.setAlphaComponent(entry.category.colorInt, 38))

            backButton.setOnClickListener { viewModel.closeDetails() }
            editButton.setOnClickListener { viewModel.openEditDialog(entry) }
            deleteButton.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Delete entry?")
                    .setMessage("\"${entry.title}\" will be removed.")
                    .setPositiveButton("Delete") { _, _ -> viewModel.deleteEntry(entry.id) }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    // Add / Edit popup (same XML form)
    DisposableEffect(dialogMode) {
        var dialog: Dialog? = null
        when (val mode = dialogMode) {
            DialogMode.Hidden -> {}
            else -> {
                dialog = TimeEntryDialog.create(
                    context = context,
                    existing = (mode as? DialogMode.Edit)?.entry,
                    categories = categories,
                    onSave = viewModel::saveEntry,
                    onDismiss = viewModel::closeDialog
                ).also { it.show() }
            }
        }
        onDispose {
            dialog?.setOnDismissListener(null)
            dialog?.dismiss()
        }
    }
}