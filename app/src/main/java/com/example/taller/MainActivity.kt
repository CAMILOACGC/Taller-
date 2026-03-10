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

/**
 * 1. ACTIVIDAD PRINCIPAL
 * Es la ventana que Android abre al tocar el icono de la app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // MaterialTheme aplica los colores y estilos por defecto
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PuzzleScreen()
                }
            }
        }
    }
}

/**
 * 2. VIEWMODEL (Lógica y Datos)
 * Aquí reside toda la "inteligencia" del juego.
 */
class PuzzleViewModel : ViewModel() {

    // El tablero es una lista de 1 al 9 mezclada aleatoriamente
    var board by mutableStateOf((1..9).shuffled())
        private set

    var moves by mutableIntStateOf(0)
        private set

    var goal by mutableIntStateOf(calculateGoal(board))
        private set

    var selectedIndex by mutableStateOf<Int?>(null)

    /**
     * Gestiona qué sucede cuando el usuario toca una celda.
     */
    fun selectCell(index: Int) {
        val currentSelection = selectedIndex
        if (currentSelection == null) {
            // Primer toque: seleccionamos la celda
            selectedIndex = index
        } else {
            // Segundo toque: intentamos mover
            move(currentSelection, index)
            selectedIndex = null // Limpiamos selección tras el intento
        }
    }

    /**
     * Intercambia dos piezas si son vecinas (arriba, abajo, izq, der).
     */
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

    /**
     * Cálculo matemático para saber si dos celdas son vecinas en una rejilla 3x3.
     */
    private fun isAdjacent(i1: Int, i2: Int): Boolean {
        val row1 = i1 / 3; val col1 = i1 % 3
        val row2 = i2 / 3; val col2 = i2 % 3
        return (row1 == row2 && abs(col1 - col2) == 1) || 
               (col1 == col2 && abs(row1 - row2) == 1)
    }

    /**
     * El juego se gana cuando la lista es exactamente [1, 2, 3, 4, 5, 6, 7, 8, 9]
     */
    fun isSolved(): Boolean = board == (1..9).toList()

    /**
     * Calcula cuántas piezas NO están en su lugar correcto.
     */
    private fun calculateGoal(currentBoard: List<Int>): Int {
        return currentBoard.indices.count { currentBoard[it] != it + 1 }
    }

    fun resetGame() {
        board = (1..9).shuffled()
        moves = 0
        goal = calculateGoal(board)
        selectedIndex = null
    }
}

/**
 * 3. INTERFAZ (UI)
 * Define cómo se ven los datos en la pantalla.
 */
@Composable
fun PuzzleScreen(vm: PuzzleViewModel = viewModel()) {
    val isSolved = vm.isSolved()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Puzzle de Números",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Genera la cuadrícula de 3x3 de forma automática
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .size(300.dp)
                .background(Color.DarkGray, shape = MaterialTheme.shapes.medium)
                .padding(8.dp)
        ) {
            itemsIndexed(vm.board) { index, number ->
                val isSelected = vm.selectedIndex == index
                
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f)
                        .clickable { vm.selectCell(index) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFFFD700) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = number.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Estadísticas del juego
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            StatItem(label = "Movimientos", value = vm.moves.toString())
            StatItem(label = "Meta (Piezas fuera)", value = vm.goal.toString())
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isSolved) {
            Text(
                "¡FELICIDADES! HAS GANADO",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { vm.resetGame() },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("REINICIAR JUEGO", fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Componente visual pequeño para mostrar estadísticas.
 */
@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
