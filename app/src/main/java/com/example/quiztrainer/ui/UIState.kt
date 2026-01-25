package com.example.quiztrainer.ui

import com.example.quiztrainer.questions.Question

sealed class UIState {
    data object Welcome : UIState()
    data class QuestionScreen(
        val currentQuestion: Question,
        val currentQuestionIndex: Int,
        val totalQuestions: Int,
        val selectedAnswerIndex: Int?,
        val correctAnswersCount: Int
    ) : UIState()

    data class ResultScreen(
        val correctAnswers: Int,
        val totalQuestions: Int
    ) : UIState()
}