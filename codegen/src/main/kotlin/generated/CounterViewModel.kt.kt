package generated

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.Int

public class CounterViewModel : ViewModel() {
  public val count: MutableState<Int> = mutableStateOf(0)

  public fun increment() {
    count.value++
  }
}
