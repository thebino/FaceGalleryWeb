package de.stuermerbenjamin.facegallery.frontend.services

import de.stuermerbenjamin.facegallery.shared.AccessToken
import de.stuermerbenjamin.facegallery.shared.TokenResponse
import de.stuermerbenjamin.facegallery.shared.response.ImagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters

private const val baseUrl = "http://127.0.0.1:9000"

class ImageService(val httpClient: HttpClient) {
    suspend fun requestIdentiyToken(name: String, password: String): AccessToken {
        val tokenResponse = httpClient.post<TokenResponse>("$baseUrl/auth") {
            method = HttpMethod.Post
            body = FormDataContent(Parameters.build {
                append("client_id", "None")
                append("response_type", "token id_token")
                append("grant_type", "password")
                append("scope", "openid profile email")
                append("username", name)
                append("password", password)
            })
        }

        return AccessToken(
            tokenResponse.access_token,
            tokenResponse.refresh_token,
            tokenResponse.id_token
        )
    }

    suspend fun getImages(apiKey: String): ImagesResponse {
        return httpClient.get("$baseUrl/images") {
            method = HttpMethod.Get
            headers.append("X-App-Version", "2.0.0")
            headers.append("X-App-Name", "cgwWebApp")
            headers.append("X-Market", "de_DE")
            headers.append("Accept-Charset", "utf-8")
            headers.append("X-API-KEY", apiKey)
        }
    }
}
