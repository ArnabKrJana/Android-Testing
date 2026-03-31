package com.example.thisiscinema.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatchersQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatchersQualifier
