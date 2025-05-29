package com.kombucha.dailyhabitat.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.Multibinds

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DailyHabitatModule {

    @Multibinds
    abstract fun presentationFactories(): Set<Presenter.Factory>

    @Multibinds
    abstract fun uiFactories(): Set<Ui.Factory>

    companion object {
        @[Provides ActivityRetainedScoped]
        fun provideCircuit(
            presentationFactories: @JvmSuppressWildcards Set<Presenter.Factory>,
            uiFactories: @JvmSuppressWildcards Set<Ui.Factory>,
        ): Circuit = Circuit.Builder()
            .addPresenterFactories(presentationFactories)
            .addUiFactories(uiFactories)
            .build()
    }
}