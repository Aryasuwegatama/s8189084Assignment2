package com.example.s8189084assignment2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// Points Dispatchers.Main at a test dispatcher so viewModelScope.launch runs on the JVM during tests.
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    // Before each test, swap in the test dispatcher.
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    // After each test, put the real Main dispatcher back.
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
