package com.example.quiztrainer.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quiztrainer.ui.screens.QuestionScreen
import com.example.quiztrainer.ui.screens.ResultScreen
import com.example.quiztrainer.ui.screens.WelcomeScreen
import androidx.compose.runtime.collectAsState

@Composable
fun QuizApp() {
    val viewModel: QuizViewModel = viewModel()
    val uiState = viewModel.uiState

    when (val state = uiState.collectAsState().value) {
        is UIState.Welcome -> {
            WelcomeScreen(
                onStartQuiz = { viewModel.startQuiz() }
            )
        }

        is UIState.QuestionScreen -> {
            QuestionScreen(
                question = state.currentQuestion,
                currentQuestionIndex = state.currentQuestionIndex,
                totalQuestions = state.totalQuestions,
                selectedAnswerIndex = state.selectedAnswerIndex,
                correctAnswersCount = state.correctAnswersCount,
                onAnswerSelected = { index -> viewModel.selectAnswer(index) },
                onNextClicked = { viewModel.nextQuestion() }
            )
        }

        is UIState.ResultScreen -> {
            ResultScreen(
                correctAnswers = state.correctAnswers,
                totalQuestions = state.totalQuestions,
                onRestart = { viewModel.restartQuiz() }
            )
        }
    }
}