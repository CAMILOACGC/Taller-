import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                PuzzleScreen()
            }
        }
    }
}

class PuzzleViewModel : ViewModel() {
    // Es vital usar 'by' para que Compose detecte los cambios
    var board by mutableStateOf((1..9).shuffled())
        private set

    var moves by mutableStateOf(0)
        private set

    var goal by mutableStateOf(calculateGoal(board))
        private set

    var selectedIndex by mutableStateOf<Int?>(null)

    // ... (resto de tus funciones: selectCell, move, isAdjacent, isSolved, calculateGoal, resetGame)
    fun selectCell(index: Int) {}
    private fun move(i1: Int, i2: Int) {}
    private fun isAdjacent(i1: Int, i2: Int): Boolean = false
    fun isSolved(): Boolean = false
    private fun calculateGoal(board: List<Int>): Int = 0
    fun resetGame() {}
}

@Composable
fun PuzzleScreen(vm: PuzzleViewModel = viewModel()) {
    // ... (resto de tu UI: Column, Text, LazyVerticalGrid, etc.)
}
