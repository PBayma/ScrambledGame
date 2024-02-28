package com.example.unscramble.data.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotEquals

class GameViewModelTest {
    private val viewModel = GameViewModel();

    // Format of test name is <thingUnderTest_TriggerOfTest_ResultOfTest>
    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdated() {
        var currentUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentUiState = viewModel.uiState.value

        assertFalse(currentUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectWord = "wrongGuess"

        viewModel.updateUserGuess(incorrectWord)
        viewModel.checkUserGuess()

        val currentUiState = viewModel.uiState.value

        assertTrue(currentUiState.isGuessedWordWrong)
        assertEquals(0, currentUiState.score)
    }

    @Test
    fun gameViewModel_StartGuess_FirstWordLoaded() {
        val currentUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(currentUiState.currentScrambledWord)

        assertFalse(currentUiState.isGuessedWordWrong)
        assertFalse(currentUiState.isGameOver)
        assertEquals(0, currentUiState.score)
        assertNotEquals(unScrambledWord, currentUiState.currentScrambledWord)
        assertEquals(1, currentUiState.currentWordCount)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

            assertEquals(expectedScore, currentGameUiState.score)
        }

        assertTrue(currentGameUiState.isGameOver)
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)

    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.currentWordCount
        viewModel.skipWord()
        currentGameUiState = viewModel.uiState.value

        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)

        assertEquals(lastWordCount + 1, currentGameUiState.currentWordCount)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}