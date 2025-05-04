package com.example.bookapitest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookapitest.models.Recommendation
import com.example.bookapitest.ui.theme.BookApiTestTheme

class RecomendationsList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val recViewModel = RecViewModel()
            BookApiTestTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { RecTopBar { finish() }}
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Column {
                            RecInputField(recViewModel)
                            RecBigButton(recViewModel)
                            BookList(recViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecTopBar(onBackClick:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 8.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "Закрытие",
            modifier = Modifier.clickable {
                onBackClick()
            },
            tint = Color.White
        )
        Text(
            text = "Рекомендации",
            color = Color.White
        )
    }
}

@Composable
fun RecInputField(recViewModel: RecViewModel){
    val uiState = recViewModel.uiState.collectAsState()
    OutlinedTextField(
        value = uiState.value.id_text.toString(),
        onValueChange = {recViewModel.updateIdText(it)},
        label = {
            Row{
                Text("ID", color = Color.Black)
                Text("*", color = Color.Red)
            }
        },
        supportingText = if (uiState.value.id_text.toString().isEmpty()) {
            { Text("ID обязателен", color = Color.Red) }
        }else {
            { Text("", color = Color.Red) }
        },
        singleLine = true,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxWidth()
    )
}

@Composable
fun RecBigButton(recViewModel: RecViewModel){
    Button(
        onClick = {
            recViewModel.updateRecList(recViewModel.uiState.value.id_text)
        },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("Добавить пользователя")
    }
}

@Composable
fun BookList(recViewModel: RecViewModel
){
    val uiState = recViewModel.uiState.collectAsState()
    LazyRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(uiState.value.recList){item->
            BookCard(item)
        }
    }
}

@Composable
fun BookCard(book:Recommendation){
    Surface(){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val headers = NetworkHeaders.Builder()
                .set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .build()
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .httpHeaders(headers)
                    .data(book.URL)
                    .build(),
                contentDescription = "Изображение",
                modifier = Modifier.size(height = 240.dp, width = 160.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                onSuccess = { success ->
                    println("Изображение успешно загружено: ${success.result.dataSource}")
                },
                onError = { error ->
                    println("Ошибка загрузки изображения: ${error.result.throwable.message}")
                }
            )
            Text(
                book.Title.substring(0,15)+"...",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                book.ISBN,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    book.Score.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.star),
                    contentDescription = "Звезда",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookApiTestTheme {
        Column {
            RecTopBar({})
            RecInputField(recViewModel = RecViewModel())
            RecBigButton(recViewModel = RecViewModel())
            BookList(recViewModel = RecViewModel())
        }
    }
}