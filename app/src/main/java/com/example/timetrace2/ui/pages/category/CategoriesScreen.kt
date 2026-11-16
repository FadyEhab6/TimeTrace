package com.example.timetrace2.ui.pages.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timetrace2.ui.theme.TimeTrace2Theme
import com.example.timetrace2.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(modifier: Modifier = Modifier) {
    val categoryViewModel: CategoryViewModel = viewModel()
    val selectedPeriod by categoryViewModel.selectedPeriod.collectAsState()
    val selectedDateMillis by categoryViewModel.selectedDateMillis.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()

    var isAddDialogVisible by rememberSaveable { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryItemUi?>(null) }
    var deletingCategory by remember { mutableStateOf<CategoryItemUi?>(null) }
    var isCustomDatePickerVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // Header Row: "Categories" + "+ Add Category" button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Categories",
                color = Color(0xFF0F172A),
                fontSize = Typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
            )
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.clickable { isAddDialogVisible = true },
            ) {
                Text(
                    text = "+ Add Category",
                    color = Color(0xFF1E293B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                )
            }
        }

        // Main Card Container (matches Wireframe #9)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Filter by period",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 10.dp),
                )

                CategoryPeriodSelector(
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = { period ->
                        categoryViewModel.selectPeriod(period)
                        if (period == CategoryPeriod.Custom) {
                            isCustomDatePickerVisible = true
                        }
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (categories.isEmpty()) {
                    EmptyCategoriesState(onAddClick = { isAddDialogVisible = true })
                } else {
                    categories.forEachIndexed { index, category ->
                        CategoryRowItem(
                            category = category,
                            onEditClick = { editingCategory = category },
                            onDeleteClick = { deletingCategory = category },
                        )
                        if (index < categories.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 14.dp),
                                color = Color(0xFFF1F5F9),
                                thickness = 1.dp,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // 1. Add Category Popup (Wireframe #11)
    if (isAddDialogVisible) {
        AddEditCategoryDialog(
            initialCategory = null,
            onDismiss = { isAddDialogVisible = false },
            onSave = { name, colorHex, subtitle ->
                categoryViewModel.addCategory(name, colorHex, subtitle)
                isAddDialogVisible = false
            },
        )
    }

    // 2. Edit Category Popup (Wireframe #12)
    editingCategory?.let { categoryToEdit ->
        AddEditCategoryDialog(
            initialCategory = categoryToEdit,
            onDismiss = { editingCategory = null },
            onSave = { name, colorHex, subtitle ->
                categoryViewModel.updateCategory(categoryToEdit.id, name, colorHex, subtitle)
                editingCategory = null
            },
        )
    }

    // 3. Delete Category Confirmation Popup (Wireframe #13)
    deletingCategory?.let { categoryToDelete ->
        DeleteCategoryDialog(
            categoryName = categoryToDelete.name,
            onDismiss = { deletingCategory = null },
            onConfirmDelete = {
                categoryViewModel.deleteCategory(categoryToDelete.id)
                deletingCategory = null
            },
        )
    }

    // 4. Custom Date Picker
    if (isCustomDatePickerVisible) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { isCustomDatePickerVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            categoryViewModel.selectDate(it)
                        }
                        isCustomDatePickerVisible = false
                    }
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { isCustomDatePickerVisible = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun CategoryPeriodSelector(
    selectedPeriod: CategoryPeriod,
    onPeriodSelected: (CategoryPeriod) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CategoryPeriod.entries.forEach { period ->
            val isSelected = period == selectedPeriod
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) Color(0xFF3B6EF6) else Color(0xFFF8FAFC))
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color(0xFF3B6EF6) else Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(20.dp),
                    )
                    .clickable { onPeriodSelected(period) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = period.label,
                    color = if (isSelected) Color.White else Color(0xFF475569),
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun CategoryRowItem(
    category: CategoryItemUi,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Soft colored circle badge
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(category.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(category.color),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = category.name,
                color = Color(0xFF0F172A),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = category.subtitle,
                color = Color(0xFF64748B),
                fontSize = 12.sp,
                maxLines = 1,
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = category.duration,
                color = Color(0xFF0F172A),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit ${category.name}",
                    tint = Color(0xFF64748B),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(onClick = onEditClick),
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ${category.name}",
                    tint = Color(0xFF64748B),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(onClick = onDeleteClick),
                )
            }
        }
    }
}

@Composable
private fun EmptyCategoriesState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "No categories yet",
            color = Color(0xFF0F172A),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Create your first category to get started.",
            color = Color(0xFF64748B),
            fontSize = 13.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B6EF6)),
        ) {
            Text("Add Category", color = Color.White)
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 860)
@Composable
private fun CategoriesScreenPreview() {
    TimeTrace2Theme(darkTheme = false, dynamicColor = false) {
        CategoriesScreen()
    }
}