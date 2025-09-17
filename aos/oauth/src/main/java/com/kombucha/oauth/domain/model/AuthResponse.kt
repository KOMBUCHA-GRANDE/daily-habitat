package com.anipen.anipenauth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class AuthResponse(
    val request: Request,
    val state: String = "",
    val code: String = "",
    @SerialName("additional_parameters")
    val additionalParameters: AdditionalParameters = AdditionalParameters(),
)

@Serializable
internal data class Request(
    val configuration: Configuration,
    val clientId: String = "",
    val responseType: String = "",
    val redirectUri: String = "",
    val scope: String = "",
    val state: String = "",
    val nonce: String = "",
    val codeVerifier: String = "",
    val codeVerifierChallenge: String = "",
    val codeVerifierChallengeMethod: String = "",
    val additionalParametersRequest: AdditionalParametersRequest = AdditionalParametersRequest(),
)

@Serializable
internal data class Configuration(
    val authorizationEndpoint: String = "",
    val tokenEndpoint: String = "",
)

@Serializable
internal data class AdditionalParameters(
    val none: String? = null,
)

@Serializable
internal data class AdditionalParametersRequest(
    val os: String = "",
    val lang: String = "",
    @SerialName("guest_code")
    val guestCode: String = "",
)