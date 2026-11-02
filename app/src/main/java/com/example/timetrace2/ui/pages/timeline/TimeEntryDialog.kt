package com.example.timetrace2.ui.pages.timeline

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.timetrace2.databinding.DialogTimeEntryBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TimeEntryDialog {

    private val fmt = DateTimeFormatter.ofPattern("hh:mm a")

    /** existing == null -> Add mode, otherwise Edit mode (same form). */
    fun create(
        context: Context,
        existing: TimeEntry?,
        onSave: (EntryCategory, String, LocalTime, LocalTime) -> Unit,
        onDismiss: () -> Unit
    ): Dialog {
        val b = DialogTimeEntryBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(b.root)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout((context.resources.displayMetrics.widthPixels * 0.92).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        var category = existing?.category ?: EntryCategory.STUDY
        var start = existing?.start ?: LocalTime.of(9, 0)
        var end = existing?.end ?: LocalTime.of(11, 0)

        b.dialogTitle.text = if (existing == null) "Add Time Entry" else "Edit Time Entry"
        b.saveButton.text = if (existing == null) "Save Entry" else "Save Changes"
        b.titleInput.setText(existing?.title.orEmpty())

        val cats = EntryCategory.values()
        b.categorySpinner.adapter = ArrayAdapter(
            context, android.R.layout.simple_spinner_dropdown_item, cats.map { it.label }
        )
        b.categorySpinner.setSelection(cats.indexOf(category))
        b.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, pos: Int, id: Long) {
                category = cats[pos]
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        fun refreshTimes() {
            b.startField.text = start.format(fmt)
            b.endField.text = end.format(fmt)
        }
        refreshTimes()

        b.startField.setOnClickListener {
            TimePickerDialog(context, { _, h, m -> start = LocalTime.of(h, m); refreshTimes() },
                start.hour, start.minute, false).show()
        }
        b.endField.setOnClickListener {
            TimePickerDialog(context, { _, h, m -> end = LocalTime.of(h, m); refreshTimes() },
                end.hour, end.minute, false).show()
        }

        b.closeButton.setOnClickListener { dialog.dismiss() }
        b.cancelButton.setOnClickListener { dialog.dismiss() }
        b.saveButton.setOnClickListener {
            val title = b.titleInput.text.toString()
            when {
                title.isBlank() -> b.titleInput.error = "Title is required"
                !end.isAfter(start) ->
                    Toast.makeText(context, "End time must be after start time", Toast.LENGTH_SHORT).show()
                else -> onSave(category, title, start, end)
            }
        }

        dialog.setOnDismissListener { onDismiss() }
        return dialog
    }
}