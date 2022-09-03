package com.example.quiz

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quiz.databinding.ActivityQuizQuestionBinding


class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAns : Int = 0
    private var mUserName : String? = null


    private lateinit var binding: ActivityQuizQuestionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)
        binding = ActivityQuizQuestionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun setQuestion() {

        // Internally the first question has 1 id but it has 0 index
        val question = mQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList!!.size) {
            binding.btnSubmit.text = "FINISH"
        } else {
            binding.btnSubmit.text = "SUBMIT"
        }

        //setting the first question
        binding.tvQuestion.text = question!!.question

        //changing progressBar process from 0 to 1
        binding.progressBar.progress = mCurrentPosition

        //changing progress text 0/10 to 1/10
        binding.tvProgress.text = "$mCurrentPosition" + "/" + binding.progressBar.max

        //Setting the image
        binding.ivImage.setImageResource(question.image)

        // assign text to option 1
        binding.tvOptionOne.text = question.optionOne

        binding.tvOptionTwo.text = question.optionTwo

        binding.tvOptionThree.text = question.optionThree

        binding.tvOptionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(binding.tvOptionOne, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(binding.tvOptionTwo, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(binding.tvOptionThree, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(binding.tvOptionFour, 4)
            }
            R.id.btn_submit -> {

                /* mSelectedOptionPosition is by Default 0 means you havn't selected any option

                 if you select option 1 than mSelectOptionPosition = 1
                 if you select option 2 than mSelectOptionPosition = 2
                 if you select option 3 than mSelectOptionPosition = 3
                 if you select option 4 than mSelectOptionPosition = 4

                 mCurrentPosition = 1 by default so from
                 val question = mQuestionList!![mCurrentPosition - 1]
                 we get the question from the questionlist<Question> having id 1 and index 0

                 let say if mCurrentPostion == 2 so it means we will get the question having id 2
                 at index 1  = [2 - 1] and so on.. */

                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    /* means user is not selected anything because maybe he dont hav right ans
                     == 2 means we are now on the second question having id 2 index 1
                     simply we jump to the next question becuase user click submit
                     this setQuestion will work for mCurrentPosition = 2 means we can set the second question with id 2
                     if mCurrentPosition is increased to  6 means we can set the sixth question but we have only
                     10 question or our question list size is 10 if mCurrentPosition is 11 we will show completed quiz status */
                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                          val intent = Intent(this,ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME,mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAns)
                            intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    // ans(green) hmesha show hoga par glt hua to wrong(red) bhi show hoga
                    val question = mQuestionsList!![mCurrentPosition - 1]
                    if (mSelectedOptionPosition != question.correctAnswer) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAns++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    // Means if you are on the last page button text = FINISH otherwise move to next qs
                    if (mCurrentPosition == mQuestionsList!!.size) {
                        binding.btnSubmit.text = "FINISH"
                    } else {
                        binding.btnSubmit.text = "GO TO NEXT QUESTION"
                    }
                    // for next question reset the mSelectedOptionPosition
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        // when you click on something previous selected and rest all will set to default
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)

    }

    /* this fun is used for assigning the right color to the options
     if we submit the correct ans than we get correct ans in green
     but if we submit the incorrect ans than we get the incorrect ans in red as well as correct ans in green */

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4 -> {
                binding.tvOptionFour.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }

}