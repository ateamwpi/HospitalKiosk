package models.tts;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import core.KioskMain;

/**
 * Created by Kevin O'Brien on 4/11/2017.
 */
public class TTSManager {
    private static final String VOICE_NAME = "kevin16";

    private final Voice voice;

    public TTSManager() {
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICE_NAME);
        voice.allocate();
    }

    public void speak(String s) {
        if (s != null && !s.isEmpty()) {
            KioskMain.getTimeout().stopTimer();
            voice.speak(s);
            KioskMain.getTimeout().resetTimer();
        }
    }

}
