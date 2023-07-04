package com.agro.inventory.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.agro.inventory.data.preferences.AccessManager.PreferencesKey.sessionKey
import com.agro.inventory.util.Const.Access.AUTH_PREFIX
import com.agro.inventory.util.emptyBoolean
import com.agro.inventory.util.emptyString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.*

class AccessManager(private val context: Context) {
    suspend fun setAccess(tokenAccess: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.accessKey] = "$AUTH_PREFIX $tokenAccess"
        }

        clearSessionId()
    }

    val access: Flow<String> = context.dataStore.data
            .catch { throwable ->
                emit(emptyPreferences())
                Timber.e(throwable)
            }.map { preferences ->
                preferences[PreferencesKey.accessKey] ?: emptyString
            }

    suspend fun setAccessLogin(usernameAccess: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.usernameKey] = usernameAccess
        }

    }

    val accessUsername: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.usernameKey] ?: emptyString
        }

    val accessPassword: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.passwordKey] ?: emptyString
        }

    val accessToken: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.tokenKey] ?: emptyString
        }

    val accessSobiDate: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.sobiDateKey] ?: emptyString
        }

    suspend fun clearSessionId() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.sessionIdKey] = emptyString
        }
    }

    val sessionId: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.sessionIdKey] ?: emptyString
        }


    val accessId: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.userAccesIdKey] ?: emptyString
        }


    suspend fun setSession(session: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.sessionKey] = session
        }
    }

    suspend fun setAreaAccess(
        area: String

    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.areaKey] = area
        }

    }

    suspend fun setNameMemberAccess(
        nameMember: String
        ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.nameMemberKey] = nameMember
        }

    }

    suspend fun setNoMemberAccess(
        memberNo: String

        ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.noMemberKey] = memberNo
        }

    }

    suspend fun setPlotAccess(
        plotCount: String,
        ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.plotCountKey] = plotCount
        }

    }

    val accessArea: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.areaKey] ?: emptyString
        }

    val accessNameMember: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.nameMemberKey] ?: emptyString
        }

    val accessNoMember: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.noMemberKey] ?: emptyString
        }

    val accessPlot: Flow<String> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[PreferencesKey.plotCountKey] ?: emptyString
        }

    suspend fun setUserAccess(
        userAcces: String,
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.userAccesIdKey] = userAcces
        }

    }

    val accesSession: Flow<Boolean> = context.dataStore.data
        .catch { throwable ->
            emit(emptyPreferences())
            Timber.e(throwable)
        }.map { preferences ->
            preferences[sessionKey] ?: emptyBoolean
        }



    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = PreferencesKey.AUTH_PREFERENCES_KEY.toUpperCase(Locale.ROOT)
        )
    }

    private object PreferencesKey {
        const val AUTH_PREFERENCES_KEY = "auth_preferences"
        const val TOKEN_ACCESS_REF = "token_access_key"
        const val SESSION_ID_REF = "session_id_reference_key"
        const val USERNAME_AUTH = "username_pref"
        const val PASSWORD_AUTH = "password_pref"
        const val TOKEN = "auth"
        const val SOBI_DATE = "sobi_date"
        const val AREA = "area_pref"
        const val NAME_MEMBER = "name_member_pref"
        const val NO_MEMBER = "no_member_pref"
        const val PLOT = "plot_pref"
        const val SESSION = "session_pref"
        const val USERACCESS = "userid_pref"

        val accessKey = stringPreferencesKey(TOKEN_ACCESS_REF)
        val sessionIdKey = stringPreferencesKey(SESSION_ID_REF)
        val usernameKey = stringPreferencesKey(USERNAME_AUTH)
        val passwordKey = stringPreferencesKey(PASSWORD_AUTH)
        val tokenKey = stringPreferencesKey(TOKEN)
        val sobiDateKey = stringPreferencesKey(SOBI_DATE)
        val areaKey = stringPreferencesKey(AREA)
        val nameMemberKey = stringPreferencesKey(NAME_MEMBER)
        val noMemberKey = stringPreferencesKey(NO_MEMBER)
        val plotCountKey = stringPreferencesKey(PLOT)
        val sessionKey = booleanPreferencesKey(SESSION)
        val userAccesIdKey = stringPreferencesKey(USERACCESS)
    }

}
