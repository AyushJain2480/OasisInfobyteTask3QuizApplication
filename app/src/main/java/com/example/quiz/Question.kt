package com.example.quiz

// it contains only data
// this is the model
data class Question (
  val id: Int,
  val question: String,
  val image: Int,
  val optionOne: String,
  val optionTwo: String,
  val optionThree: String,
  val optionFour: String,
  val correctAnswer: Int
  )