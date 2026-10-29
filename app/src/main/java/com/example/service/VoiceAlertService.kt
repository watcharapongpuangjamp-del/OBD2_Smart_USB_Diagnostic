package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.data.model.AppLanguage
import java.util.Locale

class VoiceAlertService(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var currentLang: AppLanguage = AppLanguage.TH
    private var lastAlertTime = 0L

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            setLanguage(currentLang)
        }
    }

    fun updateLanguage(lang: AppLanguage) {
        currentLang = lang
        if (isInitialized) {
            setLanguage(lang)
        }
    }

    private fun setLanguage(lang: AppLanguage) {
        val locale = if (lang == AppLanguage.TH) Locale("th", "TH") else Locale.US
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to English if Thai is not supported by the TTS engine
            tts?.setLanguage(Locale.US)
        }
    }

    fun speakAlert(message: String, debounceMs: Long = 10000L) {
        val now = System.currentTimeMillis()
        if (now - lastAlertTime > debounceMs && isInitialized) {
            tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "ALERT_ID")
            lastAlertTime = now
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
