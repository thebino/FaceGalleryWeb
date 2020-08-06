package com.emotionrec.api

import com.google.gson.GsonBuilder
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.ktor.server.testing.handleRequest
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
object HelloApplicationSpec : Spek({
    given("an application") {
        val engine = TestApplicationEngine(createTestEnvironment())
        engine.start(wait = false) // for now we can't eliminate it
        engine.application.main() // our main module function

        with(engine) {
            describe("diagnostics") {
                it("ping should return pong") {
                    handleRequest(HttpMethod.Get, "/ping").let { call ->
                        assertEquals("pong", call.response.content)
                    }
                }
            }
//            describe("prediction") {
//                it("Space separated 2304 Ints should return 200") {
//                    handleRequest(HttpMethod.Post, "/prediction", {
//                        body = GsonBuilder().create().toJson(PostPredictionData("70 80 82 72 58 58 60 63 54 58 60 48 89 115 121 119 115 110 98 91 84 84 90 99 110 126 143 153 158 171 169 172 169 165 129 110 113 107 95 79 66 62 56 57 61 52 43 41 65 61 58 57 56 69 75 70 65 56 54 105 146 154 151 151 155 155 150 147 147 148 152 158 164 172 177 182 186 189 188 190 188 180 167 116 95 103 97 77 72 62 55 58 54 56 52 44 50 43 54 64 63 71 68 64 52 66 119 156 161 164 163 164 167 168 170 174 175 176 178 179 183 187 190 195 197 198 197 198 195 191 190 145 86 100 90 65 57 60 54 51 41 49 56 47 38 44 63 55 46 52 54 55 83 138 157 158 165 168 172 171 173 176 179 179 180 182 185 187 189 189 192 197 200 199 196 198 200 198 197 177 91 87 96 58 58 59 51 42 37 41 47 45 37 35 36 30 41 47 59 94 141 159 161 161 164 170 171 172 176 178 179 182 183 183 187 189 192 192 194 195 200 200 199 199 200 201 197 193 111 71 108 69 55 61 51 42 43 56 54 44 24 29 31 45 61 72 100 136 150 159 163 162 163 170 172 171 174 177 177 180 187 186 187 189 192 192 194 195 196 197 199 200 201 200 197 201 137 58 98 92 57 62 53 47 41 40 51 43 24 35 52 63 75 104 129 143 149 158 162 164 166 171 173 172 174 178 178 179 187 188 188 191 193 194 195 198 199 199 197 198 197 197 197 201 164 52 78 87 69 58 56 50 54 39 44 42 26 31 49 65 91 119 134 145 147 152 159 163 167 171 170 169 174 178 178 179 187 187 185 187 190 188 187 191 197 201 199 199 200 197 196 197 182 58 62 77 61 60 55 49 59 52 54 44 22 30 47 68 102 123 136 144 148 150 153 157 167 172 173 170 171 177 179 178 186 190 186 189 196 193 191 194 190 190 192 197 201 203 199 194 189 69 48 74 56 60 57 50 59 59 51 41 20 34 47 79 111 132 139 143 145 147 150 151 160 169 172 171 167 171 177 177 174 180 182 181 192 196 189 192 198 195 194 196 198 201 202 195 189 70 39 69 61 61 61 53 59 59 45 40 26 40 61 93 124 135 138 142 144 146 151 152 158 165 168 168 165 161 164 173 172 167 172 167 180 198 198 193 199 195 194 198 200 198 197 195 190 65 35 68 59 59 62 57 60 59 50 44 32 54 90 115 132 137 138 140 144 146 146 156 165 168 174 176 176 175 168 168 169 171 175 171 172 192 194 184 198 205 201 194 195 193 195 192 186 57 38 72 65 57 62 58 57 60 54 49 47 79 116 130 138 141 141 139 141 143 145 157 164 164 166 173 174 176 179 179 176 181 189 188 173 180 175 160 182 189 198 192 189 190 190 188 172 46 44 64 66 59 62 57 56 62 53 50 66 103 133 137 141 143 141 136 132 131 136 127 118 111 107 108 123 131 143 154 158 166 177 181 175 170 159 148 171 161 176 185 192 194 188 190 162 53 49 58 63 61 61 55 56 61 51 50 81 116 139 142 142 146 144 136 128 119 112 97 85 90 91 88 92 90 80 81 84 106 122 132 144 145 144 147 163 147 163 173 181 190 187 191 167 61 48 53 61 61 58 54 56 61 51 53 89 123 140 144 145 146 147 136 122 107 99 95 92 90 87 83 76 67 52 46 52 63 69 83 96 119 132 148 159 136 137 143 138 143 152 156 156 70 48 50 59 61 57 54 54 61 52 56 93 124 135 140 144 148 150 140 125 114 101 80 54 56 54 41 41 33 40 39 35 49 60 63 74 107 129 147 147 116 111 100 77 76 86 108 111 73 49 50 60 62 60 57 55 63 59 56 89 121 134 139 146 151 152 150 141 127 111 96 77 85 70 32 31 37 91 65 50 48 59 73 83 112 136 155 130 60 46 38 40 43 81 116 91 72 52 48 58 62 62 59 53 61 59 52 85 114 134 140 147 154 159 158 153 145 143 150 126 121 125 68 45 89 137 95 70 78 75 95 109 131 153 171 94 23 16 32 82 82 65 113 77 71 54 48 56 62 62 60 53 60 56 52 75 108 133 141 149 158 166 169 167 163 156 155 146 112 119 134 127 142 140 121 117 129 114 120 129 146 174 191 98 46 33 33 109 147 98 109 67 73 55 50 56 64 64 61 58 61 53 54 64 106 129 140 148 159 169 175 176 174 165 159 156 145 120 115 124 127 131 133 141 147 142 141 147 161 182 202 154 114 96 100 158 158 153 123 61 76 57 48 56 64 64 63 62 61 54 55 44 97 131 137 147 158 168 177 181 183 179 170 168 169 165 155 152 151 152 154 162 165 158 153 158 168 187 206 186 147 135 144 145 152 178 115 57 74 58 48 58 64 63 63 59 63 55 53 66 104 130 132 144 153 162 170 180 185 187 181 178 182 180 177 173 171 171 177 176 172 164 161 167 164 185 207 197 173 152 141 141 161 191 104 54 69 60 48 57 65 62 60 57 64 55 50 94 111 124 130 135 150 159 163 172 179 184 184 178 178 177 173 171 174 177 178 176 169 165 161 163 161 180 205 201 183 171 177 178 180 194 101 55 65 60 47 55 65 63 59 58 63 57 52 90 105 117 122 130 143 153 157 163 171 174 182 183 182 178 174 175 175 177 175 172 163 161 159 157 162 178 200 201 188 181 172 177 187 198 98 57 63 61 48 52 61 64 63 60 65 57 51 95 104 113 117 127 136 145 152 156 162 162 165 173 177 182 183 183 180 181 177 165 153 154 152 153 160 174 193 200 188 185 180 182 192 196 101 60 60 56 49 50 60 66 64 62 64 59 53 99 104 111 112 118 132 142 147 155 158 160 159 162 171 176 184 186 183 180 169 154 141 135 145 155 164 180 196 205 188 189 188 189 193 192 98 61 64 55 49 49 60 66 63 64 63 60 57 99 105 108 112 113 125 139 143 150 155 158 164 169 174 176 182 183 182 177 163 141 133 147 151 164 170 185 200 210 194 188 192 186 185 180 88 64 67 60 46 50 59 65 64 64 64 59 56 101 103 108 109 109 118 134 143 143 147 155 159 166 171 174 177 179 178 172 153 129 143 161 159 166 171 186 197 207 203 185 191 183 179 164 73 67 67 66 48 50 57 65 65 63 64 61 57 103 108 114 112 110 115 128 138 144 145 152 156 159 164 168 172 172 169 161 139 125 147 156 161 162 164 180 188 188 197 185 187 181 180 137 65 70 68 70 52 47 53 62 65 63 65 61 58 105 109 112 120 113 112 122 134 141 149 150 153 155 159 164 167 167 162 152 134 115 126 119 106 99 109 141 158 150 155 175 184 176 175 106 63 70 68 68 50 46 50 57 63 63 64 61 59 107 110 110 117 117 114 117 128 137 147 148 150 153 156 161 162 163 156 150 148 105 70 45 26 25 47 73 74 79 128 177 180 173 157 77 66 68 67 68 52 49 51 56 62 62 62 62 60 101 107 108 114 115 114 117 125 134 143 148 149 152 154 158 160 158 155 160 158 132 88 73 73 64 52 66 91 138 160 174 173 171 125 64 67 63 64 68 54 50 49 54 60 60 60 62 60 98 105 105 109 111 114 117 125 131 139 145 148 153 153 156 157 156 161 168 165 153 139 122 115 105 89 103 150 182 161 171 173 162 89 64 64 62 64 69 56 48 49 56 58 60 59 62 60 89 99 108 106 109 111 119 120 125 134 140 146 152 153 153 153 156 159 162 160 150 136 129 133 133 122 133 148 178 168 168 175 132 61 67 66 65 63 69 57 47 50 55 58 59 61 62 60 89 96 105 107 105 107 117 120 123 124 133 141 149 153 151 145 151 145 139 140 138 128 126 124 129 125 136 142 164 172 168 168 87 58 67 63 62 61 69 57 39 44 55 56 59 63 62 62 84 91 92 98 102 103 113 119 121 118 128 138 146 151 147 142 140 128 127 128 129 126 135 140 135 130 143 146 149 166 174 131 62 65 62 59 67 63 68 83 89 65 42 52 60 60 62 63 77 84 84 91 99 101 107 112 117 118 122 134 145 149 144 134 127 127 129 130 134 125 126 132 152 153 151 150 151 165 171 87 59 65 64 61 58 86 122 138 208 207 154 71 52 56 55 56 69 77 83 85 93 91 102 112 116 118 119 127 140 144 142 131 112 95 85 75 62 58 56 59 87 88 83 127 142 165 149 62 65 62 59 77 113 192 156 84 185 196 197 168 81 70 75 69 58 65 73 82 81 79 95 107 114 116 116 123 136 142 136 132 131 102 71 58 49 41 33 41 36 49 60 99 136 168 111 53 63 71 138 186 203 195 146 87 91 72 79 95 103 82 61 74 55 57 68 75 76 77 84 96 106 110 111 121 130 138 136 142 153 159 152 152 154 145 133 136 147 158 156 155 147 158 74 57 60 123 181 174 126 89 72 67 57 43 55 67 76 86 60 45 51 45 52 68 75 73 77 88 96 100 104 113 115 121 134 146 149 146 149 148 155 168 174 179 178 169 169 174 161 131 44 47 82 150 168 136 104 75 66 80 67 58 48 54 68 88 121 102 51 45 38 53 66 65 70 86 92 96 102 103 109 116 130 136 136 133 136 138 137 135 128 130 143 158 165 164 147 87 62 74 123 160 170 100 99 107 79 71 86 75 57 45 49 65 122 130 43 48 40 39 55 61 59 71 82 87 88 93 105 118 123 128 130 124 111 98 94 88 67 55 84 129 147 148 105 48 82 142 161 164 164 76 72 85 100 88 72 90 84 54 48 54 73 100 73 36 44 31 37 53 51 55 67 74 77 87 97 108 118 125 132 122 106 86 80 82 75 73 83 110 129 126 46 22 130 177 196 193 166 72 52 54 73 100 92 75 99 95 65 68 61 63 91 65 42 37 22 28 39 44 57 68 74 83 92 101 119 131 143 141 134 136 140 139 134 136 139 138 136 85 23 114 202 198 199 180 173 98 36 86 130 150 137 99 77 101 99 72 56 43 77 82 79 70 56 28 20 25 36 50 63 73 83 98 111 124 139 156 160 159 169 168 165 163 159 149 114 43 26 133 183 192 177 152 137 130 125 139 173 195 186 137 101 88 101 105 70 46 77 72 84 87 87 81 64 37 20 31 40 46 65 88 108 110 125 149 157 153 162 164 158 159 154 140 78 21 11 61 144 168 173 157 138 150 148 132 159 182 183 136 106 116 95 106 109 82",
//                                null))
//                    }).let {
//                        assertEquals(HttpStatusCode.OK, it.response.status())
//                    }
//                }
//            }
        }
    }
})

//                    get("http://localhost:8080/ping").then().statusCode(200)

//                    given()
////                            .cookie(apiTestSessionID)
////                            .contentType("application/json")
////                            .body(jsonObj.toString())
////                            .`when`()
////                            .post("/post/Config")
////                    given()
////                            .contentType("application/json")
////                            .body(PostPredictionData("", null))
////                            .when()
////                            .post("/prediction")
////                            .then()
////                            .statusCode(200)
////                    post("/prediction")
//////                            .then()
//////                            .body