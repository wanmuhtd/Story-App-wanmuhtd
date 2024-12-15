package com.dicoding.wanmuhtd.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import com.dicoding.wanmuhtd.storyapp.data.ResultState
import com.dicoding.wanmuhtd.storyapp.data.UserRepository
import com.dicoding.wanmuhtd.storyapp.utils.DataDummy
import com.dicoding.wanmuhtd.storyapp.utils.MainDispatcherRule
import com.dicoding.wanmuhtd.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateDummyLogin()
    private val dummyEmail = "arwan@mail.com"
    private val dummyPassword = "12345678"

    private val idlingResource = CountingIdlingResource("LoginIdlingResource")

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when Login Should Not Null and Return Success`() = runTest {
        IdlingRegistry.getInstance().register(idlingResource)

        `when`(userRepository.login(dummyEmail, dummyPassword))
            .thenReturn(dummyLoginResponse)

        loginViewModel.login(dummyEmail,dummyPassword)

        val result = loginViewModel.responseResult.getOrAwaitValue()

        Assert.assertNotNull(result)
        Assert.assertTrue(result is ResultState.Success)
        Assert.assertEquals(dummyLoginResponse, (result as ResultState.Success).data)

        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}