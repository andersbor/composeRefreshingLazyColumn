package com.example.refreshinglazycolumn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.refreshinglazycolumn.ui.theme.RefreshingLazyColumnTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RefreshingLazyColumnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Lists(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// library dependency
// at the bottom of the dependencies in the build.gradle.kts file (app)
@OptIn(ExperimentalMaterial3Api::class) // PullToRefreshBox
@Composable
fun Lists(modifier: Modifier = Modifier) {
    var itemList by remember { mutableStateOf(listOf(1, 2, 3, 4, 5)) }

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val random = Random.Default

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                itemList = itemList.plus(random.nextInt(100))
            }) {
                Text("Add item")
            }
            Button(onClick = {
                itemList = itemList.dropLast(1)
            }) {
                Text("Remove item")
            }
        }

        // https://composables.com/material3/pulltorefreshbox
        // library dependency
        // at the bottom of the dependencies in the build.gradle.kts file (app)
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                coroutineScope.launch {
                    delay(500) // pause for 500ms
                    itemList = itemList.plus(random.nextInt(100))
                    isRefreshing = false
                }
            },
        ) {
            LazyColumn {
                items(itemList) { Text("Item is $it") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListsPreview() {
    RefreshingLazyColumnTheme {
        Lists()
    }
}