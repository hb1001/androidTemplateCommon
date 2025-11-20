package com.template.generated.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.template.generated.base.CardItem

@Composable
fun SimpleCard(item: CardItem, onClick: () -> Unit = {}){
    Card(
        modifier = Modifier.fillMaxSize().clickable{
            onClick()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Text(text = item.title ?: "")
            Text(text = item.description ?: "")
            Text(text = item.value ?: "")
        }
    }
}