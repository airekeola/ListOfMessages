package com.bright.listofmessages

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bright.listofmessages.ui.theme.ListOfMessagesTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListOfMessagesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val programs = getProgramsFromJson(this)
                    Conversation(programs)
                }
            }
        }
    }
}

@Composable
fun Conversation(programs: List<Program>) {
    LazyColumn {
        items(programs) {message -> ProgramCard(message)}
    }
}

@Composable
fun ProgramCard(message: Program) {
    Spacer(modifier = Modifier.height(10.dp))
    Row {
        Image(
            painter = painterResource(id = R.drawable.miu),
            contentDescription = "person",
            modifier = Modifier
                .clip(CircleShape)
                .size(70.dp)
                .border(width = 2.dp, shape = CircleShape, color = Color.LightGray)
        )
        Spacer(modifier = Modifier.width(4.dp))
        //create a mutable state variable that trigger UI updates when its value changes
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = message.programName,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge,

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if(isExpanded) Int.MAX_VALUE else 1
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}

@Preview(showBackground = true)
@Composable
fun ConversationPreview() {
    ListOfMessagesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            listOf(
                Program(
                    "Bachelor of Applied Arts & Sciences",
                    "The conversation is getting more interesting. It's time to play with animations! You will add the ability to expand a message to show a longer one, animating both the content size and the background color."
                ),
            )
        }
    }
}

fun getProgramsFromJson(context: Context): List<Program> {
    val inputStream = context.resources.openRawResource(R.raw.programs)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val json = reader.use { it.readText() }
    return Json { ignoreUnknownKeys = true }.decodeFromString<List<Program>>(json)
}

@Serializable
data class Program(
    val programName: String,
    val description: String
)