package com.example.jora.json.leagueData

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.example.jora.R
import com.example.jora.composable.DualRowContent
import com.example.jora.ui.theme.buttonTint2
import com.example.jora.ui.theme.dmDisplay
import com.example.jora.ui.theme.regularBlack
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Type

data class QuizData(
    val categories: List<Category>
) {
    data class Category(
        val name: String,
        val questions: List<Question>
    )

    data class Question(
        val question: String,
        val answers: List<String>
    )
}

fun loadJsonFromRaw(context: Context, resourceId: Int): String {
    val inputStream = context.resources.openRawResource(resourceId)
    return inputStream.bufferedReader().use { it.readText() }
}

fun parseQuizData(jsonString: String): QuizData {
    val gson = Gson()
    val type: Type = object : TypeToken<QuizData>() {}.type
    return gson.fromJson(jsonString, type)
}

@Composable
fun QuizApp(context: Context) {
    val viewModel: QuizViewModel = viewModel()

    // Load quiz data when the composable first starts
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = loadJsonFromRaw(context, R.raw.relationshipquiz)
            viewModel.loadQuizData(data)
        }
    }

    val quizData by viewModel.quizData.collectAsState()

    quizData?.let {
        QuizScreen(
            quizData = it,
            viewModel = viewModel
        )
    } ?: run {
        Text("Loading...", modifier = Modifier
            .fillMaxSize()
            .wrapContentSize())
    }
}


@Composable
fun QuizScreen(quizData: QuizData, viewModel: QuizViewModel) {
    val currentIndex by viewModel.currentIndex.collectAsState()
    val answeredQuestions = viewModel.completedAnswers.collectAsState().value
    val perceivedCommunicationAnswer by viewModel.perceivedCommunicationAnswers.collectAsState()
    val emotionalSatisfactionAnswer by viewModel.emotionalSatisfactionAnswers.collectAsState()
    val conflictHandlingAnswer by viewModel.conflictHandlingAnswers.collectAsState()
    val trustTransparencyAnswer by viewModel.trustTransparency.collectAsState()
    val alignmentGoalsValuesAnswer by viewModel.alignmentGoalsValues.collectAsState()
    val careerImportanceAnswer by viewModel.careerImportance.collectAsState()
    val sharedActivitiesQualityTimeAnswer by viewModel.sharedActivitiesQualityTime.collectAsState()
    val intimacyPhysicalAffectionAnswer by viewModel.intimacyPhysicalAffection.collectAsState()
    val budgetingSpendingAnswer by viewModel.budgetingSpending.collectAsState()
    val financialGoalsPlanningAnswer by viewModel.financialGoalsPlanning.collectAsState()
    val parentingStyleAnswer by viewModel.parentingStyle.collectAsState()
    val disciplineEducationAnswer by viewModel.disciplineEducation.collectAsState()
    val supportDependabilityAnswer by viewModel.supportDependability.collectAsState()
    val balancingWorkParentingAnswer by viewModel.balancingWorkParenting.collectAsState()

    Scaffold {
        Column(
            Modifier
                .padding(it)
                .padding(12.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            DualRowContent(leftSide = {
                Text(text = quizData.categories[currentIndex].name, style = MaterialTheme.typography.bodyLarge)
            }, rightSide = {
                Text(text = "${currentIndex + 1}/${quizData.categories.size}", style = MaterialTheme.typography.bodySmall)
            })

            DualRowContent(leftSide = {
                Text(text = "Questions Answered", style = MaterialTheme.typography.bodySmall)
            }, rightSide = {
                Text(text = "${answeredQuestions}/20", style = MaterialTheme.typography.bodySmall)
            })

            // show questions answered
            Row {
                OutlinedButton(onClick = { viewModel.previousCategory() }) {
                    Text("Back")
                }
                Button(onClick = { viewModel.nextCategory() }) {
                    Text("Next")
                }
            }
            LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    for ((questionIndex, question) in quizData.categories[currentIndex].questions.withIndex()) {
                        Text(text = question.question, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))

                        when (quizData.categories[currentIndex].name) {
                            "Perceived Communication Effectiveness" -> {
                                for (answer in question.answers) {
                                    val isSelected = perceivedCommunicationAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Emotional Satisfaction" -> {
                                for (answer in question.answers) {
                                    val isSelected = emotionalSatisfactionAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Conflict Handling" -> {
                                for (answer in question.answers) {
                                    val isSelected = conflictHandlingAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Trust and Transparency" -> {
                                for (answer in question.answers) {
                                    val isSelected = trustTransparencyAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Alignment of Goals and Values" -> {
                                for (answer in question.answers) {
                                    val isSelected = alignmentGoalsValuesAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Career Importance" -> {
                                for (answer in question.answers) {
                                    val isSelected = careerImportanceAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Shared Activities and Quality Time" -> {
                                for (answer in question.answers) {
                                    val isSelected = sharedActivitiesQualityTimeAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Support and Dependability" -> {
                                for (answer in question.answers) {
                                    val isSelected = supportDependabilityAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Intimacy and Physical Affection" -> {
                                for (answer in question.answers) {
                                    val isSelected = intimacyPhysicalAffectionAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Financial Goals and Planning" -> {
                                for (answer in question.answers) {
                                    val isSelected = financialGoalsPlanningAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Budgeting and Spending" -> {
                                for (answer in question.answers) {
                                    val isSelected = budgetingSpendingAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Parenting Style" -> {
                                for (answer in question.answers) {
                                    val isSelected = parentingStyleAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Discipline and Education" -> {
                                for (answer in question.answers) {
                                    val isSelected = disciplineEducationAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                            "Balancing Work and Parenting" -> {
                                for (answer in question.answers) {
                                    val isSelected = balancingWorkParentingAnswer[questionIndex] == answer
                                    Box(modifier = Modifier
                                        .padding(8.dp)
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .border(
                                            BorderStroke(1.38.dp, Color.LightGray),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(8.dp)
                                    ) {
                                        DualRowContent(
                                            leftSide = {
                                                Text(
                                                    "- $answer",
                                                    color = regularBlack,
                                                    fontFamily = dmDisplay,
                                                    fontWeight = FontWeight.W300,
                                                    fontSize = 16.sp
                                                )
                                            },
                                            rightSide = {
                                                RadioButton(
                                                    colors = RadioButtonColors(
                                                        selectedColor = buttonTint2,
                                                        unselectedColor = Color.Gray,
                                                        disabledSelectedColor = Color.LightGray,
                                                        disabledUnselectedColor = Color.DarkGray
                                                    ),
                                                    selected = isSelected,
                                                    onClick = {
                                                        when (quizData.categories[currentIndex].name) {
                                                            "Perceived Communication Effectiveness" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.PerceivedCommunicationEffectiveness
                                                            )

                                                            "Emotional Satisfaction" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.EmotionalSatisfaction
                                                            )

                                                            "Conflict Handling" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ConflictHandling
                                                            )

                                                            "Trust and Transparency" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.TrustTransparency
                                                            )

                                                            "Alignment of Goals and Values" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.AlignmentGoalsValues
                                                            )

                                                            "Career Importance" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.CareerImportance
                                                            )

                                                            "Shared Activities and Quality Time" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SharedActivitiesQualityTime
                                                            )

                                                            "Support and Dependability" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.SupportDependability
                                                            )

                                                            "Intimacy and Physical Affection" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.IntimacyPhysicalAffection
                                                            )

                                                            "Financial Goals and Planning" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.FinancialGoalsPlanning
                                                            )

                                                            "Budgeting and Spending" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BudgetingSpending
                                                            )

                                                            "Parenting Style" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.ParentingStyle
                                                            )

                                                            "Discipline and Education" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.DisciplineEducation
                                                            )

                                                            "Balancing Work and Parenting" -> viewModel.selectAnswer(
                                                                questionIndex,
                                                                answer,
                                                                QuizType.BalancingWorkParenting
                                                            )
                                                        }
                                                    })
                                            },
                                            vertical = Alignment.CenterVertically
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

class QuizViewModel : ViewModel() {
    // Holds the quiz data and current state
    private val _quizData = MutableStateFlow<QuizData?>(null)
    var quizData: StateFlow<QuizData?> = _quizData

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    private val _completedAnswers = MutableStateFlow(0)
    val completedAnswers: StateFlow<Int> = _completedAnswers

    fun loadQuizData(data: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _quizData.value = parseQuizData(data)
        }
    }

    fun nextCategory() {
        _quizData.value?.let {
            if (currentIndex.value < it.categories.size - 1) {
                _currentIndex.value += 1
                resetCompletedAnswersOnQuizChange()
            }
        }
    }

    fun resetCompletedAnswersOnQuizChange() {
        getCompleteAnswerSize(_quizData.value?.categories?.get(_currentIndex.value)?.name ?: "")
    }

    fun previousCategory() {
        if (currentIndex.value > 0) {
            _currentIndex.value -= 1
            resetCompletedAnswersOnQuizChange()
        }
    }


    private val _perceivedCommunicationAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    var perceivedCommunicationAnswers: StateFlow<Map<Int, String>> = _perceivedCommunicationAnswers

    private val _emotionalSatisfactionAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    var emotionalSatisfactionAnswers: StateFlow<Map<Int, String>> = _emotionalSatisfactionAnswers

    private val _conflictHandlingAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    var conflictHandlingAnswers: StateFlow<Map<Int, String>> = _conflictHandlingAnswers

    private val _alignmentGoalsValues = MutableStateFlow<Map<Int, String>>(emptyMap())
    var alignmentGoalsValues: StateFlow<Map<Int, String>> = _alignmentGoalsValues

    private val _careerImportance = MutableStateFlow<Map<Int, String>>(emptyMap())
    var careerImportance: StateFlow<Map<Int, String>> = _careerImportance

    private val _sharedActivitiesQualityTime = MutableStateFlow<Map<Int, String>>(emptyMap())
    var sharedActivitiesQualityTime: StateFlow<Map<Int, String>> = _sharedActivitiesQualityTime

    private val _supportDependability = MutableStateFlow<Map<Int, String>>(emptyMap())
    var supportDependability: StateFlow<Map<Int, String>> = _supportDependability

    private val _intimacyPhysicalAffection = MutableStateFlow<Map<Int, String>>(emptyMap())
    var intimacyPhysicalAffection: StateFlow<Map<Int, String>> = _intimacyPhysicalAffection

    private val _financialGoalsPlanning = MutableStateFlow<Map<Int, String>>(emptyMap())
    var financialGoalsPlanning: StateFlow<Map<Int, String>> = _financialGoalsPlanning

    private val _budgetingSpending = MutableStateFlow<Map<Int, String>>(emptyMap())
    var budgetingSpending: StateFlow<Map<Int, String>> = _budgetingSpending

    private val _parentingStyle = MutableStateFlow<Map<Int, String>>(emptyMap())
    var parentingStyle: StateFlow<Map<Int, String>> = _parentingStyle

    private val _disciplineEducation = MutableStateFlow<Map<Int, String>>(emptyMap())
    var disciplineEducation: StateFlow<Map<Int, String>> = _disciplineEducation

    private val _balancingWorkParenting = MutableStateFlow<Map<Int, String>>(emptyMap())
    var balancingWorkParenting: StateFlow<Map<Int, String>> = _balancingWorkParenting

    private val _trustTransparency = MutableStateFlow<Map<Int, String>>(emptyMap())
    var trustTransparency: StateFlow<Map<Int, String>> = _trustTransparency

    fun selectAnswer(questionIndex: Int, answer: String, type: QuizType) {
        when (type) {
            QuizType.PerceivedCommunicationEffectiveness -> {
                _perceivedCommunicationAnswers.value = _perceivedCommunicationAnswers.value.toMutableMap().apply {
                    this[questionIndex] = answer
                }
                getCompleteAnswerSize("Perceived Communication Effectiveness")
            }
            QuizType.EmotionalSatisfaction -> {
                _emotionalSatisfactionAnswers.value = _emotionalSatisfactionAnswers.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Emotional Satisfaction")
                }
            }
            QuizType.ConflictHandling -> {
                _conflictHandlingAnswers.value = _conflictHandlingAnswers.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Conflict Handling")
                }
            }
            QuizType.TrustTransparency -> {
                _trustTransparency.value = _trustTransparency.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Trust and Transparency")
                }
            }
            QuizType.AlignmentGoalsValues -> {
                _alignmentGoalsValues.value = _alignmentGoalsValues.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Alignment of Goals and Values")
                }
            }
            QuizType.CareerImportance -> {
                _careerImportance.value = _careerImportance.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Career Importance")
                }
            }
            QuizType.SharedActivitiesQualityTime -> {
                _sharedActivitiesQualityTime.value = _sharedActivitiesQualityTime.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Shared Activities and Quality Time")
                }
            }
            QuizType.SupportDependability -> {
                _supportDependability.value = _supportDependability.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Support and Dependability")
                }
            }
            QuizType.IntimacyPhysicalAffection -> {
                _intimacyPhysicalAffection.value = _intimacyPhysicalAffection.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Intimacy and Physical Affection")
                }
            }
            QuizType.FinancialGoalsPlanning -> {
                _financialGoalsPlanning.value = _financialGoalsPlanning.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Financial Goals and Planning")
                }
            }
            QuizType.BudgetingSpending -> {
                _budgetingSpending.value = _budgetingSpending.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Budgeting and Spending")
                }
            }
            QuizType.ParentingStyle -> {
                _parentingStyle.value = _parentingStyle.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Parenting Style")
                }
            }
            QuizType.DisciplineEducation -> {
                _disciplineEducation.value = _disciplineEducation.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Discipline and Education")
                }
            }
            QuizType.BalancingWorkParenting -> {
                _balancingWorkParenting.value = _balancingWorkParenting.value.toMutableMap().apply {
                    this[questionIndex] = answer
                    getCompleteAnswerSize("Balancing Work and Parenting")
                }
            }
        }
    }

    private fun getCompleteAnswerSize(quizName: String)  {
         when (quizName) {
            "Perceived Communication Effectiveness" -> _completedAnswers.value = _perceivedCommunicationAnswers.value.size
            "Emotional Satisfaction" -> _completedAnswers.value =  _emotionalSatisfactionAnswers.value.size
            "Conflict Handling" -> _completedAnswers.value = _conflictHandlingAnswers.value.size
            "Trust and Transparency" -> _completedAnswers.value = _trustTransparency.value.size
            "Alignment of Goals and Values" -> _completedAnswers.value = _alignmentGoalsValues.value.size
            "Career Importance" -> _completedAnswers.value = _careerImportance.value.size
            "Shared Activities and Quality Time" -> _completedAnswers.value = _sharedActivitiesQualityTime.value.size
            "Support and Dependability" -> _completedAnswers.value = _supportDependability.value.size
            "Intimacy and Physical Affection" -> _completedAnswers.value = _intimacyPhysicalAffection.value.size
            "Financial Goals and Planning" -> _completedAnswers.value = _financialGoalsPlanning.value.size
            "Budgeting and Spending" -> _completedAnswers.value = _budgetingSpending.value.size
            "Parenting Style" -> _completedAnswers.value = _parentingStyle.value.size
            "Discipline and Education" -> _completedAnswers.value = _disciplineEducation.value.size
            "Balancing Work and Parenting" -> _completedAnswers.value = _balancingWorkParenting.value.size
        }
    }
}

enum class QuizType {
    PerceivedCommunicationEffectiveness,
    EmotionalSatisfaction,
    ConflictHandling,
    TrustTransparency,
    AlignmentGoalsValues,
    CareerImportance,
    SharedActivitiesQualityTime,
    SupportDependability,
    IntimacyPhysicalAffection,
    FinancialGoalsPlanning,
    BudgetingSpending,
    ParentingStyle,
    DisciplineEducation,
    BalancingWorkParenting
}


