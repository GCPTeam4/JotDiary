package gcp.global.jotdiary.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gcp.global.jotdiary.R
import gcp.global.jotdiary.model.models.Diaries
import gcp.global.jotdiary.model.repository.Resources
import gcp.global.jotdiary.view.components.audio.coilImage
import gcp.global.jotdiary.view.components.bottomBars.BottomNavigationHome
import gcp.global.jotdiary.viewmodel.HomeUiState
import gcp.global.jotdiary.viewmodel.HomeViewModel

@Composable
fun Home(
    homeViewModel: HomeViewModel?,
    onDiaryClick: (id: String) -> Unit,
    onNavToDiaryPage: () -> Unit,
    onNavToDiaryEditPage: (id: String) -> Unit,
    onNavToLoginPage: () -> Unit,
    onNavToSettingsPage: () -> Unit,
    onNavToCalenderPage: () -> Unit,
) {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()

    var editDiaryDialog by remember {
        mutableStateOf(false)
    }

    var selectedDiary: Diaries? by remember {
        mutableStateOf(null)
    }

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit){
        homeViewModel?.loadDiaries()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavToDiaryPage.invoke() },
                backgroundColor = MaterialTheme.colors.onSurface,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .width(65.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.final_logo),
                            contentDescription = "JotDiary Logo",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(40.dp)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .width(65.dp)
                    ) {
                        IconButton(onClick = {
                            homeViewModel?.signOut()
                            onNavToLoginPage.invoke()
                        }) {
                            Column {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onSurface,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = "Sign Out",
                                    color = MaterialTheme.colors.onSurface,
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "JotDiary",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1,
                        fontSize = 20.sp,
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
        },
        bottomBar = { BottomNavigationHome(navToSettingsScreen = onNavToSettingsPage, navToCalenderScreen = onNavToCalenderPage) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (homeUiState.diariesList) {

                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }

                is Resources.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(4.dp),
                    ) {
                        items(
                            homeUiState.diariesList.data ?: emptyList()
                        ) { diary ->
                            DiaryItem(
                                diaries = diary,
                                onLongClick = {
                                    editDiaryDialog = true
                                    selectedDiary = diary
                                },
                                onClick = {
                                    onDiaryClick.invoke(diary.diaryId)
                                },
                                onDiaryEditClick = {
                                    onNavToDiaryEditPage.invoke(diary.diaryId)
                                }
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = editDiaryDialog
                    ) {
                        AlertDialog(
                            onDismissRequest = {
                                editDiaryDialog = false
                            },
                            title = { Text(
                                text = "Delete this Diary?",
                                color = MaterialTheme.colors.onSurface,
                            ) },
                            backgroundColor = MaterialTheme.colors.primary,
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedDiary?.diaryId?.let {
                                            homeViewModel?.deleteDiary(it)
                                        }
                                        editDiaryDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.onSurface
                                    ),
                                ) {
                                    Text(
                                        text = "Delete",
                                        color = MaterialTheme.colors.surface
                                    )
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { editDiaryDialog = false },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.surface
                                    ),
                                ) {
                                    Text(
                                        text = "Cancel",
                                        color = MaterialTheme.colors.onSurface
                                    )
                                }
                            }
                        )
                    }
                }

                is Resources.Failure -> {
                    Text(
                        text = homeUiState
                            .diariesList.throwable?.localizedMessage ?: "Unknown Error",
                        color = Color.Black
                    )

                }

            }
        }
    }

    LaunchedEffect(key1 = homeViewModel?.hasUser){
        if (homeViewModel?.hasUser == false){
            onNavToLoginPage.invoke()
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiaryItem(
    diaries: Diaries,
    onDiaryEditClick: () -> Unit,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth()
            .height(400.dp),
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Text(
                    text = diaries.diaryTitle,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${diaries.diaryCreatedDate.toDate().date}/${diaries.diaryCreatedDate.toDate().month}/${diaries.diaryCreatedDate.toDate().year.plus(1900)}",
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                    )

                    IconButton(
                        onClick = { onDiaryEditClick.invoke() },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Diary",
                            tint = MaterialTheme.colors.onSurface,
                        )
                    }

                }
            }
            
            Divider(
                color = MaterialTheme.colors.onSurface,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            coilImage(
                Url = diaries.imageUrl,
                Modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp),
                Shape = MaterialTheme.shapes.small.copy(all = CornerSize(0.dp))
            )

            Divider(
                color = MaterialTheme.colors.onSurface,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = diaries.diaryDescription,
                style = MaterialTheme.typography.body1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(8.dp)
            )
            
        }
    }
}
