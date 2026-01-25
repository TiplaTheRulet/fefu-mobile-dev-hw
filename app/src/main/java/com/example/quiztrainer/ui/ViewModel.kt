package com.example.quiztrainer.ui

import androidx.lifecycle.ViewModel
import com.example.quiztrainer.questions.Question
import com.example.quiztrainer.questions.questionList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Welcome)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val questions: List<Question> = questionList
    private var currentQuestionIndex = 0
    private var selectedAnswerIndex: Int? = null
    private var correctAnswersCount = 0

    fun startQuiz() {
        _uiState.value = UIState.QuestionScreen(
            currentQuestion = questions[currentQuestionIndex],
            currentQuestionIndex = currentQuestionIndex,
            totalQuestions = questions.size,
            selectedAnswerIndex = null,
            correctAnswersCount = correctAnswersCount
        )
    }

    fun selectAnswer(answerIndex: Int) {
        selectedAnswerIndex = answerIndex

        (uiState.value as? UIState.QuestionScreen)?.let { currentState ->
            _uiState.value = currentState.copy(
                selectedAnswerIndex = answerIndex
            )
        }
    }

    fun nextQuestion() {
        selectedAnswerIndex?.let { selectedIndex ->
            val currentQuestion = questions[currentQuestionIndex]
            if (selectedIndex == currentQuestion.correctAnswerIndex) {
                correctAnswersCount++
            }
        }

        currentQuestionIndex++

        if (currentQuestionIndex < questions.size) {
            selectedAnswerIndex = null
            _uiState.value = UIState.QuestionScreen(
                currentQuestion = questions[currentQuestionIndex],
                currentQuestionIndex = currentQuestionIndex,
                totalQuestions = questions.size,
                selectedAnswerIndex = null,
                correctAnswersCount = correctAnswersCount
            )
        } else {
            _uiState.value = UIState.ResultScreen(
                correctAnswers = correctAnswersCount,
                totalQuestions = questions.size
            )
        }
    }

    fun restartQuiz() {
        currentQuestionIndex = 0
        selectedAnswerIndex = null
        correctAnswersCount = 0
        _uiState.value = UIState.Welcome
    }
}