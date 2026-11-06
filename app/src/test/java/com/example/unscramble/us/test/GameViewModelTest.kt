package com.example.unscramble.us.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.us.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "qwerty"
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()
        val currentGameUiState = viewModel.uiState.value
        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(0, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val currentGameUiState = viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(0, currentGameUiState.score)
        assertEquals(1, currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.currentScrambledWord.isNotBlank())
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        repeat(MAX_NO_OF_WORDS) {
            val correctPlayerWord = getUnscrambledWord(viewModel.uiState.value.currentScrambledWord)
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
        }
        val currentGameUiState = viewModel.uiState.value
        assertTrue(currentGameUiState.isGameOver)
        assertEquals(MAX_NO_OF_WORDS * SCORE_INCREASE, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        val initialUiState = viewModel.uiState.value
        val initialScore = initialUiState.score
        val initialWordCount = initialUiState.currentWordCount

        viewModel.skipWord()

        val currentUiState = viewModel.uiState.value
        assertEquals(initialScore, currentUiState.score)
        assertEquals(initialWordCount + 1, currentUiState.currentWordCount)
        assertFalse(currentUiState.isGuessedWordWrong)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}

// Вспомогательная функция теперь прямо в этом файле, в самом низу!
fun getUnscrambledWord(scrambledWord: String): String {
    return when (scrambledWord) {
        "animal" -> "animal"
        "auto" -> "auto"
        "anecdote" -> "anecdote"
        "allusion" -> "allusion"
        "accommodate" -> "accommodate"
        "abstract" -> "abstract"
        "absence" -> "absence"
        "academy" -> "academy"
        "account" -> "account"
        "accuse" -> "accuse"
        "achieve" -> "achieve"
        "acquire" -> "acquire"
        "address" -> "address"
        "advice" -> "advice"
        "afraid" -> "afraid"
        "again" -> "again"
        "agree" -> "agree"
        "ahead" -> "ahead"
        "alarm" -> "alarm"
        "album" -> "album"
        "alcohol" -> "alcohol"
        "alive" -> "alive"
        else -> {
            val sortedScrambled = scrambledWord.toCharArray().sorted().joinToString("")
            com.example.unscramble.data.allWords.first { it.toCharArray().sorted().joinToString("") == sortedScrambled }
        }
    }
}