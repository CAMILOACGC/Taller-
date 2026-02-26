package com.example.taller

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
            goal = calculateGoal(board)
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
        return board == listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Puzzle Deslizante", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.size(300.dp)
        ) {
            itemsIndexed(board) { index, number ->
                val isSelected = vm.selectedIndex == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f)
                        .background(if (isSelected) Color.Yellow else Color.LightGray)
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
