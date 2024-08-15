package com.example.jora.views.profileScreen.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jora.BackgroundScreen
import com.example.jora.MainViewModel
import com.example.jora.UserProfileScreenVM
import com.example.jora.composable.DualRowContent
import com.example.jora.ui.theme.buttonTint2
import com.example.jora.ui.theme.dmDisplay
import com.example.jora.ui.theme.regularBlack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegionViewModel : ViewModel() {
    // Holds the selected language
    private var _selectedRegion: MutableStateFlow<String> = MutableStateFlow("North America")
    var selectedRegion: StateFlow<String> = _selectedRegion

    // List of languages
    val regions = listOf(
        "Premier League - (England)",
        "Bunesliga - (Germany)",
        "La Liga - (Spain)",
        "Serie A - (Italy)",
        "Major League Soccer - (USA / CA)",
        "Brasileiaro Serie A -  (Brazil)",
        "Argentine Primera Division - Argentina",
        "Eredivisie - Netherlands",
        " Primeira Liga - Portugal",
        "Liga MX - Mexico",
        "Superliga - Denmark",
        "Eliteserien - Norway",
        "Allscenskan - Sweden",
        "Swiss Super League - Switzerland"


    )

    // Update the selected language
    fun selectLanguage(language: String) {
        viewModelScope.launch {
            _selectedRegion.value = language
        }
    }
}

@Composable
fun SelectRegionScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    contentVM: UserProfileScreenVM,
    vm: LanguageViewModel = viewModel()
) {


    val appSettings = mainViewModel.appSettings.collectAsState().value
    val selectedLanguage = vm.selectedLanguage.collectAsState().value
    val languages = vm.languages

    BackgroundScreen(padding = PaddingValues()) {
        Column(modifier = Modifier.fillMaxSize(1f)) {
            DualRowContent(
                leftSide = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "",
                        tint = regularBlack,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .background(Color.White, shape = RoundedCornerShape(6.dp))
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(6.dp))
                            .padding(8.dp)
                            .clickable {
                                contentVM.setView("Profile")
                            }
                    )
                },
                rightSide = {Text(
                    "Selected Language",
                    color = regularBlack,
                    fontFamily = dmDisplay,
                    fontWeight = FontWeight.W300,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 12.dp, top = 12.dp)

                )
                    Text(
                        appSettings.region.collectAsState().value, // current language
                        color = regularBlack,
                        fontFamily = dmDisplay,
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 12.dp, bottom = 12.dp)
                    )},
                vertical = Alignment.CenterVertically)
            LazyColumn(modifier = Modifier.fillMaxSize(1f)) {
                languages.forEach { title ->
                    item {
                        Box(modifier = Modifier
                            .padding(8.dp)
                            .background(Color.White, RoundedCornerShape(6.dp))
                            .border(BorderStroke(1.38.dp, Color.LightGray), shape = RoundedCornerShape(6.dp))
                            .padding(8.dp)
                        ) {
                            DualRowContent(
                                leftSide = {
                                    Text(
                                        title,
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
                                        selected = (appSettings.region.collectAsState().value == selectedLanguage && appSettings.region.collectAsState().value == title),
                                        onClick = {
                                            vm.selectLanguage(title)
                                            mainViewModel.appSettings.value.updateRegion(title)
                                        })
                                },
                                vertical = Alignment.CenterVertically)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRegion() {
    SelectRegionScreen(navController = rememberNavController(), mainViewModel = MainViewModel(), UserProfileScreenVM())
}