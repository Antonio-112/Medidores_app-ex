package com.iplacex.medidores_app.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iplacex.medidores_app.ui.vm.FormScreen
import com.iplacex.medidores_app.ui.vm.ListScreen
import com.iplacex.medidores_app.ui.vm.AppViewModel

private object Routes {
    const val LIST = "list"
    const val FORM = "form"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNav(navController: NavHostController = rememberNavController()) {
    val vm: AppViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            ListScreen(
                state = vm.uiState,
                onAddClick = { navController.navigate(Routes.FORM) }
            )
        }
        composable(Routes.FORM) {
            FormScreen(
                state = vm.uiState,
                onValueChange = vm::updateDraft,
                onSave = {
                    vm.saveDraft()
                    navController.popBackStack(Routes.LIST, false)
                },
                onCancel = { navController.popBackStack(Routes.LIST, false) }
            )
        }
    }
}
