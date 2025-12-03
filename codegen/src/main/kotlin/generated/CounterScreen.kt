package generated

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
public fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = 'Current count: ' + viewModel.count.value,
    )
    Button(
      onClick = {
        viewModel.increment()
      },
    ) {
      Text(
        text = 'Increment',
      )
    }
  }
}
