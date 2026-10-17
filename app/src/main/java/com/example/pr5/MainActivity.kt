    package com.example.pr5

    import android.app.DatePickerDialog
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.shadow
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.colorResource
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import kotlinx.coroutines.delay
    import java.util.Calendar

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormBiodataScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FormBiodataScreen() {
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var birthDate by remember { mutableStateOf("") }
        var experience by remember { mutableStateOf("") }

        var androidChecked by remember { mutableStateOf(false) }
        var uiuxChecked by remember { mutableStateOf(false) }
        var webChecked by remember { mutableStateOf(false) }

        var expandedExperience by remember { mutableStateOf(false) }
        val experienceList = listOf("Beginner", "Intermediate", "Expert")

        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                birthDate = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Tombol Proceed aktif (ungu) hanya jika semua data sudah terisi
        val isFormComplete = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank() &&
                birthDate.isNotBlank() &&
                experience.isNotBlank() &&
                (androidChecked || uiuxChecked || webChecked)

        // Pop up hasil submit (otomatis hilang setelah 5 detik)
        var showSuccessDialog by remember { mutableStateOf(false) }

        if (showSuccessDialog) {
            LaunchedEffect(Unit) {
                delay(5000)
                showSuccessDialog = false
            }

            val skillText = buildList {
                if (androidChecked) add("Android")
                if (uiuxChecked) add("UI/UX")
                if (webChecked) add("Web")
            }.ifEmpty { listOf("-") }.joinToString()

            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = "Data Berhasil Dikirim",
                        color = colorResource(id = R.color.purple_title)
                    )
                },
                text = {
                    Text(
                        text = "Nama: $firstName $lastName\n" +
                                "Email: $email\n" +
                                "Tgl Lahir: $birthDate\n" +
                                "Experience: $experience\n" +
                                "Skill: $skillText"
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showSuccessDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 1. Gambar Latar Belakang Banner
                Image(
                    painter = painterResource(id = R.drawable.bg_banner),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                // 2. Floating Card Form
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 220.dp, start = 16.dp, end = 16.dp)
                        .shadow(8.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.card_background)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.form_title),
                            fontSize = 26.sp,
                            color = colorResource(id = R.color.purple_title),
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(id = R.string.form_subtitle),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.text_subtitle)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Row: Nama Depan & Nama Belakang
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                placeholder = { Text(stringResource(id = R.string.hint_first_name)) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                placeholder = { Text(stringResource(id = R.string.hint_last_name)) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Input Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text(stringResource(id = R.string.hint_email)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Row: Tanggal Lahir & Experience
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = birthDate,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = false,
                                    placeholder = { Text(stringResource(id = R.string.hint_birth_date)) },
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = android.R.drawable.ic_menu_my_calendar),
                                            contentDescription = null
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { datePickerDialog.show() }
                                )
                            }

                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = experience,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = false,
                                    placeholder = { Text(stringResource(id = R.string.hint_experience)) },
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = android.R.drawable.arrow_down_float),
                                            contentDescription = null
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { expandedExperience = true }
                                )

                                DropdownMenu(
                                    expanded = expandedExperience,
                                    onDismissRequest = { expandedExperience = false }
                                ) {
                                    experienceList.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item) },
                                            onClick = {
                                                experience = item
                                                expandedExperience = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
