package com.example.taller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.taller.ui.theme.TallerTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuzzleScreen()
        }
    }
}

class PuzzleViewModel : ViewModel() {

    var board by mutableStateOf((1..9).shuffled())
        private set

    var moves by mutableStateOf(0)
        private set

    var goal by mutableStateOf(calculateGoal(board))
        private set

    var selectedIndex by mutableStateOf<Int?>(null)

    fun selectCell(index: Int) {
        if (selectedIndex == null) {
            selectedIndex = index
        } else {
            move(selectedIndex!!, index)
            selectedIndex = null
        }
    }

    private fun move(i1: Int, i2: Int) {
        if (isAdjacent(i1, i2)) {
            val newBoard = board.toMutableList()
            val temp = newBoard[i1]
            newBoard[i1] = newBoard[i2]
            newBoard[i2] = temp
            board = newBoard
            moves++
        }
    }

    private fun isAdjacent(i1: Int, i2: Int): Boolean {
        val r1 = i1 / 3
        val c1 = i1 % 3
        val r2 = i2 / 3
        val c2 = i2 % 3

        return (r1 == r2 && abs(c1 - c2) == 1) ||
                (c1 == c2 && abs(r1 - r2) == 1)
    }

    fun isSolved(): Boolean {
        return board == listOf(1,2,3,4,5,6,7,8,9)
    }

    private fun calculateGoal(board: List<Int>): Int {
        var misplaced = 0
        for (i in board.indices) {
            if (board[i] != i + 1) misplaced++
        }
        return misplaced
    }

    fun resetGame() {
        board = (1..9).shuffled()
        moves = 0
        goal = calculateGoal(board)
        selectedIndex = null
    }
}

@Composable
fun PuzzleScreen(vm: PuzzleViewModel = viewModel()) {

    val board = vm.board
    val moves = vm.moves
    val goal = vm.goal
    val solved = vm.isSolved()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Puzzle Deslizante", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.size(300.dp)
        ) {
            itemsIndexed(board) { index, number ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.LightGray)
                        .fillMaxSize()
                        .clickable { vm.selectCell(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(number.toString(), fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Movimientos: $moves")
        Text("Meta mínima sugerida: $goal")

        Spacer(modifier = Modifier.height(16.dp))

        if (solved) {
            val resultMessage = when {
                moves == goal -> "Igualó la meta"
                moves <= goal + 3 -> "Estuvo cerca"
                else -> "Superó ampliamente la meta"
            }

            Text(
                "¡Victoria! $resultMessage",
                color = Color.Green,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { vm.resetGame() }) {
            Text("Reiniciar")
        }
    }
}